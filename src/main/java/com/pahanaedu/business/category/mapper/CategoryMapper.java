package com.pahanaedu.business.category.mapper;

import com.pahanaedu.business.category.dto.CategoryDTO;
import com.pahanaedu.business.category.model.Category;
import com.pahanaedu.business.item.dto.ItemDTO;


public class CategoryMapper {

    public static CategoryDTO toCategoryDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getCategoryName());
    }

}
