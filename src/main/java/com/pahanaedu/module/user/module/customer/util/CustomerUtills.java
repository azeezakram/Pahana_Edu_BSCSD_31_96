package com.pahanaedu.module.user.module.customer.util;

import com.pahanaedu.module.user.module.customer.model.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerUtills {

    public static Customer getCustomerByResultSet(ResultSet result) throws SQLException {
        return new Customer(
                result.getLong("id"),
                result.getString("name"),
                result.getString("role"),
                result.getTimestamp("created_at").toLocalDateTime(),
                result.getTimestamp("updated_at").toLocalDateTime(),
                result.getString("account_number"),
                result.getString("address"),
                result.getString("phone_number")
        );
    }

}
