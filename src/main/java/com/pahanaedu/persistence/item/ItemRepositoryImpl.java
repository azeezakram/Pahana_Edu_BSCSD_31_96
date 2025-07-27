package com.pahanaedu.persistence.item;

import com.pahanaedu.common.interfaces.Repository;
import com.pahanaedu.business.item.model.Item;
import com.pahanaedu.business.item.util.ItemUtils;
import com.pahanaedu.config.db.DbConnectionFactoryImpl;
import com.pahanaedu.config.db.factory.DbConnectionFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ItemRepositoryImpl implements Repository<Item> {

    private final DbConnectionFactory dbConnectionFactory;

    public ItemRepositoryImpl() {
        this.dbConnectionFactory = new DbConnectionFactoryImpl();
    }

    @Override
    public Item findById(Long id) {
        Item item = null;
        String query = "select * from item i join category c on c.id = i.category_id where i.id = ?";

        try (
                Connection connection = dbConnectionFactory.getConnection("production");
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                item = ItemUtils.getItemByResultSet(result);
            }
            System.out.println(item);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return item;
    }

    @Override
    public List<Item> findAll() {
        List<Item> items = new ArrayList<>();
        String query = "select * from item i join category c on c.id = i.category_id";

        try (
                Connection connection = dbConnectionFactory.getConnection("production");
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                items.add(ItemUtils.getItemByResultSet(result));
            }
            System.out.println(items);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return items;
    }

    @Override
    public Item save(Item item) {
        int result;
        long generatedId = 0L;

        String newUserSQL = """
                    insert into item(item_name, description, brand, stock, price, category_id, created_at, updated_at)
                    values (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        System.out.println(item);
        try (
                Connection connection = dbConnectionFactory.getConnection("production");
        ) {

            try (PreparedStatement statement = connection.prepareStatement(newUserSQL, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, item.getItemName());
                statement.setString(2, item.getDescription());
                statement.setString(3, item.getBrand());
                statement.setInt(4, item.getStock());
                statement.setInt(5, item.getPrice());
                statement.setInt(6, item.getCategory().getId());
                statement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
                statement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
                result = statement.executeUpdate();

                if (result > 0) {
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            generatedId = generatedKeys.getLong("id");
                        }
                    }
                } else {
                    connection.rollback();
                    throw new SQLException("Creating item failed, no rows affected.");
                }

            }

            item.setId(generatedId);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return item;
    }

    @Override
    public Item update(Item obj) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

}
