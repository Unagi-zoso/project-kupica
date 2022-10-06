package com.litaa.projectkupica.domain.mention;

import com.litaa.projectkupica.domain.member.Member;
import com.litaa.projectkupica.domain.member.MemberRepository;
import com.litaa.projectkupica.domain.photo.Photo;
import com.litaa.projectkupica.domain.photo.PhotoRepository;
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
class MentionRepositoryTest {
    @Autowired
    PostRepository postRep;

    @Autowired
    MentionRepository menRep;
    @Autowired
    EntityManager em;

    Post post;

    Mention mention;

    @BeforeEach
    void beforeEach() {
        post = Post.builder()
                .postFkMember(1)
                .build();

        postRep.save(post);

        mention = Mention.builder()
                .mentionFkMember(1)
                .mentionFkPost(post.getPostId())
                .content("참 잘 찍으시네요!")
                .build();

        menRep.save(mention);
    }

    @AfterEach
    void afterEach() {
        Query q = em.createNativeQuery("ALTER TABLE post ALTER COLUMN post_id RESTART WITH 1");
        q.executeUpdate();

        q = em.createNativeQuery("ALTER TABLE mention ALTER COLUMN mention_id RESTART WITH 1");
        q.executeUpdate();
    }

    @DisplayName("1. mention 생성")
    @Test
    void test_1(){
        assertEquals(menRep.findById(1).orElseThrow(RuntimeException::new).getContent(), "참 잘 찍으시네요!");
    }

    @DisplayName("2. mention 수정")
    @Test
    void test_2(){
        String prevSrc = menRep.findById(1).orElseThrow(RuntimeException::new).getContent();

        mention.setContent("이건 걸작이네요!");

        menRep.save(mention);

        assertNotEquals(prevSrc, menRep.findById(1).orElseThrow(RuntimeException::new).getContent());
    }

    @DisplayName("3. mention 삭제")
    @Test
    void test_3(){
        menRep.deleteById(1);
        assertFalse(menRep.findById(1).isPresent());
    }
}