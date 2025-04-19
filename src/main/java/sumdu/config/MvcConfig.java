package sumdu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 Конфігураційний клас Spring MVC для налаштування веб-компонентів.
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     Метод addResourceHandlers використовується для реєстрації обробників,
     які відповідають за доставку статичного контенту,
     такого як файли CSS, JavaScript, зображення тощо

     @param registry Об'єкт класу ResourceHandlerRegistry, який надає API для реєстрації
     обробників статичних ресурсів. Використовується для налаштування
     відображення між URL-шляхами та розташуванням ресурсів
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}