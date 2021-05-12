package de.jan.techsupport.nural;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;

public class NLChunker {
  final static String DEFAULT_CHUNKER_MODEL = "en-chunking.bin"; // Source: http://opennlp.sourceforge.net/models-1.5/

  ParserModel model;
  Parser parser;

  public NLChunker() {
    this(DEFAULT_CHUNKER_MODEL);
  }
  public NLChunker(String model) {
    init(model);
  } 

  void init(String modlePath) {
    InputStream in;
    try {
      in = new FileInputStream(modlePath);
      model = new ParserModel(in);
      in.close();
    } catch (IOException e) {
      throw new RuntimeException("No chunking modle at \""+modlePath+"\"!");
    }
    parser = ParserFactory.create(model);
  }

  public String getLikelyNounPhrase(String input) {
    List<String> phrases = getNounPhrases(input);
    if(phrases.size() == 0) return null;
    return phrases.get(0);
  }

  public List<String> getNounPhrases(String input) {
    Parse[] topParses = getTopParses(input);
    List<String> phrases = new ArrayList<String>();
    Comparator<String> comparator = getWordCountComparator();
    for(Parse parse : topParses)
      extract(parse, phrases);
    Collections.sort(phrases, comparator);
    return phrases;
  }

  void extract(Parse sample, List<String> list) {
    String type = sample.getType();
    Parse[] children = sample.getChildren();
    if(type.equals("NP")) // NP: Noun Phrase
      list.add(sample.getCoveredText());
    for(Parse child : children)
      extract(child, list);
  }

  Parse[] getTopParses(String input) {
    return ParserTool.parseLine(input, parser, 1);
  }

  Comparator<String> getWordCountComparator() { // Sorted in reverse order! (big -> small)
    return new Comparator<String>() { 

      @Override
      public int compare(String sample, String compare) { 
        int sampleCount = count(sample);
        int compareCount = count(compare);
        if(sampleCount > compareCount) return -1;
        if(sampleCount < compareCount) return 1;
        return 0;
      }
      
      int count(String input) {
        String str = input.strip();
        char[] chars = str.toCharArray();
        int count = 0;
        for(char c : chars)
          if(c == ' ') 
            count++;
        return count;
      }

    };
  }

}
