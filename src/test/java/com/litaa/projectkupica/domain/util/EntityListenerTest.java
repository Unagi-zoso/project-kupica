package com.litaa.projectkupica.domain.util;

import com.litaa.projectkupica.domain.post.Post;
import com.litaa.projectkupica.domain.post.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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
                .password("1234")
                .source("/asdf.jpg")
                .cachedImageUrl("cf/asdf.jpg")
                .caption("좋은 사진")
                .erasedFlag(0)
                .downloadKey("s3://temp")
                .build();

        postRep.save(post1);
    }

    @AfterEach
    void afterEach() {
        Query q = em.createNativeQuery("ALTER TABLE post ALTER COLUMN post_id RESTART WITH 1");
        q.executeUpdate();
    }

    @DisplayName("1. PrePersist 데이터 입력 시 정상작동")
    @Test
    void When_CreatedAtIsNull_Then_False(){

        assertNotNull(postRep.findById(1).orElseThrow(RuntimeException::new).getCreatedDateTime());
    }

    @DisplayName("2. PreUpdate 데이터 입력 시 정상작동")
    @Test
    void When_UpdatedAtIsNull_Then_False(){

        assertNotNull(postRep.findById(1).orElseThrow(RuntimeException::new).getUpdatedDateTime());
    }

    @DirtiesContext(methodMode = BEFORE_METHOD)
    @DisplayName("3. PreUpdate 수정 시 정상작동")
    @Test
    void When_UpdateTimeWasNotUpdated_Then_False(){

        LocalDateTime prevUpdate = postRep.findById(1).orElseThrow(RuntimeException::new).getUpdatedDateTime();

        post1.setCaption("이 사진이 더 멋져!");
        postRep.save(post1);
        em.flush();

        LocalDateTime curUpdate = postRep.findById(1).orElseThrow(RuntimeException::new).getUpdatedDateTime();

        assertNotEquals(prevUpdate, curUpdate);

    }
}