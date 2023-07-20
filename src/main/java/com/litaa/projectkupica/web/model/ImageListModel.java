package com.litaa.projectkupica.web.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.litaa.projectkupica.domain.image.Image.ImageResponse;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

/**
 * @author : Unagi_zoso
 * @date : 2023-07-20
 */
public class ImageListModel extends RepresentationModel<ImageListModel> {
    @JsonUnwrapped
    private final List<ImageResponse> imageResponses;

    public ImageListModel(List<ImageResponse> imageResponses) {
        this.imageResponses = imageResponses;
    }
}
