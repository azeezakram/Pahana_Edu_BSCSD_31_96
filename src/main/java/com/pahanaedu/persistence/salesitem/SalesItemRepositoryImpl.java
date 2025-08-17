package com.pahanaedu.persistence.salesitem;

import com.pahanaedu.business.saleshistory.model.SalesHistory;
import com.pahanaedu.business.salesItem.model.SalesItem;
import com.pahanaedu.business.salesItem.util.SalesItemUtils;
import com.pahanaedu.config.db.impl.DbConnectionFactoryImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalesItemRepositoryImpl implements SalesItemRepository {

    private final DbConnectionFactoryImpl dbConnectionFactoryImpl;
    private static final String DATABASE_TYPE = "production";

    public SalesItemRepositoryImpl() {
        this.dbConnectionFactoryImpl = new DbConnectionFactoryImpl();
    }

    @Override
    public SalesItem findById(Long id) {

        SalesItem salesItem = null;
        String query = "select * from sales_item sl join item i on i.id = sl.item_id where sl.id=?";

        try (
                Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE);
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, id.intValue());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                salesItem = SalesItemUtils.getSalesItemByResultSet(result);
            }
            System.out.println(salesItem);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return salesItem;

    }

    public List<SalesItem> findBySellHistoryId(Long sellHistoryId) {
        List<SalesItem> salesItems = new ArrayList<>();
        String query = "SELECT * FROM sales_item WHERE sales_history_id = ?";

        try (
                Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE);
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, sellHistoryId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                salesItems.add(SalesItemUtils.getSalesItemByResultSet(rs));

            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return salesItems;
    }


    @Override
    public List<SalesItem> findAll() {
        return List.of();
    }

    @Override
    public SalesItem save(SalesItem sellitem) {
        return null;
    }


    public void saveBySellHistory(SalesHistory salesHistory) {
        String insertQuery = """
        INSERT INTO sales_item(sales_history_id, item_id, sell_price, unit, subtotal)
        VALUES (?, ?, ?, ?, ?)
    """;

        try (Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE)) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
                for (SalesItem salesItem : salesHistory.getSalesItems()) {
                    statement.setLong(1, salesHistory.getId());
                    statement.setLong(2, salesItem.getItemId());
                    statement.setInt(3, salesItem.getSellPrice());
                    statement.setInt(4, salesItem.getUnit());
                    statement.setInt(5, salesItem.getSubTotal());

                    int affectedRows = statement.executeUpdate();
                    if (affectedRows == 0) {
                        connection.rollback();
                        throw new SQLException("Creating sales item failed, no rows affected.");
                    }
                }
                connection.commit();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error saving sales items for sellHistory id: " + salesHistory.getId(), e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sellItemsQuery = "DELETE FROM sales_item WHERE sales_history_id = ?";

        try (Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE);
             PreparedStatement statement = connection.prepareStatement(sellItemsQuery)) {

            statement.setLong(1, id);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                return false;
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to delete sales item with id: " + id, e);
        }
        return true;
    }
}
