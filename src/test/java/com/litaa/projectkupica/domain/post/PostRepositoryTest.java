package com.litaa.projectkupica.domain.post;

import com.litaa.projectkupica.domain.image.Image;
import com.litaa.projectkupica.domain.image.ImageRepository;
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

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : Unagi_zoso
 * @date : 2022-10-06
 */

@ActiveProfiles("local")
@DataJpaTest
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    EntityManager entityManager;
    ArrayList<Post> givenPosts;
    ArrayList<Image> givenImages;

    @BeforeEach
    void defaultGivenImages5() {
        givenPosts = new ArrayList<>(5);
        givenImages = new ArrayList<>(5);

        for (int i = 0; i < 5; i++) {
            givenPosts.add(Post.builder().password("1234").caption("test caption").erasedFlag(0).build());
        }
        givenPosts = (ArrayList<Post>) postRepository.saveAll(givenPosts);

        entityManager.flush();

        for (int i = 0; i < 5; i++) {
            givenImages.add(Image.builder().source("/asdf1.jpg").cachedImageUrl("cf/asdf1.jpg").downloadKey("s3://temp").post(givenPosts.get(i)).build());
        }
        imageRepository.saveAll(givenImages);
    }

    @AfterEach
    void initializePostIdAndImageId0() {
        Query post_query = entityManager.createNativeQuery("ALTER TABLE post ALTER COLUMN post_id RESTART WITH 1");
        Query image_query = entityManager.createNativeQuery("ALTER TABLE image ALTER COLUMN image_id RESTART WITH 1");

        post_query.executeUpdate();
        image_query.executeUpdate();
    }

    @DisplayName("post 생성")
    @Test
    void testCreatePost(){

        assertEquals(postRepository.findById(1).orElseThrow(RuntimeException::new).getCaption(), "test caption");
        assertEquals(postRepository.findById(1).orElseThrow(RuntimeException::new).getPostId(), 1);

        entityManager.flush();
    }

    @DisplayName("전체 post 목록 가져오기")
    @Test
    void testFindAll(){

        assertEquals(postRepository.findAll().size(), 5);
        entityManager.flush();
    }

    @DisplayName("post 삭제")
    @Test
    void testDeleteById(){

        postRepository.deleteById(1);

        assertFalse(postRepository.findById(1).isPresent());
    }

    @DisplayName("지워진 post 빼고 페이지로 가져오기")
    @Test
    void testFindAllUnErased() {

        int pageNum = 0;
        int size = 5;
        PageRequest pageRequest = PageRequest.of(pageNum, size);

        postRepository.updatePostErasedTrue(1);

        Page<Post> page = postRepository.findAllUnErased(pageRequest);

        assertEquals(page.getContent().size(), 4);
    }

    @DisplayName("post의 eraseFlag를 true로 변경하기")
    @Test
    void When_EraseFlagWasNotUpdated_Then_False() {
        Post post4 = Post.builder().password("1234").caption("test caption").erasedFlag(0).build();

        postRepository.save(post4);

        int postId = post4.getPostId();
        postRepository.updatePostErasedTrue(postId);

        int postEraseFlag = postRepository.findById(postId).orElseThrow(RuntimeException::new).getErasedFlag();
        assertEquals(postEraseFlag, 1);
    }

    @DisplayName("post caption 변경하기")
    @Test
    void testUpdatePost() {

        Post testPost = postRepository.findPostById(givenPosts.get(1).getPostId());
        String prevCaption = testPost.getCaption();

        postRepository.updatePostCaption(givenPosts.get(1).getPostId(), "참 멋진 사진에요.");

        testPost = postRepository.findPostById(givenPosts.get(1).getPostId());
        String curCaption = testPost.getCaption();

        assertNotEquals(curCaption, prevCaption);
    }
}
