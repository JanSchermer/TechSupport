package de.jan.techsupport.data;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JokeFetcher extends DataFetcher {
  final static String JOKE_API_DOMAIN = "https://v2.jokeapi.dev/joke/Any?type=single";

  JsonParser parser;

  public JokeFetcher() {
    parser = new JsonParser();
  }

  @Override
  public String[] getTags() {
    return new String[]{"joke"};
  }

  @Override
  public String process(String input) {
    return fetchJoke(input);
  }
  
  public String fetchJoke(String input) {
    try {
      URL url = new URL(JOKE_API_DOMAIN);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestProperty("accept", "application/json");
      InputStream in = con.getInputStream();
      byte[] bytes = in.readAllBytes();
      String data = new String(bytes);
      JsonObject obj = (JsonObject) parser.parse(data);
      String joke = obj.get("joke").getAsString();
      return joke;
    } catch (Exception e) {
      e.printStackTrace();
      return input;
    }
  }

}
