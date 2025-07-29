package com.pahanaedu.business.item.mapper;

import com.pahanaedu.business.item.dto.ItemDto;
import com.pahanaedu.business.item.model.Item;

public class ItemMapper {

    public static ItemDto toItemMinimalDTO(Item item) {

        return new ItemDto.Builder()
                .id(item.getId())
                .itemName(item.getItemName())
                .description(item.getDescription())
                .brand(item.getBrand())
                .category(item.getCategory())
                .price(item.getPrice())
                .stock(item.getStock())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }

}
