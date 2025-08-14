package com.pahanaedu.persistence.item;

import com.pahanaedu.business.item.model.Item;
import com.pahanaedu.common.interfaces.Repository;

public interface ItemRepository extends Repository<Item> {
    boolean updateStock(Long itemId, Integer updatedStock);
    Item update(Item item);
}
