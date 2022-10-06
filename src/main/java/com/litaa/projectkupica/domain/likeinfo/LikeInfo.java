package com.litaa.projectkupica.domain.likeinfo;

/**
 * @author : Unagi_zoso
 * @date : 2022-10-07
 */
import com.litaa.projectkupica.domain.util.Auditable;
import com.litaa.projectkupica.domain.util.EntityListener;
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
@EntityListeners(value = EntityListener.class)
public class LikeInfo implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int likeInfoId;

    @Column(nullable = false)
    private int likeInfoFkPost;

    @Column(nullable = false)
    private int likeInfoFkMember;

    @Column(nullable = false)
    private int toMember;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
