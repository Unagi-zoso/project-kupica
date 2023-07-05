package com.litaa.projectkupica.web;

import com.litaa.projectkupica.domain.post.Post;
import com.litaa.projectkupica.service.PostService;
import com.litaa.projectkupica.web.dto.DeletePostFormDto;
import com.litaa.projectkupica.web.dto.PageDto;
import com.litaa.projectkupica.web.dto.PostDto;
import com.litaa.projectkupica.web.dto.UpdatePostFormDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping("/post/upload")
    public ResponseEntity<Void> uploadPost(@Valid PostDto postDto) throws IOException {

        long startTime = System.currentTimeMillis();
        LOGGER.info("[PostController] upload post. post caption : {}", postDto.getCaption());

        HttpStatus resStatus = postService.uploadPost(postDto).getStatusCode();

        LOGGER.info("[PostController] upload post processing time : {}", (System.currentTimeMillis() - startTime));

        return new ResponseEntity<>(resStatus);
    }

    @PostMapping("/post/update")
    public ResponseEntity<?> updatePost(@Valid UpdatePostFormDto updatePostFormDto) throws IOException {

        long startTime = System.currentTimeMillis();
        LOGGER.info("[PostController] update post. post id : {}, post filename : {}, post caption : {}", updatePostFormDto.getId(), updatePostFormDto.getFile().getOriginalFilename(), updatePostFormDto.getCaption());

        ResponseEntity<?> response = postService.updatePost(updatePostFormDto);

        LOGGER.info("[PostController] update post processing time : {}", (System.currentTimeMillis() - startTime));

        return response;
    }

    @PostMapping("/post/delete")
    public ResponseEntity<?> deletePost(@Valid @RequestBody DeletePostFormDto deletePostFormDto) {

        LOGGER.info("[PostController] delete post. post id : {}", deletePostFormDto.getId());

        return postService.updatePostErasedTrue(deletePostFormDto.getId(), deletePostFormDto.getPassword());
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam(value = "fileUrl") String fileUrl) throws IOException {

        long startTime = System.currentTimeMillis();
        LOGGER.info("[PostController] download post. downloadImageUrl : {}", fileUrl);
        ResponseEntity<byte[]> response = postService.download(fileUrl);

        LOGGER.info("[PostController] download post processing time : {}", (System.currentTimeMillis() - startTime));
        return response;
    }

    @GetMapping("/latestimage")
    public List<Post> findPostsLatest5() {

        long startTime = System.currentTimeMillis();
        LOGGER.info("[PostController] find latest 5 posts .");

        List<Post> posts = postService.findPostsLatest5();

        LOGGER.info("[PostController] find latest 5 posts. post size : {}", posts.size());
        LOGGER.info("[PostController] find latest 5 posts processing time : {}", (System.currentTimeMillis() - startTime));

        return posts;
    }

    @PostMapping("/paging")
    public List<Post> findPostsByPageRequest(@Valid @RequestBody PageDto pageDto) {

        long startTime = System.currentTimeMillis();
        LOGGER.info("[PostController] find posts By pageRequest. last page id : {}, page size : {}", pageDto.getLastPageId(), pageDto.getDefaultPageSize());

        List<Post> posts = postService.findPostsByPageRequest(pageDto.getLastPageId(), pageDto.getDefaultPageSize());

        LOGGER.info("[PostController] find posts By pageRequest. post size : {}", posts.size());
        LOGGER.info("[PostController] find posts By pageRequest processing time : {}", (System.currentTimeMillis() - startTime));

        return posts;
    }
}