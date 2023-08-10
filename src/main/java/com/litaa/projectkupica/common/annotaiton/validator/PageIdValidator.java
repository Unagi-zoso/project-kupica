package com.litaa.projectkupica.common.annotaiton.validator;

import com.litaa.projectkupica.common.annotaiton.PageIdValidation;
import com.litaa.projectkupica.exception.validation.ParameterValidationException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author : Unagi_zoso
 * @date : 2023-08-05
 */
public class PageIdValidator implements ConstraintValidator<PageIdValidation, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (null == value || 0 > value || 2147483640 < value) {
            throw new ParameterValidationException("pageId는 0보다 크거나 같아야 합니다.");
        }

        return true;
    }
}
