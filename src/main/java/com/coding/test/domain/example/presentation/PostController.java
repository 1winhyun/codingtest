package com.coding.test.domain.example.presentation;

import com.coding.test.domain.example.application.dto.request.CreatePostRequest;
import com.coding.test.domain.example.application.dto.response.PostResponse;
import com.coding.test.domain.example.application.usecase.PostUseCase;
import com.coding.test.domain.example.presentation.response.PostResponseCode;
import com.coding.test.global.common.response.CommonResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Validated
public class PostController {

    private final PostUseCase postUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponse<PostResponse> createPost(@RequestBody @Valid CreatePostRequest request) {
        PostResponse response = postUseCase.createPost(request);
        return CommonResponse.success(PostResponseCode.POST_CREATED, response);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<List<PostResponse>> getAllPosts() {
        List<PostResponse> responses = postUseCase.getAllPosts();
        return CommonResponse.success(PostResponseCode.POSTS_FOUND, responses);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<List<PostResponse>> searchPostsByTitle(
            @RequestParam @NotBlank @Size(max = 100) String title) {
        List<PostResponse> responses = postUseCase.searchPostsByTitle(title);
        return CommonResponse.success(PostResponseCode.POSTS_SEARCHED, responses);
    }
}
