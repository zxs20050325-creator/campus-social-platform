package com.social.platform.service;

import com.social.platform.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    Post createPost(String content, java.util.List<String> imageUrls, Long userId);
    
    Page<Post> getPostList(Pageable pageable);
    
    Page<Post> getPostsByUserId(Long userId, Pageable pageable);
    
    Long getUserIdByStudentId(String studentId);
}