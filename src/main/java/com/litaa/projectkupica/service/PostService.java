package com.litaa.projectkupica.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.litaa.projectkupica.domain.post.Post;
import com.litaa.projectkupica.domain.post.PostRepository;
import com.litaa.projectkupica.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

/**
 * @author : Unagi_zoso
 * @date : 2022-12-31
 */

@RequiredArgsConstructor
@Service
public class PostService {

    // @Value("cloud.aws.s3.bucket")
    private String s3Bucket = "kupica-img";
    @Value("${cloud.aws.s3.bucket-url}")
    private String bucketUrl;
    private final AmazonS3Client amazonS3Client;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ResponseEntity<?> uploadPost(PostDto postDto) throws IOException {
        UUID uuid = UUID.randomUUID(); // uuid
        String imageFileName = uuid+"_"+postDto.getFile().getOriginalFilename(); // 1.jpg
        long imageSize = postDto.getFile().getSize();

        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType(postDto.getFile().getContentType());
        objectMetaData.setContentLength(imageSize);

        amazonS3Client.putObject(
                new PutObjectRequest(s3Bucket, imageFileName, postDto.getFile().getInputStream(), objectMetaData)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        String imagePath = amazonS3Client.getUrl(s3Bucket, imageFileName).toString();

        // image 테이블에 저장

        String downloadUrl = imagePath.substring(bucketUrl.length());

        Post post = postDto.toEntity(imagePath, downloadUrl);
        post.setPassword(passwordEncoder.encode(post.getPassword()));

        post.setEraseFlag(0);

        postRepository.save(post);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<byte[]> download(String storedFileUrl) throws IOException {
        System.out.println(storedFileUrl);
        S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest(s3Bucket, storedFileUrl));
        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
        byte[] bytes = IOUtils.toByteArray(objectInputStream);


        String fileName = URLEncoder.encode(storedFileUrl, "UTF-8").replaceAll("\\+", "%20");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(contentType(storedFileUrl));
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

    private MediaType contentType(String keyName) {
        String[] arr = keyName.split("\\.");
        String type = arr[arr.length - 1];

        switch (type) {
            case "txt":
                return MediaType.TEXT_PLAIN;
            case "png":
            case "PNG":
                return MediaType.IMAGE_PNG;
            case "jpg":
            case "JPG":
                return MediaType.IMAGE_JPEG;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    public List<Post> findPostsByPageRequest(Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "post_id");
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return postRepository.findAllUnErased(pageRequest).getContent();
    }

    public List<Post> findPostsLatest5() {
        return postRepository.findPostsLatest5();
    }

    @Transactional
    public ResponseEntity<?> updatePostErasedTrue(Integer id, String password) {
        String realPassword = postRepository.findPasswordById(id);
        if (isPasswordValid(password, realPassword)) {
            postRepository.updatePostErasedTrue(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 틀립니다.");
        }
    }

    public boolean isPasswordValid(String password, String encodedPassword) {
        return passwordEncoder.matches(password, encodedPassword);
    }
}
