package com.pahanaedu.persistence.item;

import com.pahanaedu.business.item.model.Item;
import com.pahanaedu.business.sellHistory.model.SellHistory;
import com.pahanaedu.business.sellItem.model.SellItem;
import com.pahanaedu.common.interfaces.Repository;

import java.util.List;

public interface ItemRepository extends Repository<Item> {
    boolean updateStock(Long itemId, Integer updatedStock);
    Item update(Item item);
}
