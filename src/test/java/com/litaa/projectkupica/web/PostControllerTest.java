package com.litaa.projectkupica.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.litaa.projectkupica.domain.post.Post.PostResponse;
import com.litaa.projectkupica.service.PostService;
import com.litaa.projectkupica.web.dto.DeletePostFormDto;
import com.litaa.projectkupica.web.dto.UpdatePostFormDto;
import com.litaa.projectkupica.web.dto.UploadPostFormDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * @author : Unagi_zoso
 * @date : 2023-03-02
 */
@ActiveProfiles("local")
@WithMockUser(username = "테스트 관리자", roles = {"SUPER"})
@WebMvcTest(PostController.class)
class PostControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    PostService postServiceMock;
    @Autowired
    PostController postController;

    /**
     * @throws Exception
     * @see com.litaa.projectkupica.service.PostService#uploadPost(UploadPostFormDto)
     */
    @DisplayName("post 업로드 하기")
    @Test
    void testUploadPost() throws Exception {

        final String fileName = "testimage1"; //파일명
        final String contentType = "jpg"; //파일타입
        final String filePath = "src/test/resources/testimage/" + fileName + "." + contentType; //파일경로
        FileInputStream fileInputStream = new FileInputStream(filePath);

        MockMultipartFile file = new MockMultipartFile(
                "testImage1", //name
                fileName + "." + contentType, //originalFilename
                contentType,
                fileInputStream
        );

        UploadPostFormDto postDto = UploadPostFormDto.builder()
                .file(file)
                .password("qwer1234")
                .caption("실로.. 좋은 사진이로다..")
                .erasedFlag(0)
                .build();

        when(postServiceMock.uploadPost(any(UploadPostFormDto.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(
                        multipart("/posts")
                                .file((MockMultipartFile) postDto.getFile())
                                .param("password", postDto.getPassword())
                                .param("caption", postDto.getCaption())
                                .param("erasedFlag", Integer.toString(postDto.getErasedFlag()))
                                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(postServiceMock, times(1)).uploadPost(any(UploadPostFormDto.class));
    }

    @DisplayName("post 페이지로 넘겨받기")
    @Test
    void testFindPostsByPageRequest() throws Exception {

        List<PostResponse> posts = Stream.of(
                PostResponse.builder().postId(1).caption("test caption").source("s3://source").cachedImageUrl("cf://source").downloadKey("down://source").build(),
                PostResponse.builder().postId(2).caption("test caption").source("s3://source").cachedImageUrl("cf://source").downloadKey("down://source").build(),
                PostResponse.builder().postId(3).caption("test caption").source("s3://source").cachedImageUrl("cf://source").downloadKey("down://source").build()).collect(Collectors.toList());

        int lastPageId = 0;
        int defaultPageSize = 3;

        when(postServiceMock.findPostsByPageRequest(lastPageId, defaultPageSize)).thenReturn(posts);

        String ret = objectMapper.writeValueAsString(posts);

        mockMvc.perform(get("/posts/page")
                        .param("lastPageId", Integer.toString(lastPageId))
                        .param("pageSize", Integer.toString(defaultPageSize))
                        .with(csrf()))
                .andDo(print())
                .andExpect(content().json(ret));
    }

    @DisplayName("post 삭제하기 ")
    @Test
    void testDeletePost() {

        int postId = 1;
        DeletePostFormDto deletePostFormDto = DeletePostFormDto.builder()
                .password("qwer1234")
                .build();

        when(postServiceMock.updatePostErasedTrue(postId, deletePostFormDto.getPassword())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // when
        try {
            mockMvc.perform(delete("/posts/{postId}/delete", postId)
                            .param("password", deletePostFormDto.getPassword())
                            .with(csrf())
                    )
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception ignored) {
        }
    }

    @DisplayName("post 수정하기 ")
    @Test
    void testUpdatePost() throws IOException {

        final String fileName = "testimage1";
        final String contentType = "jpg";
        final String filePath = "src/test/resources/testimage/" + fileName + "." + contentType;
        FileInputStream fileInputStream = new FileInputStream(filePath);

        MockMultipartFile testImage1 = new MockMultipartFile(
                "testImage1",
                fileName + "." + contentType,
                contentType,
                fileInputStream
        );

        UpdatePostFormDto updatePostFormDto = UpdatePostFormDto.builder()
                .caption("해는 뜬다.")
                .file(testImage1)
                .password("qwer1234")
                .build();

        when(postServiceMock.updatePost(1, updatePostFormDto))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // when
        ResponseEntity<?> response = postController.updatePost(1, updatePostFormDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @DisplayName("UploadPostFormDto 유효성 검사")
    @Test
    void testUploadPostFormDtoValidation_Password() throws IOException {

        final String fileName = "testimage1";
        final String contentType = "jpg";
        final String filePath = "src/test/resources/testimage/" + fileName + "." + contentType;
        FileInputStream fileInputStream = new FileInputStream(filePath);

        MockMultipartFile file = new MockMultipartFile(
                "testImage1", //name
                fileName + "." + contentType, //originalFilename
                contentType,
                fileInputStream
        );

        UploadPostFormDto postDto = UploadPostFormDto.builder()
                .file(file)
                .password("qwe")
                .caption("안녕하세요?")
                .erasedFlag(0)
                .build();

        when(postServiceMock.uploadPost(any(UploadPostFormDto.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        try {
            mockMvc.perform(
                            multipart("/posts")
                                    .file((MockMultipartFile) postDto.getFile())
                                    .param("password", postDto.getPassword())
                                    .param("caption", postDto.getCaption())
                                    .param("erasedFlag", Integer.toString(postDto.getErasedFlag()))
                                    .with(csrf()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception ignored) {
        }
    }

    @DisplayName("UploadPostFormDto 유효성 검사")
    @Test
    void testUploadPostFormDtoValidation_erasedFlag() throws IOException {

        final String fileName = "testimage1";
        final String contentType = "jpg";
        final String filePath = "src/test/resources/testimage/" + fileName + "." + contentType; //파일경로
        FileInputStream fileInputStream = new FileInputStream(filePath);

        MockMultipartFile file = new MockMultipartFile(
                "testImage1", //name
                fileName + "." + contentType, //originalFilename
                contentType,
                fileInputStream
        );

        UploadPostFormDto postDto = UploadPostFormDto.builder()
                .file(file)
                .password("qwe1234")
                .caption("안녕하세요?")
                .erasedFlag(5)
                .build();

        when(postServiceMock.uploadPost(any(UploadPostFormDto.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        try {
            mockMvc.perform(
                            multipart("/posts")
                                    .file((MockMultipartFile) postDto.getFile())
                                    .param("password", postDto.getPassword())
                                    .param("caption", postDto.getCaption())
                                    .param("erasedFlag", Integer.toString(postDto.getErasedFlag()))
                                    .with(csrf()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception ignored) {
        }
    }
}