package main.java.model;

import java.util.Objects;

public class HttpInfo {
    private String title;
    private int statusCode;
    private String serverInfo;
    private String responseDateTime;
    private String location;
    private String ip;

    public HttpInfo(String title, int statusCode, String serverInfo, String responseDateTime, String location, String ip) {
        if(title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be blank");
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
        if(!IpValidator.isValidIp(ip)){
            throw new IllegalArgumentException("Invalid IP");
        }
        this.title = title;
        this.statusCode = statusCode;
        this.serverInfo = serverInfo;
        this.responseDateTime = responseDateTime;
        this.location = location;
        this.ip = ip;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(String serverInfo) {
        this.serverInfo = serverInfo;
    }

    public String getResponseDateTime() {
        return responseDateTime;
    }

    public void setResponseDateTime(String responseDateTime) {
        this.responseDateTime = responseDateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "main.java.sitechecker.HttpInfo{" +
                "title='" + title + '\'' +
                ", statusCode=" + statusCode +
                ", serverInfo='" + serverInfo + '\'' +
                ", responseDateTime='" + responseDateTime + '\'' +
                ", location='" + location + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            throw new NullPointerException("Cannot compare null objects");
        }
        if (getClass() != o.getClass()) return false;
        HttpInfo httpInfo = (HttpInfo) o;
        return statusCode == httpInfo.statusCode && Objects.equals(title, httpInfo.title) && Objects.equals(serverInfo, httpInfo.serverInfo) && Objects.equals(responseDateTime, httpInfo.responseDateTime) && Objects.equals(location, httpInfo.location) && Objects.equals(ip, httpInfo.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, statusCode, serverInfo, responseDateTime, location, ip);
    }
}
