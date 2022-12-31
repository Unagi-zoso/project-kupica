package com.litaa.projectkupica.domain.post;

import com.litaa.projectkupica.domain.member.Member;
import com.litaa.projectkupica.domain.member.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author : Unagi_zoso
 * @date : 2022-10-06
 */
@DataJpaTest
class PostRepositoryTest {

    @Autowired
    MemberRepository memRep;

    @Autowired
    PostRepository postRep;

    @Autowired
    EntityManager em;

    Member member;

    @BeforeEach
    void beforeEach() {
        member = Member.builder()
            .memberNickname("개구리~")
            .memberPassword("a1234")
            .build();

        memRep.save(member);
    }

    @AfterEach
    void afterEach() {
        Query q = em.createNativeQuery("ALTER TABLE post ALTER COLUMN post_id RESTART WITH 1");
        q.executeUpdate();

        q = em.createNativeQuery("ALTER TABLE member ALTER COLUMN member_id RESTART WITH 1");
        q.executeUpdate();
    }

    @DisplayName("1. post 생성")
    @Test
    void test_1(){
        Post newPost = Post.builder()
                .caption("뇨로로롱")
                .postFkMember(member)
                .build();

        postRep.save(newPost);

        assertEquals(postRep.findById(1).orElseThrow(RuntimeException::new).getCaption(), "뇨로로롱");
        assertEquals(postRep.findById(1).orElseThrow(RuntimeException::new).getPostFkMember(), 1);
    }

    @DisplayName("2. member의 post 목록 보기")
    @Test
    void test_2(){
        Post newPost1 = Post.builder()
                .postFkMember(member)
                .build();

        Post newPost2 = Post.builder()
                .postFkMember(member)
                .build();

        Post newPost3 = Post.builder()
                .postFkMember(member)
                .build();

        postRep.save(newPost1);
        postRep.save(newPost2);
        postRep.save(newPost3);

        em.flush();

        assertEquals(postRep.findAllByPostFkMember(member.getMemberId()).size(), 3);
    }

    @DisplayName("3. 전체 post 보기")
    @Test
    void test_3() {
        Post newPost1 = Post.builder()
                .postFkMember(member)
                .build();

        Post newPost2 = Post.builder()
                .postFkMember(member)
                .build();

        Post newPost3 = Post.builder()
                .postFkMember(member)
                .build();

        postRep.save(newPost1);
        postRep.save(newPost2);
        postRep.save(newPost3);

        em.flush();

        assertEquals(postRep.findAll().size(), 3);
    }

    @DisplayName("4. post 삭제")
    @Test
    void test_4(){
        Post newPost = Post.builder()
                .postFkMember(member)
                .build();

        postRep.save(newPost);

        postRep.deleteById(1);

        em.flush();

        assertFalse(postRep.findById(1).isPresent());
    }
}