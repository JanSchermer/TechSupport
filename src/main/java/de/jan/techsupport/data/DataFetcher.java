package de.jan.techsupport.data;

import java.util.ArrayList;
import java.util.List;

public abstract class DataFetcher {
  public static final List<DataFetcher> DEFAULTS;
  static {
    DEFAULTS = new ArrayList<DataFetcher>();
    DEFAULTS.add(new JokeFetcher());
    DEFAULTS.add(new TimeFetcher());
    DEFAULTS.add(new WeatherFetcher());
    DEFAULTS.add(new KnowledgeFetcher());
  }

  public abstract String process(String output, String input);
  public abstract String[] getTags();

  public boolean handles(String tag) {
    for(String compare : getTags())
      if(compare.equalsIgnoreCase(tag))
        return true;
    return false;
  }

}
