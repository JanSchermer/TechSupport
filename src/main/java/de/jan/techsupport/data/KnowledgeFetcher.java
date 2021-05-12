package de.jan.techsupport.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.jan.techsupport.nural.NLChunker;

public class KnowledgeFetcher extends DataFetcher {
  // Search quety format: &query=taylor+swift
  static String GOOGLE_API_DOMAIN = "https://kgsearch.googleapis.com/v1/entities:search?limit=1&indent=True&key=";

  // Search query format: &i=what+is+this
  static String WOLFRAM_API_DOMAIN = "https://api.wolframalpha.com/v1/result?appid=";
  static String WOLFRAM_NOT_FOUND = "Wolfram|Alpha did not understand your input";
  
  static {
    KeyManager keyManager = KeyManager.DEFAULT;
    String googleKey = keyManager.getKey("googleapis.com");
    String wolframKey = keyManager.getKey("wolframalpha.com");
    GOOGLE_API_DOMAIN += googleKey;
    WOLFRAM_API_DOMAIN += wolframKey;
  }

  NLChunker chunker;
  JsonParser parser;

  public KnowledgeFetcher() {
    super();
    chunker = new NLChunker();
    parser = new JsonParser();
  }

  @Override
  public String[] getTags() {
    return new String[]{"notfound"};
  }

  @Override
  public String process(String output, String input) { // GoogleAPI -> WolframAPI -> Not Found
    String response = fetchGoogleKnowledge(input);
    if(response == null) response = fetchWolframKnowledge(input);
    if(response == null) response = output;
    return response;
  }

  String makeRequest(String query) {
    try {
      URL url = new URL(query);
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

  String capitalize(String input) {
    char[] chars = input.toCharArray();
    char firstChar = chars[0];
    chars[0] = Character.toUpperCase(firstChar);
    input = new String(chars);
    return input;
  }

  String restructure(String input) {
    input = input.replaceAll("[^a-zA-Z0-9 ]", "");
    input = input.replaceAll(" ", "+");
    return input;
  }

  String fetchGoogleKnowledge(String input) {
    String phrase = chunker.getLikelyNounPhrase(input);
    if(phrase == null) return null;
    String query = GOOGLE_API_DOMAIN + "&query=" + restructure(phrase);
    String response = makeRequest(query);
    JsonObject responseObj = parser.parse(response).getAsJsonObject();
    JsonArray results = responseObj.get("itemListElement").getAsJsonArray();
    if(results.size() == 0) return null;
    JsonObject result = results.get(0).getAsJsonObject();
    result = result.get("result").getAsJsonObject();
    String description = null;
    if(result.has("detailedDescription")) {
      JsonObject detailedDescription = result.get("detailedDescription").getAsJsonObject();
      description = detailedDescription.get("articleBody").getAsString();
    }else if(result.has("description"))
      description = result.get("description").getAsString();
    return description;
  }

  String fetchWolframKnowledge(String input) {
    String query = WOLFRAM_API_DOMAIN + "&i=" + restructure(input);
    String data = makeRequest(query);
    if(data == null || data.equalsIgnoreCase(WOLFRAM_NOT_FOUND)) return null;
    data = capitalize(data);
    if(!data.endsWith("."))
      data += ".";  
    return data;
  }

}