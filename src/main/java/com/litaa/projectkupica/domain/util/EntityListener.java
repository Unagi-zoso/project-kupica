package com.litaa.projectkupica.domain.util;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

/**
 * @author : Unagi_zoso
 * @date : 2022-10-03
 */
public class EntityListener {
    @PrePersist
    public void setCreateTime(Object o) {
        if (o instanceof Auditable) {
            ((Auditable) o).setCreatedDateTime(LocalDateTime.now());
            ((Auditable) o).setUpdatedDateTime(LocalDateTime.now());
        }
    }

    @PreUpdate
    public void setUpdateTime(Object o) {
        if (o instanceof Auditable) {
            ((Auditable) o).setUpdatedDateTime(LocalDateTime.now());
        }
    }
}
