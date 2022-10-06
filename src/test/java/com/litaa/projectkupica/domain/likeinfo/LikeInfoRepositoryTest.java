package com.litaa.projectkupica.domain.likeinfo;

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
class LikeInfoRepositoryTest {
    @Autowired
    PostRepository postRep;

    @Autowired
    LikeInfoRepository likeRep;
    @Autowired
    EntityManager em;

    Post post;

    LikeInfo like;

    @BeforeEach
    void beforeEach() {
        post = Post.builder()
                .postFkMember(1)
                .build();

        postRep.save(post);

        like = LikeInfo.builder()
                .likeInfoFkMember(1)
                .likeInfoFkPost(post.getPostId())
                .toMember(2)
                .build();

        likeRep.save(like);
    }

    @AfterEach
    void afterEach() {
        Query q = em.createNativeQuery("ALTER TABLE post ALTER COLUMN post_id RESTART WITH 1");
        q.executeUpdate();

        q = em.createNativeQuery("ALTER TABLE like_info ALTER COLUMN like_info_id RESTART WITH 1");
        q.executeUpdate();
    }

    @DisplayName("1. like_info 생성")
    @Test
    void test_1(){
        assertEquals(likeRep.findById(1).orElseThrow(RuntimeException::new).getToMember(), 2);
    }

    @DisplayName("2. like_info 수정")
    @Test
    void test_2(){
        int prevToMem = likeRep.findById(1).orElseThrow(RuntimeException::new).getToMember();

        like.setToMember(3);

        likeRep.save(like);

        assertNotEquals(prevToMem, likeRep.findById(1).orElseThrow(RuntimeException::new).getToMember());
    }

    @DisplayName("3. like_info 삭제")
    @Test
    void test_3(){
        likeRep.deleteById(1);
        assertFalse(likeRep.findById(1).isPresent());
    }
}