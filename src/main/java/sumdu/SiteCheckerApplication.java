package sumdu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 Головний клас застосунку для перевірки сайтів
 */
@SpringBootApplication
public class SiteCheckerApplication {

    /**
     Головний метод застосунку, який запускає Spring Boot
     @param args Аргументи командного рядка, передані при запуску застосунку
     */
    public static void main(String[] args) {
        SpringApplication.run(SiteCheckerApplication.class, args);
    }
}