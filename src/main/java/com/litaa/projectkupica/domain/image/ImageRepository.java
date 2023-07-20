package com.litaa.projectkupica.domain.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author : Unagi_zoso
 * @date : 2023-07-14
 */
public interface ImageRepository extends JpaRepository<Image, Integer> {

    @Query(value = "SELECT i.* FROM image i INNER JOIN post p ON i.post_id = p.post_id WHERE p.erased_flag = 0 ORDER BY i.created_date_time DESC LIMIT 5", nativeQuery = true)
    List<Image> findLatestImages5();

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE image SET image.source = ?2, image.cached_image_url = ?3, image.download_key = ?4 WHERE image.post_id = ?1", nativeQuery = true)
    void updateImage(int id, String source, String cachedImageUrl, String downloadKey);

    @Query(value = "SELECT i.download_key FROM image i INNER JOIN post p ON i.post_id = p.post_id WHERE p.erased_flag = 0 AND i.image_id = ?1", nativeQuery = true)
    String findDownloadKeyByImageId(int id);
}
