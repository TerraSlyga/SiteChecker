package sumdu.repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 Клас для конфігурації підключення до бази даних.
 */
public class DatabaseConfig {

    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;

    /**
     Завантажує параметри конфігурації бази даних з файлу
     */
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

    /**
     Повертає URL для підключення до бази даних

     @return dbUrl URL для підключення до бази даних
     */
    public static String getDbUrl() {
        return dbUrl;
    }

    /**
     Повертає ім'я користувача для підключення до бази даних

     @return dbUser Ім'я користувача бази даних
     */
    public static String getDbUser() {
        return dbUser;
    }

    /**
     Повертає пароль для підключення до бази даних

     @return dbPassword Пароль користувача бази даних
     */
    public static String getDbPassword() {
        return dbPassword;
    }
}