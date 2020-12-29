package Game;

import Application.Application;
import GUI.CustomPanels.PlayerPanel;
import Parser.Parser;
import Utility.Node;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import Image.ImageFile;
import Utility.PathMaker;

public class Player {
  Application application;
  Node currentNode;
  PlayerPanel panel;
  String playerName;
  final ImageFile originalImage;
  Game currentGame;
  AtomicBoolean done = new AtomicBoolean(false);
  PlayerWorker thread;
  Player other;
  Parser customAlgo;
  AtomicBoolean isDone = new AtomicBoolean(false);


  /**
   * @param maxSize the max size that any panels in the game can be displayed at
   */
  public Player(Dimension maxSize, String playerName, Application application, Game game) {
    this.originalImage = new ImageFile(application.getImageFile());

    //Create a new application
    this.application = new Application(application);
    this.playerName = playerName;
    this.currentGame = game;

    this.panel = new PlayerPanel(application, playerName, maxSize);
  }


  public Player(String playerName, Application application, Game game) {
    this.originalImage = new ImageFile(application.getImageFile());

    //Create a new application
    this.application = new Application(application);
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
    System.out.println(this + " is updating");
    this.currentNode = node;

    //Create a duplicate image file
    ImageFile newImage = new ImageFile(originalImage);

    //Create a path from the current node
    newImage.fillNodePath(PathMaker.generatePathArraylist(currentNode), true);

    newImage.saveImage("Images/Solved/" + this.playerName + " " + System.currentTimeMillis() + ".png");

    if (panel != null) updateSolveImage(newImage);
  }

  /**
   * @return the JPanel that is the visual representation of this players progress
   */
  public PlayerPanel getPanel() {
    return panel;
  }

  /**
   * Solve the maze
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

    if (customAlgo != null) {
      //Start a new thread using the custom algorithm
      thread = new PlayerWorker(this, customAlgo, delay);
      solveThread = thread;
    } else {
      //Start using one of the prebuilt algorithms
      solveThread = application.solve(algorithm, "Loading", false, delay, this);
    }
    solveThread.start();
  }

  /**
   * Make the panel display a done message.
   * @param message, the message to display
   */
  public void makeDoneDisplay(String message) {
    //Make the completed image
    ImageFile newImage = new ImageFile(originalImage);

    //Create a path from the current node
    newImage.fillNodePath(PathMaker.generatePathArraylist(currentNode), true);

    panel.markDone(message, newImage);
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
    return panel.equals(player.panel) && playerName.equals(player.playerName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(panel, playerName);
  }
}

/**
 * This class deal with running each custom algorithms for each player.
 * This is done in a separate class because when both players use a
 * custom algorithm in the game mode, one thread seems to override
 * the other.
 */
class PlayerWorker extends Thread {
//  String threadId;
  volatile Player player;
  volatile Parser customAlgo;
  volatile int delay;

  PlayerWorker(Player player, Parser customAlgo, int delay) {
    this.player = player;
    this.customAlgo = customAlgo;
    this.delay = delay;
  }

  @Override
  public void run() {
    System.out.println("Started thread for " + player);
    customAlgo.handler.setPlayer(player);
    customAlgo.handler.setDelay(delay);
    customAlgo.execute();
    System.out.println(player + " has completed the thread");
    player.isDone.set(true);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PlayerWorker that = (PlayerWorker) o;
    return delay == that.delay && player.equals(that.player) && customAlgo.equals(that.customAlgo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(player, customAlgo, delay);
  }
}