package com.litaa.projectkupica.exception.post;

/**
 * @author : Unagi_zoso
 * @date : 2023-08-03
 */
public class WrongPasswordException extends RuntimeException {

    public WrongPasswordException() {
    }

    public WrongPasswordException(String message) {
        super(message);
    }
}
