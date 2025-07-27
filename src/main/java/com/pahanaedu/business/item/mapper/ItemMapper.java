package com.pahanaedu.business.item.mapper;

import com.pahanaedu.business.item.dto.ItemMinimalDTO;
import com.pahanaedu.business.item.model.Item;

public class ItemMapper {

    public static ItemMinimalDTO toItemMinimalDTO(Item item) {
        return new ItemMinimalDTO(item.getId(), item.getItemName(), item.getDescription(), item.getBrand(),
                item.getCategory(), item.getPrice(), item.getStock(), item.getCreatedAt(), item.getUpdatedAt());
    }

}
