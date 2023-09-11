package com.litaa.projectkupica.common.annotaiton.validator;

import com.litaa.projectkupica.common.annotaiton.PageSizeValidation;
import com.litaa.projectkupica.common.annotaiton.PasswordValidation;
import com.litaa.projectkupica.exception.validation.ParameterValidationException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author : Unagi_zoso
 * @date : 2023-08-05
 */
public class PageSizeValidator implements ConstraintValidator<PageSizeValidation, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (null == value || 1 > value || 10 <= value) {
            throw new ParameterValidationException("pageSize는 1보다 크거나 같고 10보다 작아야 합니다.");
        }

        return true;
    }
}
