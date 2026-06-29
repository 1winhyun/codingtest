package com.coding.test.domain.example.application.usecase;

import com.coding.test.domain.example.application.dto.request.CreatePostRequest;
import com.coding.test.domain.example.application.dto.response.PostResponse;
import com.coding.test.domain.example.domain.entity.Post;
import com.coding.test.domain.example.domain.service.PostCreateService;
import com.coding.test.domain.example.domain.service.PostGetService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("PostUseCase")
class PostUseCaseTest {

    @Mock
    private PostCreateService postCreateService;

    @Mock
    private PostGetService postGetService;

    @InjectMocks
    private PostUseCase postUseCase;

    private Post savedPost(Long id, String title, String content, String authorName) {
        Post post = Post.create(title, content, authorName);
        ReflectionTestUtils.setField(post, "id", id);
        ReflectionTestUtils.setField(post, "createdAt", LocalDateTime.now());
        return post;
    }

    @Nested
    @DisplayName("createPost")
    class CreatePost {

        @Test
        @DisplayName("유효한 요청으로 게시글을 생성하면 저장된 게시글 정보가 반환된다")
        void returnsSavedPostWhenRequestIsValid() {
            // given
            CreatePostRequest request = new CreatePostRequest("제목", "내용", "작성자");
            Post saved = savedPost(1L, "제목", "내용", "작성자");
            given(postCreateService.createPost(any(Post.class))).willReturn(saved);

            // when
            PostResponse response = postUseCase.createPost(request);

            // then
            assertThat(response.id()).isEqualTo(1L);
            assertThat(response.title()).isEqualTo("제목");
            assertThat(response.content()).isEqualTo("내용");
            assertThat(response.authorName()).isEqualTo("작성자");
            assertThat(response.createdAt()).isNotNull();
            verify(postCreateService).createPost(any(Post.class));
        }

        @Test
        @DisplayName("title이 정확히 100자인 경우 정상 생성된다")
        void createsPostWhenTitleIsExactly100Characters() {
            // given
            String title = "가".repeat(100);
            CreatePostRequest request = new CreatePostRequest(title, "내용", "작성자");
            given(postCreateService.createPost(any(Post.class)))
                    .willReturn(savedPost(1L, title, "내용", "작성자"));

            // when
            PostResponse response = postUseCase.createPost(request);

            // then
            assertThat(response.title()).hasSize(100);
            assertThat(response.title()).isEqualTo(title);
        }

        @Test
        @DisplayName("content가 정확히 2000자인 경우 정상 생성된다")
        void createsPostWhenContentIsExactly2000Characters() {
            // given
            String content = "가".repeat(2000);
            CreatePostRequest request = new CreatePostRequest("제목", content, "작성자");
            given(postCreateService.createPost(any(Post.class)))
                    .willReturn(savedPost(1L, "제목", content, "작성자"));

            // when
            PostResponse response = postUseCase.createPost(request);

            // then
            assertThat(response.content()).hasSize(2000);
            assertThat(response.content()).isEqualTo(content);
        }
    }

    @Nested
    @DisplayName("getAllPosts")
    class GetAllPosts {

        @Test
        @DisplayName("게시글이 존재하면 전체 목록이 반환된다")
        void returnsAllPostsWhenPostsExist() {
            // given
            Post post1 = savedPost(1L, "제목1", "내용1", "작성자1");
            Post post2 = savedPost(2L, "제목2", "내용2", "작성자2");
            given(postGetService.getAllPosts()).willReturn(List.of(post1, post2));

            // when
            List<PostResponse> responses = postUseCase.getAllPosts();

            // then
            assertThat(responses).hasSize(2);
            assertThat(responses).extracting(PostResponse::id).containsExactly(1L, 2L);
            assertThat(responses).extracting(PostResponse::title).containsExactly("제목1", "제목2");
            verify(postGetService).getAllPosts();
        }

        @Test
        @DisplayName("게시글이 없으면 빈 리스트가 반환된다")
        void returnsEmptyListWhenNoPostsExist() {
            // given
            given(postGetService.getAllPosts()).willReturn(List.of());

            // when
            List<PostResponse> responses = postUseCase.getAllPosts();

            // then
            assertThat(responses).isEmpty();
            verify(postGetService).getAllPosts();
        }

        @Test
        @DisplayName("삭제된 게시글은 목록에 포함되지 않는다")
        void excludesDeletedPosts() {
            // given - PostGetService(findAllByDeletedFalse)가 삭제되지 않은 게시글만 반환
            Post activePost = savedPost(1L, "활성 게시글", "내용", "작성자");
            given(postGetService.getAllPosts()).willReturn(List.of(activePost));

            // when
            List<PostResponse> responses = postUseCase.getAllPosts();

            // then
            assertThat(responses).hasSize(1);
            assertThat(responses).extracting(PostResponse::id).containsExactly(1L);
            verify(postGetService).getAllPosts();
        }
    }

    @Nested
    @DisplayName("searchPostsByTitle")
    class SearchPostsByTitle {

        @Test
        @DisplayName("키워드로 시작하는 제목의 게시글들이 반환된다")
        void returnsPostsWhoseTitleStartsWithKeyword() {
            // given - PostGetService(searchByTitle)가 접두어 일치 게시글만 반환
            Post post1 = savedPost(1L, "Spring 입문", "내용1", "작성자1");
            Post post2 = savedPost(2L, "Spring Boot 심화", "내용2", "작성자2");
            given(postGetService.searchByTitle("Spring")).willReturn(List.of(post1, post2));

            // when
            List<PostResponse> responses = postUseCase.searchPostsByTitle("Spring");

            // then
            assertThat(responses).hasSize(2);
            assertThat(responses).extracting(PostResponse::title)
                    .containsExactly("Spring 입문", "Spring Boot 심화");
            verify(postGetService).searchByTitle("Spring");
        }

        @Test
        @DisplayName("일치하는 게시글이 없으면 빈 리스트가 반환된다")
        void returnsEmptyListWhenNoMatch() {
            // given
            given(postGetService.searchByTitle("없는키워드")).willReturn(List.of());

            // when
            List<PostResponse> responses = postUseCase.searchPostsByTitle("없는키워드");

            // then
            assertThat(responses).isEmpty();
            verify(postGetService).searchByTitle("없는키워드");
        }
    }
}
