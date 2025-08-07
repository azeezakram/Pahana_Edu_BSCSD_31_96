package com.pahanaedu.business.sellHistory.util;

import com.pahanaedu.business.category.model.Category;
import com.pahanaedu.business.item.model.Item;
import com.pahanaedu.business.sellHistory.model.SellHistory;
import com.pahanaedu.business.sellItem.model.SellItem;
import com.pahanaedu.business.user.module.customer.model.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SellHistoryUtils {

    public static SellHistory getSellHistoryByResultSet(ResultSet result) throws SQLException {
        return new SellHistory(
                result.getLong("id"),
                new Customer(
                        result.getLong("c_id"),
                        result.getString("c_name"),
                        result.getString("c_role"),
                        result.getTimestamp("c_created_at").toLocalDateTime(),
                        result.getTimestamp("c_updated_at").toLocalDateTime(),
                        result.getString("account_number"),
                        result.getString("address"),
                        result.getString("phone_number")
                ),
                null,
                result.getInt("grand_total"),
                result.getTimestamp("created_at").toLocalDateTime()
        );
    }

    public static SellItem getSellItemByResultSet(ResultSet rs) throws SQLException {
        return new SellItem(
                rs.getLong("si_id"),
                new Item(
                        rs.getLong("i_id"),
                        rs.getString("item_name"),
                        rs.getString("description"),
                        rs.getString("brand"),
                        new Category(
                                rs.getInt("i_category_id"),
                                rs.getString("i_category_name")
                        ),
                        rs.getInt("price"),
                        rs.getInt("stock"),
                        rs.getTimestamp("i_created_at") != null ? rs.getTimestamp("i_created_at").toLocalDateTime() : null,
                        rs.getTimestamp("i_updated_at") != null ? rs.getTimestamp("i_updated_at").toLocalDateTime() : null
                ),
                rs.getLong("sell_history_id"),
                rs.getInt("sell_price"),
                rs.getInt("unit"),
                rs.getInt("subtotal")
        );
    }

    public static boolean isInvalid(SellHistory sellHistory) {
        return sellHistory == null ||
                sellHistory.getCustomer() == null ||
                sellHistory.getCustomer().getId() == null ||
                sellHistory.getCustomer().getId() < 1 ||
                sellHistory.getSellItems() == null ||
                sellHistory.getSellItems().isEmpty();
    }

}
