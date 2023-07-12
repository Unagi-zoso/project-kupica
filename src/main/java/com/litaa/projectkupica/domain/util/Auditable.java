package com.litaa.projectkupica.domain.util;

import java.time.LocalDateTime;

/**
 * @author : Unagi_zoso
 * @date : 2022-10-03
 */
public interface Auditable {

    LocalDateTime getCreatedDateTime();
    LocalDateTime getUpdatedDateTime();

    void setCreatedDateTime(LocalDateTime createdDateTime);
    void setUpdatedDateTime(LocalDateTime updatedDateTime);
}
