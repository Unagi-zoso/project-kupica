package com.litaa.projectkupica.web;

import com.litaa.projectkupica.common.annotaiton.PageIdValidation;
import com.litaa.projectkupica.common.annotaiton.PageSizeValidation;
import com.litaa.projectkupica.common.annotaiton.PasswordValidation;
import com.litaa.projectkupica.common.annotaiton.PostIdValidation;
import com.litaa.projectkupica.domain.post.Post.PostResponse;
import com.litaa.projectkupica.service.PostService;
import com.litaa.projectkupica.web.dto.UpdatePostRequest;
import com.litaa.projectkupica.web.dto.UploadPostRequest;
import com.litaa.projectkupica.web.model.DefaultResponseModel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author : Unagi_zoso
 * @date : 2022-12-30
 */

@Validated
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;
    private final Logger LOGGER = LoggerFactory.getLogger(PostController.class);

    @PostMapping("/posts")
    public ResponseEntity<?> uploadPost(@Valid UploadPostRequest uploadPostRequest,
                                        @RequestHeader(name = "Authorization") @PasswordValidation String password) {

        long startTime = System.currentTimeMillis();
        LOGGER.info("[PostController] upload post. post caption : {}", uploadPostRequest.getCaption());

        postService.uploadPost(uploadPostRequest, password);

        LOGGER.info("[PostController] upload post processing time : {}", (System.currentTimeMillis() - startTime));

        return new ResponseEntity<>(new RepresentationModel<>(), HttpStatus.CREATED);
    }

    @PatchMapping("/posts/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable @PostIdValidation Integer postId,
                                        @Valid UpdatePostRequest updatePostRequest,
                                        @RequestHeader(name = "Authorization") @PasswordValidation String password) {

        String updatedFileName = "* No Changed";
        if (null != updatePostRequest.getFile()) {
            updatedFileName = updatePostRequest.getFile().getOriginalFilename();
        }

        long startTime = System.currentTimeMillis();
        LOGGER.info("[PostController] update post. post id : {}, post filename : {}, post caption : {}", postId, updatedFileName, updatePostRequest.getCaption());

        postService.updatePost(postId, updatePostRequest, password);

        LOGGER.info("[PostController] update post processing time : {}", (System.currentTimeMillis() - startTime));

        return new ResponseEntity<>(new RepresentationModel<RepresentationModel<?>>(), HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}/delete")
    public ResponseEntity<?> deletePost(@PathVariable(name = "postId") @PostIdValidation Integer postId,
                                        @RequestHeader(name = "Authorization") @PasswordValidation String password) {

        LOGGER.info("[PostController] delete post. post id : {}", postId);

        postService.updatePostErasedTrue(postId, password);

        return new ResponseEntity<>(new RepresentationModel<RepresentationModel<?>>(), HttpStatus.OK);
    }

    @GetMapping("/posts/page")
    public ResponseEntity<?> findPostsByPageRequest(@RequestParam(name = "requestedPageId") @PageIdValidation Integer requestedPageId,
                                                    @RequestParam(name = "pageSize") @PageSizeValidation Integer pageSize) {

        long startTime = System.currentTimeMillis();
        LOGGER.info("[PostController] find posts By pageRequest. last page id : {}, page size : {}", requestedPageId, pageSize);

        List<PostResponse> posts = postService.findPostsByPageRequest(requestedPageId, pageSize);

        LOGGER.info("[PostController] find posts By pageRequest. post size : {}", posts.size());
        LOGGER.info("[PostController] find posts By pageRequest processing time : {}", (System.currentTimeMillis() - startTime));

        return new ResponseEntity<>(new DefaultResponseModel<>(posts), HttpStatus.OK);
    }
}