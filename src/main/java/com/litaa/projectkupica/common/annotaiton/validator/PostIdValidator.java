package com.litaa.projectkupica.common.annotaiton.validator;

import com.litaa.projectkupica.common.annotaiton.PasswordValidation;
import com.litaa.projectkupica.common.annotaiton.PostIdValidation;
import com.litaa.projectkupica.exception.validation.ParameterValidationException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author : Unagi_zoso
 * @date : 2023-08-05
 */
public class PostIdValidator implements ConstraintValidator<PostIdValidation, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (null == value || 0 >= value || 2147483640 <= value) {
            throw new ParameterValidationException("post_id는 0보다 커야합니다.");
        }

        return true;
    }
}
