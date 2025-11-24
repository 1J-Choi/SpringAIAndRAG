package com.ll.domain.post.service;

import com.ll.domain.post.entity.Post;
import com.ll.domain.post.repository.PostRepository;
import com.ll.domain.rag.RagService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostRagService {

    private final PostRepository postRepository;
    private final RagService ragService;
    private final ChatClient chatClient;

    /**
     * RAG 기반 게시글 검색 + LLM 답변
     */
    public String searchPostsWithRag(String question) {
        // 1. 벡터 검색
        List<Long> similarPostIds = ragService.searchSimilarPostIds(question, 3);
        if (similarPostIds.isEmpty()) {
            return "검색 결과가 없습니다.";
        }

        // 2. DB에서 Post 조회
        List<Post> posts = postRepository.findAllById(similarPostIds);

        // 3. 컨텍스트 생성
        String context = posts.stream()
                .map(this::formatPostForContext)
                .collect(Collectors.joining("\n\n"));

        // 4. LLM 호출
        String prompt = String.format("""
            다음 대여 게시글 정보를 바탕으로 질문에 답변해주세요.
            
            === 참고 정보 ===
            %s
            
            === 질문 ===
            %s
            
            === 답변 규칙 ===
            - 위 참고 정보를 바탕으로만 답변하세요
            - 가격, 위치, 상태 등 구체적인 정보를 포함하세요
            - 정보에 없는 내용은 "제공된 정보에서는 확인할 수 없습니다"라고 답변하세요
            - 한국어로 자연스럽게 답변하세요
            """, context, question);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }

    private String formatPostForContext(Post post) {
        StringBuilder sb = new StringBuilder();
        sb.append("제목: ").append(post.getTitle()).append("\n");
        sb.append("카테고리: ").append(post.getCategory()).append("\n");
        sb.append("가격: ").append(post.getPricePerDay()).append("원/일\n");
        sb.append("설명: ").append(post.getContent()).append("\n");
        sb.append("위치: ").append(post.getLocation()).append("\n");
        sb.append("상태: ").append(post.getPostCondition()).append("\n");

        if (post.getOptions() != null && !post.getOptions().isEmpty()) {
            sb.append("추가 사항: ").append(post.getOptions());
        }
        return sb.toString();
    }
}
