package com.litaa.projectkupica.domain.util;

import com.litaa.projectkupica.domain.member.Member;
import com.litaa.projectkupica.domain.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.BEFORE_METHOD;

/**
 * @author : Unagi_zoso
 * @date : 2022-10-03
 */
@Transactional
@SpringBootTest
class EntityListenerTest {

    @Autowired
    MemberRepository memRep;

    @Autowired
    EntityManager em;



    @DisplayName("1. PrePersist, PreUpdate 데이터 입력 시 정상작동")
    @Test
    void test_1(){
        Member member = Member.builder()
                .memberNickname("zoso")
                .memberPassword("u1234")
                .build();

        memRep.save(member);

        assertNotNull(memRep.findById(1).orElseThrow(RuntimeException::new).getCreatedAt());
        assertNotNull(memRep.findById(1).orElseThrow(RuntimeException::new).getUpdatedAt());


    }

    @DirtiesContext(methodMode = BEFORE_METHOD)
    @DisplayName("2. PreUpdate 수정 시 정상작동")
    @Test
    void test_2(){
        Member member = Member.builder()
                .memberNickname("zoso")
                .memberPassword("u1234")
                .build();

        memRep.save(member);

        System.out.println(memRep.findAll());

        LocalDateTime prevUpdate = memRep.findById(1).orElseThrow(RuntimeException::new).getUpdatedAt();

        member.setMemberNickname("Unagi~");
        memRep.save(member);

        em.flush();

        LocalDateTime curUpdate = memRep.findById(1).orElseThrow(RuntimeException::new).getUpdatedAt();
        System.out.println(memRep.findAll());

        System.out.println(prevUpdate + "   " + curUpdate);
        assertNotEquals(prevUpdate, curUpdate);


    }
}