import Utility.Thread.AlgorithmWorkerThread;
import Application.Application;
import Algorithm.SolveAlgorithm;
import Utility.Exceptions.GenericError;
import Utility.Image.ImageProcessor;
import Utility.Location;
import Utility.Node;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {

  //TESTS OF NODE SCANNING

  /**
   * Test the scanning of nodes to ensure that the correct number are found
   */
  @Test
  public void testScanNode() {
    Application application = new Application();
    File image = new File("Images/Tiny.png");
    try {
      application.parseImageFile(image);
    } catch (GenericError genericError) {
      genericError.printStackTrace();
      System.out.println("Failed to parse image");
    }

    ImageProcessor processor = new ImageProcessor(application);

    processor.scanAll();
    HashMap<Location, Node> nodes = processor.getNodes();
    int expectedSize = 12;
    assertEquals(expectedSize, nodes.size(), "Size is: " + nodes.size() + ",  expected size: " + expectedSize);
  }


  //TESTS OF THE DEPTH FIRST SEARCH
  @Test
  public void DFSTinyOnly() throws GenericError, InterruptedException {
    //Delete all the files in the solved folder
    deleteFiles(new File("Images/Solved"));

    ArrayList<File> files = getAllFiles(new File("Images"));

    //Remove anything that is not an image
    files = removeNonImages(files, false, "Tiny");

    Comparator<File> smallest = (File f1, File f2) -> {
      if (f1.length() < f2.length()) return -1;
      if (f1.length() > f2.length()) return 1;
      return 0;
    };

    files.sort(smallest);

    System.out.println("files: " + files);

    String[] options = {"Loading", "Solving"};
    Boolean[] threading = {true, false};
    for (File file : files) {
      for (String option : options) {
        for (Boolean multi : threading) {
          System.out.println("DFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          AlgorithmWorkerThread thread = application.solve("Depth First", option, multi);
          thread.start();
          thread.join();
          System.out.println("Thread complete");

          application.saveImage("Images/Solved/Test DFS " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }

  }

  @Test
  public void DFSTwoKOnly() throws GenericError, InterruptedException {
    //Delete all the files in the solved folder
    deleteFiles(new File("Images/Solved"));

    ArrayList<File> files = getAllFiles(new File("Images"));

    //Remove anything that is not an image
    files = removeNonImages(files, false, "TwoK");

    Comparator<File> smallest = (File f1, File f2) -> {
      if (f1.length() < f2.length()) return -1;
      if (f1.length() > f2.length()) return 1;
      return 0;
    };

    files.sort(smallest);

    System.out.println("files: " + files);

    String[] options = {"Loading", "Solving"};
    Boolean[] threading = {true, false};

    for (File file : files) {
      for (String option : options) {
        for (Boolean multi : threading) {
          System.out.println("DFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          AlgorithmWorkerThread thread = application.solve("Depth First", option, multi);
          thread.start();
          thread.join();
          System.out.println("Thread complete");

          application.saveImage("Images/Solved/Test DFS " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }
  }

  @Test
  public void DFSUnevenOnly() throws GenericError, InterruptedException {
    //Delete all the files in the solved folder
    deleteFiles(new File("Images/Solved"));

    ArrayList<File> files = getAllFiles(new File("Images"));

    //Remove anything that is not an image
    files = removeNonImages(files, false, "Uneven");

    Comparator<File> smallest = (File f1, File f2) -> {
      if (f1.length() < f2.length()) return -1;
      if (f1.length() > f2.length()) return 1;
      return 0;
    };

    files.sort(smallest);

    System.out.println("files: " + files);

    String[] options = {"Loading", "Solving"};

    Boolean[] threading = {true, false};
    for (File file : files) {
      for (String option : options) {
        for (Boolean multi : threading) {
          System.out.println("DFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          AlgorithmWorkerThread thread = application.solve("Depth First", option, multi);
          thread.start();
          thread.join();
          System.out.println("Thread complete");

          application.saveImage("Images/Solved/Test DFS " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }
  }

  @Test
  public void DFSSmallOnly() throws GenericError, InterruptedException {
    deleteFiles(new File("Images/Solved"));
    ArrayList<File> files = getAllFiles(new File("Images"));

    //Remove anything that is not an image
    files = removeNonImages(files, false, "Small");

    Comparator<File> smallest = (File f1, File f2) -> {
      if (f1.length() < f2.length()) return -1;
      if (f1.length() > f2.length()) return 1;
      return 0;
    };

    files.sort(smallest);

    System.out.println("files: " + files);

    String[] options = {"Loading", "Solving"};
    Boolean[] threading = {true, false};
    for (File file : files) {
      for (String option : options) {
        for (Boolean multi : threading) {
          System.out.println("DFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          AlgorithmWorkerThread thread = application.solve("Depth First", option, multi);
          thread.start();
          thread.join();
          System.out.println("Thread complete");

          application.saveImage("Images/Solved/Test DFS " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }
  }

  @Test
  public void DFSMediumOnly() throws GenericError, InterruptedException {
    deleteFiles(new File("Images/Solved"));
    ArrayList<File> files = getAllFiles(new File("Images"));

    //Remove anything that is not an image
    files = removeNonImages(files, false, "Medium");

    Comparator<File> smallest = (File f1, File f2) -> {
      if (f1.length() < f2.length()) return -1;
      if (f1.length() > f2.length()) return 1;
      return 0;
    };

    files.sort(smallest);

    System.out.println("files: " + files);

    String[] options = {"Loading", "Solving"};

    Boolean[] threading = {true, false};
    for (File file : files) {
      for (String option : options) {
        for (Boolean multi : threading) {
          System.out.println("DFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          AlgorithmWorkerThread thread = application.solve("Depth First", option, multi);
          thread.start();
          thread.join();
          System.out.println("Thread complete");

          application.saveImage("Images/Solved/Test DFS " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }
  }

  @Test
  public void DFSHugeOnly() throws GenericError, InterruptedException {
    ArrayList<File> files = getAllFiles(new File("Images"));

    //Remove anything that is not an image
    files = removeNonImages(files, false, "Huge");

    Comparator<File> smallest = (File f1, File f2) -> {
      if (f1.length() < f2.length()) return -1;
      if (f1.length() > f2.length()) return 1;
      return 0;
    };

    files.sort(smallest);

    System.out.println("files: " + files);

    String[] options = {"Loading", "Solving"};

    Boolean[] threading = {true, false};
    for (File file : files) {
      for (String option : options) {
        for (Boolean multi : threading) {
          System.out.println("DFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          AlgorithmWorkerThread thread = application.solve("Depth First", option, multi);
          thread.start();
          thread.join();
          System.out.println("Thread complete");

          application.saveImage("Images/Solved/Test DFS " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }
  }


  @Test
  public void testDFS() throws InterruptedException, GenericError {
    deleteFiles(new File("Images/Solved"));
    ArrayList<File> files = getAllFiles(new File("Images"));

    //Remove anything that is not an image
    files = removeNonImages(files, false, null);

    Comparator<File> smallest = (File f1, File f2) -> {
      if (f1.length() < f2.length()) return -1;
      if (f1.length() > f2.length()) return 1;
      return 0;
    };

    files.sort(smallest);

    System.out.println("files: " + files);

    String[] options = {"Loading", "Solving"};
    Boolean[] threading = {true, false};
    for (File file : files) {
      for (String option : options) {
        for (Boolean multi : threading) {
          System.out.println("DFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          AlgorithmWorkerThread thread = application.solve("Depth First", option, multi);
          thread.start();
          thread.join();
          System.out.println("Thread complete");

          application.saveImage("Images/Solved/Test DFS " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }

  }


  /**
   * Test select few images and save the results
   *
   * @throws InterruptedException
   */
  @Test
  public void testDFSSaveResults() throws InterruptedException, GenericError, IOException {
    ArrayList<String> testFiles = new ArrayList<>();
    testFiles.add("Tiny.png");
    testFiles.add("Small Imperfect.png");
    testFiles.add("Medium Imperfect.png");
    testFiles.add("Large Imperfect.png");
    testFiles.add("Huge Imperfect.png");
    testFiles.add("OneK Imperfect.png");
    testFiles.add("TwoK Imperfect.png");
    testFiles.add("FourK Imperfect.png");
    testFiles.add("SixK Imperfect.png");

    ResultTracker tracker = new ResultTracker();
    String[] options = {"Loading", "Solving"};
    Boolean[] threading = {true, false};

    for (String fileStr : testFiles) {
      File file = new File("Images/" + fileStr);
      for (String option : options) {
        for (Boolean multi : threading) {
          System.out.println("DFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          AlgorithmWorkerThread thread = application.solve("Depth First", option, multi);
          thread.start();
          thread.join();
          System.out.println("Thread complete");

          //Add these to the tracker
          String loading = null, solving = null;
          if (option.equals("Loading")) loading = "Loading";
          else if (option.equals("Solving")) solving = "Solving";
          tracker.addResult("Depth First", fileStr, thread.getMazeSize(), loading, solving, thread.getExecTime(), multi);
        }
      }
    }

    tracker.saveResult();
  }

  //TEST THE WORKER THREAD
  @Test
  public void testWorkerThread() throws InterruptedException {
    Application application = new Application();
    File image = new File("Images/TwoK Imperfect.png");
    try {
      application.parseImageFile(image);
    } catch (GenericError genericError) {
      genericError.printStackTrace();
      System.out.println("Failed to parse image");
    }

    AlgorithmWorkerThread thread = new AlgorithmWorkerThread("Depth First", "Loading", application, "test", false);
    thread.start();

    thread.join(); //Wait for the other thread to finish

    System.out.println("Thread finished");
  }

  //TEST SAVING
  @Test
  public void testSave() throws InterruptedException {
    Application application = new Application();
    File image = new File("Images/Small Imperfect.png");
    try {
      application.parseImageFile(image);
    } catch (GenericError genericError) {
      genericError.printStackTrace();
      System.out.println("Failed to parse image");
    }

    AlgorithmWorkerThread thread = new AlgorithmWorkerThread("Depth First", "Loading", application, "test", false);
    thread.start();

    thread.join(); //Wait for the other thread to finish

    System.out.println("Thread finished");

    application.saveImage("Images/Solved/Test.png");
  }

  /**
   * @param dir the directory to check
   * @return all the files in the directory
   */
  private ArrayList<File> getAllFiles(final File dir) {
    ArrayList<File> files = new ArrayList<>();
    for (final File fileEntry : dir.listFiles()) {
      if (!fileEntry.isDirectory()) files.add(fileEntry);
    }
    return files;
  }

  /**
   * Remove any files without a supported extension
   *
   * @param files arraylist of files.
   */
  private ArrayList<File> removeNonImages(ArrayList<File> files, boolean inclDeliberateInvalid, String optionParam) {
    ArrayList<File> toReturn = new ArrayList<>();
    for (File file : files) {
      if (file.getName().contains(".png") || file.getName().contains(".jpg") || file.getName().contains(".jpeg")) {
        if (!inclDeliberateInvalid && !file.getName().contains("Invalid")) {
          if (optionParam != null) {
            if (file.getName().contains(optionParam)) toReturn.add(file);
          } else toReturn.add(file);
        }
      }
    }
    return toReturn;
  }

  /**
   * Delete all the files in a given folder.
   *
   * @param file the folder
   */
  private void deleteFiles(File file) {
    System.out.println("Deleting images");
    for (File toDelete : file.listFiles()) {
      if (!toDelete.isDirectory()) toDelete.delete();
    }
  }

  /**
   * Class used for tracking and saving test results
   */
  private class ResultTracker {
    ArrayList<TestResult> results = new ArrayList<>();

    public void addResult(String algorithm, String file, double size, String onLoad, String onSolve, long execTime, boolean multiThreading) {
      results.add(new TestResult(algorithm, file, size, onLoad, onSolve, execTime, multiThreading));
    }

    /**
     * Create a CSV file and save the results
     */
    public void saveResult() throws IOException {
      String time = new Timestamp(new Date().getTime()).toString();
      File newCSV = new File("Test Results/Test Results (" + time + ").csv");
      FileWriter csv = new FileWriter(newCSV);

      //Add the headers
      csv.append("File, Algorithm, Size, Load, Solve, execTime, Multi Threading\n");

      //Add each of the results
      for (TestResult test: results) csv.append(test.printCSV());

      csv.flush();
      csv.close();
    }
  }

  /**
   * Holds each individual result
   */
  private class TestResult {
    //todo multi threading should be treated as two different files
    String fileName, onLoad, onSolve, algorithm;
    long execTime;
    boolean multiThreading;
    double size;

    public TestResult(String algorithm, String fileName, double size, String onLoad, String onSolve, long execTime, boolean multiThreading) {
      this.fileName = fileName;
      this.onLoad = onLoad;
      this.onSolve = onSolve;
      this.execTime = execTime;
      this.multiThreading = multiThreading;
      this.size = size;
      this.algorithm = algorithm;
    }

    /**
     * @return return the data in csv format
     */
    public String printCSV() {
      return fileName + ", " + algorithm + ", " + size + ", " + onLoad + ", " + onSolve + ", " + execTime + ", " + multiThreading + "\n";
    }
  }
}
