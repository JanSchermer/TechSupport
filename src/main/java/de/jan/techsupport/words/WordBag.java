package de.jan.techsupport.words;

import java.util.ArrayList;
import java.util.List;

public class WordBag {
  Pattern pattern;
  List<String> words;

  public WordBag(List<Question> questions) {
    pattern = new Pattern();
    words = new ArrayList<String>();
    for(Question question : questions)
      addPatterns(question.getPatterns());
    System.out.println(words);
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
  
}
