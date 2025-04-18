package main.java.model;

import java.util.regex.*;

public class IpValidator {
    private static final Pattern IP_REGEX = Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.|$)){4}$"
    );

    public static boolean isValidIp(String ip) {
        return IP_REGEX.matcher(ip).matches();
    }
}