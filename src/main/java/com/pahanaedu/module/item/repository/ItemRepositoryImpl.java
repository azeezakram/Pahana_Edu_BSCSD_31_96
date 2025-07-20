package com.pahanaedu.module.item.repository;

import com.pahanaedu.common.interfaces.IRepositoryPrototype;
import com.pahanaedu.config.DbConnectionFactory;
import com.pahanaedu.module.item.model.Item;
import com.pahanaedu.module.item.util.ItemUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemRepositoryImpl implements IRepositoryPrototype<Item> {

    @Override
    public Item findById(Long id) {
        Item item = null;
        String query = "select * from item i join category c on c.id = i.category_id where i.id = ?";

        try (
                Connection connection = DbConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                item = ItemUtils.getItemByResultSet(result);
            }
            System.out.println(item);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return item;
    }

    @Override
    public List<Item> findAll() {
        List<Item> items = new ArrayList<>();
        String query = "select * from item i join category c on c.id = i.category_id";

        try (
                Connection connection = DbConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                items.add(ItemUtils.getItemByResultSet(result));
            }
            System.out.println(items);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return items;
    }

    @Override
    public Item save(Item obj) {
        return null;
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
