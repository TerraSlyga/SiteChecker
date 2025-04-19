package sumdu.controler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestMapping;
import sumdu.DTO.HttpInfo;
import sumdu.repository.DatabaseController;
import sumdu.repository.DbHandler;
import sumdu.servise.HttpInfoFetcher;
import com.google.gson.*;

/**
 REST-контролер для API, що надає інформацію про сайти
 */
@RestController
@RequestMapping("/api")
public class RESTController {

    /**
     Перевіряє доступність сайту та повертає основну інформацію про нього

     @param url URL сайту для перевірки

     @return Map, що містить інформацію про сайт: URL, код статусу, інформацію про сервер,
     час відповіді, редирект (якщо є) та IP-адресу
     */

    @GetMapping("/check")
    public ResponseEntity<String> checkSite(@RequestParam String url) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String jsonString = gson.toJson(HttpInfoFetcher.fetchInfo(url));
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(jsonString);
    }

    /**
     Отримує інформацію про всі сайти, пов'язані з певним ідентифікатором користувача

     @param id Ідентифікатор користувача, для якого потрібно отримати інформацію про сайти

     @return Map, де ключ - це порядковий номер сайту, а значення - це Map
     з інформацією про сайт
     */
    @GetMapping("/getsites")
    public ResponseEntity<String> getSites(@RequestParam int id) {

        DbHandler dbHandler = null;

        try {
            dbHandler = new DbHandler(DatabaseController.getConnection());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<HttpInfo> httpInfoList = null;
        try{
            httpInfoList  = dbHandler.getAllSites(id);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String jsonString = gson.toJson(httpInfoList);

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(jsonString);
    }

    /**
     Отримує інформацію про конкретний сайт за його URL

     @param url URL сайту, інформацію про який потрібно отримати

     @return Map, що містить інформацію про сайт
     */
    @GetMapping("/getsite")
    public ResponseEntity<String> getSite(@RequestParam String url) {
        DbHandler dbHandler = null;

        try {
            dbHandler = new DbHandler(DatabaseController.getConnection());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        HttpInfo httpInfo = null;
        try {
            httpInfo = dbHandler.getSite(url);
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String jsonString = gson.toJson(httpInfo);

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(jsonString);
    }

    /**
     Додає інформацію про новий веб-сайт до бази даних для вказаного користувача

     @param url URL веб-сайту, який потрібно додати до бази даних

     @param id  Ідентифікатор користувача
     */
    @PostMapping("/addsite")
    public void addSite(@RequestParam String url, @RequestParam Integer id) {
        HttpInfo httpInfo = HttpInfoFetcher.fetchInfo(url);

        DbHandler dbHandler = null;

        try {
            dbHandler = new DbHandler(DatabaseController.getConnection());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        dbHandler.addSite(id, httpInfo);

    }
}
