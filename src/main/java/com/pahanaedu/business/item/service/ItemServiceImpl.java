package com.pahanaedu.business.item.service;

import com.pahanaedu.business.item.dto.ItemDTO;
import com.pahanaedu.business.item.exception.ItemException;
import com.pahanaedu.business.item.mapper.ItemMapper;
import com.pahanaedu.business.item.model.Item;
import com.pahanaedu.business.item.util.ItemUtils;
import com.pahanaedu.common.interfaces.Service;
import com.pahanaedu.common.interfaces.UpdatableService;
import com.pahanaedu.persistence.item.ItemRepositoryImpl;

import java.util.List;
import java.util.function.Function;

public class ItemServiceImpl implements Service<Item, ItemDTO>, UpdatableService<Item, ItemDTO> {

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
        ItemUtils.stockValidation(itemId, itemCurrentStock, soldUnit);
        int updatedStock = itemCurrentStock - soldUnit;
        boolean result = itemIRepository.updateStock(itemId, updatedStock);
        if (!result) {
            throw new ItemException("Stock update failed, please try again");
        }
    }

}
