package com.litaa.projectkupica.web.dto;

import com.litaa.projectkupica.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {

    private String password;
    private MultipartFile file;
    private String caption;
    private Integer eraseFlag;

    public Post toEntity(String postPhotoUrl, String imageDownloadUrl) {
        return Post.builder()
                .password(password)
                .caption(caption)
                .source(postPhotoUrl)
                .downloadKey(imageDownloadUrl)
                .eraseFlag(eraseFlag)
                .build();
    }
}
