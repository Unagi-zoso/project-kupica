package com.litaa.projectkupica.web.dto;
import com.litaa.projectkupica.domain.member.Member;
import com.litaa.projectkupica.domain.photo.Photo;
import com.litaa.projectkupica.domain.post.Post;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : Unagi_zoso
 * @date : 2022-12-31
 */

@Data
@NoArgsConstructor
public class PostDto {

    private MultipartFile file;
    private String caption;

    public Post toEntity(String postPhotoUrl) {
        return Post.builder()
                .caption(caption)
                .source(postPhotoUrl)
                .build();
    }
}
