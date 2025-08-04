package com.pahanaedu.persistence.sellhistory;

import com.pahanaedu.business.sellHistory.model.SellHistory;
import com.pahanaedu.business.sellHistory.util.SellHistoryUtils;
import com.pahanaedu.business.sellItem.model.SellItem;
import com.pahanaedu.business.sellItem.util.SellItemUtils;
import com.pahanaedu.common.interfaces.Repository;
import com.pahanaedu.config.db.impl.DbConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public SellHistory save(SellHistory obj) {
        return null;
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
