package com.social.platform.controller;

import com.social.platform.entity.Post;
import com.social.platform.service.PostService;
import com.social.platform.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = {"*"})
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 发布帖子
     */
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody CreatePostRequest request,
                                       @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = null;
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
            }
            
            if (token == null || !jwtUtil.validateToken(token, null)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
            
            Long userId = getUserIdFromToken(token);
            
            Post post = postService.createPost(
                    request.getContent(),
                    request.getImageUrls(),
                    userId
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Post created successfully");
            response.put("post", post);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 分页查询帖子列表
     */
    @GetMapping
    public ResponseEntity<?> getPostList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : 
                Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Post> postsPage = postService.getPostList(pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("posts", postsPage.getContent());
        response.put("currentPage", postsPage.getNumber());
        response.put("totalItems", postsPage.getTotalElements());
        response.put("totalPages", postsPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }

    /**
     * 获取当前用户的帖子列表
     */
    @GetMapping("/my-posts")
    public ResponseEntity<?> getMyPosts(@RequestHeader("Authorization") String authorizationHeader,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        try {
            String token = null;
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
            }
            
            if (token == null || !jwtUtil.validateToken(token, null)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
            
            Long userId = getUserIdFromToken(token);
            Pageable pageable = PageRequest.of(page, size);
            
            Page<Post> postsPage = postService.getPostsByUserId(userId, pageable);
            
            Map<String, Object> response = new HashMap<>();
            response.put("posts", postsPage.getContent());
            response.put("currentPage", postsPage.getNumber());
            response.put("totalItems", postsPage.getTotalElements());
            response.put("totalPages", postsPage.getTotalPages());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    private Long getUserIdFromToken(String token) {
        // 从token中提取用户名，然后通过用户名查询用户ID
        String username = jwtUtil.extractUsername(token);
        return postService.getUserIdByStudentId(username);
    }

    // 请求数据传输对象
    public static class CreatePostRequest {
        private String content;
        private List<String> imageUrls;

        // Getters and Setters
        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<String> getImageUrls() {
            return imageUrls;
        }

        public void setImageUrls(List<String> imageUrls) {
            this.imageUrls = imageUrls;
        }
    }
}