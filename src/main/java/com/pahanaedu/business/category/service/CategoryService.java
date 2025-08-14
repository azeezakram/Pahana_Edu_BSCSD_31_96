package com.pahanaedu.business.category.service;

import com.pahanaedu.business.category.dto.CategoryDTO;
import com.pahanaedu.business.category.model.Category;
import com.pahanaedu.common.interfaces.Service;

public interface CategoryService extends Service<Category, CategoryDTO> {
    CategoryDTO update(Category category);
}
