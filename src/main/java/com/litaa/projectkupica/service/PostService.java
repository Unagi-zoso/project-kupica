package com.litaa.projectkupica.service;

import com.litaa.projectkupica.config.auth.PrincipalDetails;
import com.litaa.projectkupica.domain.post.Post;
import com.litaa.projectkupica.domain.post.PostRepository;
import com.litaa.projectkupica.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

/**
 * @author : Unagi_zoso
 * @date : 2022-12-31
 */

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    @Value("${file.path}")
    private String uploadFolder;

    @Transactional
    public void uploadPost(PostDto postDto) {
        UUID uuid = UUID.randomUUID(); // uuid
        String imageFileName = uuid+"_"+postDto.getFile().getOriginalFilename(); // 1.jpg
        System.out.println("이미지 파일이름 : "+imageFileName);

        Path imageFilePath = Paths.get(uploadFolder+imageFileName);

        // 통신, I/O -> 예외가 발생할 수 있다.
        try {
            Files.write(imageFilePath, postDto.getFile().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }



        // image 테이블에 저장
        Post post = postDto.toEntity(imageFileName); // 5cf6237d-c404-43e5-836b-e55413ed0e49_bag.jpeg
        postRepository.save(post);
    }

    public List<Post> findPostsByPageRequest(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Post> a = postRepository.findAll(pageRequest).getContent();
        System.out.println(a);
        return a;
    }
}
