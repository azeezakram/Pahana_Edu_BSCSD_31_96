package com.pahanaedu.business.item.service;

import com.pahanaedu.business.item.dto.ItemDTO;
import com.pahanaedu.business.item.util.ItemUtils;
import com.pahanaedu.common.interfaces.Repository;
import com.pahanaedu.common.interfaces.Service;
import com.pahanaedu.business.item.exception.ItemException;
import com.pahanaedu.business.item.mapper.ItemMapper;
import com.pahanaedu.business.item.model.Item;
import com.pahanaedu.persistence.item.ItemRepositoryImpl;

import java.util.List;

public class ItemServiceImpl implements Service<Item, ItemDTO> {

    private final ItemRepositoryImpl itemIRepository;

    public ItemServiceImpl() {
        this.itemIRepository = new ItemRepositoryImpl();
    }

    @Override
    public ItemDTO findById(Long id) {
        Item item = itemIRepository.findById(id);
        return item != null ? ItemMapper.toItemDTO(item) : null;
    }

    @Override
    public List<ItemDTO> findAll() {
        List<Item> items = itemIRepository.findAll();

        return !items.isEmpty() ? items.stream()
                .map(ItemMapper::toItemDTO)
                .toList() : null;
    }

    @Override
    public ItemDTO create(Item item) {
        if (ItemUtils.isInvalid(item)) {
            throw  new ItemException("Invalid item detail/s provided");
        }

        Item newItem = itemIRepository.save(item);
        return findById(newItem.getId());
    }

    @Override
    public ItemDTO update(Item item) {
        Item updatedItem = itemIRepository.update(item);

        return findById(updatedItem.getId());
    }

    @Override
    public boolean delete(Long id) {
        return itemIRepository.delete(id);
    }

    public void updateStock(Long itemId, Integer itemCurrentStock, Integer soldUnit) {

        if (itemId == null || itemId < 1 ||
                itemCurrentStock == null || itemCurrentStock < 0 ||
                soldUnit == null || soldUnit < 0) {
            throw new ItemException("Problem in updating stock");
        }


        itemIRepository.updateStock(itemId, itemCurrentStock - soldUnit);

    }
}
