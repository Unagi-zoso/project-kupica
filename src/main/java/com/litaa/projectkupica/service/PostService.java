package com.litaa.projectkupica.service;

import com.litaa.projectkupica.domain.post.Post;
import com.litaa.projectkupica.domain.post.Post.PostResponse;
import com.litaa.projectkupica.domain.post.PostRepository;
import com.litaa.projectkupica.web.dto.UpdatePostFormDto;
import com.litaa.projectkupica.web.dto.UploadPostFormDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Unagi_zoso
 * @date : 2022-12-31
 */

@RequiredArgsConstructor
@Service
public class PostService {

    private final ImageService imageService;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    private final Logger LOGGER = LoggerFactory.getLogger(PostService.class);

    @Transactional
    public ResponseEntity<?> uploadPost(UploadPostFormDto uploadPostFormDto) throws IOException {

        if (uploadPostFormDto.getFile().isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Post post = Post.builder()
                .password(passwordEncoder.encode(uploadPostFormDto.getPassword()))
                .caption(uploadPostFormDto.getCaption())
                .erasedFlag(0)
                .build();

        postRepository.save(post);

        imageService.uploadImage(post.getPostId(), uploadPostFormDto.getFile());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updatePost(int postId, UpdatePostFormDto updatePostFormDto) throws IOException {

        PostResponse existingPost = findPostResponseById(postId);
        if (existingPost == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 하는 게시글이 없습니다.");
        }

        String realPassword = postRepository.findPasswordById(postId);
        if (!isPasswordValid(updatePostFormDto.getPassword(), realPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 틀립니다.");
        }

        if (updatePostFormDto.getFile() != null) {

            imageService.updateImage(postId, updatePostFormDto.getFile());
        }
        String currentCaption = findPostResponseById(postId).getCaption();
        String newCaption = updatePostFormDto.getCaption();

        if (!currentCaption.equals(newCaption)) postRepository.updatePostCaption(postId, newCaption);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public List<PostResponse> findPostsByPageRequest(Integer page, Integer size) {

        Sort sort = Sort.by(Sort.Direction.DESC, "post_id");
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return postRepository.findAllUnErased(pageRequest).getContent().stream().map(PostResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public ResponseEntity<?> updatePostErasedTrue(int postId, String password) {

        PostResponse existingPost = findPostResponseById(postId);
        if (existingPost == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 하는 게시글이 없습니다.");
        }

        String realPassword = postRepository.findPasswordById(postId);
        if (!isPasswordValid(password, realPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 틀립니다.");
        }

        postRepository.updatePostErasedTrue(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public boolean isPasswordValid(String password, String encodedPassword) {

        return passwordEncoder.matches(password, encodedPassword);
    }

    public PostResponse findPostResponseById(Integer postId) {

        Post post = postRepository.findPostById(postId);

        return new PostResponse(post);

    }
}
