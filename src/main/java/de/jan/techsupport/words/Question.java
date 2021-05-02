package de.jan.techsupport.words;

import java.util.List;

public class Question {
  String tag;
  List<String> responses;
  List<String> patterns;

  public Question(String tag, List<String> patterns, List<String> responses) {
    this.tag = tag;
    this.responses = responses;
    this.patterns = patterns;
  }

  public String getTag() {
    return tag;
  }

  public List<String> getResponses() {
    return responses;
  }

  public List<String> getPatterns() {
    return patterns;
  }
}
