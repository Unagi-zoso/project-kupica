package com.litaa.projectkupica.service;

import com.litaa.projectkupica.domain.post.Post;
import com.litaa.projectkupica.domain.post.Post.PostResponse;
import com.litaa.projectkupica.domain.post.PostRepository;
import com.litaa.projectkupica.exception.File.EmptyFileException;
import com.litaa.projectkupica.exception.post.PostNotFoundException;
import com.litaa.projectkupica.exception.post.WrongPasswordException;
import com.litaa.projectkupica.web.dto.UpdatePostRequest;
import com.litaa.projectkupica.web.dto.UploadPostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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

    @Transactional
    public void uploadPost(UploadPostRequest uploadPostRequest,
                           String password) {

        if (uploadPostRequest.getFile().isEmpty()) {
            throw new EmptyFileException();
        }

        Post post = Post.builder()
                .password(passwordEncoder.encode(password))
                .caption(uploadPostRequest.getCaption())
                .erasedFlag(0)
                .build();

        postRepository.save(post);

        imageService.uploadImage(post.getPostId(), uploadPostRequest.getFile());
    }

    @Transactional
    public void updatePost(int postId, UpdatePostRequest updatePostRequest, String password) {

        PostResponse currentPost = findPostResponseById(postId).orElseThrow(PostNotFoundException::new);

        String realPassword = postRepository.findPasswordById(postId);
        if (!isPasswordValid(password, realPassword)) {
            throw new WrongPasswordException();
        }

        if (null != updatePostRequest.getFile()) {
            imageService.updateImage(postId, updatePostRequest.getFile());
        }

        String currentCaption = currentPost.getCaption();
        String newCaption = updatePostRequest.getCaption();

        if (isCaptionChanged(currentCaption, newCaption)) {
            postRepository.updatePostCaption(postId, newCaption);
        }
    }

    private boolean isCaptionChanged(String currentCaption, String newCaption) {
        return !currentCaption.equals(newCaption);
    }

    public List<PostResponse> findPostsByPageRequest(Integer pageId, Integer size) {

        Sort sort = Sort.by(Sort.Direction.DESC, "post_id");
        PageRequest pageRequest = PageRequest.of(pageId, size, sort);

        return postRepository.findAllUnErased(pageRequest).getContent().stream().map(PostResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public void updatePostErasedTrue(int postId, String password) {

        PostResponse currentPost = findPostResponseById(postId).orElseThrow(PostNotFoundException::new);

        String realPassword = postRepository.findPasswordById(postId);
        if (!isPasswordValid(password, realPassword)) {
            throw new WrongPasswordException();
        }

        postRepository.updatePostErasedTrue(postId);
    }

    public boolean isPasswordValid(String password, String encodedPassword) {

        return passwordEncoder.matches(password, encodedPassword);
    }

    public Optional<PostResponse> findPostResponseById(Integer postId) {

        Post post = postRepository.findPostById(postId);

        if (null != post) {
                return Optional.ofNullable(new PostResponse(post));
        }

        return Optional.empty();
    }
}
