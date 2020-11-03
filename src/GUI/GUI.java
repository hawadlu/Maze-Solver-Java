package GUI;

import Application.Application;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class GUI {
  Application application;
  public final int width = 1280;
  public final int height = 800;
  public final Color activeCol = new Color(0, 131, 233);
  public final Color inactiveCol = new Color(66, 66, 66);

  //Panels
  JFrame window;
  JPanel container; //This panel holds all other panels
  JPanel activityArea; //This is the panel hosts the two panels below as required
  JPanel algoMainArea = null;
  JPanel gameMainArea = null;

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

    //Setup the container
    container = new JPanel();
    container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
    container.setBackground(activeCol);

    //Setup the top tabs
    JPanel topTabs = new JPanel();
    makeTopTabs(topTabs, activeCol, inactiveCol);

    container.add(topTabs);

    activityArea = new JPanel();
    activityArea.setBackground(activeCol);
    activityArea.setSize(new Dimension(width, height));
    activityArea.setLayout(new BoxLayout(activityArea, BoxLayout.Y_AXIS));

    //Add the algorithms panel by default
    algoMainArea = makeAlgoLoadScreen(activityArea.getWidth(), activityArea.getHeight());
    activityArea.add(algoMainArea);

    container.add(activityArea);

    window.add(container);


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
    topTabs.setSize(new Dimension(width, 100));

    //The algorithm tab
    JPanel algoTab = new JPanel();
    System.out.println("max size: " + width/2);
    algoTab.setMinimumSize(new Dimension(width/2, 100));
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

          //Remake the algorithms screen
          activityArea.removeAll();
          activityArea.add(algoMainArea);

          refresh();
        }
      }
    });
    topTabs.add(algoTab);

    //The game tab
    JPanel gameTab = new JPanel();
    gameTab.setMinimumSize(new Dimension(width/2, 100));
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

          //Make the game screen
          activityArea.removeAll();

          //Check if the game screen has bene made already
          if (gameMainArea == null) gameMainArea = makeGameStartScreen(width, width - 100);

          activityArea.add(gameMainArea);

          refresh();
        }
      }
    });
    topTabs.add(gameTab);
  }

  /**
   * Make the start screen for playing games
   * @param width the desired width of the screen
   * @param height the desired height of the screen
   * @return the new JPanel
   */
  //todo properly implement this
  private JPanel makeGameStartScreen(int width, int height) {
    JPanel startScreen = new JPanel();
    startScreen.setMinimumSize(new Dimension(width, height));
    startScreen.setBackground(Color.red);
    return startScreen;
  }

  /**
   * Make the panel that will be used to create the algorithms.
   * It creates a simple panel that houses a button for loading images
   * @return the panel that holds the load image button
   */
  private JPanel makeAlgoLoadScreen(int width, int height) {
    JPanel loadPanel = new JPanel();
    loadPanel.setBackground(activeCol);
    loadPanel.setMinimumSize(new Dimension(width, height));
    JButton loadImage = new JButton("Load Image");
    
    //Bind functionality to the load image button
    loadImage.addActionListener(e -> {
      application.parseFile(UIFileChooser());
      
      //Load the image to the main screen
      algoMainArea.removeAll();
      makeAlgoSolveScreen();
      refresh();
    });
    
    loadPanel.add(loadImage);
    return loadPanel;
  }
  
  /**
   * Make the sc®een that will be used to load the options for solving the maze
   */
  private JPanel makeAlgoSolveScreen() {
    System.out.println("Making algorithm solve screen");
    JPanel main = new JPanel();
    main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
    
    JPanel imageArea = new JPanel(); //this hosts the image
    imageArea.setBackground(Color.BLACK);
    
    JPanel buttonArea = new JPanel(); //this hosts the solve options
    buttonArea.setBackground(Color.DARK_GRAY);
    
    main.add(imageArea);
    main.add(buttonArea);
    
    return main;
  }
  
  /**
   * Get and return the file that the user wants
   *
   * @return the file
   */
  private File UIFileChooser()  {
    System.out.println("Load image");
    //Get the file
    final JFileChooser filePicker = new JFileChooser();
    int fileReturn = filePicker.showOpenDialog(filePicker);
    
    if (fileReturn == JFileChooser.APPROVE_OPTION) {
      File fileIn = filePicker.getSelectedFile();
      System.out.println("Opened: " + fileIn);
      return fileIn;
    } else {
      return null;
    }
  }

  /**
   * Reload the gui
   */
  private void refresh() {
    System.out.println("Refreshing gui");
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
}
