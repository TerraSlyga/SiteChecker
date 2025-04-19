package sumdu.utility;

import java.util.regex.*;

/**
 Клас для валідації IP-адрес
 */
public class IpValidator {
    private static final Pattern IP_REGEX = Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.|$)){4}$"
    );

    /**
     Перевіряє, чи є переданий рядок валідною IPv4-адресою
     @param ip Рядок, який потрібно перевірити на валідність IP-адреси
     @return true якщо рядок є валідною IPv4-адресою
     */
    public static boolean isValidIp(String ip) {
        return IP_REGEX.matcher(ip).matches();
    }
}