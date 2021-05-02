package de.jan.techsupport.nural;

import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class LayerBuilder {
  static final Activation DEFAULT_ACTIVATION = Activation.RELU;
  static final WeightInit DEFAULT_WEIGHT = WeightInit.XAVIER;
  static final Activation OUTPUT_ACTIVATION = Activation.SOFTMAX;
  static final LossFunctions.LossFunction OUTPUT_LOSS = LossFunctions.LossFunction.MCXENT;

  Activation activation;
  int inputN, outputN, hiddenN;
  DenseLayer.Builder denseBuilder;
  OutputLayer.Builder outputBuilder;

  public LayerBuilder(int inputN, int outputN, int hiddenN) {
    this.inputN = inputN;
    this.outputN = outputN;
    this.hiddenN = hiddenN;
    init();
  }

  void init() {
    denseBuilder = new DenseLayer.Builder();
    outputBuilder = new OutputLayer.Builder();
    denseBuilder.activation(DEFAULT_ACTIVATION);
    denseBuilder.weightInit(DEFAULT_WEIGHT);
    outputBuilder.activation(OUTPUT_ACTIVATION);
    outputBuilder.weightInit(DEFAULT_WEIGHT);
    outputBuilder.lossFunction(OUTPUT_LOSS);
  }

  public DenseLayer buildDense(boolean first) {
    denseBuilder.nIn(first ? inputN : hiddenN);
    denseBuilder.nOut(hiddenN);
    return denseBuilder.build();
  }

  public OutputLayer buildOutput() {
    outputBuilder.nIn(hiddenN);
    outputBuilder.nOut(outputN);
    return outputBuilder.build();
  }

}
