package com.pahanaedu.module.item.mapper;

import com.pahanaedu.module.item.dto.ItemMinimalDTO;
import com.pahanaedu.module.item.model.Item;
import com.pahanaedu.module.user.module.customer.dto.CustomerMinimalDTO;
import com.pahanaedu.module.user.module.customer.model.Customer;

public class ItemMapper {

    public static ItemMinimalDTO toItemMinimalDTO(Item item) {
        return new ItemMinimalDTO(item.getId(), item.getItemName(), item.getDescription(),
                item.getCategory(), item.getPrice(), item.getStock(), item.getCreatedAt(), item.getUpdatedAt());
    }

}
