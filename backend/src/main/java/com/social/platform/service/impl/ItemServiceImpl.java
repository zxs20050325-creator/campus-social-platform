package com.social.platform.service.impl;

import com.social.platform.entity.Item;
import com.social.platform.entity.User;
import com.social.platform.repository.ItemRepository;
import com.social.platform.repository.UserRepository;
import com.social.platform.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Item createItem(String title, String description, List<String> imageUrls, 
                          Double price, String exchangeIntention, String category, Long userId) {
        // 验证用户是否存在
        User user = userRepository.findById(userId).orElseThrow(() -> 
            new RuntimeException("User not found with id: " + userId));

        Item item = new Item();
        item.setTitle(title);
        item.setDescription(description);
        item.setImageUrls(imageUrls);
        if (price != null) {
            item.setPrice(java.math.BigDecimal.valueOf(price));
        }
        item.setExchangeIntention(exchangeIntention);
        item.setCategory(category);
        item.setUserId(userId);
        item.setStatus(Item.ItemStatus.ON_SHELF);

        return itemRepository.save(item);
    }

    @Override
    public Page<Item> getItemList(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    @Override
    public Item updateItemStatus(Long itemId, String status) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> 
            new RuntimeException("Item not found with id: " + itemId));

        // 将字符串状态转换为枚举
        Item.ItemStatus itemStatus;
        try {
            itemStatus = Item.ItemStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + status);
        }

        item.setStatus(itemStatus);
        return itemRepository.save(item);
    }

    @Override
    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> 
            new RuntimeException("Item not found with id: " + itemId));
    }

    @Override
    public Page<Item> getItemsByUserId(Long userId, Pageable pageable) {
        return itemRepository.findByUserId(userId, pageable);
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