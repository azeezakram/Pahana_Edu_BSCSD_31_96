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
                result.getLong("customer_id"),
                null,
                result.getInt("grand_total"),
                result.getTimestamp("created_at").toLocalDateTime()
        );
    }


    public static boolean isInvalid(SellHistory sellHistory) {
        return sellHistory == null ||
                sellHistory.getCustomerId() == null ||
                sellHistory.getCustomerId() < 1 ||
                sellHistory.getSellItems() == null ||
                sellHistory.getSellItems().isEmpty();
    }

}
