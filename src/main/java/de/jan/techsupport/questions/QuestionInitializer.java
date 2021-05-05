package de.jan.techsupport.questions;

import java.io.File;
import java.util.List;

import de.jan.techsupport.data.DataFetcher;

public class QuestionInitializer {
  static final String DEFAULT_FILE_PATH = "intents.json";
  
  File file;
  
  public QuestionInitializer() {
    this(DEFAULT_FILE_PATH);
  }
  public QuestionInitializer(String filePath) {
    file = new File(filePath);
  }

  public List<Question> load() {
    QuestionReader reader = new QuestionReader(file);
    List<Question> questions = reader.getList();
    addFetchers(questions);
    return questions;
  }

  void addFetchers(List<Question> questions) {
    for(Question question : questions)
      addFetchers(question); 
  }
  void addFetchers(Question question) {
    String tag = question.getTag();
    List<DataFetcher> fetchers = question.getFetchers();
    for(DataFetcher fetcher : DataFetcher.DEFAULTS)
      if(fetcher.handles(tag))
        fetchers.add(fetcher);
  }

}
