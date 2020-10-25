package GUI;

import javax.swing.*;
import java.awt.*;

public class GUI {
  JPanel mainItem;

  /**
   * Constructor, creates and displays the gui
   */
  public GUI() {
    JFrame window = new JFrame("Maze Solver");

    //Set the default options
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setSize(1000, 750);

    //Create the top menu bar
    JMenuBar topBar = new JMenuBar();
    JMenuItem solve = new JMenuItem("Solve");
    JMenuItem game = new JMenuItem("Game");
    topBar.add(solve);
    topBar.add(game);

    //Add the top bar to the window
    window.setJMenuBar(topBar);

    //Add the main item
    setSolveDisplay();
    window.add(mainItem);

    //Make the window visible
    window.setVisible(true);
  }

  /**
   * Setup the layout required for the solver
   */
  private void setSolveDisplay() {
    mainItem = new JPanel();

    mainItem.setBackground(Color.blue);
  }
}
