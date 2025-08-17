package com.pahanaedu.business.saleshistory.util;

import com.pahanaedu.business.saleshistory.model.SalesHistory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SalesHistoryUtils {

    public static SalesHistory getSalesHistoryByResultSet(ResultSet result) throws SQLException {
        return new SalesHistory(
                result.getLong("id"),
                result.getLong("customer_id"),
                null,
                result.getInt("grand_total"),
                result.getTimestamp("created_at").toLocalDateTime()
        );
    }


    public static boolean isInvalid(SalesHistory salesHistory) {
        return salesHistory == null ||
                salesHistory.getCustomerId() == null ||
                salesHistory.getCustomerId() < 1 ||
                salesHistory.getSalesItems() == null ||
                salesHistory.getSalesItems().isEmpty();
    }

}
