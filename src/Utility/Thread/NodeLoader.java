package Utility.Thread;

import Application.Application;
import GUI.CustomPanels.PlayerPanel;
import GUI.GUI;

import javax.swing.*;

/**
 * Class used by the game gui to load all the nodes while setup is happening
 */
public class NodeLoader extends Thread {
  JPanel componentToUpdate;
  PlayerPanel playerOne, playerTwo;
  private Application application;

  public void setComponentToUpdate(JPanel component) {
    this.componentToUpdate = component;
  }

  public void setPlayers(PlayerPanel playerOne, PlayerPanel playerTwo) {
    this.playerOne = playerOne;
    this.playerTwo = playerTwo;
  }

  public void setApplication(Application application) {
    this.application = application;
  }

  @Override
  public void run() {
    System.out.println("Starting node scan");
    application.scanEntireMaze();

    //Update the component
    JButton solve = new JButton("Solve");
    solve.addActionListener(e -> {
      playerOne.solve();
      playerTwo.solve();
    });

    componentToUpdate.remove(3);
    componentToUpdate.add(solve, 3);

    System.out.println("Complete node loading");
    GUI.refresh();
  }
}
