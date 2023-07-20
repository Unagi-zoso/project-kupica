package com.litaa.projectkupica.domain.image;

import com.litaa.projectkupica.domain.post.Post;
import com.litaa.projectkupica.domain.util.Auditable;
import com.litaa.projectkupica.domain.util.EntityListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author : Unagi_zoso
 * @date : 2023-07-13
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(value = EntityListener.class)
public class Image implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer imageId;

    @Column(length = 300, nullable = false)
    private String source;

    @Column(length = 300, nullable = false)
    private String cachedImageUrl;

    @Column(length = 300, nullable = false)
    private String downloadKey;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false)
    LocalDateTime createdDateTime;

    @Column(nullable = false)
    LocalDateTime updatedDateTime;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ImageResponse {

        private Integer imageId;
        private String source;
        private String cachedImageUrl;
        private String downloadKey;

        public ImageResponse(Image image) {
            this.imageId = image.getImageId();
            this.source = image.getSource();
            this.cachedImageUrl = image.getCachedImageUrl();
            this.downloadKey = image.getDownloadKey();
        }

        @Builder
        public ImageResponse(Integer imageId, String source, String cachedImageUrl, String downloadKey) {
            this.imageId = imageId;
            this.source = source;
            this.cachedImageUrl = cachedImageUrl;
            this.downloadKey = downloadKey;
        }
    }
}
