package com.litaa.projectkupica.web;

import com.litaa.projectkupica.domain.post.Post;
import com.litaa.projectkupica.service.PostService;
import com.litaa.projectkupica.web.dto.DeletePostFormDto;
import com.litaa.projectkupica.web.dto.PageDto;
import com.litaa.projectkupica.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

/**
 * @author : Unagi_zoso
 * @date : 2022-12-30
 */

@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;

    @PostMapping("/post/upload")
    public ResponseEntity<Void> uploadPost(PostDto postDto) throws IOException {
        HttpStatus resStatus = postService.uploadPost(postDto).getStatusCode();
        return new ResponseEntity<>(resStatus);
    }

    @PostMapping("/post/delete")
    public ResponseEntity<?> deletePost(@RequestBody DeletePostFormDto deletePostFormDto) {
        return postService.updatePostErasedTrue(deletePostFormDto.getId(), deletePostFormDto.getPassword());
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam(value = "fileUrl") String fileUrl) throws IOException {

        return postService.download(fileUrl);
    }

    @GetMapping("/latestimage")
    @ResponseBody
    public List<Post> findPostsTop5() {
        return postService.findPostsLatest5();
    }

    @PostMapping("/paging")
    @ResponseBody
    public List<Post> findPostsByPageRequest(@RequestBody PageDto pageDto) {
        return postService.findPostsByPageRequest(pageDto.getLastPageId(), pageDto.getDefaultPageSize());
    }
}