package Game;

import Application.Application;
import GUI.CustomPanels.PlayerPanel;
import GUI.GUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Game {
  Player playerOne, playerTwo;
  Application application;


  /**
   * @param maxSize the max size that any panels in the game can be displayed at
   */
  public Game(Dimension maxSize, Application application) {
    playerOne = new Player(maxSize, "Player One", application, this);
    playerTwo = new Player(maxSize, "Player Two", application, this);
    this.application = application;
  }

  /**
   * @param playerNum the number of the requested player
   * @return the panel representing this player
   */
  public PlayerPanel getPlayerPanel(int playerNum) {
    if (playerNum == 1) return playerOne.getPanel();
    else return playerTwo.getPanel();
  }


  /**
   * Start a new thread and load all of the nodes
   *
   * @param componentToUpdate JComponent that turns into solve when all the nodes are scanned
   * @param delayTextArea     the text area that contains the requested delay;
   */
  public void loadNodes(JPanel componentToUpdate, JTextArea delayTextArea) {
    Thread load = new Thread() {
      @Override
      public void run() {

        System.out.println("Starting node scan");
        application.scanEntireMaze();

        //Update the component
        JButton solve = new JButton("Solve");
        solve.addActionListener(e -> {
          //Extract the requested time delay
          int delay = Integer.parseInt(delayTextArea.getText()); //todo make this more robust

          playerOne.solve(delay);
          playerTwo.solve(delay);
        });


        componentToUpdate.remove(3);
        componentToUpdate.add(solve, 3);

        System.out.println("Complete node loading");
        GUI.refresh();
      }
    };

    load.start();
  }

  /**
   * Mark that this player has completed solving
   * @param player the player who has finished.
   */
  public void markDone(Player player) {
    //Determine if the other player is finished
    if (player.equals(playerOne) && !playerTwo.done.get()) playerOne.makeDoneDisplay("1st");
    else if (player.equals(playerOne) && playerTwo.done.get()) playerOne.makeDoneDisplay("2nd");
    else if (player.equals(playerTwo) && !playerOne.done.get()) playerTwo.makeDoneDisplay("1st");
    else if (player.equals(playerTwo) && playerOne.done.get()) playerTwo.makeDoneDisplay("2nd");
  }
}
