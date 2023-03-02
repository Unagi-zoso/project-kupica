package com.litaa.projectkupica.domain.util;

import com.litaa.projectkupica.domain.post.Post;
import com.litaa.projectkupica.domain.post.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.BEFORE_METHOD;

/**
 * @author : Unagi_zoso
 * @date : 2022-10-03
 */

@ActiveProfiles("local")
@Transactional
@SpringBootTest
class EntityListenerTest {

    @Autowired
    PostRepository postRep;

    @Autowired
    EntityManager em;

    Post post1;

    @BeforeEach
    void beforeEach() {
        post1 = Post.builder()
                .source("/asdf.jpg")
                .caption("좋은 사진")
                .build();

        postRep.save(post1);
    }

    @DisplayName("1. PrePersist, PreUpdate 데이터 입력 시 정상작동")
    @Test
    void test_1(){

        assertNotNull(postRep.findById(1).orElseThrow(RuntimeException::new).getCreatedAt());
        assertNotNull(postRep.findById(1).orElseThrow(RuntimeException::new).getUpdatedAt());


    }

    @DirtiesContext(methodMode = BEFORE_METHOD)
    @DisplayName("2. PreUpdate 수정 시 정상작동")
    @Test
    void test_2(){

        LocalDateTime prevUpdate = postRep.findById(1).orElseThrow(RuntimeException::new).getUpdatedAt();

        post1.setCaption("이 사진이 더 멋져!");
        postRep.save(post1);

        em.flush();

        LocalDateTime curUpdate = postRep.findById(1).orElseThrow(RuntimeException::new).getUpdatedAt();

        assertNotEquals(prevUpdate, curUpdate);

    }
}