package com.litaa.projectkupica.exception.File;

/**
 * @author : Unagi_zoso
 * @date : 2023-08-09
 */
public class EmptyFileException extends RuntimeException {

    public EmptyFileException() {
    }

    public EmptyFileException(String message) {
        super(message);
    }
}
