package com.pahanaedu.persistence.saleshistory;

import com.pahanaedu.business.saleshistory.model.SalesHistory;
import com.pahanaedu.business.saleshistory.util.SalesHistoryUtils;
import com.pahanaedu.config.db.factory.DbConnectionFactory;
import com.pahanaedu.config.db.impl.DbConnectionFactoryImpl;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SalesHistoryRepositoryImpl implements SalesHistoryRepository {

    private final DbConnectionFactory dbConnectionFactoryImpl;
    private static final String DATABASE_TYPE = "production";

    public SalesHistoryRepositoryImpl() {
        this.dbConnectionFactoryImpl = new DbConnectionFactoryImpl();
    }

    @Override
    public SalesHistory findById(Long id) {
        SalesHistory salesHistory = null;
        String salesHistoryWithCustomerQuery = "SELECT * FROM sales_history WHERE id = ?";

        try (Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE)) {

            try (PreparedStatement statement = connection.prepareStatement(salesHistoryWithCustomerQuery)) {
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    salesHistory = SalesHistoryUtils.getSalesHistoryByResultSet(resultSet);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return salesHistory;
    }

    @Override
    public List<SalesHistory> findAll() {
        List<SalesHistory> sellHistories = new ArrayList<>();

        String salesHistoryWithCustomerQuery = "SELECT * FROM sales_history";

        try (Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE)) {
            try (PreparedStatement statement = connection.prepareStatement(salesHistoryWithCustomerQuery)) {
                ResultSet sellHistoryWithCustomerResult = statement.executeQuery();
                while (sellHistoryWithCustomerResult.next()) {

                    SalesHistory salesHistory = SalesHistoryUtils.getSalesHistoryByResultSet(sellHistoryWithCustomerResult);
                    sellHistories.add(salesHistory);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return sellHistories;
    }

    @Override
    public SalesHistory save(SalesHistory salesHistory) {

        int result;
        long generatedId = 0L;

        String newSalesHistoryQuery = """
                    insert into sales_history(customer_id, grand_total, created_at)
                    values (?, ?, ?)
                """;

        System.out.println(salesHistory);

        try (
                Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE)
        ) {

            try (PreparedStatement statement = connection.prepareStatement(newSalesHistoryQuery, Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, salesHistory.getCustomerId());
                statement.setInt(2, salesHistory.getGrandTotal());
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
                    throw new SQLException("Creating sales history failed, no rows affected.");
                }

            }

            salesHistory.setId(generatedId);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return salesHistory;
    }

    @Override
    public boolean delete(Long id) {
        String salesHistoryQuery = "DELETE FROM sales_history WHERE id = ?";

        try (Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE);
             PreparedStatement statement = connection.prepareStatement(salesHistoryQuery)) {

            statement.setLong(1, id);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                return false;
            }

            return true;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to delete sales history with id: " + id, e);
        }
    }


}
