package com.ll.domain.rag;

import com.ll.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/rag")
@RequiredArgsConstructor
public class RagController {

    private final RagService ragService;
    private final PostService postService;

    /**
     * 유사한 Post ID 검색 (디버깅용)
     */
    @GetMapping("/search")
    public Map<String, Object> searchPostIds(
            @RequestParam String query,
            @RequestParam(defaultValue = "3") int topK
    ) {
        List<Long> postIds = ragService.searchSimilarPostIds(query, topK);
        return Map.of(
                "query", query,
                "postIds", postIds,
                "count", postIds.size()
        );
    }

    /**
     * RAG 상태 확인
     */
//    @GetMapping("/status")
//    public Map<String, Object> status() {
//        return Map.of(
//                "documentCount", ragService.getDocumentCount(),
//                "status", "running"
//        );
//    }

    /**
     * 유사도 테스트 (디버깅용)
     */
//    @GetMapping("/test-similarity")
//    public Map<String, String> testSimilarity(@RequestParam String query) {
//        ragService.testSimilarity(query);
//        return Map.of(
//                "message", "콘솔에서 유사도 결과를 확인하세요.",
//                "query", query
//        );
//    }
}