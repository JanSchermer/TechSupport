package de.jan.techsupport.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFetcher extends DataFetcher {

  @Override
  public String[] getTags() {
    return new String[]{"time"};
  }
  
  @Override
  public String process(String output, String input) {
    String timeFormat = "HH:mm:ss";
    String dateFormat = "dd.MM.yyyy";
    String time = getCurrentTimeStamp(timeFormat);
    String date = getCurrentTimeStamp(dateFormat);
    output = output.replaceAll("%TIME%", time);
    output = output.replaceAll("%DATE%", date);
    return output;
  }

  String getCurrentTimeStamp(String format) {
    SimpleDateFormat sdfDate = new SimpleDateFormat(format);
    Date now = new Date();
    String strDate = sdfDate.format(now);
    return strDate;
  }
  
}
