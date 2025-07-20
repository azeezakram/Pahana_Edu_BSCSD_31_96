package com.pahanaedu.module.user.module.customer.repository;

import com.pahanaedu.common.interfaces.IRepositoryPrototype;
import com.pahanaedu.config.DbConnectionFactory;
import com.pahanaedu.module.user.enums.Role;
import com.pahanaedu.module.user.model.User;
import com.pahanaedu.module.user.module.customer.model.Customer;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.pahanaedu.module.user.module.customer.util.CustomerUtils.getCustomerByResultSet;

public class CustomerRepositoryImpl implements IRepositoryPrototype<Customer> {

    @Override
    public Customer findById(Long id) {
        Customer customer = null;
        String query = "select * from customer c join users u on c.id = u.id where c.id=?";

        try (
                Connection connection = DbConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, id.intValue());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                customer = getCustomerByResultSet(result);
            }
            System.out.println(customer);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return customer;
    }

    @Override
    public List<Customer> findAll() {

        List<Customer> customers = new ArrayList<>();
        String query = "select * from customer c join users u on u.id = c.id";

        try (
                Connection connection = DbConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                customers.add(getCustomerByResultSet(result));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return customers;
    }

    @Override
    public Customer save(Customer customer) {

        int result;
        long generatedId = 0L;

        String newUserSQL = """
                    insert into users(name, role, created_at, updated_at)
                    values (?, ?, ?, ?)
                """;

        String newStaffSQL = """
                    insert into customer(id, account_number, address, phone_number)
                    values (?, ?, ?, ?)
                """;
        System.out.println(customer);
        try (
                Connection connection = DbConnectionFactory.getConnection()
        ) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(newUserSQL, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, customer.getName());
                statement.setString(2, Role.CUSTOMER.name());
                statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                result = statement.executeUpdate();

                if (result > 0) {
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            generatedId = generatedKeys.getLong("id");
                        }
                    }
                } else {
                    connection.rollback();
                    throw new SQLException("Creating customer failed, no rows affected.");
                }

            }
            if (customer.getAccountNumber() == null || customer.getAccountNumber().isBlank()) {
                customer.setAccountNumber("ph-edu-c-".concat(String.valueOf(generatedId)));
                System.out.println(customer.getAccountNumber());
            }

            try (PreparedStatement statement = connection.prepareStatement(newStaffSQL)) {
                statement.setLong(1, generatedId);
                statement.setString(2, customer.getAccountNumber());
                statement.setString(3, customer.getAddress());
                statement.setString(4, customer.getPhoneNumber());
                statement.executeUpdate();
            }

            connection.commit();
            customer.setId(generatedId);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return customer;
    }

    @Override
    public Customer update(Customer customer) {
        int result;

        String updateUserSql = """
                    update users
                    set name = ?, role = ?, updated_at = ? 
                    where id = ?
                """;

        String updateCustomerSql = """
                    update customer
                    set account_number = ?, address = ?, phone_number = ?
                    where id = ?
                """;
        System.out.println(customer);
        try (
                Connection connection = DbConnectionFactory.getConnection()
        ) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(updateUserSql)) {
                statement.setString(1, customer.getName());
                statement.setString(2, Role.CUSTOMER.name());
                statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                statement.setLong(4, customer.getId());
                result = statement.executeUpdate();

                if (result < 0) {
                    connection.rollback();
                    throw new SQLException("Creating customer failed, no rows affected.");
                }

            }

            try (PreparedStatement statement = connection.prepareStatement(updateCustomerSql)) {
                statement.setString(1, customer.getAccountNumber());
                statement.setString(2, customer.getAddress());
                statement.setString(3, customer.getPhoneNumber());
                statement.setLong(4, customer.getId());
                statement.executeUpdate();
            }

            connection.commit();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return customer;
    }

    @Override
    public boolean delete(Long id) {

        boolean isDeleted = false;
        String query = "delete from users where id = ?";

        try (
                Connection connection = DbConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, id);
            int result = statement.executeUpdate();
            if (result > 0) {
                isDeleted = true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return isDeleted;

    }

    public Customer findByAccountNumber(String accountNumber) {

        Customer customer = null;
        String query = "select * from customer c join users u on c.id = u.id where c.account_number = ?";

        try (
                Connection connection = DbConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, accountNumber);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                customer = getCustomerByResultSet(result);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return customer;

    }

    public boolean deleteByAccountNumber(String accountNumber) {

        boolean isDeleted = false;
        String query = "delete from users u using customer c where u.id = c.id and c.account_number = ?";

        try (
                Connection connection = DbConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, accountNumber);
            int result = statement.executeUpdate();
            if (result > 0) {
                isDeleted = true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return isDeleted;

    }
}
