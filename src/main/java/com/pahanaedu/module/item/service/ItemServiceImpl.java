package com.pahanaedu.module.item.service;

import com.pahanaedu.common.interfaces.Repository;
import com.pahanaedu.common.interfaces.Service;
import com.pahanaedu.module.item.dto.ItemMinimalDTO;
import com.pahanaedu.module.item.mapper.ItemMapper;
import com.pahanaedu.module.item.model.Item;
import com.pahanaedu.module.item.repository.ItemRepositoryImpl;

import java.util.List;

public class ItemServiceImpl implements Service<Item, ItemMinimalDTO> {

    private final Repository<Item> itemIRepository;

    public ItemServiceImpl() {
        this.itemIRepository = new ItemRepositoryImpl();
    }

    @Override
    public ItemMinimalDTO findById(Long id) {
        Item item = itemIRepository.findById(id);
        return item != null ? ItemMapper.toItemMinimalDTO(item) : null;
    }

    @Override
    public List<ItemMinimalDTO> findAll() {
        List<Item> items = itemIRepository.findAll();

        return !items.isEmpty() ? items.stream()
                .map(ItemMapper::toItemMinimalDTO)
                .toList() : null;
    }

    @Override
    public ItemMinimalDTO create(Item item) {
        return null;
    }

    @Override
    public ItemMinimalDTO update(Item item) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

}
