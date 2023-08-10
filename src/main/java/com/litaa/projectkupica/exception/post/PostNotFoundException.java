package com.litaa.projectkupica.exception.post;

/**
 * @author : Unagi_zoso
 * @date : 2023-08-03
 */
public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException() {
    }

    public PostNotFoundException(String message) {
        super(message);
    }
}
