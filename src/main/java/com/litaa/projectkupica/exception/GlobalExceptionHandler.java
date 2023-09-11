package com.litaa.projectkupica.exception;

import com.litaa.projectkupica.common.constant.ResponseConstants;
import com.litaa.projectkupica.exception.File.EmptyFileException;
import com.litaa.projectkupica.exception.image.ImageConversionException;
import com.litaa.projectkupica.exception.image.ImageNotFoundException;
import com.litaa.projectkupica.exception.image.ImageUploadException;
import com.litaa.projectkupica.exception.post.PostNotFoundException;
import com.litaa.projectkupica.exception.post.WrongPasswordException;
import com.litaa.projectkupica.exception.validation.ParameterValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author : Unagi_zoso
 * @date : 2023-08-03
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ImageConversionException.class)
    public final ResponseEntity<?> handleImageConversionException(ImageConversionException e) {
        LOGGER.debug(e.getLocalizedMessage());
        return ResponseConstants.FAIL_TO_CONVERT_IMAGE_FILE;
    }

    @ExceptionHandler(ImageNotFoundException.class)
    public final ResponseEntity<?> handleImageNotFoundException(ImageNotFoundException e) {
        LOGGER.debug(e.getLocalizedMessage());
        return ResponseConstants.IMAGE_NOT_FOUND;
    }

    @ExceptionHandler(ImageUploadException.class)
    public final ResponseEntity<?> handleImageUploadException(ImageUploadException e) {
        LOGGER.debug(e.getLocalizedMessage());
        return ResponseConstants.FAIL_TO_UPLOAD_IMAGE_FILE;
    }

    @ExceptionHandler(PostNotFoundException.class)
    public final ResponseEntity<?> handlePostNotFoundException(PostNotFoundException e) {
        LOGGER.debug(e.getLocalizedMessage());
        return ResponseConstants.POST_NOT_FOUND;
    }

    @ExceptionHandler(WrongPasswordException.class)
    public final ResponseEntity<?> handleWrongPasswordException(WrongPasswordException e) {
        LOGGER.debug(e.getLocalizedMessage());
        return ResponseConstants.WRONG_PASSWORD;
    }

    @ExceptionHandler(ParameterValidationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ParameterValidationException e) {
        LOGGER.debug(e.getLocalizedMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<?> handleEmptyFileException(EmptyFileException e) {
        LOGGER.debug(e.getLocalizedMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }

    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}

