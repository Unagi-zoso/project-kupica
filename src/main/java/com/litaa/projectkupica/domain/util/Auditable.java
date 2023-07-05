package com.litaa.projectkupica.domain.util;

import java.time.LocalDateTime;

/**
 * @author : Unagi_zoso
 * @date : 2022-10-03
 */
public interface Auditable {

    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();

    void setCreatedAt(LocalDateTime createdAt);
    void setUpdatedAt(LocalDateTime updatedAt);
}
