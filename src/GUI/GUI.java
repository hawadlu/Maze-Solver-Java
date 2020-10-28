package GUI;

import Application.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.DataInput;
import java.util.ArrayList;

public class GUI implements ItemListener {
  Application application;
  public final int width = 1280;
  public final int height = 800;
  public final Color backgroundCol = new Color(0, 131, 233);
  public final Color inactiveTabCol = new Color(66, 66, 66);

  //Panels
  JFrame window;
  JPanel activityArea; //This is the panel where all the action happens

  /**
   * Constructor, creates and displays the gui
   */
  public GUI(Application application){
    System.out.print("initiating gui");
    this.application = application;

    window = new JFrame("Maze Solver");

    //Setup the size
    window.setSize(width, height);
    window.setResizable(false);
    window.setDefaultCloseOperation(window.EXIT_ON_CLOSE);

    //Setup the top tabs
    JPanel topTabs = new JPanel();
    topTabs.setLayout(new BoxLayout(topTabs, BoxLayout.X_AXIS));
    topTabs.setBackground(Color.red); //todo remove me
    topTabs.setSize(new Dimension(width, 100));

    //The algorithm tab
    JPanel algoTab = new JPanel();
    algoTab.setMaximumSize(new Dimension(width/2, 100));
    algoTab.setBackground(backgroundCol);
    algoTab.add(makeTabLabel("Algorithms"));
    topTabs.add(algoTab);

    //The game tab
    JPanel gameTab = new JPanel();
    gameTab.setMaximumSize(new Dimension(width/2, 100));
    gameTab.setBackground(inactiveTabCol);
    gameTab.add(makeTabLabel("Game"));
    topTabs.add(gameTab);

    window.add(topTabs);

    activityArea = new JPanel();
    activityArea.setBackground(backgroundCol);
    window.add(activityArea);

    //Make the window visible
    window.setVisible(true);
  }

  /**
   * Make JLabels for the tabs
   * @param name the tab title
   * @return a new JLabel
   */
  private Component makeTabLabel(String name) {
    JLabel tabLabel = new JLabel(name);
    tabLabel.setFont(new Font("Verdana", 1, 50));
    tabLabel.setForeground(Color.WHITE);
    return tabLabel;
  }

  /**
   * Change the cards at the bottom of the screen
   * @param e the event
   */
  @Override
  public void itemStateChanged(ItemEvent e) {
  }
}
