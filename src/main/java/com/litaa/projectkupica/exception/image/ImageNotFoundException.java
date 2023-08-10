package com.litaa.projectkupica.exception.image;

/**
 * @author : Unagi_zoso
 * @date : 2023-08-03
 */
public class ImageNotFoundException extends RuntimeException {

    public ImageNotFoundException() {
    }

    public ImageNotFoundException(String message) {
        super(message);
    }
}
