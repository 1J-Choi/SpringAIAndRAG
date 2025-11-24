package com.ll.domain.post.repository;

import com.ll.domain.post.entity.Post;
import com.ll.global.queryDsl.CustomQuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class PostQueryRepository extends CustomQuerydslRepositorySupport {
    public PostQueryRepository(){
        super(Post.class);
    }
}
