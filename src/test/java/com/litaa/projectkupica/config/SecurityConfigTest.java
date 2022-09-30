package com.litaa.projectkupica.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private Environment environment;

    @DisplayName("1. env로 profile 제대로 가져오는지 테스트")
    @Test
    void test_1(){
        assertEquals("local", environment.getActiveProfiles()[0]);
    }
}