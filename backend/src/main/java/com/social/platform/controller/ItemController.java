package com.social.platform.controller;

import com.social.platform.entity.Item;
import com.social.platform.entity.User;
import com.social.platform.service.ItemService;
import com.social.platform.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = {"*"})
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 发布商品
     */
    @PostMapping
    public ResponseEntity<?> createItem(@RequestBody CreateItemRequest request, 
                                       @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = null;
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
            }
            
            if (token == null || !jwtUtil.validateToken(token, null)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
            
            String username = jwtUtil.extractUsername(token);
            Long userId = getUserIdFromToken(token);
            
            Item item = itemService.createItem(
                    request.getTitle(),
                    request.getDescription(),
                    request.getImageUrls(),
                    request.getPrice(),
                    request.getExchangeIntention(),
                    request.getCategory(),
                    userId
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Item created successfully");
            response.put("item", item);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 分页查询商品列表
     */
    @GetMapping
    public ResponseEntity<?> getItemList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : 
                Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Item> itemsPage = itemService.getItemList(pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("items", itemsPage.getContent());
        response.put("currentPage", itemsPage.getNumber());
        response.put("totalItems", itemsPage.getTotalElements());
        response.put("totalPages", itemsPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }

    /**
     * 更新商品状态（下架）
     */
    @PutMapping("/{itemId}/status")
    public ResponseEntity<?> updateItemStatus(
            @PathVariable Long itemId,
            @RequestBody UpdateItemStatusRequest request,
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
            
            // 验证当前用户是否有权限修改该商品
            Item existingItem = itemService.getItemById(itemId);
            if (!existingItem.getUserId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update your own items");
            }
            
            Item updatedItem = itemService.updateItemStatus(itemId, request.getStatus());
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Item status updated successfully");
            response.put("item", updatedItem);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 获取当前用户的商品列表
     */
    @GetMapping("/my-items")
    public ResponseEntity<?> getMyItems(@RequestHeader("Authorization") String authorizationHeader,
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
            
            Page<Item> itemsPage = itemService.getItemsByUserId(userId, pageable);
            
            Map<String, Object> response = new HashMap<>();
            response.put("items", itemsPage.getContent());
            response.put("currentPage", itemsPage.getNumber());
            response.put("totalItems", itemsPage.getTotalElements());
            response.put("totalPages", itemsPage.getTotalPages());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    private Long getUserIdFromToken(String token) {
        // 这里需要实现从token中提取用户ID的方法
        // 通常是在token中编码用户ID或者先获取用户名再查询数据库获取ID
        String username = jwtUtil.extractUsername(token);
        // 在实际应用中，这里应该通过用户名查询数据库获取用户ID
        // 为了简化实现，我们假设有一个方法可以从用户名获取用户ID
        return itemService.getUserIdByStudentId(username);
    }

    // 请求数据传输对象
    public static class CreateItemRequest {
        private String title;
        private String description;
        private List<String> imageUrls;
        private Double price;
        private String exchangeIntention;
        private String category;

        // Getters and Setters
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<String> getImageUrls() {
            return imageUrls;
        }

        public void setImageUrls(List<String> imageUrls) {
            this.imageUrls = imageUrls;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public String getExchangeIntention() {
            return exchangeIntention;
        }

        public void setExchangeIntention(String exchangeIntention) {
            this.exchangeIntention = exchangeIntention;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }

    public static class UpdateItemStatusRequest {
        private String status;

        // Getters and Setters
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}