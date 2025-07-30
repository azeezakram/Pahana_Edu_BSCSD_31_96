package com.pahanaedu.business.item.service;

import com.pahanaedu.business.item.dto.ItemDTO;
import com.pahanaedu.common.interfaces.Repository;
import com.pahanaedu.common.interfaces.Service;
import com.pahanaedu.business.item.exception.ItemException;
import com.pahanaedu.business.item.mapper.ItemMapper;
import com.pahanaedu.business.item.model.Item;
import com.pahanaedu.persistence.item.ItemRepositoryImpl;

import java.util.List;

import static com.pahanaedu.business.item.util.ItemUtils.isValid;

public class ItemServiceImpl implements Service<Item, ItemDTO> {

    private final Repository<Item> itemIRepository;

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
        if (isValid(item)) {
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

}
