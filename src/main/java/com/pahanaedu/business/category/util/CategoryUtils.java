package com.pahanaedu.business.category.util;

import com.pahanaedu.business.category.model.Category;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryUtils {

    public static Category getCategoryByResultSet(ResultSet result) throws SQLException {
        return new Category(
                result.getInt("id"),
                result.getString("category_name")
        );
    }

}
