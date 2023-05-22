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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @Transactional
    public void uploadPost(PostDto postDto) throws IOException {
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

        String downloadUrl = imagePath.substring(bucketUrl.length(), imagePath.length());

        Post post = postDto.toEntity(imagePath, downloadUrl); // 5cf6237d-c404-43e5-836b-e55413ed0e49_bag.jpeg

        post.setEraseFlag(0);

        postRepository.save(post);
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

        PageRequest pageRequest = PageRequest.of(page, size);
        return postRepository.findAllUnErased(pageRequest).getContent();
    }

    public List<Post> findPostsLatest5() {
        return postRepository.findPostsLatest5();
    }

    @Transactional
    public void updatePostErasedTrue(Integer id, String password) {
        String realPassword = postRepository.findPasswordById(id);
        if (password.equals(realPassword)) {
            postRepository.updatePostErasedTrue(id);
        }
        else {
            System.out.println("비밀번호가 틀립니다.");
        }
    }
}
