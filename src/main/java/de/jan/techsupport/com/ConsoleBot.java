package de.jan.techsupport.com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.jan.techsupport.nural.SupportClassifier;
import de.jan.techsupport.questions.Question;

public class ConsoleBot {
  SupportClassifier classifier;
  BufferedReader reader;

  public ConsoleBot(SupportClassifier classifier) {
    this.classifier = classifier;
    InputStream inStream = System.in;
    InputStreamReader inReader = new InputStreamReader(inStream);
    reader = new BufferedReader(inReader);
  }

  public void start() {
    try {
      while(true)
        listen();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void listen() throws IOException {
    String line = reader.readLine();
    Question classification = classifier.classify(line);
    String tag = classification.getTag();
    String response = classification.getResponse();
    System.out.println("[Console] "+tag+" > "+response+"\n");
  }

}
