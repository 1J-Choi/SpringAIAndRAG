package com.ll.domain.post.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostVectorRepository{
    void upsertEmbedding(Long postId, String embeddingJson);
    List<Long> findSimilarPostIds(String embeddingJson, int topK);

    int countEmbeddings();
}
