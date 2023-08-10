package com.litaa.projectkupica.domain.image;

import com.litaa.projectkupica.domain.post.Post;
import com.litaa.projectkupica.domain.post.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author : Unagi_zoso
 * @date : 2023-07-14
 */

@ActiveProfiles("local")
@DataJpaTest
class ImageRepositoryTest {

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
        createPosts5();
        givenPosts = (ArrayList<Post>) postRepository.saveAll(givenPosts);

        entityManager.flush();
        createImages5();
        givenImages = (ArrayList<Image>) imageRepository.saveAll(givenImages);
    }

    @AfterEach
    void initializePostIdAndImageId0() {
        Query post_query = entityManager.createNativeQuery("ALTER TABLE post ALTER COLUMN post_id RESTART WITH 1");
        Query image_query = entityManager.createNativeQuery("ALTER TABLE image ALTER COLUMN image_id RESTART WITH 1");

        post_query.executeUpdate();
        image_query.executeUpdate();
    }

    @DisplayName("최근 등록된 이미지 5개를 가져옵니다.")
    @Test
    void testFindLatestImages5() {

        List<Image> images = imageRepository.findLatestImages5();

        int numOfImages = images.size();
        assertEquals(5, numOfImages);
    }

    @DisplayName("등록된 이미지가 4개일 때 최근 등록된 이미지 5개를 가져옵니다.")
    @Test
    void When_NumOfImagesEquals4_Expect_True() {
        imageRepository.delete(givenImages.get(0));

        List<Image> images = imageRepository.findLatestImages5();

        int numOfImages = images.size();
        assertEquals(4, numOfImages);
        assertNotEquals(5, numOfImages);
    }

    @DisplayName("등록된 이미지가 6개일 때 최근 등록된 이미지 5개를 가져옵니다.")
    @Test
    void When_NumOfImagesEquals6_Expect_False() {
        Post givenPost = Post.builder().password("1234").caption("test caption").erasedFlag(0).build();
        Image givenImage = Image.builder().source("/test1.jpg").cachedImageUrl("cf/test1.jpg").downloadKey("s3://temp").post(givenPost).build();
        imageRepository.save(givenImage);

        List<Image> images = imageRepository.findLatestImages5();

        int numOfImages = images.size();
        assertEquals(5, numOfImages);
        assertNotEquals(6, numOfImages);
    }

    @DisplayName("이미지를 업데이트 합니다.")
    @Test
    void testUpdateImage() {

        Image prevImage = imageRepository.findById(1).orElseThrow(RuntimeException::new);
        String prevSource = prevImage.getSource();
        String prevCachedUrl = prevImage.getCachedImageUrl();
        String prevDownloadKey = prevImage.getDownloadKey();

        imageRepository.updateImage(1, "newSource", "newCache", "newDown");

        Image curImage = imageRepository.findById(1).orElseThrow(RuntimeException::new);
        String curSource = curImage.getSource();
        String curCachedUrl = curImage.getCachedImageUrl();
        String curDownloadKey = curImage.getDownloadKey();

        assertNotEquals(curSource, prevSource);
        assertNotEquals(curCachedUrl, prevCachedUrl);
        assertNotEquals(curDownloadKey, prevDownloadKey);
    }

    private void createImages5() {
        givenImages = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            givenImages.add(Image.builder().source("/test1.jpg").cachedImageUrl("cf/test1.jpg").downloadKey("s3://temp").post(givenPosts.get(i)).build());
        }
    }

    private void createPosts5() {
        givenPosts = new ArrayList<>(5);

        for (int i = 0; i < 5; i++) {
            givenPosts.add(Post.builder().password("1234").caption("test caption").erasedFlag(0).build());
        }
    }
}