package de.jan.techsupport.data;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WeatherFetcher extends DataFetcher {
  // Lat: 51.961563, Lon: 7.628202 - Coordinates of Münser, DE
  static String WEATHER_API_DOMAIN = "http://api.openweathermap.org/data/2.5/onecall?lat=51.961563&lon=7.628202&units=metric&appid=";
  final static int MAX_DAYS_FORECAST = 7;
  final static String[] WEATHER_VALUES = {"main", "description", "temperature"};
  static {
    KeyManager keyManager = KeyManager.DEFAULT;
    String apiKey = keyManager.getKey("openweathermap.org");
    WEATHER_API_DOMAIN += apiKey;
  }

  JsonParser parser;

  public WeatherFetcher() {
    parser = new JsonParser();
  }

  @Override
  public String[] getTags() {
    return new String[]{"weather"};
  }

  @Override
  public String process(String input) {
    JsonArray data = fetchData();
    for(int day = 0; day < 7; day++)
      input = replaceDay(input, data, day);
    return input;
  }

  public String replaceDay(String response, JsonArray data, int day) {
    JsonObject weather = getWeather(day, data);
    for(String value : WEATHER_VALUES) {
      String pattern = "%"+day+"-"+value+"%";
      pattern = pattern.toUpperCase();
      if(!response.contains(pattern)) continue;
      String replacement = weather.get(value).getAsString();
      response = response.replaceAll(pattern, replacement);
    }
    return response;
  }

  public JsonObject getWeather(int dayIndex, JsonArray data) {
    JsonObject day = data.get(dayIndex).getAsJsonObject();
    JsonObject temp = day.get("temp").getAsJsonObject();
    String temperature = temp.get("day").getAsString();
    JsonArray weatherArr = day.get("weather").getAsJsonArray();
    JsonObject weather = weatherArr.get(0).getAsJsonObject();
    weather.addProperty("temperature", temperature);
    return weather;
  }

  public JsonArray fetchData() {
    try {
      URL url = new URL(WEATHER_API_DOMAIN);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestProperty("accept", "application/json");
      InputStream in = con.getInputStream();
      byte[] bytes = in.readAllBytes();
      String data = new String(bytes);
      JsonObject obj = (JsonObject) parser.parse(data);
      JsonArray weather = obj.get("daily").getAsJsonArray();
      return weather;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

}
