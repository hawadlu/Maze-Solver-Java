package game;

import algorithm.SolveAlgorithm;
import dispatcher.Dispatcher;
import gui.CustomPanels.PlayerPanel;
import image.ImageFile;
import server.Requests;
import utility.*;
import parser.Handler;
import parser.Parser;
import image.ImageProcessor;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class represents a single player.
 * It contains methods to start solving and update
 * the gui.
 *
 * It also contains methods to send and receive updates
 * from a server while solving in race mode.
 */
public class Player {
  PlayerPanel panel;
  String playerName;
  boolean done = false;
  Handler handler;
  private Parser customAlgo;
  ImageProcessor imageProcessor;
  Dispatcher dispatcher;
  SolveAlgorithm solve;
  String type; //Algorithm or Game
  Logger logger = new Logger();
  boolean online = false;
  boolean local = false;
  boolean hasOpponent = false;
  int delay;

  /**
   * Create a player with a custom algorithm.
   *
   * Creates the parser object and performs an image scan.
   *
   * @param playerName the player name.
   * @param customAlgo the file containing the algorithm to be parsed.
   * @param dispatcher the Algorithm dispatcher.
   */
  public Player(String playerName, File customAlgo, Dispatcher dispatcher) {
    this.playerName = playerName;
    this.customAlgo = new Parser(customAlgo, this);
    this.dispatcher = dispatcher;

    //Scan the image
    this.imageProcessor = new ImageProcessor();
    imageProcessor.scanAll(dispatcher.getImageFile());
  }

  /**
   * Create a new player object.
   *
   * @param maxSize the max size that any panels in the game can be displayed at
   * @param dispatcher the dispatcher object being used.
   * @param playerName the name of the player.
   */
  public Player(Dimension maxSize, String playerName, Dispatcher dispatcher) {
    this.playerName = playerName;
    this.panel = new PlayerPanel(maxSize, this);
    this.dispatcher = dispatcher;
  }

  /**
   * Create a player that is updated by the server.
   *
   * @param dispatcher the dispatcher that is being used.
   * @param playerName the name of the player.
   * @param online boolean to indicate if the game is online.
   * @param type is this a Game or Algorithm
   */
  public Player(String playerName, String type, Dispatcher dispatcher, boolean online) {
    this.playerName = playerName;
    this.type = type;
    this.dispatcher = dispatcher;

    //Create the image processor
    this.imageProcessor = new ImageProcessor();

    this.panel = new PlayerPanel(new Dimension(500,500), this);

    //make the algorithm solve screen
    if (this.type.equals("algorithm") && dispatcher.getImageFile() != null) {
      panel.setAlgoSolveScreen();
    }

    this.online = online;
  }

  /**
   * Create a player object
   * @param playerName the name of the player.
   * @param type is this running an algorithm or a game.
   * @param dispatcher the dispatcher object.
   */
  public Player(String playerName, String type, Dispatcher dispatcher) {
    this.playerName = playerName;
    this.type = type;
    this.dispatcher = dispatcher;

    //Create the image processor
    this.imageProcessor = new ImageProcessor();

    this.panel = new PlayerPanel(new Dimension(500,500), this);

    //make the algorithm solve screen
    if (this.type.equals("algorithm") && dispatcher.getImageFile() != null) {
      panel.setAlgoSolveScreen();
    }
  }

  /**
   * @return the handler object being used by this player.
   */
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
   * Create the panel that shows the solved image
   */
  public void createSolveImagePanel() {
    panel.initSolvePanel();
  }

  /**
   * @param node the node to draw the image from
   */
  public void update(Node node) {

    //Create a duplicate image file
    ImageFile newImage = new ImageFile(dispatcher.getImageFile());

    //Create a path from the current node
    newImage.fillNodePath(PathMaker.generatePathArraylist(node), true);
    if (panel != null) {
      panel.updateImage(newImage);
    }

    //send the image to the server if necessary
    if (local && online) {
      dispatcher.sendMessage(PathMaker.generatePathLocationArraylist(node));
    }
  }

  /**
   * Update the player image using a list of locations
   * @param locationList the list of locations to draw.
   */
  public void update(LocationList locationList) {
    //Create a duplicate image file
    ImageFile newImage = new ImageFile(dispatcher.getImageFile());

    //Fill in the path
    newImage.fillLocationPath(locationList.getUnderlyingList(), true);
    if (panel != null) {
      panel.updateImage(newImage);
    }
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
    if (panel != null) panel.markDone(message);
  }

  /**
   * Mark this player as done
   */
  public void markDone() {
    done = true;
  }

  /**
   * @return a string representation of the player.
   */
  @Override
  public String toString() {
    return playerName + " Done: " + done;
  }

  /**
   * Check object equality.
   * @param o the object to check.
   * @return a boolean indicating object equality.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Player player = (Player) o;
    if (panel != null) return panel.equals(player.panel) && playerName.equals(player.playerName);
    else return playerName.equals(player.playerName);
  }

  /**
   * @return a hashcode version of the object.
   */
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

  /**
   * @return an arraylist of all the exits in the maze.
   */
  public ArrayList<Location> getMazeExits() {
    return imageProcessor.getExits();
  }

  /**
   * @return a Map containing all of teh nodes in the maze, indexed by location.
   */
  public ConcurrentHashMap<Location, Node> getNodes() {
    return imageProcessor.getNodes();
  }

  /**
   * @return the imageFile being used.
   */
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
   * Set and compile the custom algorithm.
   * @param customAlgo the new Parser object.
   */
  public void setCustomAlgo(Parser customAlgo) {
    this.customAlgo = customAlgo;

    //Perform a scan if required
    if (imageProcessor.getExits().size() == 0) imageProcessor.scanAll(dispatcher.getImageFile());

    //set the handler if required
    if (this.handler == null) handler = new Handler(this);

    this.customAlgo.compile();
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
    solve = new SolveAlgorithm(this, logger);

    //update the display of this panel
    if (!dispatcher.isLive()) panel.makeAlgoWorkingScreen();

    //Check if the nodes have already been scanned
    if (imageProcessor.getNodes().isEmpty()) solve.scan(params);

    //If the param is set to loading set the scanAll field in the SolveAlgorithm to true
    if (params.equals("Loading")) solve.scanAll = true;

    logger.add(this.playerName + "started solve");

    //Start the solve
    ImageFile solved = solve.solve(algorithm, multiThread);

    //Update the display
    panel.makeAlgoSolvedScreen(solved);
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
    solve = new SolveAlgorithm(this, logger);

    if (this.imageProcessor.getNodes().size() == 0) solve.scan("Loading");

    solve.scanAll = true;

    //Start the solve
    ImageFile solved = solve.solve(algorithm, false);
    panel.makeAlgoSolvedScreen(solved);
  }

  /**
   * Solve the requested algorithm.
   *
   * If the custom algorithm is not null use it.
   * Otherwise get the predefined algorithm from the panel.
   */
  public void solve() {
    if (customAlgo != null) {
      customAlgo.execute();
    } else {
      solve(panel.getAlgorithm());
    }

    this.done = true;

    if (online || hasOpponent) {
      //if this is online send the message to the server
      if (online) sendMessage(Requests.done);
      dispatcher.checkStatus(this);
    } else {
      ImageFile solved = new ImageFile(dispatcher.getImageFile());
      solved.fillNodePath(PathMaker.generatePathArraylist(handler.getLastNode()), true);
      panel.makeAlgoSolvedScreen(solved);
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
    //Only scan if the processor is empty
    if (imageProcessor.getExits().size() == 0 && imageProcessor.getNodes().size() == 0) {
      imageProcessor.scanAll(dispatcher.getImageFile());
    }
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
  public void makeGameScreen() {
    panel.makeGameScreen();
  }

  /**
   * Make the screen that shows the algorithm being solved live.
   */
  public void makeSolvingScreen() { panel.makeSolvingScreen(); }

  /**
   * Make the screen this is displayed when waiting for an online game to start.
   */
  public void makeOnlineWaitingScreen() {
    System.out.println("Making wait screen");
    panel.makeOnlineWaitingScreen();
  }

  /**
   * @return an int representing the specified delay in Game mode.
   */
  public int getDelay() {
    return delay;
  }

  /**
   * @return a boolean indicating if this player has completed execution.
   */
  public boolean isDone() {
    return this.done;
  }

  /**
   * Set if the player is done.
   * @param done boolean to indicate if the player is done
   */
  public void setDone(boolean done) {
    this.done = done;
  }

  /**
   * Send a message to the sever.
   * @param message the message to send.
   */
  public void sendMessage(Object message) {
    dispatcher.sendMessage(message);
  }

  /**
   * @return a boolean indicating iof this player is online.
   */
  public boolean isOnline() {
    return online;
  }

  /**
   * Set the online status of the player.
   * @param online boolean to indicate online/offline.
   */
  public void setOnline(boolean online) {
    this.online = online;
  }

  /**
   * @return a boolean indicating whether or not this player has an opponent.
   */
  public boolean hasOpponent() {
    return hasOpponent;
  }

  /**
   * @param hasOpponent boolean indicating if there is an opponent.
   */
  public void setOpponent(boolean hasOpponent) {
    this.hasOpponent = hasOpponent;
  }

  /**
   * @return boolean indicating if thus player is local
   */
  public boolean isLocal() {
    return local;
  }

  /**
   * Set if this player is the local player.
   * @param local a boolean to indicate if local.
   */
  public void setLocal(boolean local) {
    this.local = local;
  }

  /**
   * Get the username from the dispatcher.
   * @param dimension is this player online or local
   * @return the username
   */
  public String getUserName(String dimension) {
    return dispatcher.getUserName(dimension);
  }

  /**
   * Pass a restart request to the dispatcher.
   */
  public void requestRestart() {
    dispatcher.requestRestart(this);
  }

  /**
   * Reset the variables required for multiplayer
   */
  public void reset() {
    this.imageProcessor = new ImageProcessor();
    this.done = false;
  }
}
