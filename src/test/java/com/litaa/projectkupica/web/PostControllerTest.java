package com.litaa.projectkupica.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.litaa.projectkupica.domain.post.Post;
import com.litaa.projectkupica.service.PostService;
import com.litaa.projectkupica.web.dto.DeletePostFormDto;
import com.litaa.projectkupica.web.dto.PageDto;
import com.litaa.projectkupica.web.dto.PostDto;
import com.litaa.projectkupica.web.dto.UpdatePostFormDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    PostService postService;

    @Autowired
    PostController postController;

    /**
     * @throws Exception
     * @see com.litaa.projectkupica.service.PostService#uploadPost(PostDto)
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

        PostDto postDto = PostDto.builder()
                .file(file)
                .password("qwer1234")
                .caption("실로.. 좋은 사진이로다..")
                .eraseFlag(0)
                .build();

        when(postService.uploadPost(any(PostDto.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(
                        multipart("/post/upload")
                                .file((MockMultipartFile) postDto.getFile())
                                .param("password", postDto.getPassword())
                                .param("caption", postDto.getCaption())
                                .param("eraseFlag", Integer.toString(postDto.getEraseFlag()))
                                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(postService, times(1)).uploadPost(any(PostDto.class));
    }

    @DisplayName("post 페이지로 넘겨받기")
    @Test
    void testFindPostsByPageRequest() throws Exception {

        List<Post> posts = Stream.of(
                Post.builder().password("qwer1234").source("/asdf.jpg").caption("1이얏").downloadKey("s3://temp").eraseFlag(0).build(),
                Post.builder().password("qwer1234").source("/asdf.jpg").caption("2이얏").downloadKey("s3://temp").eraseFlag(0).build(),
                Post.builder().password("qwer1234").source("/asdf.jpg").caption("3이얏").downloadKey("s3://temp").eraseFlag(0).build()).collect(Collectors.toList());

        PageDto pageDto = PageDto.builder()
                .lastPageId(0)
                .defaultPageSize(3)
                .build();

        when(postService.findPostsByPageRequest(pageDto.getLastPageId(), pageDto.getDefaultPageSize())).thenReturn(posts);

        String content = objectMapper.writeValueAsString(pageDto);
        String ret = objectMapper.writeValueAsString(posts);

        mockMvc.perform(post("/paging")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .with(csrf()))
                .andDo(print())
                .andExpect(content().json(ret));
    }

    @DisplayName("post 이미지 다운로드받기")
    @Test
    void testDownload() throws IOException {

        final String pathName = "src/test/resources/testimage/testimage1.jpg";
        File file = new File(pathName);
        byte[] mockImageData = Files.readAllBytes(file.toPath());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        httpHeaders.setContentLength(mockImageData.length);
        httpHeaders.setContentDispositionFormData("attachment", "testimage1");


        when(postService.download(pathName)).thenReturn(new ResponseEntity<>(mockImageData, httpHeaders, HttpStatus.OK));

        // when
        ResponseEntity<byte[]> response = postController.download(pathName);

        // then
        // response 상태 검증
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.IMAGE_JPEG, response.getHeaders().getContentType());

        // 이미지 데이터 검증
        byte[] downloadedImageData = response.getBody();
        assertNotNull(downloadedImageData);
        assertEquals(mockImageData.length, downloadedImageData.length);
        assertArrayEquals(mockImageData, downloadedImageData);
    }

    @DisplayName("post 삭제하기 ")
    @Test
    void testDeletePost() {

        DeletePostFormDto deletePostFormDto = DeletePostFormDto.builder()
                .id(1)
                .password("qwer1234")
                .build();

        when(postService.updatePostErasedTrue(deletePostFormDto.getId(), deletePostFormDto.getPassword()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // when
        ResponseEntity<?> response = postController.deletePost(deletePostFormDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @DisplayName("post 수정하기 ")
    @Test
    void testUpdatePost() throws IOException {

        final String fileName = "testimage1"; //파일명
        final String contentType = "jpg"; //파일타입
        final String filePath = "src/test/resources/testimage/" + fileName + "." + contentType; //파일경로
        FileInputStream fileInputStream = new FileInputStream(filePath);

        MockMultipartFile testImage1 = new MockMultipartFile(
                "testImage1", //name
                fileName + "." + contentType, //originalFilename
                contentType,
                fileInputStream
        );

        UpdatePostFormDto updatePostFormDto = UpdatePostFormDto.builder()
                .id(1)
                .caption("해는 뜬다.")
                .file(testImage1)
                .password("qwer1234")
                .build();

        when(postService.updatePost(updatePostFormDto))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // when
        ResponseEntity<?> response = postController.updatePost(updatePostFormDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @DisplayName("PostDto 유효성 검사")
    @Test
    void testPostDtoValidation_Password() throws IOException {

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

        PostDto postDto = PostDto.builder()
                .file(file)
                .password("qwe")
                .caption("안녕하세요?")
                .eraseFlag(0)
                .build();

        when(postService.uploadPost(any(PostDto.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        try {
            mockMvc.perform(
                            multipart("/post/upload")
                                    .file((MockMultipartFile) postDto.getFile())
                                    .param("password", postDto.getPassword())
                                    .param("caption", postDto.getCaption())
                                    .param("eraseFlag", Integer.toString(postDto.getEraseFlag()))
                                    .with(csrf()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception ignored) {
        }
    }

    @DisplayName("PostDto 유효성 검사")
    @Test
    void testPostDtoValidation_eraseFlag() throws IOException {

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

        PostDto postDto = PostDto.builder()
                .file(file)
                .password("qwe1234")
                .caption("안녕하세요?")
                .eraseFlag(5)
                .build();

        when(postService.uploadPost(any(PostDto.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        try {
            mockMvc.perform(
                            multipart("/post/upload")
                                    .file((MockMultipartFile) postDto.getFile())
                                    .param("password", postDto.getPassword())
                                    .param("caption", postDto.getCaption())
                                    .param("eraseFlag", Integer.toString(postDto.getEraseFlag()))
                                    .with(csrf()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception ignored) {
        }
    }
}