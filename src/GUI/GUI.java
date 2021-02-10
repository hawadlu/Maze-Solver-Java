package GUI;

import Application.Application;
import Utility.AlgorithmDispatcher;

import Utility.Exceptions.GenericError;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class GUI {
  Application application;
  AlgorithmDispatcher dispatcher;

  public final int width = 1280;
  public final int height = 800;
  public static final Color activeCol = new Color(0, 131, 233);
  public static final Color inactiveCol = new Color(66, 66, 66);
  public static final Color backgroundCol = new Color(211, 211, 211);

  //Panels
  static JFrame window;
  JPanel container; //This panel holds all other panels
  JPanel activityArea; //This is the panel hosts the two panels below as required
  JPanel algoMainArea = null;
  JPanel gameMainArea = null;

  /**
   * Constructor, creates and displays the gui
   */
  public GUI(Application application) {
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
    container.setBackground(backgroundCol);

    //Setup the top tabs
    JPanel topTabs = new JPanel();
    makeTopTabs(topTabs, activeCol, inactiveCol);

    container.add(topTabs);

    activityArea = new JPanel();
    activityArea.setBackground(backgroundCol);
    activityArea.setSize(new Dimension(width, height));
    activityArea.setLayout(new BoxLayout(activityArea, BoxLayout.Y_AXIS));

    //Add the algorithms panel by default
    makeLoadScreen("Algorithm");
    activityArea.add(algoMainArea);

    container.add(activityArea);

    window.add(container);


    //Make the window visible
    window.setVisible(true);
  }

  /**
   * This method makes the tabs that go at the top of the screen
   *
   * @param topTabs   the JPanel that houses the tabs
   * @param tabOneCol the colour of the first tab
   * @param tabTwoCol the colour of the second tab
   */
  private void makeTopTabs(JPanel topTabs, Color tabOneCol, Color tabTwoCol) {
    topTabs.removeAll(); //Remove all current nested components

    topTabs.setLayout(new BoxLayout(topTabs, BoxLayout.X_AXIS));
    topTabs.setSize(new Dimension(width, 100));

    //The algorithm tab
    JPanel algoTab = new JPanel();
    System.out.println("max size: " + width / 2);
    algoTab.setMinimumSize(new Dimension(width / 2, 100));
    algoTab.setMaximumSize(new Dimension(width / 2, 100));
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
    gameTab.setMinimumSize(new Dimension(width / 2, 100));
    gameTab.setMaximumSize(new Dimension(width / 2, 100));
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
          if (gameMainArea == null) makeLoadScreen("Game");

          activityArea.add(gameMainArea);

          refresh();
        }
      }
    });
    topTabs.add(gameTab);
  }

  /**
   * Make the start screen for playing games
   */
  public void makeGameStartScreen() {
    gameMainArea.removeAll();

    //Make a dispatcher for local pvp
    if (application.getImageFile() != null) {
      dispatcher = new AlgorithmDispatcher(application.getImageFile(), 2);
    } else {
      //Make a dispatcher for online pvp
      dispatcher = new AlgorithmDispatcher(application.getClient(), 2);
    }
    dispatcher.initialiseGUI(this);

    //Make the online multiplayer screen
    if (application.getClient() != null) {
      dispatcher.makeOnlineStartScreen(width, height);
    } else {
      //Local multiplayer screen
      dispatcher.makeGameScreen(width, height);
    }

    gameMainArea.add(dispatcher.getScreen());


    refresh();
  }

  /**
   * Make the panel that will be used to create the algorithms.
   * It creates a simple panel that houses a button for loading images
   *
   * @param param parameter to indicate which screen should come after this
   */
  public void makeLoadScreen(String param) {
    JPanel loadPanel = new JPanel();

    if (param.equals("Algorithm")) {
      if (algoMainArea != null) algoMainArea.removeAll();
      else algoMainArea = new JPanel();
      algoMainArea.setBackground(backgroundCol);

      loadPanel.setBackground(backgroundCol);
      loadPanel.setMinimumSize(new Dimension(activityArea.getWidth(), activityArea.getHeight()));
      JButton loadImage = new JButton("Load Image");

      //Bind functionality to the load image button
      loadImage.addActionListener(e -> {
        try {
          application.parseImageFile(UIFileChooser());
        } catch (GenericError genericError) {
          genericError.printStackTrace();
        }

        //Load the image to the main screen
        makeAlgoSolveScreen();

        refresh();
      });

      loadPanel.add(loadImage);
      algoMainArea.add(loadPanel);
      refresh();

    } else if (param.equals("Game")) {
      if (gameMainArea != null) gameMainArea.removeAll();
      else gameMainArea = new JPanel();
      gameMainArea.setBackground(backgroundCol);

      JButton connect = new JButton("Connect To Server");

      connect.addActionListener(e -> {
        application.connectToServer();
        makeGameStartScreen();
      });

      JButton loadImage = new JButton("Local Multiplayer");

      //Bind functionality to the load image button
      loadImage.addActionListener(e -> {
        try {
          application.parseImageFile(UIFileChooser());
        } catch (GenericError genericError) {
          genericError.printStackTrace();
        }

        //Load the image to the main screen
        makeGameStartScreen();

        refresh();
      });



      loadPanel.add(connect);
      loadPanel.add(loadImage);
      gameMainArea.add(loadPanel);
    }


    refresh();
  }

  /**
   * Make the sc®een that will be used to load the options for solving the maze
   */
  private void makeAlgoSolveScreen() {
    algoMainArea.removeAll();

    System.out.println("Making algorithm solve screen");

    //Make the dispatcher object
    dispatcher = new AlgorithmDispatcher(application.getImageFile(), 1);
    dispatcher.initialiseGUI(this);

    algoMainArea.add(dispatcher.getPlayerScreen(0));
    refresh();
  }

  /**
   * Reset the image so that is can be used again
   */
  private void resetImage() {
    application.resetImage();
    makeAlgoSolveScreen();
  }

  /**
   * Get a specified piece of information about the image
   *
   * @param info the requested info
   * @return the info
   */
  private String getImageInfo(String info) {
    return application.getImageInfo(info);
  }

  /**
   * Get and return the file that the user wants
   *
   * @return the file
   */
  public static File UIFileChooser() {
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
  public static void refresh() {
//    System.out.println("Refreshing gui");
    if (window != null) {
      window.revalidate();
      window.repaint();
    }
  }

  /**
   * Make JLabels for the tabs
   *
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
   * Get the current maze image from the application
   *
   * @return
   */
  public BufferedImage getImage() {
    return application.getImage();
  }

  /**
   * Get the dimensions of the maze
   *
   * @return dimensions
   */
  public Dimension getMazeDimensions() {
    return application.getMazeDimensions();
  }

  public static void makePopup(JPanel panelToDisplay, ArrayList<JButton> exitButtons, Dimension panelDimensions) {
    System.out.println("Making popup");
    JFrame frame = new JFrame();
    frame.setSize(panelDimensions);
    frame.setBackground(backgroundCol);
    frame.add(panelToDisplay);
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    //Add the button functionality
    for (JButton exit : exitButtons) {
      exit.addActionListener(e -> {
        frame.dispose();
      });
    }

    frame.setVisible(true);
  }
}
