package com.social.platform.service.impl;

import com.social.platform.entity.Post;
import com.social.platform.entity.User;
import com.social.platform.repository.PostRepository;
import com.social.platform.repository.UserRepository;
import com.social.platform.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Post createPost(String content, List<String> imageUrls, Long userId) {
        // 验证用户是否存在
        User user = userRepository.findById(userId).orElseThrow(() -> 
            new RuntimeException("User not found with id: " + userId));

        Post post = new Post();
        post.setContent(content);
        post.setImageUrls(imageUrls);
        post.setUserId(userId);

        return postRepository.save(post);
    }

    @Override
    public Page<Post> getPostList(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public Page<Post> getPostsByUserId(Long userId, Pageable pageable) {
        return postRepository.findByUserId(userId, pageable);
    }

    @Override
    public Long getUserIdByStudentId(String studentId) {
        User user = userRepository.findByStudentId(studentId);
        if (user == null) {
            throw new RuntimeException("User not found with student id: " + studentId);
        }
        return user.getId();
    }
}