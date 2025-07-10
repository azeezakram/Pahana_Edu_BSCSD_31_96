package com.pahanaedu.module.user.module.staff.util;

import com.pahanaedu.module.user.module.staff.model.Staff;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not found.");
        }
    }

}
