package sumdu.controler;

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

/**
 REST-контролер для API, що надає інформацію про сайти
 */
@RestController
@RequestMapping("/oldapi")
public class Controller {

    /**
     Перевіряє доступність сайту та повертає основну інформацію про нього

     @param url URL сайту для перевірки

     @return Map, що містить інформацію про сайт: URL, код статусу, інформацію про сервер,
     час відповіді, редирект (якщо є) та IP-адресу
     */

    @GetMapping("/check")
    public Map<String, String> checkSite(@RequestParam String url) {
        return formatSiteInfo(HttpInfoFetcher.fetchInfo(url));
    }

    /**
     Отримує інформацію про всі сайти, пов'язані з певним ідентифікатором користувача

     @param id Ідентифікатор користувача, для якого потрібно отримати інформацію про сайти

     @return Map, де ключ - це порядковий номер сайту, а значення - це Map
     з інформацією про сайт
     */
    @GetMapping("/getsites")
    public Map<Integer, Map<String, String>> getSites(@RequestParam int id) {
        Map<Integer, Map<String, String>> response = new HashMap<Integer, Map<String, String>>();

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

        int siteIdcounter = 0;
        for (HttpInfo httpInfo : httpInfoList) {
            Map<String, String> siteInfo = new HashMap<>();
            siteInfo.put("url", httpInfo.getUrl());
            siteInfo.put("statusCode", String.valueOf(httpInfo.getStatusCode()));
            siteInfo.put("serverInfo", httpInfo.getServerInfo());
            siteInfo.put("location", httpInfo.getLocation());
            siteInfo.put("responseDateTime", httpInfo.getResponseDateTime());
            siteInfo.put("ip", httpInfo.getIp());

            response.put(siteIdcounter++, siteInfo);
        }

        return response;
    }

    /**
     Отримує інформацію про конкретний сайт за його URL

     @param url URL сайту, інформацію про який потрібно отримати

     @return Map, що містить інформацію про сайт
     */
    @GetMapping("/getsite")
    public Map<String, String> getSite(@RequestParam String url) {
        Map<String, String> response = new HashMap<String, String>();

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

        return formatSiteInfo(httpInfo);
    }

    /**
     Форматує об'єкт у вигляді Map

     @param httpInfo Об'єкт класу HttpInfo, який потрібно відформатувати

     @return Map, де ключами є назви полів об'єкта класу HttpInfo (url, statusCode,
     *serverInfo, responseDateTime, location, ip), а значеннями - відповідні значення полів
     */
    private Map<String, String> formatSiteInfo(HttpInfo httpInfo) {
        Map<String, String> formatedMap = new HashMap<>();

        formatedMap.put("url", httpInfo.getUrl());
        formatedMap.put("statusCode", String.valueOf(httpInfo.getStatusCode()));
        formatedMap.put("serverInfo", httpInfo.getServerInfo());
        formatedMap.put("responseDateTime", httpInfo.getResponseDateTime());
        formatedMap.put("location", httpInfo.getLocation());
        formatedMap.put("ip", httpInfo.getIp());

        return formatedMap;
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