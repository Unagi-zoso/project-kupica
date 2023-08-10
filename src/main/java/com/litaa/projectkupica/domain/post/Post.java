package com.litaa.projectkupica.domain.post;

import com.litaa.projectkupica.domain.image.Image;
import com.litaa.projectkupica.domain.util.Auditable;
import com.litaa.projectkupica.domain.util.EntityListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author : Unagi_zoso
 * @date : 2022-10-04
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(value = EntityListener.class)
public class Post implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;

    @Column(length = 160, nullable = false)
    private String password;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    private Image image;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String caption;

    @Column(nullable = false)
    private Integer erasedFlag;

    @Column(nullable = false)
    LocalDateTime createdDateTime;

    @Column(nullable = false)
    LocalDateTime updatedDateTime;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PostResponse {

        private Integer postId;
        private String caption;
        private Integer imageId;
        private String source;
        private String cachedImageUrl;
        private String downloadKey;

        public PostResponse(Post post) {
            this.postId = post.getPostId();
            this.caption = post.getCaption();
            this.imageId = post.getImage().getImageId();
            this.source = post.getImage().getSource();
            this.cachedImageUrl = post.getImage().getCachedImageUrl();
            this.downloadKey = post.getImage().getDownloadKey();
        }

        @Builder
        public PostResponse(Integer postId, String caption, Integer imageId, String source, String cachedImageUrl, String downloadKey) {
            this.postId = postId;
            this.caption = caption;
            this.imageId = imageId;
            this.source = source;
            this.cachedImageUrl = cachedImageUrl;
            this.downloadKey = downloadKey;
        }
    }
}
