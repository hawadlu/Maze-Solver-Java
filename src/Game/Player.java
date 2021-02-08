package Game;

import Algorithm.SolveAlgorithm;
import GUI.CustomPanels.PlayerPanel;
import Image.ImageFile;
import Utility.Location;
import Utility.AlgorithmDispatcher;
import parser.Handler;
import parser.Parser;
import Utility.Node;
import Utility.PathMaker;
import Image.ImageProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class Player {
  PlayerPanel panel;
  String playerName;
  AtomicBoolean done = new AtomicBoolean(false);
  Handler handler;
  private Parser customAlgo;
  ImageProcessor imageProcessor;
  AlgorithmDispatcher dispatcher;
  SolveAlgorithm solve;
  String type;

  /**
   * Create a player with a custom algorithm.
   *
   * Creates the parser object and performs an image scan.
   *
   * @param playerName the player name.
   * @param customAlgo the file containing the algorithm to be parsed.
   * @param dispatcher the Algorithm dispatcher.
   */
  public Player(String playerName, File customAlgo, AlgorithmDispatcher dispatcher) {
    this.playerName = playerName;
    this.customAlgo = new Parser(customAlgo, this);
    this.dispatcher = dispatcher;

    //Scan the image
    this.imageProcessor = new ImageProcessor();
    imageProcessor.scanAll(dispatcher.getImageFile());
  }

  public Handler getHandler() {
    return handler;
  }

  /**
   * Initialise the handler object.
   */
  public void initialiseHandler() {
    this.handler = new Handler(this);
  }

  /**
   * @param maxSize the max size that any panels in the game can be displayed at
   */
  public Player(Dimension maxSize, String playerName, AlgorithmDispatcher dispatcher) {
    this.playerName = playerName;
    this.panel = new PlayerPanel(maxSize, this);
    this.dispatcher = dispatcher;
  }


  /**
   * Create a player object
   * @param playerName the name of the player.
   * @param type is this running an algorithm or a game.
   * @param dispatcher the dispatcher object.
   */
  public Player(String playerName, String type, AlgorithmDispatcher dispatcher) {
    this.playerName = playerName;
    this.type = type;
    this.dispatcher = dispatcher;

    //Create the image processor
    this.imageProcessor = new ImageProcessor();

    //todo make the dimension non fixed
    this.panel = new PlayerPanel(new Dimension(500,500), this);

    //make the algorithm solve screen
    if (this.type.equals("Algorithm")) {
      panel.setAlgoSolveScreen();
    }
  }

  public void createSolveImagePanel() {
    panel.initSolvePanel();
  }

  private void updateSolveImage(ImageFile displayImage) {
    panel.updateImage(displayImage);
  }

  /**
   * @param node the node to draw the image from
   */
  public void update(Node node) {

    //Create a duplicate image file
    ImageFile newImage = new ImageFile(dispatcher.getImageFile());

    //Create a path from the current node
    newImage.fillNodePath(PathMaker.generatePathArraylist(node), true);
    if (panel != null) updateSolveImage(newImage);
  }

  /**
   * @return the JPanel that is the visual representation of this players progress
   */
  public PlayerPanel getPanel() {
    return panel;
  }

  /**
   * solve the maze
   *
   * @param delay the requested delay
   */
  public void solve(int delay) {
    System.out.println(playerName + " has started the solve");

    //Make this display an image panel
    String algorithm = "";
    if (panel != null) {
      panel.setPlayer(this);
      createSolveImagePanel();
      algorithm = panel.getAlgorithm();
    }
  }

  /**
   * Make the panel display a done message.
   * @param message, the message to display
   */
  public void makeDoneDisplay(String message) {
    //Make the completed image
    ImageFile newImage = new ImageFile(dispatcher.getImageFile());

    //Create a path from the current node
    //newImage.fillNodePath(PathMaker.generatePathArraylist(currentNode), true);

    if (panel != null) panel.markDone(message, newImage);
  }

  /**
   * Mark this player as done
   */
  public void markDone() {
    done.set(true);
  }

  @Override
  public String toString() {
    return playerName + " Done: " + done;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Player player = (Player) o;
    if (panel != null) return panel.equals(player.panel) && playerName.equals(player.playerName);
    else return playerName.equals(player.playerName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(panel, playerName);
  }

  /**
   * Start executing the parser
   */
  public void startParserExec(int delay) {
    //Create the new maze handler object if necessary
    customAlgo.execute(delay);
  }

  public ArrayList<Location> getMazeExits() {
    return imageProcessor.getExits();
  }

  public ConcurrentHashMap<Location, Node> getNodes() {
    return imageProcessor.getNodes();
  }

  public ImageFile getImageFile() {
    return dispatcher.getImageFile();
  }

  /**
   * Get the image processor.
   * @return the image processor object.
   */
  public ImageProcessor getImageProcessor() {
    return imageProcessor;
  }

  /**
   * Set the image processor.
   * @param imageProcessor the new image processor.
   */
  public void setImageProcessor(ImageProcessor imageProcessor) {
    this.imageProcessor = imageProcessor;
  }

  /**
   * Get the custom algorithm.
   * @return the Parser object.
   */
  public Parser getCustomAlgo() {
    return customAlgo;
  }

  /**
   * Set the custom algorithm.
   * @param customAlgo the new Parser object.
   */
  public void setCustomAlgo(Parser customAlgo) {
    this.customAlgo = customAlgo;
  }

  /**
   * Get the screen currently being displayed in the playerPanel.
   * @return JComponent containing the current display.
   */
  public JPanel getScreen() {
    return panel;
  }

  /**
   * @return the player name
   */
  public String getName() {
    return playerName;
  }

  /**
   * Take the various algorithm parameters and solve.
   * @param algorithm the algorithm to use.
   * @param params the parameters related to the algorithm.
   * @param multiThread Should this be multithreaded.
   */
  public void solve(String algorithm, String params, Boolean multiThread) {
    System.out.println("Player " + playerName + " starting solve");
    solve = new SolveAlgorithm(0, this);

    //update the display of this panel
    if (!dispatcher.isLive()) panel.makeAlgoWorkingScreen();

    //Check if the nodes have already been scanned
    if (imageProcessor.getNodes().isEmpty()) solve.scan(params);

    //If the param is set to loading set the scanAll field in the SolveAlgorithm to true
    if (params.equals("Loading")) solve.scanAll = true;

    //Start the solve
    solve.solve(algorithm, multiThread);

    //Update the display
    panel.makeAlgoSolvedScreen(dispatcher.getImageFile());
  }

  /**
   * Take an algorithm and call the solve method.
   *
   * Assume that the program will not be multithreaded and
   * all of the nodes will be scanned first.
   *
   * @param algorithm the algorithm to use.
   */
  public void solve(String algorithm) {
    System.out.println("Player " + playerName + " starting solve");
    solve = new SolveAlgorithm(0, this);

    if (this.imageProcessor.getNodes().size() == 0) solve.scan("Loading");

    solve.scanAll = true;

    //Start the solve
    solve.solve(algorithm, false);
  }

  /**
   * Solve the requested algorithm.
   *
   * If the custom algorithm is not null use it.
   * Otherwise get the predefined algorithm from the panel.
   */
  public void solve() {
    if (customAlgo != null) {
      customAlgo.compile();
      customAlgo.execute();
    } else {
      solve(panel.getAlgorithm());
    }
  }

  /**
   * Get the exits from the image processor.
   * @return an arraylist of the exit locations.
   */
  public ArrayList<Location> getExits() {
    return imageProcessor.getExits();
  }

  /**
   * Call the scanAll() method in the image processor.
   */
  public void scanAll() {
    imageProcessor.scanAll(dispatcher.getImageFile());
  }

  /**
   * Call the find exits method in the image processor.
   */
  public void findExits() {
    imageProcessor.findExits(dispatcher.getImageFile());
  }

  /**
   * Call method to scan part of the maze.
   * @param parent the node to find neighbours of.
   * @param multiThreading boolean to indicate if the program is currently multithreading.
   */
  public void scanPart(Node parent, Boolean multiThreading) {
    imageProcessor.scanPart(parent, multiThreading, dispatcher.getImageFile());
  }

  /**
   * Get the execution time from the solveAlgorithm.
   * @return a long representing the execution time.
   */
  public long getExecTime() {
    return solve.execTime;
  }

  /**
   * Get the image from the dispatcher, copy it and fill in the path.
   * @return the filled in ImageFile.
   */
  public ImageFile fillPath() {
    ImageFile image = new ImageFile(dispatcher.getImageFile());

    //If this was solved using a parser, use the handler to get the required solve info.
    if (customAlgo != null) {
      image.fillNodePath(PathMaker.generatePathArraylist(handler.getLastNode()), true);
      return image;
    }

    //todo implement for inbuilt algorithms
    return null;
  }

  /**
   * Check if this is running in live mode.
   * @return a boolean to indicate if live or not.
   */
  public boolean isLive() {
    return dispatcher.isLive();
  }

  /**
   * Tell the algorithm dispatcher to reset the screen.
   * @param tab the tab to set it to.
   */
  public void makeLoadScreen(String tab) {
    dispatcher.makeLoadScreen(tab);
  }

  /**
   * Tell the panel to turn itself into a game screen.
   */
  public void setGameScreen() {
    panel.makeGameScreen();
  }
}
