package main.java.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseController {

    private static Connection connection;

    // Метод для отримання підключення до бази даних
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            // Завантажуємо конфігурацію з файлу
            DatabaseConfig.loadDatabaseConfig();

            // Отримуємо параметри з налаштувань
            String dbUrl = DatabaseConfig.getDbUrl();
            String dbUser = DatabaseConfig.getDbUser();
            String dbPassword = DatabaseConfig.getDbPassword();

            try {
                connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            } catch (SQLException e) {
                throw new SQLException("Не вдалося підключитися до бази даних.", e);
            }
        }
        return connection;
    }

    // Метод для закриття підключення
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Для перевірки підключення
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("Підключення до бази даних успішне!");
            }
        } catch (SQLException e) {
            System.out.println("Помилка підключення: " + e.getMessage());
        }
    }

    // Вкладений клас для зчитування конфігурації з файлу
    public static class DatabaseConfig {

        private static String dbUrl;
        private static String dbUser;
        private static String dbPassword;

        // Метод для зчитування параметрів з файлу
        public static void loadDatabaseConfig() {
            Properties properties = new Properties();

            try (FileInputStream fis = new FileInputStream("dbconfig.properties")) {
                // Завантажуємо налаштування з файлу
                properties.load(fis);

                // Читаємо параметри
                dbUrl = properties.getProperty("db.url");
                dbUser = properties.getProperty("db.user");
                dbPassword = properties.getProperty("db.password");

            } catch (IOException e) {
                System.err.println("Помилка при зчитуванні файлу конфігурації: " + e.getMessage());
            }
        }

        // Геттери для параметрів
        public static String getDbUrl() {
            return dbUrl;
        }

        public static String getDbUser() {
            return dbUser;
        }

        public static String getDbPassword() {
            return dbPassword;
        }
    }
}
