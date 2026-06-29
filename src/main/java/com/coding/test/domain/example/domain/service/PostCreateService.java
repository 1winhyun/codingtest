package com.coding.test.domain.example.domain.service;

import com.coding.test.domain.example.domain.entity.Post;
import com.coding.test.domain.example.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostCreateService {

    private final PostRepository postRepository;

    public Post createPost(Post post) {
        return postRepository.save(post);
    }
}
