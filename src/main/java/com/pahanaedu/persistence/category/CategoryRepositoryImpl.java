package com.pahanaedu.persistence.category;

import com.pahanaedu.business.category.model.Category;
import com.pahanaedu.business.category.util.CategoryUtils;
import com.pahanaedu.config.db.factory.DbConnectionFactory;
import com.pahanaedu.config.db.impl.DbConnectionFactoryImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepository {

    private final DbConnectionFactory dbConnectionFactory;
    private static final String DATABASE_TYPE = "production";

    public CategoryRepositoryImpl() {
        this.dbConnectionFactory = new DbConnectionFactoryImpl();
    }

    @Override
    public Category findById(Long id) {
        Category category = null;
        String query = "select * from category where id = ?";

        try (
                Connection connection = dbConnectionFactory.getConnection(DATABASE_TYPE);
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                category = CategoryUtils.getCategoryByResultSet(result);
            }
            System.out.println(category);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return category;
    }

    @Override
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        String query = "select * from category";

        try (
                Connection connection = dbConnectionFactory.getConnection(DATABASE_TYPE);
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                categories.add(CategoryUtils.getCategoryByResultSet(result));
            }
            System.out.println(categories);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return categories;
    }

    @Override
    public Category save(Category category) {
        int result;
        int generatedId = 0;

        String newUserSQL = """
                    insert into category(category_name)
                    values (?)
                """;

        System.out.println(category);
        try (
                Connection connection = dbConnectionFactory.getConnection(DATABASE_TYPE);
        ) {

            try (PreparedStatement statement = connection.prepareStatement(newUserSQL, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, category.getCategoryName());
                result = statement.executeUpdate();

                if (result > 0) {
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            generatedId = generatedKeys.getInt("id");
                        }
                    }
                } else {
                    connection.rollback();
                    throw new SQLException("Creating category failed, no rows affected.");
                }

            }

            category.setId(generatedId);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return category;
    }

    @Override
    public Category update(Category category) {
        int result;

        String query = """
                    update category
                    set category_name = ?
                    where id = ?
                """;

        System.out.println(category);
        try (
                Connection connection = dbConnectionFactory.getConnection(DATABASE_TYPE);
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, category.getCategoryName());
            result = statement.executeUpdate();

            if (result < 0) {
                connection.rollback();
                throw new SQLException("Creating item failed, no rows affected.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return category;
    }

    @Override
    public boolean delete(Long id) {
        boolean isDeleted = false;
        String query = "delete from category where id = ?";

        try (
                Connection connection = dbConnectionFactory.getConnection(DATABASE_TYPE);
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

}
