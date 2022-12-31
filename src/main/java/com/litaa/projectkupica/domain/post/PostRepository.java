package com.litaa.projectkupica.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author : Unagi_zoso
 * @date : 2022-10-06
 */
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query(value = "SELECT * FROM post WHERE post.post_fk_member = :memberId", nativeQuery = true)
    List<Post> findAllByPostFkMember(@Param("memberId") int memberId);
}
