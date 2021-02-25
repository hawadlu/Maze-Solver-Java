package application;

import gui.GUI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

import image.*;
import server.LocalClient;
import utility.Exceptions.GenericError;
import utility.Location;
import utility.Node;

/**
 * This class is the base of the program.
 * It creates the gui and facilitates the initial connection to
 * an outside server.
 */
public class Application {
  GUI gui;
  ImageFile currentImage;
  ImageProcessor imageProcessor;
  LocalClient client;

  /**
   * Take the image file and parse it into the appropriate format
   *
   * @param imageToParse the file containing the image to be parsed.
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

  /**
   * @return the imageFile
   */
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
   * Initiate a connect to a game server
   * @param socket the socket to connect to
   */
  public void connectToServer(int socket) {
    client = new LocalClient(socket);
    client.connect();
    client.start();
  }

  /**
   * @return the client object.
   */
  public LocalClient getClient() {
    return client;
  }

  /**
   * Start the application
   */
  public static void main(String[] args) {
    //Create the GUI
    new Application().setUpGui();
  }
}
