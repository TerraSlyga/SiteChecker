package main.java.controler;

import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api")
public class Controller {

    @GetMapping("/check")
    public Map<String, String> fetchInfo(@RequestParam String url) {
        Map<String, String> response = new HashMap<>();

        // Симуляція фетчингу
        response.put("URL", url);
        response.put("Status", "200 OK");
        response.put("Server", "Apache/2.4.54");
        response.put("Title", "Головна сторінка сайту");
        response.put("Datetime", LocalDateTime.now().toString());
        response.put("IP", "93.184.216.34");
        response.put("Location", "USA");

        return response;
    }
}
