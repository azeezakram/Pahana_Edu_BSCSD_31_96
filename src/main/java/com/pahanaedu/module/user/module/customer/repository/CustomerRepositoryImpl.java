package com.pahanaedu.module.user.module.customer.repository;

import com.pahanaedu.common.interfaces.IRepositoryPrototype;
import com.pahanaedu.config.DbConfig;
import com.pahanaedu.module.user.model.User;
import com.pahanaedu.module.user.module.customer.model.Customer;
import com.pahanaedu.module.user.module.customer.util.CustomerUtills;
import com.pahanaedu.module.user.module.staff.model.Staff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.pahanaedu.module.user.module.staff.util.StaffUtils.getStaffByResultSet;

public class CustomerRepositoryImpl implements IRepositoryPrototype<User, Customer> {

    private final DbConfig db;

    public CustomerRepositoryImpl() {
        this.db = new DbConfig();
    }

    @Override
    public Customer findById(Long id) {
        Customer customer = null;
        String query = "select * from customer c join users u on c.id = u.id where c.id=?";

        try (
                Connection connection = db.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, id.intValue());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                customer = CustomerUtills.getCustomerByResultSet(result);
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
                Connection connection = db.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                customers.add(CustomerUtills.getCustomerByResultSet(result));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return customers;
    }

    @Override
    public Customer save(Customer obj) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
