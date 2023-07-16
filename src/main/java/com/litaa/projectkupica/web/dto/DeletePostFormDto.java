package com.litaa.projectkupica.web.dto;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author : Unagi_zoso
 * @date : 2023-04-09
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeletePostFormDto {

    @NotBlank
    @Pattern(regexp = "^(?!\\s*$).{4,20}$", message = "비밀번호는 4자리 이상 20자 이하이어야 하며, 공백을 제외한 문자로 이루어져야 합니다.")
    @JsonProperty("password")
    private String password;
}
