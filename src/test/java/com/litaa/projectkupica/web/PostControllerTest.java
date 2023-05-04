package com.litaa.projectkupica.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.litaa.projectkupica.domain.post.Post;
import com.litaa.projectkupica.service.PostService;
import com.litaa.projectkupica.web.dto.PageDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @DisplayName("post 업로드 하기")
    @Test
    void testUploadPost() throws Exception {

        final String fileName = "testimage1"; //파일명
        final String contentType = "jpg"; //파일타입
        final String filePath = "src/test/resources/testimage/"+fileName+"."+contentType; //파일경로
        FileInputStream fileInputStream = new FileInputStream(filePath);

        MockMultipartFile testImage1 = new MockMultipartFile(
                "testImage1", //name
                fileName + "." + contentType, //originalFilename
                contentType,
                fileInputStream
        );

        mockMvc.perform(
                multipart("/post/upload")
                        .file(testImage1)
                        .param("password", "qwer1234")
                        .param("caption", "실로.. 좋은 사진이로다..")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"));
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
        PostService postService = Mockito.mock(PostService.class);
        final String pathName = "src/test/resources/testimage/testimage1.jpg";
        File file = new File(pathName);
        byte[] mockImageData = Files.readAllBytes(file.toPath());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        httpHeaders.setContentLength(mockImageData.length);
        httpHeaders.setContentDispositionFormData("attachment", "testimage1");

        when(postService.download(pathName)).thenReturn(new ResponseEntity<>(mockImageData, httpHeaders, HttpStatus.OK));

        PostController postController = new PostController(postService);

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
}