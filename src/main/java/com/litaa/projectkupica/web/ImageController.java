package com.litaa.projectkupica.web;

import com.litaa.projectkupica.domain.image.Image.ImageResponse;
import com.litaa.projectkupica.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author : Unagi_zoso
 * @date : 2023-07-13
 */

@RequiredArgsConstructor
@RestController
public class ImageController {

    private final ImageService imageService;
    private final Logger LOGGER = LoggerFactory.getLogger(PostController.class);

    @GetMapping("/images/{downloadKey}/download")
    public ResponseEntity<byte[]> download(@PathVariable String downloadKey) throws IOException {

        long startTime = System.currentTimeMillis();
        LOGGER.info("[ImageController] download post. downloadImageUrl : {}", downloadKey);
        ResponseEntity<byte[]> response = imageService.download(downloadKey);

        LOGGER.info("[ImageController] download post processing time : {}", (System.currentTimeMillis() - startTime));
        return response;
    }

    @GetMapping("/images/latest/5")
    public List<ImageResponse> findPostsLatest5() {

        long startTime = System.currentTimeMillis();
        LOGGER.info("[ImageController] find latest 5 images .");

        List<ImageResponse> imageResponses = imageService.findLatestImages5();

        LOGGER.info("[ImageController] find latest 5 images. post size : {}", imageResponses.size());
        LOGGER.info("[ImageController] find latest 5 images processing time : {}", (System.currentTimeMillis() - startTime));

        return imageResponses;
    }
}
