package com.pahanaedu.persistence.item;

import com.pahanaedu.business.item.model.Item;
import com.pahanaedu.business.item.util.ItemUtils;
import com.pahanaedu.common.interfaces.Repository;
import com.pahanaedu.common.interfaces.UpdatableRepository;
import com.pahanaedu.config.db.impl.DbConnectionFactoryImpl;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ItemRepositoryImpl implements Repository<Item>, UpdatableRepository<Item> {

    private final DbConnectionFactoryImpl dbConnectionFactoryImpl;
    private static final String DATABASE_TYPE = "production";

    public ItemRepositoryImpl() {
        this.dbConnectionFactoryImpl = new DbConnectionFactoryImpl();
    }

    @Override
    public Item findById(Long id) {
        Item item = null;
        String query = "select * from item i join category c on c.id = i.category_id where i.id = ?";

        try (
                Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE);
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
                Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE);
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
                Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE)
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
                    throw new SQLException("Creating items failed, no rows affected.");
                }

            }

            item.setId(generatedId);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return item;
    }

    @Override
    public Item update(Item item) {
        int result;

        String query = """
                    update item
                    set item_name = ?, description = ?, brand = ?, stock = ?, category_id = ?, price = ?, updated_at = ?
                    where id = ?
                """;

        System.out.println(item);
        try (
                Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE);
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, item.getItemName());
            statement.setString(2, item.getDescription());
            statement.setString(3, item.getBrand());
            statement.setInt(4, item.getStock());
            statement.setInt(5, item.getCategory().getId());
            statement.setInt(6, item.getPrice());
            statement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            statement.setLong(8, item.getId());
            result = statement.executeUpdate();

            if (result < 0) {
                connection.rollback();
                throw new SQLException("Creating item failed, no rows affected.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return item;
    }

    @Override
    public boolean delete(Long id) {
        boolean isDeleted = false;
        String query = "delete from item where id = ?";

        try (
                Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE);
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, id);
            int result = statement.executeUpdate();
            if (result > 0) {
                isDeleted = true;
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return isDeleted;
    }

    public boolean updateStock(Long itemId, Integer updatedStock) {
        String query = """
                    UPDATE item
                    SET stock = ?, updated_at = ?
                    WHERE id = ?
                """;

        try (
                Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE);
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, updatedStock);
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            statement.setLong(3, itemId);

            int result = statement.executeUpdate();
            return result > 0;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to update stock for itemId: " + itemId, e);
        }
    }

}
