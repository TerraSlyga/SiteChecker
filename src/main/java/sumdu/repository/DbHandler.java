package sumdu.repository;

import sumdu.DTO.HttpInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

/**
 Клас для взаємодії з базою даних
 */
public class DbHandler {

    Connection conn;

    /**
     Конструктор класу DbHandler

     @param conn Об'єкт класу Connection для підключення до бази даних
     */
    public DbHandler(Connection conn) {
        if (conn == null) {
            throw new NullPointerException("Connection is null");
        }
        this.conn = conn;
    }

    /**
     Додає інформацію про перевірку сайту до бази даних

     @param userIp IP-адреса користувача, який ініціював перевірку
     @param url URL сайту
     @param statusCode Код статусу HTTP-відповіді
     @param serverInfo Інформація про сервер з HTTP-заголовка
     @param responseDateTime Рядок з часом отримання відповіді
     @param location Локація сервера сайту
     @param ip IP-адреса сервера сайту
     */
    public void addSite(int userIp, String url, int statusCode, String serverInfo, String responseDateTime,
                        String location, String ip) {
        try {
            // 1. Перевірити, чи сайт вже існує за URL та отримати його ID
            int siteId = getSiteIdByUrl(url);

            // 2. Якщо сайт не існує, додати його
            if (siteId == -1) {
                String insertSiteSQL = "INSERT INTO sites (url) VALUES (?)";
                PreparedStatement insertSiteStmt = conn.prepareStatement(insertSiteSQL, PreparedStatement.RETURN_GENERATED_KEYS);
                insertSiteStmt.setString(1, url);
                int affectedRows = insertSiteStmt.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Створення нового сайту не вдалося.");
                }

                try (ResultSet generatedKeys = insertSiteStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        siteId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Не вдалося отримати ID новоствореного сайту.");
                    }
                }
                insertSiteStmt.close();
            }

            // 3. Перетворити responseDateTime у LocalDateTime
            LocalDateTime parsedDateTime = parseDateTime(responseDateTime);

            // 4. Відформатувати LocalDateTime у потрібний формат YYYY-MM-DD HH:mm:ss
            DateTimeFormatter mysqlFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = parsedDateTime.format(mysqlFormatter);

            // 5. Додати запис про перевірку сайту до таблиці 'checks'
            String insertCheckSQL = "INSERT INTO checks (site_id, statusCode, serverInfo, location, checkDateTime, ip) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertCheckStmt = conn.prepareStatement(insertCheckSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            insertCheckStmt.setInt(1, siteId);
            insertCheckStmt.setInt(2, statusCode);
            insertCheckStmt.setString(3, serverInfo);
            insertCheckStmt.setString(4, location);
            insertCheckStmt.setString(5, formattedDateTime);
            insertCheckStmt.setString(6, ip);
            int checkAffectedRows = insertCheckStmt.executeUpdate();

            if (checkAffectedRows == 0) {
                throw new SQLException("Додавання інформації про перевірку сайту не вдалося.");
            }

            int checkId;
            try (ResultSet generatedKeys = insertCheckStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    checkId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Не вдалося отримати ID новоствореної перевірки.");
                }
            }
            insertCheckStmt.close();

            // 6. Оновити last_check_id в таблиці 'sites'
            String updateSiteSQL = "UPDATE sites SET last_check_id = ? WHERE id = ?";
            PreparedStatement updateSiteStmt = conn.prepareStatement(updateSiteSQL);
            updateSiteStmt.setInt(1, checkId);
            updateSiteStmt.setInt(2, siteId);
            updateSiteStmt.executeUpdate();
            updateSiteStmt.close();

            // 7. Додати зв'язок між користувачем та сайтом в таблиці 'user_sites', якщо знайдено користувача
            int userId = getUserIdByIp(userIp);
            if (userId != -1 && !isUserSiteAssociated(userId, siteId)) {
                String insertUserSiteSQL = "INSERT INTO user_sites (userId, siteId) VALUES (?, ?)";
                PreparedStatement insertUserSiteStmt = conn.prepareStatement(insertUserSiteSQL);
                insertUserSiteStmt.setInt(1, userId);
                insertUserSiteStmt.setInt(2, siteId);
                insertUserSiteStmt.executeUpdate();
                insertUserSiteStmt.close();
            } else if (userId == -1) {
                System.err.println("Не знайдено користувача з IP: " + userIp + ". Зв'язок з сайтом не створено.");
            }

        } catch (SQLException e) {
            System.err.println("Помилка при додаванні сайту: " + e.getMessage());
            // Тут можна додати логування помилки або викинути власний exception
        }
    }

    /**
     Додає інформацію про перевірку сайту

     @param userIp IP-адреса користувача
     @param httpInfo Об'єкт класу HttpInfo, що містить інформацію про сайт та його перевірку
     */
    public void addSite(int userIp, HttpInfo httpInfo) {
        String url = httpInfo.getUrl();
        int statusCode = httpInfo.getStatusCode();
        String serverInfo = httpInfo.getServerInfo();
        String responseDateTime = httpInfo.getResponseDateTime();
        String location = httpInfo.getLocation();
        String ip = httpInfo.getIp();

        addSite(userIp, url, statusCode, serverInfo, responseDateTime, location, ip);
    }

    /**
     Отримує ID користувача за його IP-адресою

     @param userIp IP-адреса користувача
     @return ID користувача
     */
    private int getUserIdByIp(int userIp) throws SQLException {
        return 1;
    }

    /**
     Отримує ID сайту за його URL

     @param url URL веб-сайту
     @return ID веб-сайту, або -1, якщо сайт з таким URL не знайдено
     */
    private int getSiteIdByUrl(String url) throws SQLException {
        String selectSiteSQL = "SELECT id FROM sites WHERE url = ?";
        PreparedStatement selectSiteStmt = conn.prepareStatement(selectSiteSQL);
        selectSiteStmt.setString(1, url);
        ResultSet rs = selectSiteStmt.executeQuery();
        int siteId = -1;
        if (rs.next()) {
            siteId = rs.getInt("id");
        }
        rs.close();
        selectSiteStmt.close();
        return siteId;
    }

    /**
     Перевіряє, чи пов'язаний вказаний користувач з вказаним веб-сайтом

     @param userId ID користувача
     @param siteId ID сайту
     @return true, якщо користувач пов'язаний з сайтом, інакше - false.
     */
    private boolean isUserSiteAssociated(int userId, int siteId) throws SQLException {
        String selectUserSiteSQL = "SELECT id FROM user_sites WHERE userId = ? AND siteId = ?";
        PreparedStatement selectUserSiteStmt = conn.prepareStatement(selectUserSiteSQL);
        selectUserSiteStmt.setInt(1, userId);
        selectUserSiteStmt.setInt(2, siteId);
        ResultSet rs = selectUserSiteStmt.executeQuery();
        boolean exists = rs.next();
        rs.close();
        selectUserSiteStmt.close();
        return exists;
    }

    /**
     Розпізнає та перетворює рядок з датою та часом у об'єкт класу LocalDateTime

     @param dateTimeString Рядок з датою та часом
     @return parsedDateTime Об'єкт класу LocalDateTime, отриманий з вхідного рядка,
                            або поточний час, якщо розпізнавання не вдалося
     */
    private LocalDateTime parseDateTime(String dateTimeString) {
        LocalDateTime parsedDateTime = null;
        try {
            DateTimeFormatter rfc1123Formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTimeString, rfc1123Formatter);
            parsedDateTime = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
        } catch (Exception e1) {
            try {
                DateTimeFormatter genericFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yy HH:mm:ss z", Locale.ENGLISH);
                parsedDateTime = ZonedDateTime.parse(dateTimeString, genericFormatter).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
            } catch (Exception e2) {
                try {
                    DateTimeFormatter fullGenericFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM<ctrl3348> HH:mm:ss z", Locale.ENGLISH);
                    parsedDateTime = ZonedDateTime.parse(dateTimeString, fullGenericFormatter).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
                } catch (Exception e3) {
                    System.err.println("Не вдалося розпізнати формат дати: " + dateTimeString + ". Збережено поточний час.");
                    parsedDateTime = LocalDateTime.now();
                }
            }
        }
        return parsedDateTime;
    }

    /**
     Отримує інформацію про сайт за його ID

     @param siteId ID веб-сайту
     @return httpInfo Об'єкт клсу HttpInfo з інформацією про сайт та його останню перевірку,
                        або null, якщо сайт з вказаним ID не знайдено
     */
    public HttpInfo getSite(int siteId) throws SQLException {
        String selectSiteSQL = "SELECT s.url, c.statusCode, c.serverInfo, c.location, c.checkDateTime, c.ip " +
                "FROM sites s " +
                "LEFT JOIN checks c ON s.last_check_id = c.id " +
                "WHERE s.id = ?";
        PreparedStatement selectSiteStmt = conn.prepareStatement(selectSiteSQL);
        selectSiteStmt.setInt(1, siteId);
        ResultSet rs = selectSiteStmt.executeQuery();

        HttpInfo httpInfo = null;
        if (rs.next()) {
            String url = rs.getString("url");
            int statusCode = rs.getInt("statusCode");
            String serverInfo = rs.getString("serverInfo");
            String location = rs.getString("location");
            String checkDateTimeStr = rs.getString("checkDateTime");
            String ip = rs.getString("ip");

            httpInfo = new HttpInfo(url, statusCode, serverInfo, location, checkDateTimeStr, ip);
        }
        rs.close();
        selectSiteStmt.close();
        return httpInfo;
    }


    /**
     Отримує інформацію про сайт за його URL.

     @param url URL веб-сайту
     @return httpInfo Об'єкт клсу HttpInfo з інформацією про сайт та його останню перевірку,
                        або null, якщо сайт з вказаним URL не знайдено
     */
    public HttpInfo getSite(String url) throws SQLException {
        int siteId = getSiteIdByUrl(url);
        return getSite(siteId);
    }


    /**
     Отримує інформацію про всі сайти пов'язані з вказаним ID користувача

     @param userId ID користувача
     @return Список об'єктів HttpInfo з інформацією про сайти, пов'язані з користувачем
     */
    public ArrayList<HttpInfo> getAllSites(int userId) throws SQLException {
        ArrayList<HttpInfo> sitesInfo = new ArrayList<>();
        String selectSitesSQL = "SELECT s.id AS siteId, s.url, c.statusCode, c.serverInfo, c.location, c.checkDateTime, c.ip " +
                "FROM user_sites us " +
                "JOIN sites s ON us.siteId = s.id " +
                "LEFT JOIN checks c ON s.last_check_id = c.id " +
                "WHERE us.userId = ?";

        try (PreparedStatement selectSitesStmt = conn.prepareStatement(selectSitesSQL)) {
            selectSitesStmt.setInt(1, userId);
            ResultSet rs = selectSitesStmt.executeQuery();

            while (rs.next()) {
                int siteId = rs.getInt("siteId");
                String url = rs.getString("url");
                int statusCode = rs.getInt("statusCode");
                String serverInfo = rs.getString("serverInfo");
                String location = rs.getString("location");
                String checkDateTimeStr = rs.getString("checkDateTime");
                String ip = rs.getString("ip");

                HttpInfo httpInfo = new HttpInfo(url, statusCode, serverInfo, checkDateTimeStr, location, ip);

                sitesInfo.add(httpInfo);
            }
        } catch (SQLException e) {
            System.err.println("Помилка при отриманні сайтів користувача з ID " + userId + ": " + e.getMessage());
            // Можна обробити помилку інакше, наприклад, повернути порожний список або викинути власний exception
        }
        return sitesInfo;
    }
}