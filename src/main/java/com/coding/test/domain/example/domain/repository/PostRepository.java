package com.coding.test.domain.example.domain.repository;

import com.coding.test.domain.example.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByDeletedFalse();

    List<Post> findByTitleStartingWithIgnoreCaseAndDeletedFalse(String title);
}
