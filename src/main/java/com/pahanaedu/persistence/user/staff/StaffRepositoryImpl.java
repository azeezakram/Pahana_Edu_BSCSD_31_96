package com.pahanaedu.persistence.user.staff;

import com.pahanaedu.business.user.enums.Role;
import com.pahanaedu.business.user.module.staff.model.Staff;
import com.pahanaedu.business.user.module.staff.util.StaffUtils;
import com.pahanaedu.config.db.factory.DbConnectionFactory;
import com.pahanaedu.config.db.impl.DbConnectionFactoryImpl;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.pahanaedu.business.user.module.staff.util.StaffUtils.getStaffByResultSet;

public class StaffRepositoryImpl implements StaffRepository {

    private final DbConnectionFactory dbConnectionFactoryImpl;
    private static final String DATABASE_TYPE = "production";

    public StaffRepositoryImpl () {
        this.dbConnectionFactoryImpl = new DbConnectionFactoryImpl();
    }

    @Override
    public Staff findById(Long id) {

        Staff staff = null;
        String query = "select * from staff s join users u on s.id = u.id where s.id=? and u.role=?";

        try (
                Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE);
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, id.intValue());
            statement.setString(2, Role.STAFF.toString());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                staff = getStaffByResultSet(result);
            }
            System.out.println(staff);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return staff;
    }

    @Override
    public List<Staff> findAll() {
        List<Staff> staffs = new ArrayList<>();
        String query = "select * from staff s join users u on u.id = s.id where u.role=?";

        try (
                Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE);
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, Role.STAFF.toString());
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                staffs.add(getStaffByResultSet(result));
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return staffs;
    }

    @Override
    public Staff save(Staff staff) {
        
        int result;
        long generatedId = 0L;

        String newUserSQL = """
                    insert into users(name, role, created_at, updated_at)
                    values (?, ?, ?, ?)
                """;

        String newStaffSQL = """
                    insert into staff(id, username, password, is_active)
                    values (?, ?, ?, ?)
                """;
        System.out.println(staff);
        try (
                Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE)
        ) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(newUserSQL, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, staff.getName());
                statement.setString(2, Role.STAFF.toString());
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
                    throw new SQLException("Creating user failed, no rows affected.");
                }

            }


            try (PreparedStatement statement = connection.prepareStatement(newStaffSQL)) {
                statement.setLong(1, generatedId);
                statement.setString(2, staff.getUsername());
                statement.setString(3, StaffUtils.hashPassword(staff.getPassword()));
                statement.setBoolean(4, true);
                statement.executeUpdate();
            }

            connection.commit();

            staff.setId(generatedId);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return staff;

    }

    @Override
    public Staff update(Staff staff) {

        int result;

        String updateUserSQL = """
                    UPDATE users
                    SET name = ?, updated_at = ?
                    WHERE id = ? and role = ?
                """;

        String updateStaffSQL = """
                    UPDATE staff
                    SET username = ?, password = ?, is_active = ?
                    WHERE id = ?
                """;

        try (
                Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE)
        ) {
            connection.setAutoCommit(false);

            try (PreparedStatement updateUserStmt = connection.prepareStatement(updateUserSQL)) {
                updateUserStmt.setString(1, staff.getName());
                updateUserStmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                updateUserStmt.setLong(3, staff.getId());
                updateUserStmt.setString(4, Role.STAFF.toString());
                result = updateUserStmt.executeUpdate();
            }

            if (result > 0) {
                try (PreparedStatement updateStaffStmt = connection.prepareStatement(updateStaffSQL)) {
                    updateStaffStmt.setString(1, staff.getUsername());
                    updateStaffStmt.setString(2, StaffUtils.hashPassword(staff.getPassword()));
                    updateStaffStmt.setBoolean(3, staff.getIsActive());
                    updateStaffStmt.setLong(4, staff.getId());
                    updateStaffStmt.executeUpdate();
                }
            }
            connection.commit();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return staff;

    }

    @Override
    public boolean delete(Long id) {
        boolean isDeleted;
        String query = "DELETE FROM users WHERE id = ? and role=?";

        try (
                Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE);
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, id);
            statement.setString(2, Role.STAFF.toString());
            int rowsAffected = statement.executeUpdate();
            isDeleted = rowsAffected > 0;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return isDeleted;
    }

    public Staff findByUsername(String username) {

        Staff staff = null;
        String query = "select * from staff s join users u on s.id = u.id where s.username=? and u.role=?";

        try (
                Connection connection = dbConnectionFactoryImpl.getConnection(DATABASE_TYPE);
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, username);
            statement.setString(2, Role.STAFF.toString());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                staff = getStaffByResultSet(result);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return staff;

    }
}
