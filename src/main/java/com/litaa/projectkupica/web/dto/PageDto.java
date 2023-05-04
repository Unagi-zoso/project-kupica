package com.litaa.projectkupica.web.dto;

/**
 * @author : Unagi_zoso
 * @date : 2023-02-25
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageDto {

    @JsonProperty("lastPageId")
    Integer lastPageId;

    @JsonProperty("defaultPageSize")
    Integer defaultPageSize;
}
