package com.pahanaedu.persistence.category;

import com.pahanaedu.business.category.model.Category;
import com.pahanaedu.common.interfaces.Repository;

public interface CategoryRepository extends Repository<Category> {
    Category update(Category item);
}
