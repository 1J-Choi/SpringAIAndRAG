package com.ll.domain.post.service;

import com.ll.domain.post.entity.Post;
import com.ll.domain.post.repository.PostRepository;
import com.ll.domain.rag.RagService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public List<Post> findAllByIds(List<Long> ids) {
        return postRepository.findAllById(ids);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }
}

