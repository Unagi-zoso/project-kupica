package com.litaa.projectkupica.web;

import com.litaa.projectkupica.domain.image.Image.ImageResponse;
import com.litaa.projectkupica.service.ImageService;
import com.litaa.projectkupica.web.dto.ImageFile;
import com.litaa.projectkupica.web.model.ImageListModel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/images/{imageId}/download")
    public ResponseEntity<byte[]> download(@PathVariable int imageId) throws IOException {

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
    public ResponseEntity<ImageListModel> findLatestImages5() {

        long startTime = System.currentTimeMillis();
        LOGGER.info("[ImageController] find latest 5 images .");

        List<ImageResponse> imageResponses = imageService.findLatestImages5();

        ImageListModel imageListModel = new ImageListModel(imageResponses);
        imageListModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ImageController.class).findLatestImages5()).withSelfRel());

        LOGGER.info("[ImageController] find latest 5 images. post size : {}", imageResponses.size());
        LOGGER.info("[ImageController] find latest 5 images processing time : {}", (System.currentTimeMillis() - startTime));

        return new ResponseEntity<>(imageListModel, HttpStatus.OK);
    }
}
