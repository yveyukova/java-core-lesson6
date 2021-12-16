import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Lesson6 {

    public static void main(String[] args) {
        parseWeather(getWeather());
    }

    private static StringBuffer getWeather() {
        StringBuffer json = new StringBuffer(1024);
        URL url = null;
        try {
            url = new URL("https://api.openweathermap.org/data/2.5/onecall?lat=59.94&lon=30.31&exclude=current, minutely, hourly&appid=81d3ba1b4d59d114772cfd4bd750c667&lang=RU&units=metric");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection hpCon = null;
        try {
            hpCon = (HttpURLConnection) url.openConnection();
            System.out.println("Код ответа:" + hpCon.getResponseCode());
            if (hpCon.getResponseCode() == 200) {

                hpCon.getContent();
                InputStream is = hpCon.getInputStream();
                for (; ; ) {
                    byte b[] = new byte[256];
                    int l = is.read(b);
                    if (l > 0)
                        json.append(new String(b, 0, l));
                    else
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }

    private static void parseWeather(StringBuffer json) {
        //System.out.println(json);
        JSONParser parser = new JSONParser();
        JSONObject jsonObj = null;
        try {
            jsonObj = (JSONObject) parser.parse(json.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("Прогноз погоды в Санкт-Петербурге");
        JSONArray dailyArray = (JSONArray) jsonObj.get("daily");
        for (int i = 0; i < dailyArray.size(); i++) {
            JSONObject daily = (JSONObject) dailyArray.get(i);
            Date date  = new Date( (Long) daily.get("dt") * 1000 );
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            System.out.println(dateFormat.format(date));
            JSONObject temp  = (JSONObject) daily.get("temp");
            System.out.println("Температура: днем " + temp.get("day") + " ночью " + temp.get("night") );
            JSONArray weather = (JSONArray) daily.get("weather");
            JSONObject w  = (JSONObject) weather.get(0);
            System.out.println( w.get("description"));
        }
    }
}
