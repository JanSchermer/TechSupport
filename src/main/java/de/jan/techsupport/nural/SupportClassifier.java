package de.jan.techsupport.nural;

import java.util.List;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import de.jan.techsupport.questions.Question;
import de.jan.techsupport.questions.QuestionInitializer;

public class SupportClassifier {
  static final int DEFAULT_TRIANING_EPOCHS = 500;

  WordBag bag;
  List<Question> questions;
  NetworkBuilder builder;
  NetworkTrainer trainer;
  MultiLayerNetwork network;

  public SupportClassifier() {
    this(new QuestionInitializer().load());
  }
  public SupportClassifier(List<Question> questions) {
    bag = new WordBag(questions);
    builder = new NetworkBuilder(bag.size(), questions.size());
    this.questions = questions;
  }

  public void init() {
    if(network != null) return;
    network = builder.build();
    trainer = new NetworkTrainer(network, questions, bag);
    trainer.train(DEFAULT_TRIANING_EPOCHS, true);
  }

  public Question classify(String input) {
    if(network == null) throw new RuntimeException("SupportClassifier needs to be initialized!");
    INDArray matrix = buildMatrix(input);
    int predictionIndex = predict(matrix);
    Question prediction = questions.get(predictionIndex);
    return prediction;
  }

  int predict(INDArray matrix) {
    if(matrix == null) return 0;
    int[] predictions = network.predict(matrix);
    int prediction = predictions[0];
    return prediction;
  }

  INDArray buildMatrix(String input) {
    float[][] matrix = new float[1][];
    float[] map = bag.getMap(input);
    if(isEmpty(map)) return null;
    matrix[0] = map;
    INDArray matrixArr = Nd4j.create(matrix);
    return matrixArr;
  }

  public boolean isEmpty(float[] map) {
    for(int i = 0; i < map.length; i++)
      if(map[i] != 0f) return false;
    return true;
  }

  public WordBag getBag() {
    return bag;
  }

  public MultiLayerNetwork getNetwork() {
    return network;
  }

}
