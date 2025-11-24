package com.ll.domain.post.controller;

import com.ll.domain.post.entity.Post;
import com.ll.domain.post.service.PostRagService;
import com.ll.domain.post.service.PostService;
import com.ll.domain.rag.RagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;
    private final PostRagService postRagService;
    private final RagService ragService;

    /**
     * RAG + LLM 기반 검색
     * 예: GET /api/v1/posts/search/rag?question=강남에서 빌릴 수 있는 카메라 있어?
     */
    @GetMapping("/search/rag")
    public ResponseEntity<String> searchWithRag(@RequestParam String question) {
        String answer = postRagService.searchPostsWithRag(question);
        return ResponseEntity.ok(answer);
    }

    /**
     * 단일 게시글 조회 (간단 예시)
     * 예: GET /api/v1/posts/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable Long id) {
        return postService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * (옵션) 전체 게시글 목록 조회
     * 예: GET /api/v1/posts
     */
    @GetMapping
    public ResponseEntity<List<Post>> getPosts() {
        List<Post> posts = postService.findAll();
        return ResponseEntity.ok(posts);
    }

    /**
     * (옵션) 현재 벡터 색인 개수 확인용 디버깅 API
     * 예: GET /api/v1/posts/rag/count
     */
    @GetMapping("/rag/count")
    public ResponseEntity<Integer> getRagDocumentCount() {
        int count = ragService.getDocumentCount();
        return ResponseEntity.ok(count);
    }
}