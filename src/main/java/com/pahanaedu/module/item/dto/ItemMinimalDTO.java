package com.pahanaedu.module.item.dto;

import com.pahanaedu.module.category.model.Category;

import java.time.LocalDateTime;

public record ItemMinimalDTO(Long id, String itemName, String description, Category category,
                             Integer price, Integer stock, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
