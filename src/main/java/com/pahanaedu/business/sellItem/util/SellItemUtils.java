package com.pahanaedu.business.sellItem.util;

import com.pahanaedu.business.category.model.Category;
import com.pahanaedu.business.item.model.Item;
import com.pahanaedu.business.item.util.ItemUtils;
import com.pahanaedu.business.sellItem.model.SellItem;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SellItemUtils {

    public static SellItem getSellItemByResultSet(ResultSet result) throws SQLException {
        return new SellItem(
                result.getLong("id"),
                result.getLong("item_id"),
                result.getLong("sell_history_id"),
                result.getInt("sell_price"),
                result.getInt("unit"),
                result.getInt("subtotal")
        );
    }


    public static boolean isValid(Item item) {
        return (item.getItemName() == null || item.getItemName().isBlank()) || (item.getPrice() < 1 || item.getPrice() == null) || (item.getStock() < 0);
    }
}
