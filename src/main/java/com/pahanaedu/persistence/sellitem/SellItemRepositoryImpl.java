package com.pahanaedu.persistence.sellitem;

import com.pahanaedu.business.sellHistory.model.SellHistory;
import com.pahanaedu.business.sellItem.model.SellItem;
import com.pahanaedu.business.sellItem.util.SellItemUtils;
import com.pahanaedu.common.interfaces.Repository;
import com.pahanaedu.config.db.impl.DbConnectionFactoryImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SellItemRepositoryImpl implements Repository<SellItem> {

    private final DbConnectionFactoryImpl dbConnectionFactoryImpl;
    private static final String DATABASE_TYPE = "production";

    public SellItemRepositoryImpl() {
        this.dbConnectionFactoryImpl = new DbConnectionFactoryImpl();
    }

    @Override
    public SellItem findById(Long id) {

        SellItem sellItem = null;
        String query = "select * from sell_item sl join item i on i.id = sl.item_id where sl.id=?";

        try (
                Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE);
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, id.intValue());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                sellItem = SellItemUtils.getSellItemByResultSet(result);
            }
            System.out.println(sellItem);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return sellItem;

    }

    public List<SellItem> findBySellHistoryId(Long sellHistoryId) {
        List<SellItem> sellItems = new ArrayList<>();
        String query = "SELECT * FROM sell_item WHERE sell_history_id = ?";

        try (
                Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE);
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, sellHistoryId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                sellItems.add(SellItemUtils.getSellItemByResultSet(rs));

            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return sellItems;
    }


    @Override
    public List<SellItem> findAll() {
        return List.of();
    }

    @Override
    public SellItem save(SellItem sellitem) {
        return null;
    }


    public void saveBySellHistory(SellHistory sellHistory) {
        String insertQuery = """
        INSERT INTO sell_item(sell_history_id, item_id, sell_price, unit, subtotal)
        VALUES (?, ?, ?, ?, ?)
    """;

        try (Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE)) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
                for (SellItem sellItem : sellHistory.getSellItems()) {
                    statement.setLong(1, sellHistory.getId());
                    statement.setLong(2, sellItem.getItemId());
                    statement.setInt(3, sellItem.getSellPrice());
                    statement.setInt(4, sellItem.getUnit());
                    statement.setInt(5, sellItem.getSubTotal());

                    int affectedRows = statement.executeUpdate();
                    if (affectedRows == 0) {
                        connection.rollback();
                        throw new SQLException("Creating sell item failed, no rows affected.");
                    }
                }
                connection.commit();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error saving sell items for sellHistory id: " + sellHistory.getId(), e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sellItemsQuery = "DELETE FROM sell_item WHERE sell_history_id = ?";

        try (Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE);
             PreparedStatement statement = connection.prepareStatement(sellItemsQuery)) {

            statement.setLong(1, id);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                return false;
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to delete sell item with id: " + id, e);
        }
        return true;
    }
}
