package com.ll.domain.post.entity;

import com.ll.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Post extends BaseEntity {
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String category;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private Integer pricePerDay;
    @Column(nullable = false)
    private String location;
    @Column(nullable = false)
    private String postCondition;
    private String options;
}
