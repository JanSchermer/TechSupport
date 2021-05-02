package de.jan.techsupport.main;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import de.jan.techsupport.words.Question;
import de.jan.techsupport.words.QuestionReader;
import de.jan.techsupport.words.WordBag;

public class Main {
  
  public static void main(String[] args) {
    File file = new File("intents.json");
    QuestionReader reader = new QuestionReader(file);
    List<Question> questions = reader.getList();
    WordBag bag = new WordBag(questions);
    System.out.println(toString(bag.getMap("Hello how are you, my frined?")));
  }

  public static String toString(float[] arr) {
    return Arrays.toString(arr);
  }

}
