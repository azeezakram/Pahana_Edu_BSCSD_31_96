package com.pahanaedu.persistence.sellitem;

import com.pahanaedu.business.sellItem.model.SellItem;
import com.pahanaedu.business.sellItem.util.SellItemUtils;
import com.pahanaedu.common.interfaces.Repository;
import com.pahanaedu.config.db.impl.DbConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SellItemRepository implements Repository<SellItem> {

    private final DbConnectionFactory dbConnectionFactory;
    private static final String DATABASE_TYPE = "production";

    public SellItemRepository() {
        this.dbConnectionFactory = new DbConnectionFactory();
    }

    @Override
    public SellItem findById(Long id) {

        SellItem sellItem = null;
        String query = "select * from sell_item sl join item i on i.id = sl.item_id where sl.id=?";

        try (
                Connection connection = dbConnectionFactory.getConnection(DATABASE_TYPE);
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

    @Override
    public List<SellItem> findAll() {
        return List.of();
    }

    @Override
    public SellItem save(SellItem obj) {
        return null;
    }

    @Override
    public SellItem update(SellItem obj) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
