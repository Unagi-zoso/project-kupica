package com.litaa.projectkupica.common.annotaiton.validator;

import com.litaa.projectkupica.common.annotaiton.PasswordValidation;
import com.litaa.projectkupica.exception.validation.ParameterValidationException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author : Unagi_zoso
 * @date : 2023-08-05
 */
public class PasswordValidator implements ConstraintValidator<PasswordValidation, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (null == value || !value.matches("^(?!\\s).{4,20}$")) {
            throw new ParameterValidationException("비밀번호는 4자리 이상 20자 이하이어야 하며, 공백을 제외한 문자로 이루어져야 합니다.");
        }

        return true;
    }
}
