package com.litaa.projectkupica.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadPostFormDto {

    @NotBlank
    @Pattern(regexp = "^(?!\\s*$).{4,20}$", message = "비밀번호는 4자리 이상 20자 이하이어야 하며, 공백을 제외한 문자로 이루어져야 합니다.")
    private String password;

    private MultipartFile file;

    private String caption;

    @Max(value = 1)
    @Min(value = 0)
    private Integer erasedFlag;
}