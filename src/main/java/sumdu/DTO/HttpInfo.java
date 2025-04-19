package sumdu.DTO;

import sumdu.utility.IpValidator;

import java.util.Objects;

/**
 Клас, що представляє інформацію про HTTP-відповідь сайту
 */
public class HttpInfo {
    private String url;
    private int statusCode;
    private String serverInfo;
    private String responseDateTime;
    private String location;
    private String ip;

    /**
     Конструктор класу HttpInfo

     @param url URL сайту
     @param statusCode Код статусу HTTP-відповіді
     @param serverInfo Інформація про сервер з HTTP-заголовка
     @param responseDateTime Рядок з часом отримання відповіді
     @param location Локація сервера сайту
     @param ip IP-адреса сервера сайту
     */
    public HttpInfo(String url, int statusCode, String serverInfo, String responseDateTime,
                    String location, String ip) {
        if(url.isBlank()) {
            throw new IllegalArgumentException("URL cannot be blank");
        }
        if(statusCode < 100 || statusCode >= 600) {
            throw new IllegalArgumentException("Status code must be between 100 and 600");
        }
        if(serverInfo.isBlank()) {
            throw new IllegalArgumentException("Server info cannot be blank");
        }
        if(responseDateTime.isBlank()) {
            throw new IllegalArgumentException("Response datetime cannot be blank");
        }
        if(location.isBlank()) {
            throw new IllegalArgumentException("Location cannot be blank");
        }
        if(ip.isBlank()) {
            throw new IllegalArgumentException("IP cannot be blank");
        }
        if(!IpValidator.isValidIp(ip)) {
            throw new IllegalArgumentException("Invalid IP");
        }
        this.url = url;
        this.statusCode = statusCode;
        this.serverInfo = serverInfo;
        this.responseDateTime = responseDateTime;
        this.location = location;
        this.ip = ip;
    }

    /**
     Повертає URL веб-сайту

     @return URL веб-сайту
     */
    public String getUrl() {
        return url;
    }

    /**
     Встановлює URL веб-сайту

     @param url URL веб-сайту
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     Повертає код статусу HTTP-відповіді

     @return Код статусу HTTP-відповіді
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     Встановлює код статусу HTTP-відповіді

     @param statusCode Код статусу HTTP-відповіді
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     Повертає інформацію про сервер з HTTP-заголовка

     @return Інформація про сервер
     */
    public String getServerInfo() {
        return serverInfo;
    }

    /**
     Встановлює інформацію про сервер з HTTP-заголовка

     @param serverInfo Інформація про сервер
     */
    public void setServerInfo(String serverInfo) {
        this.serverInfo = serverInfo;
    }

    /**
     Повертає час отримання HTTP-відповіді

     @return Рядок з часом відповіді
     */
    public String getResponseDateTime() {
        return responseDateTime;
    }

    /**
     Встановлює час отримання HTTP-відповіді

     @param responseDateTime Рядок з часом відповіді
     */
    public void setResponseDateTime(String responseDateTime) {
        this.responseDateTime = responseDateTime;
    }

    /**
     Повертає локацію сервера сайту

     @return Локація сервера сайту
     */
    public String getLocation() {
        return location;
    }

    /**
     Встановлює локацію сервера сайту

     @param location Локація сервера сайту
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     Повертає IP-адресу сервера сайту

     @return IP-адреса
     */
    public String getIp() {
        return ip;
    }

    /**
     Встановлює IP-адресу сервера сайту

     @param ip IP-адреса
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     Повертає рядкове представлення об'єкта HttpInfo

     @return Рядок з усіма полями об'єкта
     */
    @Override
    public String toString() {
        return "HttpInfo{" +
                "url='" + url + '\'' +
                ", statusCode=" + statusCode +
                ", serverInfo='" + serverInfo + '\'' +
                ", responseDateTime='" + responseDateTime + '\'' +
                ", location='" + location + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }

    /**
     Порівнює даний об'єкт HttpInfo з іншим об'єктом на рівність

     @param o Об'єкт для порівняння
     @return true, якщо об'єкти є рівними за всіма полями, інакше - fals
     */
    @Override
    public boolean equals(Object o) {
        if (o == null){
            throw new NullPointerException("o == null");
        }
        if (getClass() != o.getClass()) return false;
        HttpInfo httpInfo = (HttpInfo) o;
        return statusCode == httpInfo.statusCode && Objects.equals(url, httpInfo.url)
                && Objects.equals(serverInfo, httpInfo.serverInfo)
                && Objects.equals(responseDateTime, httpInfo.responseDateTime)
                && Objects.equals(location, httpInfo.location)
                && Objects.equals(ip, httpInfo.ip);
    }

    /**
     Повертає хеш-код для об'єкта HttpInfo

     @return Хеш-код об'єкта
     */
    @Override
    public int hashCode() {
        return Objects.hash(url, statusCode, serverInfo, responseDateTime, location, ip);
    }
}