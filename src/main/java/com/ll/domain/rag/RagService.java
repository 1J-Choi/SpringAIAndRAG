package com.ll.domain.rag;

import com.ll.domain.post.entity.Post;
import com.ll.domain.post.repository.PostVectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RagService {

    private final EmbeddingModel embeddingModel;
    private final PostVectorRepository postVectorRepository;

    @Transactional
    public void indexPost(Post post) {
        String content = formatPostForRag(post);
        String embeddingJson = generateEmbeddingJson(content);
        postVectorRepository.upsertEmbedding(post.getId(), embeddingJson);
    }

    public List<Long> searchSimilarPostIds(String query, int topK) {
        String embeddingJson = generateEmbeddingJson(query);
        return postVectorRepository.findSimilarPostIds(embeddingJson, topK);
    }

    private String generateEmbeddingJson(String content) {
        EmbeddingResponse response = embeddingModel.embedForResponse(List.of(content));
        float[] embeddingArray = response.getResult().getOutput();

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < embeddingArray.length; i++) {
            json.append(embeddingArray[i]);
            if (i < embeddingArray.length - 1) json.append(",");
        }
        json.append("]");
        return json.toString();
    }

    public String formatPostForRag(Post post) {
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

    public int getDocumentCount() {
        return postVectorRepository.countEmbeddings();
    }
}

