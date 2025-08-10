package com.pahanaedu.business.item.service;

import com.pahanaedu.business.item.dto.ItemDTO;
import com.pahanaedu.business.item.model.Item;
import com.pahanaedu.common.interfaces.Service;

public interface ItemService extends Service<Item, ItemDTO> {
    ItemDTO update(Item item);
    void updateStock(ItemDTO item, Integer soldUnit);
}
