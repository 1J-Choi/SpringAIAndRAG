package com.ll.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostRagService {

    private final VectorStore vectorStore;
    private final ChatClient chatClient;

    // RAG용 기본 프롬프트
    private static final String RAG_PROMPT = """
            너는 장비 추천 및 정보 제공 전문가야.
            
            아래 제공된 게시글 정보(context)를 기반으로 정확하고 자연스러운 답변을 작성해줘.
            
            - 하나의 결론만 내리지 말고, 사용자의 의도를 분석하고 여러 가능성을 설명해줘.
            - 가격, 위치, 상태 등 중요한 정보가 있으면 꼭 언급해줘.
            - 문장은 부드럽게 이어지도록 자연스럽게 작성해줘.
            - 반드시 context 안에 있는 정보만 사용하고, 없는 내용은 추측하지 말고
              "제공된 정보에서는 확인할 수 없습니다." 라고 답해줘.
            """;

    /**
     * 의미 기반 검색 → 유사한 게시글 ID 리스트 반환
     */
    public List<Long> searchPostIds(String query, int topK) {

        SearchRequest request = SearchRequest.builder()
                .query(query)
                .topK(topK)
                .build();

        List<Document> docs = vectorStore.similaritySearch(request);

        return docs.stream()
                .map(doc -> (Number) doc.getMetadata().get("postId"))
                .map(Number::longValue)
                .toList();
    }

    /**
     * RAG + LLM 기반 검색/답변
     */
    public String searchWithLLM(String query) {
        // 1. 벡터 검색: 관련 문단(topK) 가져오기
        List<Document> docs = searchDocuments(query, 5);

        if (docs.isEmpty()) {
            return """
                    %s
                    
                    관련된 게시글 정보를 찾지 못했어요.
                    다른 검색어로 다시 시도해 주세요.
                    """.formatted(query);
        }

        // 2. context 구성 (문단들 텍스트 합치기)
        String context = docs.stream()
                .map(Document::getText)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n\n"));

        // 3. 최종 프롬프트 생성
        String prompt = """
                %s
                
                ---------------------
                [사용자 질문]
                %s
                
                [관련 게시글 정보]
                %s
                """.formatted(RAG_PROMPT, query, context);

        // 4. LLM 호출 (GPT-5-mini, temperature=1.0 고정)
        return chatClient.prompt(prompt)
                .options(ChatOptions.builder()
                        .temperature(1.0)   // GPT-5-mini는 1.0만 지원
                        .build())
                .call()
                .content();
    }

    /**
     * 검색 문장을 벡터화 → VectorStore에서 topK 문서 가져오기
     */
    private List<Document> searchDocuments(String query, int topK) {
        SearchRequest request = SearchRequest.builder()
                .query(query)
                .topK(topK)
                .build();

        return vectorStore.similaritySearch(request);
    }
}
