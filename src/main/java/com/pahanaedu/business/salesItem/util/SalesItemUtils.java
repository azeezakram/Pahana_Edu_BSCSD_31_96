package com.pahanaedu.business.salesItem.util;

import com.pahanaedu.business.item.model.Item;
import com.pahanaedu.business.salesItem.model.SalesItem;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SalesItemUtils {

    public static SalesItem getSalesItemByResultSet(ResultSet result) throws SQLException {
        return new SalesItem(
                result.getLong("id"),
                result.getLong("item_id"),
                result.getLong("sales_history_id"),
                result.getInt("sell_price"),
                result.getInt("unit"),
                result.getInt("subtotal")
        );
    }

    public static boolean isValid(Item item) {
        return (item.getItemName() == null || item.getItemName().isBlank()) || (item.getPrice() < 1 || item.getPrice() == null) || (item.getStock() < 0);
    }
}
