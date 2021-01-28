package Game;

import Application.Application;
import GUI.CustomPanels.PlayerPanel;
import Image.ImageFile;
import Utility.Location;
import Utility.Thread.AlgorithmDispatcher;
import parser.Parser;
import Utility.Node;
import Utility.PathMaker;
import Image.ImageProcessor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class Player {
  Node currentNode;
  PlayerPanel panel;
  String playerName;
  Game currentGame;
  AtomicBoolean done = new AtomicBoolean(false);
  Player other;
  Parser customAlgo;
  AtomicBoolean isDone = new AtomicBoolean(false);
  Game game;
  ImageProcessor imageProcessor;


  /**
   * @param maxSize the max size that any panels in the game can be displayed at
   */
  public Player(Dimension maxSize, String playerName, Game game) {

    //Create a new application
    this.playerName = playerName;
    this.currentGame = game;

    this.panel = new PlayerPanel(playerName, maxSize);
  }


  public Player(String playerName, Game game) {
    this.playerName = playerName;
    this.currentGame = game;
  }

  public void setOther(Player other) {
    this.other = other;
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
    System.out.println(playerName + " is updating");

    this.currentNode = node;

    //Create a duplicate image file
    ImageFile newImage = new ImageFile(game.getImage());

    //Create a path from the current node
    newImage.fillNodePath(PathMaker.generatePathArraylist(currentNode), true);
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
      createSolveImagePanel();
      algorithm = panel.getAlgorithm();
    }

    //Start the solve algorithm
    Thread solveThread;
    if (customAlgo == null) customAlgo = panel.getCustomAlgo();

    //Start using one of the prebuilt algorithms
    solveThread = new AlgorithmDispatcher(algorithm, "Loading", false, delay, this, customAlgo);
    solveThread.start();
  }

  /**
   * Make the panel display a done message.
   * @param message, the message to display
   */
  public void makeDoneDisplay(String message) {
    //Make the completed image
    ImageFile newImage = new ImageFile(game.getImage());

    //Create a path from the current node
    newImage.fillNodePath(PathMaker.generatePathArraylist(currentNode), true);

    if (panel != null) panel.markDone(message, newImage);
  }

  /**
   * Mark this player as done
   */
  public void markDone() {
    done.set(true);
    currentGame.markDone(this);
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

  public Map<Location, Node> getNodes() {
    return imageProcessor.getNodes();
  }

  public ImageFile getImageFile() {
    return game.getImage();
  }
}
