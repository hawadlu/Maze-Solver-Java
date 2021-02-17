package Tests;

import Application.Application;
import Utility.Exceptions.GenericError;
import Dispatcher.Dispatcher;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import static Tests.TestUtilities.*;

public class ArticulationPoints {
  //TEST ARTICULATION ALGORITHM
  @Test
  public void ArticulationTinyOnly() throws GenericError, InterruptedException {
    String algorithm = "Articulation";

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
    for (File file : files) {
      Application application = new Application();
      application.parseImageFile(file);

      Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
      dispatcher.solve(algorithm);

      System.out.println("Thread complete");

      dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
      System.out.println("Complete");
    }
  }

  @Test
  public void ArticulationTwoKOnly() throws GenericError, InterruptedException {
    String algorithm = "Articulation";

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
    for (File file : files) {
      System.out.println("Prims solve " + file.getName());

      Application application = new Application();
      application.parseImageFile(file);

      Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
      dispatcher.solve(algorithm);

      System.out.println("Thread complete");

      dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
      System.out.println("Complete");
    }
  }

  @Test
  public void ArticulationUnevenOnly() throws GenericError, InterruptedException {
    String algorithm = "Articulation";

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
    for (File file : files) {
      System.out.println("Prims solve " + file.getName());

      Application application = new Application();
      application.parseImageFile(file);

      Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
      dispatcher.solve(algorithm);

      System.out.println("Thread complete");

      dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
      System.out.println("Complete");
    }
  }

  @Test
  public void ArticulationSmallOnly() throws GenericError, InterruptedException {
    String algorithm = "Articulation";

    //Delete all the files in the solved folder
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
    for (File file : files) {
      System.out.println("Prims solve " + file.getName());

      Application application = new Application();
      application.parseImageFile(file);

      Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
      dispatcher.solve(algorithm);

      System.out.println("Thread complete");

      dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
      System.out.println("Complete");
    }
  }

  @Test
  public void ArticulationMediumOnly() throws GenericError, InterruptedException {
    String algorithm = "Articulation";

    //Delete all the files in the solved folder
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
    for (File file : files) {
      System.out.println("Prims solve " + file.getName());

      Application application = new Application();
      application.parseImageFile(file);

      Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
      dispatcher.solve(algorithm);

      System.out.println("Thread complete");

      dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
      System.out.println("Complete");
    }
  }

  @Test
  public void ArticulationHugeOnly() throws GenericError, InterruptedException {
    String algorithm = "Articulation";

    //Delete all the files in the solved folder
    deleteFiles(new File("Images/Solved"));

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
    for (File file : files) {
      System.out.println("Prims solve " + file.getName());

      Application application = new Application();
      application.parseImageFile(file);

      Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
      dispatcher.solve(algorithm);

      System.out.println("Thread complete");

      dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
      System.out.println("Complete");
    }
  }

  @Test
  public void testArticulation() throws InterruptedException, GenericError {
    String algorithm = "Articulation";

    //Delete all the files in the solved folder
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
    for (File file : files) {
      System.out.println("Prims solve " + file.getName());

      Application application = new Application();
      application.parseImageFile(file);

      Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
      dispatcher.solve(algorithm);

      System.out.println("Thread complete");

      dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
      System.out.println("Complete");
    }

  }

  /**
   * Test select few images and save the results
   *
   * @throws InterruptedException
   */
  @Test
  public void testArticulationSaveResults() throws InterruptedException, GenericError, IOException {
    String algorithm = "Articulation";


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
    ;

    for (String fileStr : testFiles) {
      File file = new File("Images/" + fileStr);
      System.out.println(algorithm + " " + file.getName());

      Application application = new Application();
      application.parseImageFile(file);

      Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
      dispatcher.solve(algorithm);

      System.out.println("Thread complete");

      //Add these to the tracker
      String loading = null, solving = null;
      tracker.addResult(algorithm, fileStr, dispatcher.getMazeSize(), loading, solving, dispatcher.getExecTime(), false, "Success");
    }

    tracker.saveResult();
  }
}
