package com.social.platform.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "parent_comment_id")
    private Long parentCommentId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 检查约束：评论必须关联帖子或商品之一，不能同时为空或同时存在
    @PrePersist
    @PreUpdate
    public void validate() {
        if ((postId == null && itemId == null) || (postId != null && itemId != null)) {
            throw new IllegalStateException("Comment must be associated with either a post or an item, but not both");
        }
    }
}