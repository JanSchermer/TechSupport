package de.jan.techsupport.nural;

import java.util.List;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

import de.jan.techsupport.words.Question;
import de.jan.techsupport.words.WordBag;

public class SupportClassifier {
  WordBag bag;
  List<Question> questions;
  NetworkBuilder builder;
  MultiLayerNetwork network;

  public SupportClassifier(List<Question> questions) {
    this.questions = questions;
    bag = new WordBag(questions);
    builder = new NetworkBuilder(bag.size(), questions.size());
    network = builder.build();
  }

  public WordBag getBag() {
    return bag;
  }

}
