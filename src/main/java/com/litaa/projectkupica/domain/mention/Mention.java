package com.litaa.projectkupica.domain.mention;

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
public class Mention implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mentionId;

    @Column(nullable = false)
    private int mentionFkPost;

    @Column(nullable = false)
    private int mentionFkMember;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
