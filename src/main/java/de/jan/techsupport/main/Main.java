package de.jan.techsupport.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import de.jan.techsupport.nural.NetworkBuilder;
import de.jan.techsupport.nural.NetworkTrainer;
import de.jan.techsupport.words.Question;
import de.jan.techsupport.words.QuestionReader;
import de.jan.techsupport.words.WordBag;

public class Main {
  
  public static void main(String[] args) {
    File file = new File("intents.json");
    QuestionReader reader = new QuestionReader(file);
    List<Question> questions = reader.getList();
    WordBag bag = new WordBag(questions);
    NetworkBuilder builder = new NetworkBuilder(bag.size(), questions.size());
    MultiLayerNetwork network = builder.build();
    NetworkTrainer trainer = new NetworkTrainer(network, questions, bag);
    trainer.train(1000, true);
    System.out.println("\nNetwork-Training completed!\n");
    try {
      testInputs(network, questions, bag);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void testInputs(MultiLayerNetwork network, List<Question> questions, WordBag bag) throws IOException {
    InputStream is = System.in;
    InputStreamReader inReader = new InputStreamReader(is);
    BufferedReader reader = new BufferedReader(inReader);
    while (true) {
      System.out.print("> ");
      String input = reader.readLine();
      if(input.length() == 0) continue;
      float[] map = bag.getMap(input);
      float[][] matrix = new float[1][];
      matrix[0] = map;
      INDArray matrixArr = Nd4j.create(matrix);
      int[] output = network.predict(matrixArr);
      int prediction = output[0];
      System.out.println("Classification: "+questions.get(prediction).getTag()+"\n");
    }
  }

  public static String toString(float[] arr) {
    return Arrays.toString(arr);
  }

}
