package com.litaa.projectkupica.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author : Unagi_zoso
 * @date : 2022-10-06
 */
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query(value = "SELECT * FROM post WHERE post.erased_flag = 0", nativeQuery = true)
    Page<Post> findAllUnErased(Pageable pageable);

    @Query(value = "SELECT * FROM post WHERE post.erased_flag = 0 ORDER BY created_date_time DESC LIMIT 0, 5", nativeQuery = true)
    List<Post> findPostsLatest5();

    @Query(value = "SELECT post.password FROM post WHERE post.post_id = ?1", nativeQuery = true)
    String findPasswordById(int id);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE post SET post.erased_flag = 1 WHERE post.post_id = ?1", nativeQuery = true)
    void updatePostErasedTrue(int id);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE post SET post.caption =  ?2, post.source = ?3, post.cached_image_url = ?4, post.download_key = ?5 WHERE post.post_id = ?1", nativeQuery = true)
    void updatePostWithNewImage(int id, String caption, String source, String cachedImageUrl, String downloadKey);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE post SET post.caption =  ?2 WHERE post.post_id = ?1", nativeQuery = true)
    void updatePostWithoutNewImage(int id, String caption);

}
