package com.ll.domain.post.service;

import com.ll.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostVectorService {
    private final VectorStore vectorStore;

    /**
     * 게시글을 Vector Store에 색인
     */
    public void indexPost(Post post) {
        String text = post.getTitle() + "\n" + post.getContent();

        String docId = UUID.randomUUID().toString();

        Document document = new Document(
                docId,
                text,
                Map.of("postId", post.getId())
        );

        vectorStore.add(List.of(document));

        System.out.println("✅ Post ID " + post.getId() + " 벡터 색인 완료!(MariaDB VectorStore)");
    }
}
