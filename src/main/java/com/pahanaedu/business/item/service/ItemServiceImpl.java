package com.pahanaedu.business.item.service;

import com.pahanaedu.business.item.dto.ItemDto;
import com.pahanaedu.common.interfaces.Repository;
import com.pahanaedu.common.interfaces.Service;
import com.pahanaedu.business.item.exception.InvalidItemDetailException;
import com.pahanaedu.business.item.mapper.ItemMapper;
import com.pahanaedu.business.item.model.Item;
import com.pahanaedu.persistence.item.ItemRepositoryImpl;

import java.util.List;

import static com.pahanaedu.business.item.util.ItemUtils.isValid;

public class ItemServiceImpl implements Service<Item, ItemDto> {

    private final Repository<Item> itemIRepository;

    public ItemServiceImpl() {
        this.itemIRepository = new ItemRepositoryImpl();
    }

    @Override
    public ItemDto findById(Long id) {
        Item item = itemIRepository.findById(id);
        return item != null ? ItemMapper.toItemMinimalDTO(item) : null;
    }

    @Override
    public List<ItemDto> findAll() {
        List<Item> items = itemIRepository.findAll();

        return !items.isEmpty() ? items.stream()
                .map(ItemMapper::toItemMinimalDTO)
                .toList() : null;
    }

    @Override
    public ItemDto create(Item item) {
        if (isValid(item)) {
            throw  new InvalidItemDetailException("Invalid item detail/s provided");
        }

        Item newItem = itemIRepository.save(item);
        return findById(newItem.getId());
    }

    @Override
    public ItemDto update(Item item) {
        Item updatedItem = itemIRepository.update(item);

        return findById(updatedItem.getId());
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

}
