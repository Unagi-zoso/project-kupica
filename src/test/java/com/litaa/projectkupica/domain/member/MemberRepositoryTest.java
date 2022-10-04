package com.litaa.projectkupica.domain.member;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : Unagi_zoso
 * @date : 2022-10-03
 */
@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memRep;

    @Autowired
    EntityManager em;

    @AfterEach
    void afterEach() {
        Query q = em.createNativeQuery("ALTER TABLE member ALTER COLUMN member_id RESTART WITH 1");
        q.executeUpdate();
    }

    @DisplayName("1. member 추가")
    @Test
    void test_1(){
        Member member = Member.builder()
                .memberNickname("zoso")
                .memberPassword("s1234")
                .build();

        memRep.save(member);

        assertEquals(memRep.findById(1).orElseThrow(RuntimeException::new).getMemberNickname(), "zoso");
        assertEquals(memRep.findById(1).orElseThrow(RuntimeException::new).getMemberPassword(), "s1234");
    }

    @DisplayName("2. member 목록 보기")
    @Test
    void test_2(){
        Member member1 = Member.builder()
                .memberNickname("zoso")
                .memberPassword("s1234")
                .build();

        memRep.save(member1);

        Member member2 = Member.builder()
                .memberNickname("도롱뇽 드라이브")
                .memberPassword("s33234")
                .build();

        memRep.save(member2);

        memRep.flush();

        ArrayList<Member> members = new ArrayList<>();
        members.add(member1);
        members.add(member2);

        assertEquals(memRep.findAll(), members);
    }

    @DisplayName("3. member 한 명 보기")
    @Test
    void test_3(){
        Member member1 = Member.builder()
                .memberNickname("zoso")
                .memberPassword("s1234")
                .build();

        memRep.save(member1);

        Member member2 = Member.builder()
                .memberNickname("도롱뇽 드라이브")
                .memberPassword("s33234")
                .build();

        memRep.save(member2);

        memRep.flush();

        ArrayList<Member> members = new ArrayList<>();
        members.add(member1);
        members.add(member2);

        assertEquals(memRep.findAll().get(0).getMemberNickname(), member1.getMemberNickname());
        assertEquals(memRep.findAll().get(1).getMemberNickname(), member2.getMemberNickname());
    }

    @DisplayName("4. member 수정")
    @Test
    void test_4(){
        Member member = Member.builder()
                .memberNickname("zoso")
                .memberPassword("s1234")
                .build();

        memRep.save(member);

        String prevNickname = memRep.findById(1).orElseThrow(RuntimeException::new).getMemberNickname();
        String prevPassword = memRep.findById(1).orElseThrow(RuntimeException::new).getMemberPassword();

        member.setMemberNickname("도롱뇽꼬리");
        member.setMemberPassword("a1029348");
        memRep.save(member);

        em.flush();

        String curNickname = memRep.findById(1).orElseThrow(RuntimeException::new).getMemberNickname();
        String curPassword = memRep.findById(1).orElseThrow(RuntimeException::new).getMemberPassword();

        assertNotEquals(prevNickname, curNickname);
        assertNotEquals(prevPassword, curPassword);
    }

    @DisplayName("5. member 삭제")
    @Test
    void test_5(){
        Member member = Member.builder()
                .memberNickname("zoso")
                .memberPassword("s1234")
                .build();

        memRep.save(member);

        assertNotNull(memRep.findById(1));

        memRep.deleteById(1);

        assertEquals(memRep.findById(1), Optional.empty());
    }
}