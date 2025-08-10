package com.pahanaedu.business.item.service;

import com.pahanaedu.business.item.dto.ItemDTO;
import com.pahanaedu.business.item.exception.ItemException;
import com.pahanaedu.business.item.mapper.ItemMapper;
import com.pahanaedu.business.item.model.Item;
import com.pahanaedu.business.item.util.ItemUtils;
import com.pahanaedu.persistence.item.ItemRepositoryImpl;

import java.util.List;

public class ItemServiceImpl implements ItemService {

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
            throw new ItemException("Invalid item detail/s provided");
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

    @Override
    public void updateStock(ItemDTO item, Integer soldUnit) {

        if (item.getId() == null || item.getId() < 1 ||
                item.getStock() == null || item.getStock() < 0 ||
                soldUnit == null || soldUnit < 0) {
            throw new ItemException("Item id or item stock or sell item unit is null");
        }

        boolean result = itemIRepository.updateStock(item.getId(), item.getStock() - soldUnit);

        if (!result) {
            throw new ItemException("Problem in updating stock");
        }

    }
}
