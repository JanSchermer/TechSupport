package de.jan.techsupport.words;

import java.util.ArrayList;
import java.util.List;

import opennlp.tools.stemmer.PorterStemmer;

public class Pattern {
  PorterStemmer stemmer;

  public Pattern() {
    stemmer = new PorterStemmer();
  }

  public List<String> filter(List<String> words) {
    List<String> list = new ArrayList<String>();
    for(String word : words)
      list.add(filter(word));
    return list;
  }

  public String filter(String word) {
    word = stemmer.stem(word);
    word = word.replaceAll("[^a-zA-Z0-9 ]", "");
    word = word.toLowerCase();
    word = word.strip();
    return word;
  }

}
