package com.litaa.projectkupica.exception.validation;

/**
 * @author : Unagi_zoso
 * @date : 2023-08-05
 */
public class ParameterValidationException extends RuntimeException {

    public ParameterValidationException() {
    }

    public ParameterValidationException(String message) {
        super(message);
    }
}
