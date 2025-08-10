package com.pahanaedu.business.item.util;

import com.pahanaedu.business.category.model.Category;
import com.pahanaedu.business.item.exception.ItemException;
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

    public static void stockValidation(Long itemId, Integer itemCurrentStock, Integer soldUnit) {
        if (itemId == null || itemId < 1) {
            throw new ItemException("Invalid item ID");
        }
        if (itemCurrentStock == null || itemCurrentStock < 0) {
            throw new ItemException("Invalid current stock value");
        }
        if (soldUnit == null || soldUnit <= 0) {
            throw new ItemException("Invalid sold unit value");
        }
        if (soldUnit > itemCurrentStock) {
            throw new ItemException("Sold units exceed current stock");
        }
    }
}
