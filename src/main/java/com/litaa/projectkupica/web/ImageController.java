package com.litaa.projectkupica.web;

import com.litaa.projectkupica.common.annotaiton.ImageIdValidation;
import com.litaa.projectkupica.domain.image.Image.ImageResponse;
import com.litaa.projectkupica.service.ImageService;
import com.litaa.projectkupica.web.dto.ImageFile;
import com.litaa.projectkupica.web.model.DefaultResponseModel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : Unagi_zoso
 * @date : 2023-07-13
 */

@Validated
@RequiredArgsConstructor
@RestController
public class ImageController {

    private final ImageService imageService;
    private final Logger LOGGER = LoggerFactory.getLogger(PostController.class);

    @GetMapping("/images/{imageId}/download")
    public ResponseEntity<byte[]> download(@PathVariable @ImageIdValidation int imageId) {

        long startTime = System.currentTimeMillis();
        LOGGER.info("[ImageController] download post. downloadImageUrl : {}", imageId);

        ImageFile imageFile = imageService.download(imageId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(imageFile.getMediaType());
        httpHeaders.setContentLength(imageFile.getSize());
        httpHeaders.setContentDisposition(ContentDisposition.attachment().filename(imageFile.getFileName()).build());

        LOGGER.info("[ImageController] download post processing time : {}", (System.currentTimeMillis() - startTime));

        return new ResponseEntity<>(imageFile.getBytes(), httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/images/latest/5")
    public ResponseEntity<?> findLatestImages5() {

        long startTime = System.currentTimeMillis();
        LOGGER.info("[ImageController] find latest 5 images .");

        List<ImageResponse> imageResponses = imageService.findLatestImages5();

        LOGGER.info("[ImageController] find latest 5 images. post size : {}", imageResponses.size());
        LOGGER.info("[ImageController] find latest 5 images processing time : {}", (System.currentTimeMillis() - startTime));

        return new ResponseEntity<>(new DefaultResponseModel<>(imageResponses), HttpStatus.OK);
    }
}
