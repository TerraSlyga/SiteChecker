package sumdu.controler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 Веб-контролер для обробки запитів до статичних сторінок.
 Цей контролер відповідає за відображення статичних HTML сторінок користувачеві
 */
@Controller
public class WebController {

    /**
     Обробляє запит до головної сторінки ("/")

     @return Назва HTML-файлу головної сторінки
     */
    @GetMapping("/")
    public String index() {
        return "index.html";
    }
}