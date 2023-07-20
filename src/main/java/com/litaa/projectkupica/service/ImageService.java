package com.litaa.projectkupica.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.litaa.projectkupica.domain.image.Image;
import com.litaa.projectkupica.domain.image.Image.ImageResponse;
import com.litaa.projectkupica.domain.image.ImageRepository;
import com.litaa.projectkupica.domain.post.PostRepository;
import com.litaa.projectkupica.web.dto.ImageFile;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author : Unagi_zoso
 * @date : 2023-07-14
 */

@RequiredArgsConstructor
@Service
public class ImageService {

    @Value("${cloud.aws.s3.bucket}")
    private String s3Bucket;
    @Value("${cloud.aws.s3.bucket-url}")
    private String bucketUrl;
    @Value("${cloud.aws.cloudfront-domain}")
    private String cloudfrontDomain;

    private final AmazonS3Client amazonS3Client;
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);

    public void uploadImage(int postId, MultipartFile imageFile) throws IOException {

        ArrayList<String> s3UploadResult = uploadImageToS3Bucket(imageFile);

        String imagePath = s3UploadResult.get(0);
        String cachedImageUrl = s3UploadResult.get(1);
        String downloadUrl = s3UploadResult.get(2);

        Image image = Image.builder()
                .source(imagePath)
                .cachedImageUrl(cachedImageUrl)
                .downloadKey(downloadUrl)
                .post(postRepository.getReferenceById(postId))
                .build();

        imageRepository.save(image);
    }

    public ImageFile download(int imageId) throws IOException {
        String storedFileUrl = imageRepository.findDownloadKeyByImageId(imageId);

        S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest(s3Bucket, storedFileUrl));
        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
        byte[] bytes = IOUtils.toByteArray(objectInputStream);

        String fileName = URLEncoder.encode(storedFileUrl, "UTF-8").replaceAll("\\+", "%20");

        return new ImageFile(bytes, fileName, contentType(storedFileUrl), bytes.length);
    }

    public List<ImageResponse> findLatestImages5() {

        return imageRepository.findLatestImages5().stream()
                .map(img -> ImageResponse.builder()
                        .imageId(img.getImageId())
                        .source(img.getSource())
                        .cachedImageUrl(img.getCachedImageUrl())
                        .downloadKey(img.getDownloadKey())
                        .build())
                .collect(Collectors.toList());
    }

    private ArrayList<String> uploadImageToS3Bucket(MultipartFile file) throws IOException {

        LOGGER.info("[PostService] upload image to s3 bucket.");

        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid+"_"+file.getOriginalFilename();
        long imageSize = file.getSize();

        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType(file.getContentType());
        objectMetaData.setContentLength(imageSize);

        amazonS3Client.putObject(
                new PutObjectRequest(s3Bucket, imageFileName, file.getInputStream(), objectMetaData)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        String s3ImagePath = amazonS3Client.getUrl(s3Bucket, imageFileName).toString();
        String cloudfrontImagePath = cloudfrontDomain + imageFileName;
        String downloadKey = imageFileName;

        LOGGER.info("[PostService] upload image to s3 bucket. image filename : {}, image size : {}, s3 image path : {}, cloudfront image path : {}, download url : {}",
                imageFileName, imageSize, s3ImagePath, cloudfrontImagePath, downloadKey);

        ArrayList<String> S3UploadResult = new ArrayList<>();

        S3UploadResult.add(s3ImagePath);
        S3UploadResult.add(cloudfrontImagePath);
        S3UploadResult.add(downloadKey);

        return S3UploadResult;
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
            case "jpeg":
            case "JPEG":
                return MediaType.IMAGE_JPEG;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    public void updateImage(Integer id, MultipartFile imageFile) throws IOException {

        ArrayList<String> s3UploadResult = uploadImageToS3Bucket(imageFile);

        String imagePath = s3UploadResult.get(0);
        String cachedImageUrl = s3UploadResult.get(1);
        String downloadUrl = s3UploadResult.get(2);

        imageRepository.updateImage(id, imagePath, cachedImageUrl, downloadUrl);
    }
}
