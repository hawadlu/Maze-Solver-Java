package Utility;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Class that is used to log each action in the program.
 * Used for debugging
 */
public class Logger {
  ArrayList<String> actions = new ArrayList<>();

  /**
   * Add an action to the log.
   * @param action the action that was performed.
   */
  public void add(String action) {
    actions.add(action);
  }

  /**
   * Save the log.
   */
  public void save() {
    LocalDate date = LocalDate.now();
    LocalTime time = LocalTime.now();

    String output = "Logs/log " + date + " " + time + ".txt";
    File saveDir = new File(output);
    PrintWriter pw = null;
    try {
      pw = new PrintWriter(new FileOutputStream(saveDir));

      for (String str : actions) {
        pw.println(str); // call toString() on club, like club.toString()
      }
      pw.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}

