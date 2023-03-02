package com.litaa.projectkupica.domain.post;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author : Unagi_zoso
 * @date : 2022-10-06
 */

@ActiveProfiles("local")
@DataJpaTest
class PostRepositoryTest {

    @Autowired
    PostRepository postRep;

    @Autowired
    EntityManager em;

    Post post1, post2, post3;

    @BeforeEach
    void beforeEach() {

         post1 = Post.builder()
                .source("/asdf1.jpg")
                .caption("좀 많이 어렵네..")
                .build();

        post2 = Post.builder()
                .source("/asdf2.jpg")
                .caption("좀 많이 어렵네..")
                .build();

        post3 = Post.builder()
                .source("/asdf3.jpg")
                .caption("좀 많이 어렵네..")
                .build();

        postRep.save(post1);
        postRep.save(post2);
        postRep.save(post3);
    }

    @AfterEach
    void afterEach() {
        Query q = em.createNativeQuery("ALTER TABLE post ALTER COLUMN post_id RESTART WITH 1");
        q.executeUpdate();
    }

    @DisplayName("1. post 생성")
    @Test
    void test_1(){

        assertEquals(postRep.findById(1).orElseThrow(RuntimeException::new).getCaption(), "좀 많이 어렵네..");
        assertEquals(postRep.findById(1).orElseThrow(RuntimeException::new).getPostId(), 1);

        em.flush();
    }

    @DisplayName("2. 전체 post 목록 가져오기")
    @Test
    void test_2(){

        assertEquals(postRep.findAll().size(), 3);
        em.flush();
    }

    @DisplayName("3. post 삭제")
    @Test
    void test_4(){

        postRep.deleteById(1);

        assertFalse(postRep.findById(1).isPresent());
    }
}