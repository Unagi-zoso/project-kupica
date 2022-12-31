package com.litaa.projectkupica.domain.photo;

/**
 * @author : Unagi_zoso
 * @date : 2022-10-07
 */
import com.litaa.projectkupica.domain.member.Member;
import com.litaa.projectkupica.domain.post.Post;
import com.litaa.projectkupica.domain.util.Auditable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(value = EntityListeners.class)
public class Photo implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int photoId;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Post photoFkPost;

    @Column(length = 300, nullable = false)
    private String source;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
