package com.pahanaedu.business.item.mapper;

import com.pahanaedu.business.item.dto.ItemDTO;
import com.pahanaedu.business.item.model.Item;


public class ItemMapper {

    public static ItemDTO toItemDTO(Item item) {

        return new ItemDTO.Builder()
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
