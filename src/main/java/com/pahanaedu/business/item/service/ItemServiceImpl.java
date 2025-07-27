package com.pahanaedu.business.item.service;

import com.pahanaedu.common.interfaces.Repository;
import com.pahanaedu.common.interfaces.Service;
import com.pahanaedu.business.item.dto.ItemMinimalDTO;
import com.pahanaedu.business.item.exception.InvalidItemDetailException;
import com.pahanaedu.business.item.mapper.ItemMapper;
import com.pahanaedu.business.item.model.Item;
import com.pahanaedu.persistence.item.ItemRepositoryImpl;

import java.util.List;

import static com.pahanaedu.business.item.util.ItemUtils.isValid;

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
        if (isValid(item)) {
            throw  new InvalidItemDetailException("Invalid item detail/s provided");
        }

        Item newItem = itemIRepository.save(item);
        return findById(newItem.getId());
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
