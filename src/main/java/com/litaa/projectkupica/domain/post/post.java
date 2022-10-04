package com.litaa.projectkupica.domain.post;

import com.litaa.projectkupica.domain.util.Auditable;
import com.litaa.projectkupica.domain.util.EntityListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    int postId;

    @Column(nullable = false)
    int postFkMember;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

}
