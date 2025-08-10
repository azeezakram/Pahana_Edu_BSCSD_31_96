package com.pahanaedu.persistence.sellhistory;

import com.pahanaedu.business.sellHistory.model.SellHistory;
import com.pahanaedu.business.sellHistory.util.SellHistoryUtils;
import com.pahanaedu.common.interfaces.Repository;
import com.pahanaedu.config.db.impl.DbConnectionFactoryImpl;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SellHistoryRepositoryImpl implements Repository<SellHistory> {

    private final DbConnectionFactoryImpl dbConnectionFactoryImpl;
    private static final String DATABASE_TYPE = "production";

    public SellHistoryRepositoryImpl() {
        this.dbConnectionFactoryImpl = new DbConnectionFactoryImpl();
    }

    @Override
    public SellHistory findById(Long id) {
        SellHistory sellHistory = null;
        String sellHistoryWithCustomerQuery = "SELECT * FROM sell_history WHERE id = ?";

        try (Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE)) {

            try (PreparedStatement statement = connection.prepareStatement(sellHistoryWithCustomerQuery)) {
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    sellHistory = SellHistoryUtils.getSellHistoryByResultSet(resultSet);
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

        String sellHistoryWithCustomerQuery = "SELECT * FROM sell_history";

        try (Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE)) {
            try (PreparedStatement statement = connection.prepareStatement(sellHistoryWithCustomerQuery)) {
                ResultSet sellHistoryWithCustomerResult = statement.executeQuery();
                while (sellHistoryWithCustomerResult.next()) {

                    SellHistory sellHistory = SellHistoryUtils.getSellHistoryByResultSet(sellHistoryWithCustomerResult);
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

        System.out.println(sellHistory);

        try (
                Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE)
        ) {

            try (PreparedStatement statement = connection.prepareStatement(newSellHistoryQuery, Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, sellHistory.getCustomerId());
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

            }

            sellHistory.setId(generatedId);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return sellHistory;
    }

    @Override
    public boolean delete(Long id) {
        String sellHistoryQuery = "DELETE FROM sell_history WHERE id = ?";

        try (Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE);
             PreparedStatement statement = connection.prepareStatement(sellHistoryQuery)) {

            statement.setLong(1, id);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                return false;
            }

            return true;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to delete sell history with id: " + id, e);
        }
    }


}
