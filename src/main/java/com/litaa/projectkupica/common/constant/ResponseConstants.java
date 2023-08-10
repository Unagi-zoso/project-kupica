package com.litaa.projectkupica.common.constant;

import com.litaa.projectkupica.exception.GlobalExceptionHandler;
import com.litaa.projectkupica.exception.GlobalExceptionHandler.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author : Unagi_zoso
 * @date : 2023-08-04
 */
public class ResponseConstants {

    public static final ResponseEntity<?> FAIL_TO_CONVERT_IMAGE_FILE =
            new ResponseEntity<>(new ErrorResponse("파일 변환에 실패하였습니다."), HttpStatus.CONFLICT);

    public static final ResponseEntity<?> IMAGE_NOT_FOUND =
            new ResponseEntity<>(new ErrorResponse("이미지를 찾을 수 없습니다."), HttpStatus.NOT_FOUND);

    public static final ResponseEntity<?> FAIL_TO_UPLOAD_IMAGE_FILE =
            new ResponseEntity<>(new ErrorResponse("이미지 업로드에 실패하였습니다."), HttpStatus.CONFLICT);

    public static final ResponseEntity<?> POST_NOT_FOUND =
            new ResponseEntity<>(new ErrorResponse("게시글을 찾을 수 없습니다."), HttpStatus.NOT_FOUND);

    public static final ResponseEntity<?> WRONG_PASSWORD =
            new ResponseEntity<>(new ErrorResponse("비밀번호를 확인해주세요."), HttpStatus.UNAUTHORIZED);

    public static final ResponseEntity<?> PASSWORD_RULE_VIOLATION =
            new ResponseEntity<>(new ErrorResponse("비밀번호는 4자리 이상 20자 이하이어야 하며, 공백을 제외한 문자로 이루어져야 합니다."), HttpStatus.BAD_REQUEST);
}
