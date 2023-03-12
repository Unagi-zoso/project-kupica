package com.litaa.projectkupica.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.litaa.projectkupica.domain.post.Post;
import com.litaa.projectkupica.service.PostService;
import com.litaa.projectkupica.web.dto.PageDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileInputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @DisplayName("포스트 업로드 하기")
    @Test
    void uploadPostTest() throws Exception {

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

    @DisplayName("포스트 페이지로 넘겨받기")
    @Test
    void findPostsByPageRequestTest() throws Exception {

        List<Post> posts = Stream.of(
                Post.builder().password("qwer1234").source("/asdf.jpg").caption("1이얏").eraseFlag(false).build(),
                Post.builder().password("qwer1234").source("/asdf.jpg").caption("2이얏").eraseFlag(false).build(),
                Post.builder().password("qwer1234").source("/asdf.jpg").caption("3이얏").eraseFlag(false).build()).collect(Collectors.toList());

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
}