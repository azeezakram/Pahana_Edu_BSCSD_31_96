package com.pahanaedu.persistence.sellhistory;

import com.pahanaedu.business.sellHistory.model.SellHistory;
import com.pahanaedu.business.sellHistory.util.SellHistoryUtils;
import com.pahanaedu.business.sellItem.model.SellItem;
import com.pahanaedu.common.interfaces.Repository;
import com.pahanaedu.config.db.impl.DbConnectionFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SellHistoryRepositoryImpl implements Repository<SellHistory> {

    private final DbConnectionFactory dbConnectionFactory;
    private static final String DATABASE_TYPE = "production";

    public SellHistoryRepositoryImpl() {
        this.dbConnectionFactory = new DbConnectionFactory();
    }

    @Override
    public SellHistory findById(Long id) {
        SellHistory sellHistory = null;

        String sellHistoryWithCustomerQuery = "SELECT sh.id, sh.customer_id, sh.grand_total, sh.created_at, " +
                "c.id AS c_id, u.name AS c_name, u.role as c_role, u.created_at as c_created_at, u.updated_at as c_updated_at, c.phone_number, c.address, c.account_number " +
                "FROM sell_history sh " +
                "JOIN customer c ON sh.customer_id = c.id " +
                "JOIN users u ON u.id = c.id " +
                "WHERE sh.id = ?";

        String sellItemQuery = "SELECT si.id AS si_id, si.sell_history_id, si.sell_price, si.unit, si.subtotal, " +
                "i.id AS i_id, i.item_name, i.description, cat.id as i_category_id, cat.category_name as i_category_name, i.brand, i.price, i.stock, i.created_at as i_created_at, i.updated_at as i_updated_at " +
                "FROM sell_item si " +
                "JOIN item i ON si.item_id = i.id " +
                "join category cat on cat.id = i.category_id " +
                "WHERE si.sell_history_id = ?";

        try (Connection connection = dbConnectionFactory.getConnection(DATABASE_TYPE)) {

            try (PreparedStatement statement = connection.prepareStatement(sellHistoryWithCustomerQuery)) {
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    sellHistory = SellHistoryUtils.getSellHistoryByResultSet(resultSet);
                }
            }

            if (sellHistory != null) {

                List<SellItem> sellItems = new ArrayList<>();

                try (PreparedStatement statement = connection.prepareStatement(sellItemQuery)) {
                    statement.setLong(1, id);
                    ResultSet resultSet = statement.executeQuery();

                    while (resultSet.next()) {
                        SellItem sellItem = SellHistoryUtils.getSellItemByResultSet(resultSet);
                        sellItems.add(sellItem);
                    }
                    sellHistory.setSellItems(sellItems);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return sellHistory;
    }

    @Override
    public List<SellHistory> findAll() {
        List<SellHistory> sellHistories = new ArrayList<>();

        String sellHistoryWithCustomerQuery = "SELECT sh.id, sh.customer_id, sh.grand_total, sh.created_at, " +
                "c.id AS c_id, u.name AS c_name, u.role as c_role, u.created_at as c_created_at, u.updated_at as c_updated_at, c.phone_number, c.address, c.account_number " +
                "FROM sell_history sh " +
                "JOIN customer c ON sh.customer_id = c.id " +
                "JOIN users u ON u.id = c.id ";

        String sellItemQuery = "SELECT si.id AS si_id, si.sell_history_id, si.sell_price, si.unit, si.subtotal, " +
                "i.id AS i_id, i.item_name, i.description, cat.id as i_category_id, cat.category_name as i_category_name, i.brand, i.price, i.stock, i.created_at as i_created_at, i.updated_at as i_updated_at " +
                "FROM sell_item si " +
                "JOIN item i ON si.item_id = i.id " +
                "join category cat on cat.id = i.category_id " +
                "where si.sell_history_id = ?";

        try (Connection connection = dbConnectionFactory.getConnection(DATABASE_TYPE)) {
            ResultSet sellHistoryWithCustomerResult;
            ResultSet sellItemResult;

            try (PreparedStatement statement = connection.prepareStatement(sellHistoryWithCustomerQuery)) {
                sellHistoryWithCustomerResult = statement.executeQuery();

                while (sellHistoryWithCustomerResult.next()) {

                    SellHistory sellHistory = SellHistoryUtils.getSellHistoryByResultSet(sellHistoryWithCustomerResult);

                    try (PreparedStatement statement2 = connection.prepareStatement(sellItemQuery)) {
                        statement2.setLong(1, sellHistory.getId());
                        sellItemResult = statement2.executeQuery();

                        List<SellItem> sellItems = new ArrayList<>();

                        while (sellItemResult.next()) {
                            SellItem sellItem = SellHistoryUtils.getSellItemByResultSet(sellItemResult);
                            sellItems.add(sellItem);
                        }
                        sellHistory.setSellItems(sellItems);
                    }
                    sellHistories.add(sellHistory);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return sellHistories;
    }

    @Override
    public SellHistory save(SellHistory sellHistory) {

        int result;
        long generatedId = 0L;

        String newSellHistoryQuery = """
                    insert into sell_history(customer_id, grand_total, created_at)
                    values (?, ?, ?)
                """;

        String newSellItemQuery = """
                    insert into sell_item(sell_history_id, item_id, sell_price, unit, subtotal)
                    values (?, ?, ?, ?, ?)
                """;

        System.out.println(sellHistory);

        try (
                Connection connection = dbConnectionFactory.getConnection(DATABASE_TYPE);
        ) {

            try (PreparedStatement statement = connection.prepareStatement(newSellHistoryQuery, Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, sellHistory.getCustomer().getId());
                statement.setInt(2, sellHistory.getGrandTotal());
                statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                result = statement.executeUpdate();

                if (result > 0) {
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            generatedId = generatedKeys.getLong("id");
                        }
                    }
                } else {
                    connection.rollback();
                    throw new SQLException("Creating sell history failed, no rows affected.");
                }

                try (PreparedStatement statement2 = connection.prepareStatement(newSellItemQuery, Statement.RETURN_GENERATED_KEYS)) {
                    for (SellItem sellItem : sellHistory.getSellItems()) {
                        statement2.setLong(1, generatedId);
                        statement2.setLong(2, sellItem.getItem().getId());
                        statement2.setInt(3, sellItem.getSellPrice());
                        statement2.setInt(4, sellItem.getUnit());
                        statement2.setInt(5, sellItem.getSubTotal());
                        result = statement2.executeUpdate();

                        if (result < 0) {
                            connection.rollback();
                            throw new SQLException("Creating sell item failed, no rows affected.");
                        }
                    }
                }
            }

            sellHistory.setId(generatedId);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return sellHistory;
    }

    @Override
    public SellHistory update(SellHistory obj) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
