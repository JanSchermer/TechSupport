package de.jan.techsupport.main;

import java.util.Arrays;

import de.jan.techsupport.com.ConsoleBot;
import de.jan.techsupport.com.DiscordBot;
import de.jan.techsupport.data.KeyManager;
import de.jan.techsupport.nural.SupportClassifier;

public class Main {

  public static void main(String[] args) {
    SupportClassifier classifier = new SupportClassifier();
    classifier.init();
    System.out.println("\nNetwork-Training completed!\n");
    startBots(classifier);
  }

  public static void startBots(SupportClassifier classifier) {
    DiscordBot discordBot = new DiscordBot(classifier, KeyManager.DEFAULT.getKey("discord"));
    ConsoleBot consoleBot = new ConsoleBot(classifier);
    discordBot.start();
    consoleBot.start();
  }

  public static String toString(float[] arr) {
    return Arrays.toString(arr);
  }

}
