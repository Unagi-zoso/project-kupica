package com.litaa.projectkupica.domain.photo;

import com.litaa.projectkupica.domain.post.Post;
import com.litaa.projectkupica.domain.post.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : Unagi_zoso
 * @date : 2022-10-07
 */
@DataJpaTest
class PhotoRepositoryTest {

    @Autowired
    PostRepository postRep;

    @Autowired
    PhotoRepository photoRep;
    @Autowired
    EntityManager em;

    Post post;

    Photo photo;

    @BeforeEach
    void beforeEach() {
        post = Post.builder()
                .postFkMember(1)
                .build();

        postRep.save(post);

        photo = Photo.builder()
                .photoFkMember(post.getPostFkMember())
                .photoFkPost(post.getPostId())
                .source("/ABCD.jpeg")
                .build();

        photoRep.save(photo);
    }

    @AfterEach
    void afterEach() {
        Query q = em.createNativeQuery("ALTER TABLE post ALTER COLUMN post_id RESTART WITH 1");
        q.executeUpdate();

        q = em.createNativeQuery("ALTER TABLE photo ALTER COLUMN photo_id RESTART WITH 1");
        q.executeUpdate();
    }

    @DisplayName("1. photo 생성")
    @Test
    void test_1(){
        assertEquals(photoRep.findById(1).orElseThrow(RuntimeException::new).getSource(), "/ABCD.jpeg");
    }

    @DisplayName("2. photo 수정")
    @Test
    void test_2(){
        String prevSrc = photoRep.findById(1).orElseThrow(RuntimeException::new).getSource();

        photo.setSource("/HELLO.jpeg");

        photoRep.save(photo);

        assertNotEquals(prevSrc, photoRep.findById(1).orElseThrow(RuntimeException::new).getSource());
    }

    @DisplayName("3. photo 삭제")
    @Test
    void test_3(){
        photoRep.deleteById(1);
        assertFalse(photoRep.findById(1).isPresent());
    }
}