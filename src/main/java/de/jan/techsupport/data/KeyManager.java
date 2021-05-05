package de.jan.techsupport.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class KeyManager {
  public static KeyManager DEFAULT;
  static {
    DEFAULT = new KeyManager("keys.json");
  }

  File file;
  JsonObject data;
  JsonParser parser;

  public KeyManager(String filePath) {
    file = new File(filePath);
    parser = new JsonParser();
    try {
      loadData();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  void loadData() throws IOException {
    InputStream inStream = new FileInputStream(file);
    byte[] bytes = inStream.readAllBytes();
    inStream.close();
    String json = new String(bytes);
    data = (JsonObject) parser.parse(json);
  }

  public String getKey(String name) {
    String key = data.get(name).getAsString();
    return key;
  }

}
