package com.litaa.projectkupica.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author : Unagi_zoso
 * @date : 2022-10-06
 */
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query(value = "SELECT *  FROM image i INNER JOIN post p ON i.post_id = p.post_id WHERE p.erased_flag = 0", countQuery = "SELECT COUNT(*) FROM post WHERE post.erased_flag = 0", nativeQuery = true)
    Page<Post> findAllUnErased(Pageable pageable);

    @Query(value = "SELECT * FROM image i INNER JOIN post p ON i.post_id = p.post_id WHERE p.post_id = ?1 AND p.erased_flag = 0", nativeQuery = true)
    Post findPostById(int id);

    @Query(value = "SELECT post.password FROM post WHERE post.post_id = ?1", nativeQuery = true)
    String findPasswordById(int id);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE post SET post.erased_flag = 1 WHERE post.post_id = ?1", nativeQuery = true)
    void updatePostErasedTrue(int id);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE post SET post.caption =  ?2 WHERE post.post_id = ?1", nativeQuery = true)
    void updatePostCaption(int id, String caption);

}
