package com.ll.domain.post.repository;

import com.ll.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface PostRepository extends JpaRepository<Post, Long> {
}
