package com.coding.test.domain.example.domain.service;

import com.coding.test.domain.example.domain.entity.Post;
import com.coding.test.domain.example.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostGetService {

    private final PostRepository postRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAllByDeletedFalse();
    }

    public List<Post> searchByTitle(String title) {
        return postRepository.findByTitleStartingWithIgnoreCaseAndDeletedFalse(title);
    }
}
