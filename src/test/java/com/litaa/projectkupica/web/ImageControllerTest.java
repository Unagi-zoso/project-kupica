package com.litaa.projectkupica.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.litaa.projectkupica.domain.image.Image.ImageResponse;
import com.litaa.projectkupica.service.ImageService;
import com.litaa.projectkupica.web.dto.ImageFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author : Unagi_zoso
 * @date : 2023-07-13
 */

@ActiveProfiles("local")
@WithMockUser(username = "테스트 관리자", roles = {"SUPER"})
@AutoConfigureRestDocs
@WebMvcTest(ImageController.class)
class ImageControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ImageService imageServiceMock;

    @DisplayName("이미지 다운로드받기")
    @Test
    void testDownload() {

        byte[] imageData = createImageData();
        ImageFile imageFile = convertToImageFile(imageData);

        final int imageId = 1;

        when(imageServiceMock.download(anyInt())).thenReturn(imageFile);

        try {
            mockMvc.perform(get("/images/{imageId}/download", imageId))
                    .andExpect(content().bytes(imageData))
                    .andExpect(status().isOk())
                    .andDo(document("images/download/successful",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint())
                    ));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("imageId 1보다 작을 때 다운로드 요청 시 유효성 검사")
    @Test
    void When_ImageIdIsLessThan1_Expect_BadRequest() {

        byte[] imageData = createImageData();
        ImageFile imageFile = convertToImageFile(imageData);

        final int imageId = -5;

        when(imageServiceMock.download(anyInt())).thenReturn(imageFile);

        try {
            mockMvc.perform(get("/images/{imageId}/download", imageId))
                    .andExpect(status().isBadRequest())
                    .andDo(document("images/download/failure",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint())
                    ));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("최근에 등록된 image 5개 가져오기")
    @Test
    void testFindLatestImages5() {

        ArrayList<ImageResponse> givenImageResponses = createImageResponses();

        when(imageServiceMock.findLatestImages5()).thenReturn(givenImageResponses);

        try {
            mockMvc.perform(get("/images/latest/5"))
                    .andExpect(status().isOk())
                    .andDo(document("images/findLatestImages5",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ImageFile convertToImageFile(byte[] imageData) {

        return new ImageFile(imageData, "testImage", MediaType.IMAGE_JPEG, imageData.length);
    }

    private byte[] createImageData() {

        final String fileName = "testimage1";
        final String extension = "jpg";
        final String pathName = "src/test/resources/testimage";
        File file = new File(pathName + "/" + fileName + "." + extension);

        byte[] imageData = new byte[0];
        try {
            imageData = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return imageData;
    }

    private ArrayList<ImageResponse> createImageResponses() {

        ArrayList<ImageResponse> givenImageResponses = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            givenImageResponses.add(ImageResponse.builder()
                    .imageId(1)
                    .source("s3://test")
                    .cachedImageUrl("cf://test")
                    .downloadKey("down://test")
                    .build());
        }

        return givenImageResponses;
    }
}