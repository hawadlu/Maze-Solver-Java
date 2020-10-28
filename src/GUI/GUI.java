package GUI;

import Application.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI implements ItemListener {
  Application application;
  public final int width = 1280;
  public final int height = 800;
  public final Color activeCol = new Color(0, 131, 233);
  public final Color inactiveCol = new Color(66, 66, 66);

  //Panels
  JFrame window;
  JPanel activityArea; //This is the panel where all the action happens
  JPanel topTabs;

  /**
   * Constructor, creates and displays the gui
   */
  public GUI(Application application){
    System.out.println("initiating gui");
    this.application = application;

    window = new JFrame("Maze Solver");

    //Setup the size
    window.setSize(width, height);
    window.setResizable(false);
    window.setDefaultCloseOperation(window.EXIT_ON_CLOSE);

    //Setup the top tabs
    JPanel topTabs = new JPanel();
    makeTopTabs(topTabs, activeCol, inactiveCol);

    window.add(topTabs);

    activityArea = new JPanel();
    activityArea.setBackground(activeCol);
    window.add(activityArea);

    //Make the window visible
    window.setVisible(true);
  }

  /**
   * This method makes the tabs that go at the top of the screen
   * @param topTabs the JPanel that houses the tabs
   * @param tabOneCol the colour of the first tab
   * @param tabTwoCol the colour of the second tab
   */
  private void makeTopTabs(JPanel topTabs, Color tabOneCol, Color tabTwoCol) {
    topTabs.removeAll(); //Remove all current nested components

    topTabs.setLayout(new BoxLayout(topTabs, BoxLayout.X_AXIS));
    topTabs.setBackground(Color.red); //todo remove me
    topTabs.setSize(new Dimension(width, 100));

    //The algorithm tab
    JPanel algoTab = new JPanel();
    algoTab.setMaximumSize(new Dimension(width/2, 100));
    algoTab.setBackground(tabOneCol);
    algoTab.add(makeTabLabel("Algorithms"));
    algoTab.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        System.out.println("Clicked on the algorithms tab with col: " + algoTab.getForeground());

        //Check if this is the currently active tab
        if (algoTab.getBackground().equals(inactiveCol)) {
          System.out.println("The algorithms tab is inactive");

          //Make this tab active
          algoTab.setForeground(activeCol);

          System.out.println("Algorithm tab col: " + algoTab.getForeground());

          makeTopTabs(topTabs, activeCol, inactiveCol);

          refresh();
        }
      }
    });
    topTabs.add(algoTab);

    //The game tab
    JPanel gameTab = new JPanel();
    gameTab.setMaximumSize(new Dimension(width/2, 100));
    gameTab.setBackground(tabTwoCol);
    gameTab.add(makeTabLabel("Game"));
    gameTab.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        System.out.println("Clicked on the game tab with col: " + gameTab.getForeground());

        //Check if this is the currently active tab
        if (gameTab.getBackground().equals(inactiveCol)) {
          System.out.println("The game tab is inactive");

          //Make this tab active
          gameTab.setForeground(activeCol);

          System.out.println("Game tab col: " + gameTab.getForeground());

          makeTopTabs(topTabs, inactiveCol, activeCol);

          refresh();
        }
      }
    });
    topTabs.add(gameTab);
  }

  /**
   * Reload the gui
   */
  private void refresh() {
    window.revalidate();
    window.repaint();
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
