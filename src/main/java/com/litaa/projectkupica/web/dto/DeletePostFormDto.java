package com.litaa.projectkupica.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author : Unagi_zoso
 * @date : 2023-04-09
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeletePostFormDto {
    @JsonProperty("id")
    Integer id;

    @JsonProperty("password")
    String password;
}
