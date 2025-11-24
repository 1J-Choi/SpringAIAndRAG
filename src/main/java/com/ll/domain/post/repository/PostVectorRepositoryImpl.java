package com.ll.domain.post.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostVectorRepositoryImpl implements PostVectorRepository {

    private final EntityManager em;

    @Override
    public void upsertEmbedding(Long postId, String embeddingJson) {
        em.createNativeQuery("""
            INSERT INTO post_embedding (post_id, embedding)
            VALUES (:postId, VEC_FromText(:json))
            ON DUPLICATE KEY UPDATE embedding = VEC_FromText(:json)
        """)
                .setParameter("postId", postId)
                .setParameter("json", embeddingJson)
                .executeUpdate();
    }

    @Override
    public List<Long> findSimilarPostIds(String embeddingJson, int topK) {
        List<Number> result = em.createNativeQuery("""
            SELECT post_id
            FROM post_embedding
            ORDER BY VEC_DISTANCE_COSINE(
                embedding,
                VEC_FromText(:json)
            )
            LIMIT :topK
        """)
                .setParameter("json", embeddingJson)
                .setParameter("topK", topK)
                .getResultList();

        return result.stream().map(Number::longValue).toList();
    }

    @Override
    public int countEmbeddings() {
        Number count = (Number) em.createNativeQuery("""
            SELECT COUNT(*) FROM post_embedding
        """).getSingleResult();
        return count.intValue();
    }
}

