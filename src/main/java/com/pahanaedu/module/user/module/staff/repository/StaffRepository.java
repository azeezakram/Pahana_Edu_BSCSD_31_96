package com.pahanaedu.module.user.module.staff.repository;
import com.pahanaedu.common.interfaces.IRepositoryPrototype;
import com.pahanaedu.config.DbConfig;
import com.pahanaedu.module.user.model.User;
import com.pahanaedu.module.user.module.staff.dto.StaffWithoutPasswordDTO;
import com.pahanaedu.module.user.module.staff.model.Staff;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

        Staff checkingStaff = findById(staff.getId());

        if (checkingStaff != null) {
            update(checkingStaff);
        } else {
            String query = "insert into ";
        }

        return null;
    }

    private void update(Staff staff) {

        String updateUserSQL = """
                    UPDATE "user"
                    SET name = ?, role = ?, updated_at = ?
                    WHERE id = ?
                """;

        String updateStaffSQL = """
                    UPDATE staff
                    SET username = ?, is_active = ?
                    WHERE id = ?
                """;

        try (
                Connection connection = db.getConnection();
        ) {
            connection.setAutoCommit(false);

            try (PreparedStatement updateUserStmt = connection.prepareStatement(updateUserSQL)) {
                updateUserStmt.setString(1, staff.getName());
                updateUserStmt.setString(2, staff.getRole());
                updateUserStmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                updateUserStmt.setInt(4, staff.getId().intValue());
                updateUserStmt.executeUpdate();
            }

            try (PreparedStatement updateStaffStmt = connection.prepareStatement(updateStaffSQL)) {
                updateStaffStmt.setString(1, staff.getUsername());
                updateStaffStmt.setBoolean(2, staff.getIsActive());
                updateStaffStmt.setInt(3, staff.getId().intValue());
                updateStaffStmt.executeUpdate();
            }

            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
