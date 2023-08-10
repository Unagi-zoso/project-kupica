package com.litaa.projectkupica.web.dto;

/**
 * @author : Unagi_zoso
 * @date : 2023-06-02
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePostRequest {

    @JsonProperty("caption")
    private String caption;

    private MultipartFile file;
}
