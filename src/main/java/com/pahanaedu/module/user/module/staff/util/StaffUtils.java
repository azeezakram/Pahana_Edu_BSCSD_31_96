package com.pahanaedu.module.user.module.staff.util;

import com.pahanaedu.module.user.module.staff.model.Staff;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StaffUtils {

    public static Staff getStaffByResultSet(ResultSet result) throws SQLException {
        return new Staff(
                result.getLong("id"),
                result.getString("name"),
                result.getString("role"),
                result.getString("username"),
                result.getString("password"),
                result.getBoolean("is_active"),
                result.getTimestamp("created_at").toLocalDateTime(),
                result.getTimestamp("updated_at").toLocalDateTime()

        );
    }

}
