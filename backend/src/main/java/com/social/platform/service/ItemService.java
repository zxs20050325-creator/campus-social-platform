package com.social.platform.service;

import com.social.platform.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemService {
    Item createItem(String title, String description, java.util.List<String> imageUrls, 
                   Double price, String exchangeIntention, String category, Long userId);
    
    Page<Item> getItemList(Pageable pageable);
    
    Item updateItemStatus(Long itemId, String status);
    
    Item getItemById(Long itemId);
    
    Page<Item> getItemsByUserId(Long userId, Pageable pageable);
    
    Long getUserIdByStudentId(String studentId);
}