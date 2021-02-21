package tests;

import application.Application;
import image.ImageFile;
import dispatcher.Dispatcher;
import utility.Exceptions.GenericError;
import utility.Exceptions.ParserFailure;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static tests.TestUtilities.deleteFiles;
import static tests.TestUtilities.getAllFiles;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * This class contains tests used by the parser
 */
public class ParserTests {
  //TEST THE PARSER
  @Test
  public void testDFSParserTiny() {
    Application application = new Application();
    try {
      application.parseImageFile(new File("Images/Tiny.png"));
    } catch (GenericError genericError) {
      genericError.printStackTrace();
    }


    File dfs = new File("Programs/Working Algorithms/DFS.solver");

    Dispatcher dispatcher = new Dispatcher(dfs, application.getImageFile());
    dispatcher.solve();

    ImageFile imageFile = dispatcher.fillPlayerPath();

    imageFile.saveImage("Images/Solved/DFS Custom Algorithm.png");
    System.out.println("Saved image");
  }

  @Test
  public void testDFSParserSmall() {
    Application application = new Application();
    try {
      application.parseImageFile(new File("Images/Small Imperfect.png"));
    } catch (GenericError genericError) {
      genericError.printStackTrace();
    }

    File dfs = new File("Programs/Working Algorithms/DFS.solver");
    Dispatcher dispatcher = new Dispatcher(dfs, application.getImageFile());
    dispatcher.solve();

    ImageFile imageFile = dispatcher.fillPlayerPath();

    imageFile.saveImage("Images/Solved/DFS Custom Algorithm.png");
    System.out.println("Saved image");
  }

  @Test
  public void testBFSParserTiny() {
    Application application = new Application();
    try {
      application.parseImageFile(new File("Images/Tiny.png"));
    } catch (GenericError genericError) {
      genericError.printStackTrace();
    }

    File dfs = new File("Programs/Working Algorithms/BFS.solver");
    Dispatcher dispatcher = new Dispatcher(dfs, application.getImageFile());
    dispatcher.solve();

    ImageFile imageFile = dispatcher.fillPlayerPath();

    imageFile.saveImage("Images/Solved/BFS Custom Algorithm.png");
    System.out.println("Saved image");
  }

  @Test
  public void testBFSParserSmall() {
    Application application = new Application();
    try {
      application.parseImageFile(new File("Images/Small Imperfect.png"));
    } catch (GenericError genericError) {
      genericError.printStackTrace();
    }

    File dfs = new File("Programs/Working Algorithms/BFS.solver");
    Dispatcher dispatcher = new Dispatcher(dfs, application.getImageFile());
    dispatcher.solve();

    ImageFile imageFile = dispatcher.fillPlayerPath();

    imageFile.saveImage("Images/Solved/BFS Small Imperfect Custom Algorithm.png");
    System.out.println("Saved image");
  }

  @Test
  public void testDijkstraParserTiny() {
    Application application = new Application();
    try {
      application.parseImageFile(new File("Images/Tiny.png"));
    } catch (GenericError genericError) {
      genericError.printStackTrace();
    }

    File dfs = new File("Programs/Working Algorithms/Dijkstra.solver");
    Dispatcher dispatcher = new Dispatcher(dfs, application.getImageFile());
    dispatcher.solve();

    ImageFile imageFile = dispatcher.fillPlayerPath();

    imageFile.saveImage("Images/Solved/Dijkstra Custom Algorithm.png");
    System.out.println("Saved image");
  }

  @Test
  public void testDijkstraParserSmall() {
    Application application = new Application();
    try {
      application.parseImageFile(new File("Images/Small Imperfect.png"));
    } catch (GenericError genericError) {
      genericError.printStackTrace();
    }

    File dfs = new File("Programs/Working Algorithms/Dijkstra.solver");
    Dispatcher dispatcher = new Dispatcher(dfs, application.getImageFile());
    dispatcher.solve();

    ImageFile imageFile = dispatcher.fillPlayerPath();

    imageFile.saveImage("Images/Solved/Dijkstra Small Imperfect Custom Algorithm.png");
    System.out.println("Saved image");
  }

  @Test
  public void testAStarParserTiny() {

    Application application = new Application();
    try {
      application.parseImageFile(new File("Images/Tiny.png"));
    } catch (GenericError genericError) {
      genericError.printStackTrace();
    }


    File dfs = new File("Programs/Working Algorithms/AStar.solver");
    Dispatcher dispatcher = new Dispatcher(dfs, application.getImageFile());
    dispatcher.solve();

    ImageFile imageFile = dispatcher.fillPlayerPath();

    imageFile.saveImage("Images/Solved/AStar Custom Algorithm.png");
    System.out.println("Saved image");
  }

  @Test
  public void testAStarParserSmall() {
    Application application = new Application();
    try {
      application.parseImageFile(new File("Images/Small Imperfect.png"));
    } catch (GenericError genericError) {
      genericError.printStackTrace();
    }

    File dfs = new File("Programs/Working Algorithms/AStar.solver");
    Dispatcher dispatcher = new Dispatcher(dfs, application.getImageFile());
    dispatcher.solve();

    ImageFile imageFile = dispatcher.fillPlayerPath();

    imageFile.saveImage("Images/Solved/AStar Small Imperfect Custom Algorithm.png");
    System.out.println("Saved image");
  }

  @Test
  public void testAllParsersTiny() {
    deleteFiles(new File("Images/Solved"));
    ArrayList<File> files = getAllFiles(new File("Programs/Working Algorithms"));

    for (File file : files) {
      System.out.println("File: " + file);

      Application application = new Application();
      try {
        application.parseImageFile(new File("Images/Tiny.png"));
      } catch (GenericError genericError) {
        genericError.printStackTrace();
      }

      Dispatcher dispatcher = new Dispatcher(file, application.getImageFile());
      dispatcher.solve();

      ImageFile imageFile = dispatcher.fillPlayerPath();

      imageFile.saveImage("Images/Solved/" + file.getName() + " Tiny Custom Algorithm.png");
      System.out.println("Saved image");
    }
  }

  @Test
  public void testAllParsersSmall() {
    deleteFiles(new File("Images/Solved"));
    ArrayList<File> files = getAllFiles(new File("Programs/Working Algorithms"));

    for (File file : files) {
      System.out.println("File: " + file);

      Application application = new Application();
      try {
        application.parseImageFile(new File("Images/Small Imperfect2.png"));
      } catch (GenericError genericError) {
        genericError.printStackTrace();
      }

      Dispatcher dispatcher = new Dispatcher(file, application.getImageFile());
      dispatcher.solve();

      ImageFile imageFile = dispatcher.fillPlayerPath();

      imageFile.saveImage("Images/Solved/" + file.getName() + " Small Imperfect 2 Custom Algorithm.png");
      System.out.println("Saved image");
    }
  }
//
//  @Test
//  public void testGame() throws InterruptedException, GenericError {
//    final Parser parserOne, parserTwo;
//    Game game;
//    Application application;
//
//    application = new Application();
//    application.parseImageFile(new File("Images/Tiny.png"));
//
//    parserOne = new Parser(new File("Programs/Working Algorithms/AStar No Print.solver"));
//    parserTwo = new Parser(new File("Programs/Working Algorithms/AStar No Print.solver"));
//
//    parserOne.compile();
//    parserTwo.compile();
//
//    game = new Game(parserOne, parserTwo, application.getImageFile());
//
//    game.loadNodes(null);
//
//    //Wait five seconds for the thread to finish
//    TimeUnit.SECONDS.sleep(5);
//
//    game.startPlayers(1);
//
//    System.out.println("done");
//
////    final Parser[] pOne = new Parser[1];
////    final Parser[] pTwo = new Parser[1];
////    final Game[] game = new Game[1];
////    final Application[] application = new Application[1];
////
////    Thread runner = new Thread() {
////      @Override
////      public void run() {
////        application[0] = new Application();
////        try {
////          application[0].parseImageFile(new File("Images/Medium.png"));
////        } catch (GenericError genericError) {
////          genericError.printStackTrace();
////        }
////        application[0].scanEntireMaze();
////
////
////        pOne[0] = new Parser(new File("Programs/Working Algorithms/AStar No Print.solver"));
////        pTwo[0] = new Parser(new File("Programs/Working Algorithms/AStar No Print.solver"));
////
////
////        pOne[0].setMazeHandler(new Handler(application[0].getImageProcessor()));
////        pTwo[0].setMazeHandler(new Handler(application[0].getImageProcessor()));
////
////        pOne[0].compile();
////        pTwo[0].compile();
////
////        game[0] = new Game(pOne[0], pTwo[0], application[0].getImageFile());
////
////        game[0].playerOne.setImageProcessor(new ImageProcessor(application[0].getImageProcessor()));
////        game[0].playerTwo.setImageProcessor(new ImageProcessor(application[0].getImageProcessor()));
////
////
////        game[0].startPlayers(25);
////      }
////    };
////
////    runner.start();
////    runner.join();
////    System.out.println("Done");
//  }

  /**
   * Run all of the tests in the valid tests folder
   */
  @Test
  public void testValid() throws GenericError {
    File invalidDir = new File("Programs/Valid Tests/");
    File imageFile = new File("Images/Tiny.png");
    ArrayList<File> directories = new ArrayList<>();
    directories.add(invalidDir);

    while (!directories.isEmpty()) {
      File directory = directories.remove(0);
      for (File file : directory.listFiles()) {
        if (file.isDirectory()) directories.add(file);
        else if (file.getName().contains(".solver")) {
          System.out.println("File: " + file);

          Application application = new Application();
          application.parseImageFile(imageFile);

          Dispatcher dispatcher = new Dispatcher(file, application.getImageFile());
          dispatcher.solve();

          System.out.print("\n\n\n");
        }
      }
    }
  }

  /**
   * Launch a UI file chooser to
   */
  @Test
  public void runAnyParser() throws GenericError {
    File image = new File("Images/Tiny.png");
    File parser = new File("Programs/Invalid Tests/Variables/Invalid Values/Numbers/Number Equality.solver");
    System.out.println(parser);

    Application application = new Application();
    application.parseImageFile(image);

    Dispatcher dispatcher = new Dispatcher(parser, application.getImageFile());
    dispatcher.solve();

    ImageFile imageFile = dispatcher.fillPlayerPath();

    imageFile.saveImage("Images/Solved/" + image.getName() + " Small Imperfect 2 Custom Algorithm" + parser.getName() + ".png");
    System.out.println("Saved image");
  }

  @Test
  public void testInvalid() throws GenericError {
    File invalidDir = new File("Programs/Invalid Tests/");
    File imageFile = new File("Images/Tiny.png");
    ArrayList<File> directories = new ArrayList<>();
    directories.add(invalidDir);

    while (!directories.isEmpty()) {
      File directory = directories.remove(0);
      for (File file : directory.listFiles()) {
        if (file.isDirectory()) directories.add(file);
        else if (file.getName().contains(".solver")) {
          System.out.println("File: " + file);

          Application application = new Application();
          application.parseImageFile(imageFile);

          Dispatcher dispatcher = new Dispatcher(file, application.getImageFile());

          Exception exception = assertThrows(ParserFailure.class, dispatcher::solve);

          String expectedMessage = "FAIL";
          String actualMessage = exception.getMessage();
          System.out.println(exception.getMessage());
          assertTrue(actualMessage.contains(expectedMessage));
          System.out.print("\n\n\n");
        }
      }
    }
  }
}
