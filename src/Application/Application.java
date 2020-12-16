package Application;

//todo tidy code up so that most interfaces through this class

import GUI.CustomPanels.PlayerPanel;
import Game.Game;
import Utility.Exceptions.InvalidImage;
import Utility.Location;
import Utility.Node;
import Utility.Thread.AlgorithmDispatcher;
import GUI.GUI;
import Utility.Exceptions.GenericError;
import Image.*;
import Game.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is the base of the program.
 * Most inter package communication happens through here.
 */
public class Application {
  GUI gui;
  ImageFile currentImage;
  ImageProcessor imageProcessor;
  Game game;

  public Application() {
  }

  ;

  /**
   * Copy constructor used to deep copy fields
   *
   * @param oldApplication the old application
   */
  public Application(Application oldApplication) {
    this.currentImage = new ImageFile(oldApplication.currentImage);
    this.gui = oldApplication.gui;
    this.imageProcessor = new ImageProcessor(oldApplication.imageProcessor, this);
  }

  /**
   * Take the image file and parse it into the appropriate format
   *
   * @param imageToParse
   */
  public void parseImageFile(File imageToParse) throws GenericError {
    currentImage = new ImageFile(imageToParse);
  }

  /**
   * Setup the gui
   */
  private void setUpGui() {
    this.gui = new GUI(this);
  }

  /**
   * Gets a buffered image of the current maze.
   *
   * @return an image of the maze
   */
  public BufferedImage getImage() {
    return currentImage.makeImage();
  }

  public ImageFile getImageFile() {
    return currentImage;
  }

  /**
   * Get the dimensions of the maze
   *
   * @return the dimensions
   */
  public Dimension getMazeDimensions() {
    return currentImage.getDimensions();
  }

  /**
   * Get a specified piece of information about the image
   *
   * @param info the requested info
   * @return the info
   */
  public String getImageInfo(String info) {
    return currentImage.getInfo(info);
  }

  /**
   * Start solving the maze
   *
   * @param algorithm      the algorithm to use
   * @param params         the parameters to use
   * @param multiThreading should the algorithm run using multiple threads
   * @param delay          the delay between each step of the algorithm.
   * @param player         the player solving (may be null)
   */
  public AlgorithmDispatcher solve(String algorithm, String params, Boolean multiThreading, int delay, Player player) {
    if (this.imageProcessor == null) this.imageProcessor = new ImageProcessor(this);
    AlgorithmDispatcher worker = new AlgorithmDispatcher(algorithm, params, this, "solver", multiThreading, delay, player);
    return worker;
  }


  /**
   * Reset the current image to remove any marks from solving etc
   */
  public void resetImage() {
    currentImage.reset();
  }

  /**
   * Save the current image to a file
   *
   * @param path the place to save the image
   */
  public void saveImage(String path) {
    currentImage.saveImage(path);
  }

  /**
   * @return the nodes from the image processor
   */
  public ConcurrentHashMap<Location, Node> getNodes() {
    return imageProcessor.getNodes();
  }

  /**
   * Scan the entire maze
   */
  public void scanEntireMaze() {
    if (imageProcessor == null) imageProcessor = new ImageProcessor(this);
    imageProcessor.scanAll();
  }

  /**
   * Find the exits in the maze
   */
  public void findMazeExits() {
    imageProcessor.findExits();
  }

  /**
   * @return the maze exits
   */
  public ArrayList<Location> getMazeExits() {
    return imageProcessor.getExits();
  }

  /**
   * Scan only a part of the maze
   *
   * @param parent         the node to start at
   * @param multiThreading is the program currently multi threading?
   */
  public void scanPart(Node parent, Boolean multiThreading) {
    imageProcessor.scanPart(parent, multiThreading);
  }

  /**
   * @param maxSize the max size that any panels in the game can be displayed at
   */
  public void initialiseGame(Dimension maxSize) {
    this.game = new Game(maxSize, this);
  }

  public Game getGame() {
    return game;
  }

  /**
   * @param playerNum the player number
   * @return the panel displaying this player
   */
  public PlayerPanel getGamePanel(int playerNum) {
    return game.getPlayerPanel(playerNum);
  }

  /**
   * Tell the game object to load the nodes
   *
   * @param componentToUpdate the panel that will be updated upon completion
   * @param delayTextArea     text area containing the requested delay
   */
  public void loadGameNodes(JPanel componentToUpdate, JTextArea delayTextArea) {
    game.loadNodes(componentToUpdate, delayTextArea);
  }

  public static void main(String[] args) {
    //Create the GUI
    new Application().setUpGui();
  }
}
