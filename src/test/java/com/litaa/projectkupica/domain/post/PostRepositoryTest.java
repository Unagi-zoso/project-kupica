package com.litaa.projectkupica.domain.post;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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

        post1 = Post.builder().password("1234").source("/asdf1.jpg").cachedImageUrl("cf/asdf1.jpg").caption("좀 많이 어렵네..").downloadKey("s3://temp").erasedFlag(0).build();
        post2 = Post.builder().password("1234").source("/asdf2.jpg").cachedImageUrl("cf/asdf2.jpg").caption("좀 많이 어렵네..").downloadKey("s3://temp").erasedFlag(1).build();
        post3 = Post.builder().password("1234").source("/asdf3.jpg").cachedImageUrl("cf/asdf3.jpg").caption("좀 많이 어렵네..").downloadKey("s3://temp").erasedFlag(0).build();

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
    void testCreatePost(){

        assertEquals(postRep.findById(1).orElseThrow(RuntimeException::new).getCaption(), "좀 많이 어렵네..");
        assertEquals(postRep.findById(1).orElseThrow(RuntimeException::new).getPostId(), 1);

        em.flush();
    }

    @DisplayName("2. 전체 post 목록 가져오기")
    @Test
    void testFindAll(){

        assertEquals(postRep.findAll().size(), 3);
        em.flush();
    }

    @DisplayName("3. post 삭제")
    @Test
    void testDeleteById(){

        postRep.deleteById(1);

        assertFalse(postRep.findById(1).isPresent());
    }

    @DisplayName("4. 지워진 post 빼고 페이지로 가져오기")
    @Test
    void testFindAllUnErased() {

        int pageNum = 0;
        int size = 3;
        PageRequest pageRequest = PageRequest.of(pageNum, size);

        Page<Post> page = postRep.findAllUnErased(pageRequest);

        assertEquals(page.getContent().size(), 2);
    }

    @DisplayName("5. 최근에 등록된 post 5개 가져오기")
    @Test
    void testFindPostsLatest5() {

        Post post4 = Post.builder().password("1234").source("/asdf4.jpg").cachedImageUrl("cf/asdf4.jpg").caption("좀 많이 어렵네..").downloadKey("s3://temp").erasedFlag(0).build();
        Post post5 = Post.builder().password("1234").source("/asdf5.jpg").cachedImageUrl("cf/asdf5.jpg").caption("좀 많이 어렵네..").downloadKey("s3://temp").erasedFlag(1).build();
        postRep.save(post4);
        postRep.save(post5);

        List<Post> posts = postRep.findPostsLatest5();

        int actualReturn = 3; // before each num of 0 erase_flag is 2, this scope's is 1
        assertNotNull(posts);
        assertEquals(posts.size(), actualReturn);
    }

    @DisplayName("6. post의 eraseFlag를 true로 변경하기")
    @Test
    void When_EraseFlagWasNotUpdated_Then_False() {
        Post post4 = Post.builder().password("1234").source("/asdf1.jpg").cachedImageUrl("cf/asdf1.jpg").caption("좀 많이 어렵네..").downloadKey("s3://temp")
                .erasedFlag(0)
                .build();
        postRep.save(post4);

        int postId = post4.getPostId();
        postRep.updatePostErasedTrue(postId);

        int postEraseFlag = postRep.findById(postId).orElseThrow(RuntimeException::new).getErasedFlag();
        assertEquals(postEraseFlag, 1);
    }

    @DisplayName("7. 지워진 post 빼고 페이지로 가져오기")
    @Test
    void testFindAllUnErasedPageSize8() {

        int pageNum = 0;
        int size = 6;
        PageRequest pageRequest = PageRequest.of(pageNum, size);

        Page<Post> page = postRep.findAllUnErased(pageRequest);

        assertEquals(page.getContent().size(), 2);
    }

    @DisplayName("8. 새로운 사진으로 post 업데이트하기")
    @Test
    void testUpdatePostWithNewImage() {

        Post testPost = postRep.findById(post1.getPostId()).orElseThrow(RuntimeException::new);

        String prevCaption = testPost.getCaption();
        String prevSource = testPost.getSource();
        String prevCachedImageUrl = testPost.getCachedImageUrl();
        String prevDownloadKey = testPost.getDownloadKey();

        postRep.updatePostWithNewImage(post1.getPostId(), "참 멋진 사진에요.", "S3:kupikupi", "cloudfront:kupikupi", "Down:kupikupi");

        testPost = postRep.findById(post1.getPostId()).orElseThrow(RuntimeException::new);

        String curCaption = testPost.getCaption();
        String curSource = testPost.getSource();
        String curCachedImageUrl = testPost.getCachedImageUrl();
        String curDownloadKey = testPost.getDownloadKey();

        assertNotEquals(curCaption, prevCaption);
        assertNotEquals(curSource, prevSource);
        assertNotEquals(curCachedImageUrl, prevCachedImageUrl);
        assertNotEquals(curDownloadKey, prevDownloadKey);
    }

    @DisplayName("9. 새로운 사진 없이 post 업데이트하기")
    @Test
    void testUpdatePostWithoutNewImage() {

        Post testPost = postRep.findById(post1.getPostId()).orElseThrow(RuntimeException::new);

        String prevCaption = testPost.getCaption();

        postRep.updatePostWithoutNewImage(post1.getPostId(), "참 멋진 사진에요.");

        testPost = postRep.findById(post1.getPostId()).orElseThrow(RuntimeException::new);

        String curCaption = testPost.getCaption();

        assertNotEquals(curCaption, prevCaption);
    }
}
