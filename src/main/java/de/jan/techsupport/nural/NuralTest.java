package de.jan.techsupport.nural;

import java.util.List;
import java.util.Random;

import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration.ListBuilder;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class NuralTest {
  
  public void test() {
    int size = 100;
    int batchSize = 10;
    int epochs = 40;
    MultiLayerConfiguration conf = getConfig(size);
    MultiLayerNetwork network = new MultiLayerNetwork(conf);
    network.init();
    for(int ep = 0; ep < epochs; ep++) {
      DataSetIterator train = getDataSetIterator(10000, size, batchSize);
      DataSetIterator test = getDataSetIterator(100, size, batchSize);
      System.err.println("\nEpoche: "+ep+" / "+epochs);
      network.fit(train);
      printStats(network, test);
    }
  }

  public void printStats(MultiLayerNetwork network, DataSetIterator test) {
    Evaluation eval = network.evaluate(test);
    System.out.println("Accuraccy: "+eval.accuracy());
    System.out.println("Precision: "+eval.precision());
    System.out.println("Recall: "+eval.recall());
  }

  public MultiLayerConfiguration getConfig(int size) {
    NeuralNetConfiguration.Builder conf = new NeuralNetConfiguration.Builder();
    conf.updater(new Adam(.03));
    ListBuilder builder = conf.list();
    builder.layer(getDenseLayer(size, size*4));
    builder.layer(getOutputLayer(size*4, size));
    return builder.build();
  }

  public OutputLayer getOutputLayer(int input, int output) {
    OutputLayer.Builder builder = new OutputLayer.Builder();
    builder.nIn(input);
    builder.nOut(output);
    builder.lossFunction(LossFunctions.LossFunction.XENT);
    builder.activation(Activation.SIGMOID);
    builder.weightInit(WeightInit.XAVIER);
    return builder.build();
  }

  public DenseLayer getDenseLayer(int input, int output) {
    DenseLayer.Builder builder = new DenseLayer.Builder();
    builder.nIn(input);
    builder.nOut(output);
    builder.activation(Activation.SIGMOID);
    builder.weightInit(WeightInit.XAVIER);
    return builder.build();
  }

  public DataSetIterator getDataSetIterator(int length, int dataSize, int batchSize) {
    DataSet set = getDataSet(length, dataSize);
    List<DataSet> list = set.asList();
    return new ListDataSetIterator<>(list, batchSize);
  }

  public DataSet getDataSet(int length, int dataSize) {
    float[][] data = getRandomData(length, dataSize);
    float[][] invData = getInvData(data);
    INDArray matrix = Nd4j.create(data);
    INDArray lables = Nd4j.create(invData);
    DataSet set = new DataSet(matrix, lables);
    return set;
  }

  public float[][] getRandomData(int length, int dataSize) {
    Random rand = new Random();
    float[][] data = new float[length][dataSize];
    for(int x = 0; x < length; x++) 
      for(int y = 0; y < dataSize; y++) {
        int rNum = rand.nextInt(2);
        data[x][y] = (float) rNum;
      }
    return data;
  }

  public float[][] getInvData(float[][] data) {
    float[][] inv = new float[data.length][];
    for(int x = 0; x < inv.length; x++) {
      inv[x] = new float[data[x].length];
      for(int y = 0; y < inv[x].length; y++) 
        inv[x][y] = data[x][y] == 1f ? 0f : 1f;
    }
    return inv;
  }

}
