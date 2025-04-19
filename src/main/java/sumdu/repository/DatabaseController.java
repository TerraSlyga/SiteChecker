package sumdu.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 Клас, що відповідає за керування підключенням до бази даних
 */
public class DatabaseController {

    private static Connection connection;

    /**
     Повертає існуюче підключення, якщо воно вже встановлено
     та не закрите. В іншому випадку, він завантажує конфігурацію бази даних
     за допомогою методу loadDatabaseConfig() класу DatabaseConfig

     @return connection Об'єкт підключення до бази даних
     */
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

    /**
     Закриває поточне підключення до бази даних, якщо воно існує та не закрите
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     Тестує підключення до бази даних
     */
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("Підключення до бази даних успішне!");
            }
        } catch (SQLException e) {
            System.out.println("Помилка підключення: " + e.getMessage());
        }
    }
}