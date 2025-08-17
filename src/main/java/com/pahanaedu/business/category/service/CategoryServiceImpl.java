package com.pahanaedu.business.category.service;

import com.pahanaedu.business.category.dto.CategoryDTO;
import com.pahanaedu.business.category.mapper.CategoryMapper;
import com.pahanaedu.business.category.model.Category;
import com.pahanaedu.persistence.category.CategoryRepositoryImpl;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepositoryImpl categoryRepository;

    public CategoryServiceImpl(String dbType) {
        this.categoryRepository = new CategoryRepositoryImpl(dbType);
    }

    @Override
    public CategoryDTO findById(Long id) {
        com.pahanaedu.business.category.model.Category category = categoryRepository.findById(id);
        return category != null ? CategoryMapper.toCategoryDTO(category) : null;
    }

    @Override
    public List<CategoryDTO> findAll() {
        List<com.pahanaedu.business.category.model.Category> categories = categoryRepository.findAll();

        return !categories.isEmpty() ? categories.stream()
                .map(CategoryMapper::toCategoryDTO)
                .toList() : null;
    }

    @Override
    public CategoryDTO create(Category category) {
//        if (CategoryUtils.isInvalid(item)) {
//            throw new ItemException("Invalid category detail/s provided");
//        }

        Category newCategory = categoryRepository.save(category);
        return findById(newCategory.getId().longValue());
    }

    @Override
    public CategoryDTO update(Category category) {
        Category updatedCategory = categoryRepository.update(category);

        return findById(updatedCategory.getId().longValue());
    }

    @Override
    public boolean delete(Long id) {
        return categoryRepository.delete(id);
    }

}
