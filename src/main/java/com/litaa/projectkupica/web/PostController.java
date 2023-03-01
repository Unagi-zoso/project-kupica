package com.litaa.projectkupica.web;

import com.litaa.projectkupica.domain.post.Post;
import com.litaa.projectkupica.service.PostService;
import com.litaa.projectkupica.web.dto.PageDto;
import com.litaa.projectkupica.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Unagi_zoso
 * @date : 2022-12-30
 */

@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;

    @GetMapping("/postform")
    public String postForm() {
        return "postform";
    }

    @PostMapping("/post/upload")
    public String uploadPost(PostDto postDto) {

        postService.uploadPost(postDto);
        return "redirect:/";
    }

    @PostMapping("/paging")
    @ResponseBody
    public List<Post> findPostsByPageRequest(@RequestBody PageDto pageDto) {
        List<Post> a =postService.findPostsByPageRequest(pageDto.getLastPageId(), pageDto.getDefaultPageSize());
        System.out.println(a);
        return a;
    }
}
