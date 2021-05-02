package de.jan.techsupport.nural;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration.ListBuilder;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.learning.config.Adam;

public class NetworkBuilder {
  final static double DEFAULT_LEARN_RATE = .25;
  final static int DEFAULT_HIDDEN_NEURONS = 16;

  int inputN, outputN;
  LayerBuilder layerBuilder;

  public NetworkBuilder(int inputN, int outputN) {
    this.inputN = inputN;
    this.outputN = outputN;
    layerBuilder = new LayerBuilder(inputN, outputN, DEFAULT_HIDDEN_NEURONS);
  }

  public MultiLayerNetwork build() {
    NeuralNetConfiguration.Builder builder = new NeuralNetConfiguration.Builder();
    Adam optimizer = new Adam(DEFAULT_LEARN_RATE);
    builder.updater(optimizer);
    ListBuilder listBuilder = builder.list();
    MultiLayerConfiguration config = buildLayers(listBuilder);
    MultiLayerNetwork network = new MultiLayerNetwork(config);
    return network;
  }

  MultiLayerConfiguration buildLayers(ListBuilder builder) {
    builder.layer(0, layerBuilder.buildDense(true));
    builder.layer(1, layerBuilder.buildDense(false));
    builder.layer(2, layerBuilder.buildOutput());
    return builder.build();
  }

}
