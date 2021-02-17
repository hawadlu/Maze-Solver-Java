package Tests;

import Application.Application;
import Game.Player;
import Utility.Exceptions.GenericError;
import Dispatcher.Dispatcher;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import static Tests.TestUtilities.*;


/**
 * Methods to test the solvers
 */
public class Solvers {
  //TESTS OF THE DEPTH FIRST SEARCH
  @Test
  public void DFSTinyOnly() throws GenericError, InterruptedException {
    String algorithm = "Depth First";


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
          System.out.println("BFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }

  }

  @Test
  public void DFSTwoKOnly() throws GenericError, InterruptedException {
    String algorithm = "Depth First";


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

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }
  }

  @Test
  public void DFSUnevenOnly() throws GenericError, InterruptedException {
    String algorithm = "Depth First";


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

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }
  }

  @Test
  public void DFSSmallOnly() throws GenericError, InterruptedException {
    String algorithm = "Depth First";


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

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }
  }

  @Test
  public void DFSMediumOnly() throws GenericError, InterruptedException {
    String algorithm = "Depth First";


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

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }
  }

  @Test
  public void DFSHugeOnly() throws GenericError, InterruptedException {
    String algorithm = "Depth First";


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

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }
  }

  @Test
  public void testDFS() throws InterruptedException, GenericError {
    String algorithm = "Depth First";


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

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
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
    String algorithm = "Depth First";


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

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          //Add these to the tracker
          String loading = null, solving = null;
          if (option.equals("Loading")) loading = "Loading";
          else if (option.equals("Solving")) solving = "Solving";
          tracker.addResult(algorithm, fileStr, dispatcher.getMazeSize(), loading, solving, dispatcher.getExecTime(), multi, "Success");
        }
      }
    }

    tracker.saveResult();
  }


  //TESTS OF THE BREADTH FIRST SEARCH
  @Test
  public void BFSTinyOnly() throws GenericError, InterruptedException {
    String algorithm = "Breadth First";


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
          System.out.println("BFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }

  }

  @Test
  public void BFSTwoKOnly() throws GenericError, InterruptedException {
    String algorithm = "Breadth First";


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
          System.out.println("BFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }
  }

  @Test
  public void BFSUnevenOnly() throws GenericError, InterruptedException {
    String algorithm = "Breadth First";


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
          System.out.println("BFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }
  }

  @Test
  public void BFSSmallOnly() throws GenericError, InterruptedException {
    String algorithm = "Breadth First";

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
          System.out.println("BFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }
  }

  @Test
  public void BFSMediumOnly() throws GenericError, InterruptedException {
    String algorithm = "Breadth First";

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
          System.out.println("BFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }
  }

  @Test
  public void BFSHugeOnly() throws GenericError, InterruptedException {
    String algorithm = "Breadth First";


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
          System.out.println("BFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }
  }

  @Test
  public void testBFS() throws InterruptedException, GenericError {
    String algorithm = "Breadth First";


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
          System.out.println("BFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
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
  public void testBFSSaveResults() throws InterruptedException, GenericError, IOException {
    String algorithm = "Breadth First";


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
          System.out.println("BFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          //Add these to the tracker
          String loading = null, solving = null;
          if (option.equals("Loading")) loading = "Loading";
          else if (option.equals("Solving")) solving = "Solving";
          tracker.addResult(algorithm, fileStr, dispatcher.getMazeSize(), loading, solving, dispatcher.getExecTime(), multi, "Success");
        }
      }
    }

    tracker.saveResult();
  }


  //TESTS OF THE DIJKSTRA SEARCH
  @Test
  public void DijkstraTinyOnly() throws GenericError, InterruptedException {
    String algorithm = "Dijkstra";


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
          System.out.println("BFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }

  }

  @Test
  public void DijkstraTwoKOnly() throws GenericError, InterruptedException {
    String algorithm = "Dijkstra";


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
          System.out.println("BFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }
  }

  @Test
  public void DijkstraUnevenOnly() throws GenericError, InterruptedException {
    String algorithm = "Dijkstra";


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
          System.out.println("BFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }
  }

  @Test
  public void DijkstraSmallOnly() throws GenericError, InterruptedException {
    String algorithm = "Dijkstra";

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
          System.out.println("BFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }
  }

  @Test
  public void DijkstraMediumOnly() throws GenericError, InterruptedException {
    String algorithm = "Dijkstra";

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
          System.out.println("BFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }
  }

  @Test
  public void DijkstraHugeOnly() throws GenericError, InterruptedException {
    String algorithm = "Dijkstra";


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
          System.out.println("BFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }
  }

  @Test
  public void testDijkstra() throws InterruptedException, GenericError {
    String algorithm = "Dijkstra";


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
          System.out.println("BFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
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
  public void testDijkstraSaveResults() throws InterruptedException, GenericError, IOException {
    String algorithm = "Dijkstra";


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
          System.out.println(algorithm + " " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          //Add these to the tracker
          String loading = null, solving = null;
          if (option.equals("Loading")) loading = "Loading";
          else if (option.equals("Solving")) solving = "Solving";
          tracker.addResult(algorithm, fileStr, dispatcher.getMazeSize(), loading, solving, dispatcher.getExecTime(), multi, "Success");
        }
      }
    }

    tracker.saveResult();
  }


  //TESTS OF THE ASTAR SEARCH
  @Test
  public void AStarTinyOnly() throws GenericError, InterruptedException {
    String algorithm = "AStar";


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
          System.out.println("BFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }

  }

  @Test
  public void AStarTwoKOnly() throws GenericError, InterruptedException {
    String algorithm = "AStar";


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
          System.out.println("BFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }
  }

  @Test
  public void AStarUnevenOnly() throws GenericError, InterruptedException {
    String algorithm = "AStar";


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
          System.out.println("BFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }
  }

  @Test
  public void AStarSmallOnly() throws GenericError, InterruptedException {
    String algorithm = "AStar";

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
          System.out.println("BFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }
  }

  @Test
  public void AStarMediumOnly() throws GenericError, InterruptedException {
    String algorithm = "AStar";

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
          System.out.println("BFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }
  }

  @Test
  public void AStarHugeOnly() throws GenericError, InterruptedException {
    String algorithm = "AStar";


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
          System.out.println("BFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
          System.out.println("Complete");
        }
      }
    }
  }

  @Test
  public void testAStar() throws InterruptedException, GenericError {
    String algorithm = "AStar";


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
          System.out.println("BFS solve " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          dispatcher.getImageFile().saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
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
  public void testAStarSaveResults() throws InterruptedException, GenericError, IOException {
    String algorithm = "AStar";


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
          System.out.println(algorithm + " " + file.getName() + " " + option);

          Application application = new Application();
          application.parseImageFile(file);

          Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
          dispatcher.solve(algorithm, option, multi);

          System.out.println("Thread complete");

          //Add these to the tracker
          String loading = null, solving = null;
          if (option.equals("Loading")) loading = "Loading";
          else if (option.equals("Solving")) solving = "Solving";
          tracker.addResult(algorithm, fileStr, dispatcher.getMazeSize(), loading, solving, dispatcher.getExecTime(), multi, "Success");
        }
      }
    }

    tracker.saveResult();
  }


  //TEST ALL OF THE ALGORITHMS

  /**
   * Test select few images and save the results
   *
   * @throws InterruptedException
   */
  @Test
  public void testAllSolversSaveResults() throws InterruptedException, GenericError, IOException {
    String[] algorithmArr = new String[]{"Depth First", "Breadth First", "Dijkstra", "AStar"};

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

    ResultTracker tracker = new ResultTracker();
    String[] options = {"Loading", "Solving"};
    Boolean[] threading = {true, false};

    for (File file : files) {
      for (String option : options) {
        //Check if the maze is too big for this option
        if (option.equals("Loading") && (file.getName().contains("Eight") || file.getName().contains("Ten")))
          ; //Do nothing
        else {
          for (String algorithm : algorithmArr) {
            if ((algorithm.equals("Depth First") || algorithm.equals("Breadth First")) && (file.getName().contains("Eight") || file.getName().contains("Ten")))
              ; //Do nothing
            else {
              for (Boolean multi : threading) {
                System.out.println(algorithm + " " + file.getName() + " " + option);

                Application application = new Application();
                application.parseImageFile(file);

                Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
                dispatcher.solve(algorithm, option, multi);

                System.out.println("Thread complete");

                //Add these to the tracker
                String loading = null, solving = null;
                if (option.equals("Loading")) loading = "Loading";
                else if (option.equals("Solving")) solving = "Solving";
                tracker.addResult(algorithm, file.getName(), dispatcher.getMazeSize(), loading, solving, dispatcher.getExecTime(), multi, "Success");
              }
            }
          }
        }
      }
    }

    tracker.saveResult();
  }

  @Test
  public void testGame() throws GenericError {
    File image = new File("Images/Tiny.png");
    Application application = new Application();
    application.parseImageFile(image);

    Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 2);

    dispatcher.makeGameScreen();

    //fixme both player exit lists have the same objects

    Player playerOne = dispatcher.getPlayers().get(0);
    Player playerTwo = dispatcher.getPlayers().get(1);

    dispatcher.setLive(true);

    //Create a new thread for each of the players
    Thread pOne = new Thread(() -> {
      playerOne.solve();
    });

    Thread pTwo = new Thread(() -> {
      playerTwo.solve();
    });

    pOne.start();
    pTwo.start();

    System.out.println("Done");
  }

}
