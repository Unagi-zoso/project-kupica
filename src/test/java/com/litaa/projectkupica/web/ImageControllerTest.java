package com.litaa.projectkupica.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.litaa.projectkupica.domain.image.Image.ImageResponse;
import com.litaa.projectkupica.service.ImageService;
import com.litaa.projectkupica.web.dto.ImageFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author : Unagi_zoso
 * @date : 2023-07-13
 */

@ActiveProfiles("local")
@WithMockUser(username = "테스트 관리자", roles = {"SUPER"})
@WebMvcTest(ImageController.class)
class ImageControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ImageController imageController;
    @MockBean
    ImageService imageService;

    @DisplayName("이미지 다운로드받기")
    @Test
    void testDownload() throws IOException {
        final String fileName = "testimage1";
        final String extension = "jpg";
        final String pathName = "src/test/resources/testimage";
        File file = new File(pathName + "/" + fileName + "." + extension);
        byte[] mockImageData = Files.readAllBytes(file.toPath());

        ImageFile imageFile = new ImageFile(mockImageData, fileName, MediaType.IMAGE_JPEG, mockImageData.length);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(imageFile.getMediaType());
        httpHeaders.setContentLength(imageFile.getSize());
        httpHeaders.setContentDisposition(ContentDisposition.attachment().filename(imageFile.getFileName()).build());

        when(imageController.download(anyInt())).thenReturn(ResponseEntity.ok().headers(httpHeaders).body(mockImageData));

        // when
        ResponseEntity<byte[]> response = imageController.download(1);

        // then
        // response 상태 검증
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.IMAGE_JPEG, response.getHeaders().getContentType());

        // 이미지 데이터 검증
        byte[] downloadedImageData = Objects.requireNonNull(response.getBody());
        assertNotNull(downloadedImageData);
        assertEquals(mockImageData.length, downloadedImageData.length);
        assertArrayEquals(mockImageData, downloadedImageData);
    }

    @DisplayName("최근에 등록된 image 5개 가져오기")
    @Test
    void testFindLatestImages5() {
        ArrayList<ImageResponse> givenImageResponses = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            givenImageResponses.add(ImageResponse.builder().imageId(1).source("s3://asdf").cachedImageUrl("cf://asdf").downloadKey("down://asdf").build());
        }

        when(imageService.findLatestImages5()).thenReturn(givenImageResponses);

        try {
            mockMvc.perform(get("/images/latest/5"))
                    .andExpect(status().isOk());
        } catch (Exception ignored) {
        }
    }
}