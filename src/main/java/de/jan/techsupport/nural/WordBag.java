package de.jan.techsupport.nural;

import java.util.ArrayList;
import java.util.List;

import de.jan.techsupport.questions.Question;
import opennlp.tools.stemmer.PorterStemmer;

public class WordBag {
  Pattern pattern;
  List<String> words;

  public WordBag(List<Question> questions) {
    pattern = new Pattern();
    words = new ArrayList<String>();
    for(Question question : questions) {
      List<String> patterns = question.getPatterns();
      patterns = this.pattern.filter(patterns);
      addPatterns(patterns);
    }
  }

  void addPatterns(List<String> patterns) {
    for(String pattern : patterns)
      addWords(pattern.split(" "));
  }

  void addWords(String[] words) {
    for(String word : words) 
      if(!this.words.contains(word))
        this.words.add(word);
  }

  public int size() {
    return words.size();
  }

  public float[] getMap(String input) {
    float[] map = empty(words.size());
    if(input == null || input.length() == 0) return map;
    input = pattern.filter(input);
    String[] data = input.split(" ");
    for(String word : data)
      if(words.contains(word))
        map[words.indexOf(word)] = 1f;
    return map;
  }

  float[] empty(int length) {
    float[] empty = new float[words.size()];
    for(int i = 0; i < length; i++)
      empty[i] = 0f;
    return empty;
  }
  
  public static class Pattern {
    public static String[] blackList = {"pineapple"};

    PorterStemmer stemmer;
  
    public Pattern() {
      stemmer = new PorterStemmer();
    }
  
    public List<String> filter(List<String> input) {
      List<String> list = new ArrayList<String>();
      for(String sequence : input)
        list.add(filter(sequence));
      return list;
    }
  
    public String filter(String input) {
      input = input.replaceAll("[^a-zA-Z0-9 ]", "");
      input = input.toLowerCase();
      String[] words = input.split(" ");
      input = "";
      for(String word : words)
        input += stemmer.stem(word) + " ";
      for(String word : blackList)
        input = input.replaceAll(word, "");
      input = input.strip();
      return input;
    }
  }

}
