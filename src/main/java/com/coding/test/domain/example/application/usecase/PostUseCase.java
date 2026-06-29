package com.coding.test.domain.example.application.usecase;

import com.coding.test.domain.example.application.dto.request.CreatePostRequest;
import com.coding.test.domain.example.application.dto.response.PostResponse;
import com.coding.test.domain.example.domain.entity.Post;
import com.coding.test.domain.example.domain.service.PostCreateService;
import com.coding.test.domain.example.domain.service.PostGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostUseCase {

    private final PostCreateService postCreateService;
    private final PostGetService postGetService;

    @Transactional
    public PostResponse createPost(CreatePostRequest request) {
        Post post = Post.create(request.title(), request.content(), request.authorName());
        Post savedPost = postCreateService.createPost(post);
        return PostResponse.from(savedPost);
    }

    public List<PostResponse> getAllPosts() {
        return postGetService.getAllPosts().stream()
                .map(PostResponse::from)
                .toList();
    }

    public List<PostResponse> searchPostsByTitle(String title) {
        return postGetService.searchByTitle(title).stream()
                .map(PostResponse::from)
                .toList();
    }
}
