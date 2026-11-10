package com.handbook.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public final class JdbcH2Demo {

    public static void main(String[] args) throws Exception {
        String url = "jdbc:h2:mem:handbook;DB_CLOSE_DELAY=-1";
        try (Connection conn = DriverManager.getConnection(url, "sa", "");
                Statement ddl = conn.createStatement()) {
            ddl.execute("CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY, name VARCHAR(64))");
            try (PreparedStatement insert = conn.prepareStatement("MERGE INTO users KEY(id) VALUES (?, ?)")) {
                insert.setInt(1, 1);
                insert.setString(2, "Ada");
                insert.executeUpdate();
            }
            try (PreparedStatement select = conn.prepareStatement("SELECT name FROM users WHERE id = ?")) {
                select.setInt(1, 1);
                try (ResultSet rs = select.executeQuery()) {
                    rs.next();
                    System.out.println("name=" + rs.getString(1));
                }
            }
            try (PreparedStatement count = conn.prepareStatement("SELECT COUNT(*) FROM users")) {
                try (ResultSet rs = count.executeQuery()) {
                    rs.next();
                    System.out.println("rowCount=" + rs.getInt(1));
                }
            }
        }
        System.out.println("jdbcH2=ok");
    }
}
