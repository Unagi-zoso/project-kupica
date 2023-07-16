package com.litaa.projectkupica.web;

import com.litaa.projectkupica.domain.post.Post.PostResponse;
import com.litaa.projectkupica.service.PostService;
import com.litaa.projectkupica.web.dto.UpdatePostFormDto;
import com.litaa.projectkupica.web.dto.UploadPostFormDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.util.List;

/**
 * @author : Unagi_zoso
 * @date : 2022-12-30
 */

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;
    private final Logger LOGGER = LoggerFactory.getLogger(PostController.class);

    @PostMapping("/posts")
    public ResponseEntity<Void> uploadPost(@Valid UploadPostFormDto postDto) throws IOException {

        long startTime = System.currentTimeMillis();
        LOGGER.info("[PostController] upload post. post caption : {}", postDto.getCaption());

        HttpStatus resStatus = postService.uploadPost(postDto).getStatusCode();

        LOGGER.info("[PostController] upload post processing time : {}", (System.currentTimeMillis() - startTime));

        return new ResponseEntity<>(resStatus);
    }

    @PatchMapping("/posts/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable @NotNull Integer postId,
                                        @Valid UpdatePostFormDto updatePostFormDto) throws IOException {

        String updatedFileName = "* No Changed";;
        if (null != updatePostFormDto.getFile())  {
            updatedFileName = updatePostFormDto.getFile().getOriginalFilename();
        }

        long startTime = System.currentTimeMillis();
        LOGGER.info("[PostController] update post. post id : {}, post filename : {}, post caption : {}", postId, updatedFileName, updatePostFormDto.getCaption());

        ResponseEntity<?> response = postService.updatePost(postId, updatePostFormDto);

        LOGGER.info("[PostController] update post processing time : {}", (System.currentTimeMillis() - startTime));

        return response;
    }

    @DeleteMapping("/posts/{postId}/delete")
    public ResponseEntity<?> deletePost(@PathVariable(name = "postId") @NotNull Integer postId,
                                        @NotBlank
                                        @Pattern(regexp = "^(?!\\s*$).{4,20}$", message = "비밀번호는 4자리 이상 20자 이하이어야 하며, 공백을 제외한 문자로 이루어져야 합니다.")
                                        String password) {

        LOGGER.info("[PostController] delete post. post id : {}", postId);

        return postService.updatePostErasedTrue(postId, password);
    }

    @GetMapping("/posts/page")
    public ResponseEntity<?> findPostsByPageRequest(@RequestParam(name = "lastPageId") @NotNull @Min(value = 0)Integer lastPageId,
                                                    @RequestParam(name = "pageSize") @NotNull @Min(value = 1)Integer pageSize) {

        long startTime = System.currentTimeMillis();
        LOGGER.info("[PostController] find posts By pageRequest. last page id : {}, page size : {}", lastPageId, pageSize);

        List<PostResponse> posts = postService.findPostsByPageRequest(lastPageId, pageSize);

        LOGGER.info("[PostController] find posts By pageRequest. post size : {}", posts.size());
        LOGGER.info("[PostController] find posts By pageRequest processing time : {}", (System.currentTimeMillis() - startTime));

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}