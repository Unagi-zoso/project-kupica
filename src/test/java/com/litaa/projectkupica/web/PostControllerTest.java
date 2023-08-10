package com.litaa.projectkupica.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.litaa.projectkupica.domain.post.Post.PostResponse;
import com.litaa.projectkupica.service.PostService;
import com.litaa.projectkupica.web.dto.UpdatePostRequest;
import com.litaa.projectkupica.web.dto.UploadPostRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileInputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author : Unagi_zoso
 * @date : 2023-03-02
 */
@ActiveProfiles("local")
@WithMockUser(username = "테스트 관리자", roles = {"SUPER"})
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    PostService postServiceMock;

    @DisplayName("post 업로드 - 성공")
    @Test
    void testUploadPost_Successful() {

        UploadPostRequest uploadPostRequest = createUploadPostRequest();

        final String password = "qwer1234";

        doNothing().when(postServiceMock).uploadPost(any(UploadPostRequest.class), anyString());

        try {
            mockMvc.perform(
                            multipart("/posts")
                                    .file((MockMultipartFile) uploadPostRequest.getFile())
                                    .header("Authorization", password)
                                    .param("caption", uploadPostRequest.getCaption())
                                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                    .with(csrf()))
                    .andExpect(status().isCreated())
                    .andDo(document("/posts/upload/successful",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(headerWithName("Authorization").description("A password for the post")),
                            requestParameters(
                                    parameterWithName("caption").description("A caption provided by a user to describe an image"),
                                    parameterWithName("_csrf").description("a csrf token")
                            ),
                            requestParts(partWithName("imageFile").description("An image file to be uploaded by a user"))
                    ));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(postServiceMock, times(1)).uploadPost(any(UploadPostRequest.class), anyString());
    }

    @DisplayName("post 업로드 - 실패 - 비밀번호 규칙 부적합")
    @Test
    void testUploadPost_Failure_PasswordRuleViolation() {

        UploadPostRequest uploadPostRequest = createUploadPostRequest();

        final String password = "qwe";

        doNothing().when(postServiceMock).uploadPost(any(UploadPostRequest.class), anyString());

        try {
            mockMvc.perform(
                            multipart("/posts")
                                    .file((MockMultipartFile) uploadPostRequest.getFile())
                                    .header("Authorization", password)
                                    .param("caption", uploadPostRequest.getCaption())
                                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                    .with(csrf()))
                    .andExpect(status().isBadRequest())
                    .andDo(document("/posts/upload/failure/password-rule-violation",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(headerWithName("Authorization").description("A password for the post")),
                            requestParameters(
                                    parameterWithName("caption").description("A caption provided by a user to describe an image"),
                                    parameterWithName("_csrf").description("a csrf token")
                            ),
                            requestParts(partWithName("imageFile").description("An image file to be uploaded by a user"))

                    ));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(postServiceMock, times(0)).uploadPost(any(UploadPostRequest.class), anyString());
    }

    @DisplayName("post 페이지 요청")
    @Test
    void testFindPostsByPageRequest() {

        List<PostResponse> posts = createPostResponses();

        int requestedPageId = 0;
        int defaultPageSize = 3;

        when(postServiceMock.findPostsByPageRequest(requestedPageId, defaultPageSize)).thenReturn(posts);

        try {
            mockMvc.perform(get("/posts/page")
                            .param("requestedPageId", Integer.toString(requestedPageId))
                            .param("pageSize", Integer.toString(defaultPageSize))
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.value.[0].post_id").value(posts.get(0).getPostId()))
                    .andExpect(jsonPath("$.value.[0].caption").value(posts.get(0).getCaption()))
                    .andExpect(jsonPath("$.value.[0].source").value(posts.get(0).getSource()))
                    .andExpect(jsonPath("$.value.[0].cached_image_url").value(posts.get(0).getCachedImageUrl()))
                    .andExpect(jsonPath("$.value.[0].download_key").value(posts.get(0).getDownloadKey()))
                    .andDo(document("/posts/page",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestParameters(
                                    parameterWithName("requestedPageId").description(""),
                                    parameterWithName("pageSize").description(""),
                                    parameterWithName("_csrf").description("a csrf token")
                            )
                    ));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("post 삭제하기 ")
    @Test
    void testDeletePost() {

        int postId = 1;
        final String password = "qwer1234";

        doNothing().when(postServiceMock).updatePostErasedTrue(postId, password);

        try {
            mockMvc.perform(delete("/posts/{postId}/delete", postId)
                            .header("Authorization", password)
                            .with(csrf())
                    )
                    .andExpect(status().isOk())
                    .andDo(document("/posts/delete",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(headerWithName("Authorization").description("A password for the post")),
                            requestParameters(parameterWithName("_csrf").description("A csrf token"))
                    ));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("post 수정하기")
    @Test
    void testUpdatePost() {

        UpdatePostRequest updatePostRequest = createUpdatePostRequest();

        final int postId = 1;
        final String password = "qwer1234";

        doNothing().when(postServiceMock).updatePost(anyInt(), any(UpdatePostRequest.class), anyString());

        try {
            mockMvc.perform(
                            multipart(HttpMethod.PATCH, "/posts/{postId}", postId)
                                    .file((MockMultipartFile) updatePostRequest.getFile())
                                    .header("Authorization", password)
                                    .param("caption", "멋진 사진이에요!")
                                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                    .with(csrf()))
                    .andExpect(status().isOk())
                    .andDo(document("/posts/update",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(headerWithName("Authorization").description("A password for the post")),
                            requestParameters(
                                    parameterWithName("caption").description("A caption provided by a user to describe an image"),
                                    parameterWithName("_csrf").description("a csrf token")
                            ),
                            requestParts(partWithName("imageFile").description("An image file to be uploaded by a user"))
                    ));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private UploadPostRequest createUploadPostRequest() {
        final String fileName = "testimage1";
        final String filePath = "src/test/resources/testimage/" + fileName + ".jpg";
        final String contentType = "image/jpg";
        MockMultipartFile file = getMockMultipartFile(fileName, contentType, filePath);

        UploadPostRequest uploadPostRequest = UploadPostRequest.builder()
                .file(file)
                .caption("좋은 사진이네요.")
                .build();
        return uploadPostRequest;
    }

    private MockMultipartFile getMockMultipartFile(String fileName, String contentType, String filePath) {
        FileInputStream fileInputStream = null;
        MockMultipartFile file = null;

        try {
            fileInputStream = new FileInputStream(filePath);
            file = new MockMultipartFile(
                    "imageFile", //name
                    fileName + "." + contentType, //originalFilename
                    contentType,
                    fileInputStream
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    private List<PostResponse> createPostResponses() {
        return Stream.of(
                PostResponse.builder().postId(1).caption("test caption").imageId(1).source("s3://source").cachedImageUrl("cf://source").downloadKey("down://source").build(),
                PostResponse.builder().postId(2).caption("test caption").imageId(2).source("s3://source").cachedImageUrl("cf://source").downloadKey("down://source").build(),
                PostResponse.builder().postId(3).caption("test caption").imageId(3).source("s3://source").cachedImageUrl("cf://source").downloadKey("down://source").build()).collect(Collectors.toList());
    }

    private UpdatePostRequest createUpdatePostRequest() {
        final String fileName = "testimage1"; //파일명
        final String filePath = "src/test/resources/testimage/" + fileName + ".jpg";
        final String contentType = "image/jpg";
        MockMultipartFile file = getMockMultipartFile(fileName, contentType, filePath);

        UpdatePostRequest updatePostRequest = UpdatePostRequest.builder()
                .file(file)
                .caption("해는 뜬다.")
                .build();
        return updatePostRequest;
    }
}