package com.litaa.projectkupica.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * @author : Unagi_zoso
 * @date : 2023-07-20
 */
@AllArgsConstructor
@Getter
public class ImageFile {

    private byte[] bytes;
    private String fileName;
    private MediaType mediaType;
    private int size;
}