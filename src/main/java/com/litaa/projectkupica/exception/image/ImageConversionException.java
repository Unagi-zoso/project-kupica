package com.litaa.projectkupica.exception.image;

import java.io.IOException;

/**
 * @author : Unagi_zoso
 * @date : 2023-08-03
 */
public class ImageConversionException extends RuntimeException {

    public ImageConversionException() {
    }

    public ImageConversionException(String message) {
        super(message);
    }
}
