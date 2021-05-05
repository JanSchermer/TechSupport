package de.jan.techsupport.questions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class QuestionReader {
  JsonParser parser;
  JsonArray data;

  public QuestionReader(File file) {
    parser = new JsonParser();
    String data = readString(file);
    if(data == "") throw new NullPointerException();
    JsonElement element = parser.parse(data);
    this.data = element.getAsJsonArray();
  }

  String readString(File file) {
    InputStream in;
    byte[] bytes;
    try {
      in = new FileInputStream(file);
      bytes = in.readAllBytes();
      in.close();
    }catch (IOException e) {
      return "";
    }
    String str = new String(bytes);
    return str;
  }

  public List<Question> getList() {
    List<Question> list = new ArrayList<Question>();
    for(JsonElement element : data) {
      JsonObject obj = element.getAsJsonObject();
      String tag = obj.get("tag").getAsString();
      List<String> patterns = getList(obj.get("patterns"));
      List<String> responses = getList(obj.get("responses"));
      Question question = new Question(tag, patterns, responses);
      list.add(question);
    }
    return list;
  }

  List<String> getList(JsonElement data) {
    JsonArray array = data.getAsJsonArray();
    List<String> list = new ArrayList<String>();
    for(JsonElement element : array)
      list.add(element.getAsString());
    return list;
  }

}