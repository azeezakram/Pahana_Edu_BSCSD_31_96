package com.pahanaedu.business.item.dto;

import com.pahanaedu.business.category.model.Category;

import java.time.LocalDateTime;

public record ItemMinimalDTO(Long id, String itemName, String description, String brand, Category category,
                             Integer price, Integer stock, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
