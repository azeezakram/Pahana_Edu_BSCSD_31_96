package com.pahanaedu.module.item.util;

import com.pahanaedu.module.category.model.Category;
import com.pahanaedu.module.item.model.Item;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemUtils {

    public static Item getItemByResultSet(ResultSet result) throws SQLException {
        return new Item(
                result.getLong("id"),
                result.getString("item_name"),
                result.getString("description"),
                result.getString("brand"),
                new Category(result.getInt("category_id"), result.getString("category_name")),
                result.getInt("price"),
                result.getInt("stock"),
                result.getTimestamp("created_at").toLocalDateTime(),
                result.getTimestamp("updated_at").toLocalDateTime()
        );
    }
}
