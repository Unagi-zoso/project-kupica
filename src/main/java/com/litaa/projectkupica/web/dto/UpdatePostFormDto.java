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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePostFormDto {

    @NotNull
    @JsonProperty("id")
    private Integer id;

    @NotBlank
    @Pattern(regexp = "^(?!\\s*$).{4,20}$", message = "비밀번호는 4자리 이상 20자 이하이어야 하며, 공백을 제외한 문자로 이루어져야 합니다.")
    @JsonProperty("password")
    private String password;

    @JsonProperty("caption")
    private String caption;

    private MultipartFile file;
}
