package com.litaa.projectkupica.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * @author : Unagi_zoso
 * @date : 2023-08-02
 */
@Component
@Aspect
public class HateoasAspect {

    @Pointcut("execution(* com.litaa.projectkupica.web..*(..)) && !execution(* com.litaa.projectkupica.web.ImageController.download(..))")
    public void hateoasPointcut() {}

    @AfterReturning(value = "hateoasPointcut()", returning = "response")
    public void appendHateoas(final JoinPoint joinPoint, ResponseEntity<?> response) {
        Arrays.stream(joinPoint.getTarget().getClass().getDeclaredMethods()).forEach(method -> {
            WebMvcLinkBuilder webMvcLinkBuilder = linkTo(method);
            ((RepresentationModel<?>) response.getBody()).add(webMvcLinkBuilder.withRel(method.getName()));
        });
    }
}
