package com.litaa.projectkupica.web.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.RepresentationModel;

/**
 * @author : Unagi_zoso
 * @date : 2023-08-03
 */
public class DefaultResponseModel<T> extends RepresentationModel<DefaultResponseModel<T>>{
    @JsonUnwrapped
    private T value;

    public DefaultResponseModel(T value) {
        this.value = value;
    }
}
