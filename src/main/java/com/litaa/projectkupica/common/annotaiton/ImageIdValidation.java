package com.litaa.projectkupica.common.annotaiton;

import com.litaa.projectkupica.common.annotaiton.validator.ImageIdValidator;
import com.litaa.projectkupica.common.annotaiton.validator.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : Unagi_zoso
 * @date : 2023-08-05
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ImageIdValidator.class})
public @interface ImageIdValidation {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
