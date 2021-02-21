package tests;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class used for tracking and saving test results
 */
public class ResultTracker {
  ArrayList<TestResult> results = new ArrayList<>();

  public void addResult(String algorithm, String file, double size, String onLoad, String onSolve, long execTime, boolean multiThreading, String success) {
    results.add(new TestResult(algorithm, file, size, onLoad, onSolve, execTime, multiThreading, success));
  }

  /**
   * Create a CSV file and save the results
   */
  public void saveResult() throws IOException {
    long timeVal = new Date().getTime();
    Timestamp time = new Timestamp(timeVal);
    String timeStr = time.toString();
    File newCSV = new File("Test Results/Test Results (" + timeStr + ").csv");
    FileWriter csv = new FileWriter(newCSV);

    //Add the headers
    csv.append("File, Algorithm, Size, Load, solve, execTime (ns), Multi Threading, Success\n");

    //Add each of the results
    for (TestResult test : results) csv.append(test.printCSV());

    csv.flush();
    csv.close();
  }
}

/**
 * Holds each individual result
 */
class TestResult {
  String fileName, onLoad, onSolve, algorithm, success = "success";
  long execTime;
  boolean multiThreading;
  double size;

  public TestResult(String algorithm, String fileName, double size, String onLoad, String onSolve, long execTime, boolean multiThreading, String success) {
    this.fileName = fileName;
    this.onLoad = onLoad;
    this.onSolve = onSolve;
    this.execTime = execTime;
    this.multiThreading = multiThreading;
    this.size = size;
    this.algorithm = algorithm;
    this.success = success;
  }

  /**
   * @return return the data in csv format
   */
  public String printCSV() {
    return fileName + ", " + algorithm + ", " + size + ", " + onLoad + ", " + onSolve + ", " + execTime + ", " + multiThreading + " " + success + "\n";
  }
}
