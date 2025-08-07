package com.pahanaedu.business.item.util;

import com.pahanaedu.business.category.model.Category;
import com.pahanaedu.business.item.model.Item;

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


    public static boolean isInvalid(Item item) {
        return item == null
                || item.getItemName() == null || item.getItemName().isBlank()
                || item.getPrice() == null || item.getPrice() < 1
                || item.getStock() == null || item.getStock() < 0;
    }

}
