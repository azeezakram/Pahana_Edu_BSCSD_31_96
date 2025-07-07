package com.pahanaedu.module.user.module.staff.repository;

import com.pahanaedu.common.interfaces.IRepositoryPrototype;
import com.pahanaedu.config.DbConfig;
import com.pahanaedu.module.user.model.User;
import com.pahanaedu.module.user.module.staff.model.Staff;
import com.pahanaedu.module.user.module.staff.util.StaffUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.pahanaedu.module.user.module.staff.util.StaffUtils.getStaffByResultSet;

public class StaffRepository implements IRepositoryPrototype<User, Staff> {

    private final DbConfig db;

    public StaffRepository() {
        this.db = new DbConfig();
    }

    @Override
    public Staff findById(Long id) {

        Staff staff = null;
        String query = "select * from staff s join users u on s.id = u.id where s.id=?";

        try (
                Connection connection = db.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, id.intValue());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                staff = getStaffByResultSet(result);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return staff;

    }


    @Override
    public List<Staff> findAll() {
        List<Staff> staffs = new ArrayList<>();
        String query = "select * from staff s join users u on s.id = u.id";

        try (
                Connection connection = db.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                staffs.add(getStaffByResultSet(result));
            }
            staffs.forEach(System.out::println);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return staffs;
    }

    @Override
    public Staff save(User staff) {

        String userQuery = "insert into users(name, role, created_at, updated_at) values(?, ?, ?, ?)";
        Staff newStaff = null;
        try (
                Connection connection = db.getConnection();
                PreparedStatement statement = connection.prepareStatement(userQuery)
        ) {
            statement.setString(1, staff.getName());
            statement.setString(2, staff.getRole());
            statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                newStaff = StaffUtils.getStaffByResultSet(result);
            }
            System.out.println();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return newStaff;

    }




    @Override
    public boolean delete(int id) {
        return true;
    }

    public Staff findByUsername(String username) {

        Staff staff = null;
        String query = "select * from staff s join users u on s.id = u.id where s.username=?";

        try (
                Connection connection = db.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                staff = getStaffByResultSet(result);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return staff;

    }
}
