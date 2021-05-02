package de.jan.techsupport.nural;

import java.util.List;

import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;

import de.jan.techsupport.words.Question;
import de.jan.techsupport.words.WordBag;

public class NetworkTrainer {
  MultiLayerNetwork network;
  List<Question> questions;
  WordBag bag;

  public NetworkTrainer(MultiLayerNetwork network, List<Question> questions, WordBag bag) {
    this.network = network;
    this.questions = questions;
    this.bag = bag;
  }

  public void train(int epochs) {
    train(epochs, false);
  }
  public void train(int epochs, boolean log) {
    DataSet dataset = createSet();
    DataSetIterator iter = createIterator(dataset);
    for(int epoch = 0; epoch < epochs; epoch++) {
      if(log)
        System.out.println("Training network... Epoch: "+(epoch + 1)+" / "+epochs);
      network.fit(iter);
    }
  }

  DataSetIterator createIterator(DataSet set) {
    List<DataSet> list = set.asList();
    ListDataSetIterator<?> iter = new ListDataSetIterator<>(list, NetworkBuilder.DEAFAULT_BATCH_SIZE);
    return iter;
  }

  DataSet createSet() {
    int size = getSetSize();
    float[][] matrix = new float[size][];
    float[][] labels = new float[size][];
    int index = 0;
    for(int quest = 0; quest < questions.size(); quest++) {
      Question question = questions.get(quest);
      for(int pattern = 0; pattern < question.getPatterns().size(); pattern++) {
        matrix[index] = bag.getMap(question.getPatterns().get(pattern));
        labels[index] = getMap(quest);
        index++;
      }
    }
    INDArray matrixArr = Nd4j.create(matrix);
    INDArray labelsArr = Nd4j.create(labels);
    DataSet set = new DataSet(matrixArr, labelsArr);
    return set;
  }

  float[] getMap(int index) {
    float[] map = new float[questions.size()];
    for(int i = 0; i < map.length; i++)
      map[i] = 0f;
    map[index] = 1f;
    return map;
  }

  int getSetSize() {
    int size = 0;
    for(Question question : questions)
      size += question.getPatterns().size();
    return size;
  }

}
