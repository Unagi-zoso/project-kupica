package com.litaa.projectkupica.web.dto;

/**
 * @author : Unagi_zoso
 * @date : 2023-02-25
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageDto {

    @NotNull
    @Min(value = 0)
    @JsonProperty("lastPageId")
    private Integer lastPageId;

    @NotNull
    @Min(value = 1)
    @JsonProperty("defaultPageSize")
    private Integer defaultPageSize;
}
