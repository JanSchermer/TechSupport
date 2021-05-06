package de.jan.techsupport.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class KnowledgeFetcher extends DataFetcher {
  // Search query format: &i=what+is+this
  static String WOLFRAM_API_DOMAIN = "https://api.wolframalpha.com/v1/result?appid=";
  static String NOT_FOUND = "Wolfram|Alpha did not understand your input";
  static {
    KeyManager keyManager = KeyManager.DEFAULT;
    String apiKey = keyManager.getKey("wolframalpha.com");
    WOLFRAM_API_DOMAIN += apiKey;
  }

  @Override
  public String[] getTags() {
    return new String[]{"notfound"};
  }

  @Override
  public String process(String output, String input) {
    return fetchKnowledge(output, input);
  }

  String restructureInput(String input) {
    input = input.replaceAll("[^a-zA-Z0-9 ]", "");
    input = input.replaceAll(" ", "+");
    return input;
  }

  String fetchKnowledge(String output, String input) {
    String query = restructureInput(input);
    String data = makeRequest(query);
    if(data == null || data.equalsIgnoreCase(NOT_FOUND)) return output;
    data = capitalize(data);
    if(!data.endsWith("."))
      data += ".";
    return data;
  }

  String capitalize(String input) {
    char[] chars = input.toCharArray();
    char firstChar = chars[0];
    chars[0] = Character.toUpperCase(firstChar);
    input = new String(chars);
    return input;
  }

  String makeRequest(String query) {
    try {
      URL url = new URL(WOLFRAM_API_DOMAIN + "&i=" + query);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestProperty("accept", "application/json");
      InputStream in = con.getInputStream();
      byte[] bytes = in.readAllBytes();
      String data = new String(bytes);
      return data;
    } catch (IOException e) {
      return null;
    }
  }

}