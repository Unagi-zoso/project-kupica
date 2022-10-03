package com.litaa.projectkupica.domain.member;

import javax.persistence.*;

import com.litaa.projectkupica.domain.util.Auditable;
import com.litaa.projectkupica.domain.util.EntityListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(value = EntityListener.class)
public class Member implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int memberId;

    @Column(nullable = false, length = 24)
    private String memberPassword;

    @Column(unique = true, nullable = false, length = 24)
    private String memberNickname;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
