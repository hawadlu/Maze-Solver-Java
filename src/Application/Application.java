package Application;
/**
 * todo
 * The last things are the following...
 * Update my website to support video playback.
 * Update my website to support full size images.
 * Create a selection of screenshots to show off the project.
 * Create a video to show off the project.
 * Update the project year and description.
 * Add the new images and video.
 * Update my CV
 */


/**
 * todo features to finish
 * parser
 * Local pvp mode using arrow keys and wasd
 * Single player mode with high scores
 * Server algorithm vs algorithm
 * Server pvp using arrow keys and pvp
 */


//todo tidy code up so that most interfaces through this class


import GUI.CustomPanels.PlayerPanel;
import GUI.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import Image.*;
import Server.LocalClient;
import Utility.Exceptions.GenericError;
import Utility.Location;
import Utility.Node;

/**
 * This class is the base of the program.
 * Most inter package communication happens through here.
 */
public class Application {
  GUI gui;
  ImageFile currentImage;
  ImageProcessor imageProcessor;
  LocalClient client;

  public Application() {
  }


  /**
   * Take the image file and parse it into the appropriate format
   *
   * @param imageToParse
   */
  public void parseImageFile(File imageToParse) throws GenericError {
    currentImage = new ImageFile(imageToParse);

    //If there are already nodes in the node scanner, remove them
    if (imageProcessor != null && !imageProcessor.getNodes().isEmpty()) {
      imageProcessor.clear();
    } else {
      imageProcessor = new ImageProcessor();
    }
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
    //If the image processor is null or already contains nodes, make a new one.
    if (imageProcessor == null || !imageProcessor.getNodes().isEmpty()) imageProcessor = new ImageProcessor(this.imageProcessor);
    imageProcessor.scanAll(currentImage);
  }

  /**
   * Find the exits in the maze
   */
  public void findMazeExits() {
    imageProcessor.findExits(currentImage);
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
    imageProcessor.scanPart(parent, multiThreading, currentImage);
  }


  public static void main(String[] args) {
    //Create the GUI
    new Application().setUpGui();
  }

  public ImageProcessor getImageProcessor() {
    return imageProcessor;
  }

  /**
   * Initiate a connect to a game server
   */
  public void connectToServer() {
    client = new LocalClient();
    client.connect();
  }

  /**
   * @return the client object.
   */
  public LocalClient getClient() {
    return client;
  }
}
