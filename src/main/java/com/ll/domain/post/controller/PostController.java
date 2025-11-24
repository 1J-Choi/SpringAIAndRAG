package com.ll.domain.post.controller;

import com.ll.domain.post.entity.Post;
import com.ll.domain.post.repository.PostRepository;
import com.ll.domain.post.service.PostRagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostRagService postRagService;
    private final PostRepository postRepository;

    @GetMapping("/search")
    public ResponseEntity<List<Post>> searchPosts(
            @RequestParam String query
    ) {

        List<Long> postIds = postRagService.searchPostIds(query, 5);
        List<Post> posts = postRepository.findAllById(postIds);

        return ResponseEntity.ok(posts);
    }

    /**
     * RAG + LLM 기반 검색 API
     * 예시: GET /api/v1/posts/search/llm?query=코딩용 맥북 대여하고 싶어
     */
    @GetMapping("/search/llm")
    public ResponseEntity<Map<String, Object>> searchWithLLM(@RequestParam String query) {
        String answer = postRagService.searchWithLLM(query);
        return ResponseEntity.ok(
                Map.of(
                        "query", query,
                        "answer", answer
                )
        );
    }
}