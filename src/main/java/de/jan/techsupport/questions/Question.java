package de.jan.techsupport.questions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.jan.techsupport.data.DataFetcher;

public class Question {
  String tag;
  List<String> responses;
  List<String> patterns;
  List<DataFetcher> fetchers;
  Random rand;

  public Question(String tag, List<String> patterns, List<String> responses) {
    this.tag = tag;
    this.responses = responses;
    this.patterns = patterns;
    fetchers = new ArrayList<DataFetcher>();
    rand = new Random();
  }

  public String getResponse(String input) {
    int index = rand.nextInt(responses.size());
    String response = responses.get(index);
    for(DataFetcher fetcher : fetchers)
      response = fetcher.process(response, input);
    return response;
  }

  public String getTag() {
    return tag;
  }

  public List<String> getPatterns() {
    return patterns;
  }

  public List<String> getResponses() {
    return responses;
  }

  public List<DataFetcher> getFetchers() {
    return fetchers;
  }

}

