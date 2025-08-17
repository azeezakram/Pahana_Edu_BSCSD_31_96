package com.pahanaedu.business.item.service;

import com.pahanaedu.business.category.dto.CategoryDTO;
import com.pahanaedu.business.category.service.CategoryService;
import com.pahanaedu.business.category.service.CategoryServiceImpl;
import com.pahanaedu.business.item.dto.ItemDTO;
import com.pahanaedu.business.item.exception.ItemException;
import com.pahanaedu.business.item.mapper.ItemMapper;
import com.pahanaedu.business.item.model.Item;
import com.pahanaedu.business.item.util.ItemUtils;
import com.pahanaedu.persistence.item.ItemRepository;
import com.pahanaedu.persistence.item.ItemRepositoryImpl;

import java.util.List;

public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemIRepository;
    private final CategoryService categoryService;

    public ItemServiceImpl() {
        this.itemIRepository = new ItemRepositoryImpl();
        this.categoryService = new CategoryServiceImpl();
    }

    @Override
    public ItemDTO findById(Long id) {
        Item item = itemIRepository.findById(id);
        if (item != null && item.getCategory() != null) {
            CategoryDTO category = categoryService.findById(item.getCategory().getId().longValue());
            if (category != null) {
                item.getCategory().setCategoryName(category.categoryName());
            }
        }
        return item != null ? ItemMapper.toItemDTO(item) : null;
    }

    @Override
    public List<ItemDTO> findAll() {
        List<Item> items = itemIRepository.findAll();
        List<CategoryDTO> categories = categoryService.findAll();

        if (!items.isEmpty()) {
            items.forEach(item ->
                    categories.stream()
                            .filter(category -> category.id().equals(item.getCategory().getId()))
                            .findFirst()
                            .ifPresent(category -> item.getCategory().setCategoryName(category.categoryName()))
            );
        }

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
