package sumdu.servise;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.time.LocalDateTime;
import javax.net.ssl.HttpsURLConnection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sumdu.DTO.HttpInfo;

/**
 Клас для отримання інформації про сайт за допомогою HTTP-запиту
 */
public class HttpInfoFetcher {

    /**
     Виконує HTTP запит GET до вказаного URL та повертає інформацію про нього

     @param urlString URL сайту для отримання інформації
     @return Об'єкт HttpInfo, що містить отриману інформацію про сайт
     */
    public static HttpInfo fetchInfo(String urlString) {
        try {
            String processedUrl = urlString;

            if (!urlString.startsWith("http://") && !urlString.startsWith("https://")) {
                processedUrl = "https://" + urlString;
            }

            URL url = new URL(processedUrl);
            InetAddress ipAddress = InetAddress.getByName(url.getHost());

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int statusCode = connection.getResponseCode();
            String serverInfo = connection.getHeaderField("Server");
            String date = connection.getHeaderField("Date");
            String location = connection.getHeaderField("Location");
            String responseDateTime = (date != null) ? date : LocalDateTime.now().toString();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder htmlContent = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                htmlContent.append(inputLine);
            }
            in.close();

            Document doc = Jsoup.parse(htmlContent.toString());
            String title = doc.title();

            return new HttpInfo(
                    urlString,
                    statusCode,
                    serverInfo != null ? serverInfo : "Немає даних",
                    responseDateTime,
                    location != null ? location : "Не вказано",
                    ipAddress.getHostAddress()
            );

        } catch (Exception e) {
            System.out.println("Сталася помилка: " + e.getMessage());
            return new HttpInfo(urlString, -1, "Помилка", LocalDateTime.now().toString(), "Немає", "Невідомо");
        }
    }
}