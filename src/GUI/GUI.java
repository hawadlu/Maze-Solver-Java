package GUI;

import Application.Application;
import GUI.CustomPanels.PlayerPanel;
import GUI.CustomPanels.Scroll;
import GUI.CustomPanels.SpinnerPanel;
import Utility.Exceptions.GenericError;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class GUI {
  Application application;

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
    //Make the control panel and players which will be used both in and out of the thread
    JPanel controlPanel = new JPanel();
    JTextArea timeDelay = new JTextArea();
    Dimension maxSize = new Dimension(width / 2, height - 200);

    //initate the game
    application.initialiseGame(maxSize, this, controlPanel);

    PlayerPanel playerOne = application.getGamePanel(1);
    PlayerPanel playerTwo = application.getGamePanel(2);

    gameMainArea.removeAll();

    JPanel startScreen = new JPanel();
    startScreen.setBackground(Color.red);
    startScreen.setLayout(new BoxLayout(startScreen, BoxLayout.Y_AXIS));

    //Add the info panel
    JPanel infoPanel = new JPanel();
    infoPanel.setMaximumSize(new Dimension(width, 100));
    infoPanel.add(new JLabel(application.getImageInfo("path")));
    startScreen.add(infoPanel);

    System.out.println("Making game panel");

    //Add the player panels
    JPanel centrePanel = new JPanel();
    centrePanel.setBackground(Color.ORANGE);
    centrePanel.setLayout(new GridLayout(1, 1));


    centrePanel.add(playerOne);
    centrePanel.add(playerTwo);

    startScreen.add(centrePanel);

    controlPanel.setLayout(new GridLayout(2, 2));
    controlPanel.add(new JLabel("Set draw delay (ms)"));
    controlPanel.add(new JLabel("Start the race"));

    JComponent solve = new JLabel("Waiting for node scan to finish");

    controlPanel.add(timeDelay);
    controlPanel.add(solve);

    startScreen.add(controlPanel);

    gameMainArea.add(startScreen);
    refresh();

    application.loadGameNodes(timeDelay);
  }

  /**
   * Make the panel that will be used to create the algorithms.
   * It creates a simple panel that houses a button for loading images
   *
   * @param param parameter to indicate which screen sould come after this
   */
  private void makeLoadScreen(String param) {
    if (param.equals("Algorithm")) {
      if (algoMainArea != null) algoMainArea.removeAll();
      else algoMainArea = new JPanel();
      algoMainArea.setBackground(backgroundCol);
    } else if (param.equals("Game")) {
      if (gameMainArea != null) gameMainArea.removeAll();
      else gameMainArea = new JPanel();
      gameMainArea.setBackground(backgroundCol);
    }

    JPanel loadPanel = new JPanel();
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
      if (param.equals("Algorithm")) makeAlgoSolveScreen();
      else if (param.equals("Game")) makeGameStartScreen();

      refresh();
    });

    loadPanel.add(loadImage);

    if (param.equals("Algorithm")) algoMainArea.add(loadPanel);
    else if (param.equals("Game")) gameMainArea.add(loadPanel);

    refresh();
  }

  /**
   * Make the sc®een that will be used to load the options for solving the maze
   */
  private void makeAlgoSolveScreen() {
    algoMainArea.removeAll();

    System.out.println("Making algorithm solve screen");
    JPanel main = new JPanel();
    main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

    main.add(new Scroll(application.getImage()));

    //Add the control panel
    JPanel control = new JPanel();
    control.setLayout(new FlowLayout());

    control.setPreferredSize(new Dimension(500, 100));
    control.setBackground(backgroundCol);

    JButton solve = new JButton("Solve");
    JButton artPts = new JButton("Articulation Points");
    JButton minTree = new JButton("Minimum Tree");
    JButton startOver = new JButton("Choose another image");

    control.add(solve);
    control.add(artPts);
    control.add(minTree);
    control.add(startOver);

    //Bind the functionality
    artPts.addActionListener(e -> {
      ArrayList<JButton> cancelButtons = new ArrayList<>();

      //Make a popup JPanel
      JPanel optionPanel = new JPanel();
      optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));
      optionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
      JPanel solveControls = new JPanel();
      solveControls.setLayout(new GridLayout(0, 2));


      //The popup options
      solveControls.add(new JLabel("Image"));
      solveControls.add(new JLabel(getImageInfo("name")));
      solveControls.add(new JLabel("Algorithm"));
      JButton help = new JButton("Help");
      solveControls.add(help);
      optionPanel.add(solveControls);

      //Add functionality to the help link
      help.addActionListener(e1 -> {
        String helpText = "ALGORITHMS\n\n" +
                "There is only one algorithm\n" +
                "that I know of.";


        JPanel helpPanel = new JPanel();
        helpPanel.setLayout(new BoxLayout(helpPanel, BoxLayout.Y_AXIS));
        JTextArea helpTextPanel = new JTextArea();
        helpTextPanel.setEditable(false);
        helpTextPanel.setText(helpText);
        helpTextPanel.setBackground(backgroundCol);
        helpPanel.add(helpTextPanel);

        JButton exit = new JButton("exit");
        helpPanel.add(exit);
        helpPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        cancelButtons.add(exit);
        makePopup(helpPanel, cancelButtons, new Dimension(400, 550));
      });

      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(new FlowLayout());
      JButton solveButton = new JButton("Solve");
      JButton cancel = new JButton("Cancel"); //Cancel functionality is added to the button in the make popup method

      //Start the solve process
      solveButton.addActionListener(e12 -> {

        //This algorithm only works on a single thread and while searching for neighbours on load
        makeAlgoWorkingScreen(algoMainArea, "Articulation", "Loading", false, application);
      });

      cancelButtons.clear();
      cancelButtons.add(cancel);
      cancelButtons.add(solveButton);

      buttonPanel.add(cancel);
      buttonPanel.add(solveButton);

      optionPanel.add(buttonPanel);

      makePopup(optionPanel, cancelButtons, new Dimension(350, 200));
    });

    minTree.addActionListener(e -> {
      ArrayList<JButton> cancelButtons = new ArrayList<>();

      //Make a popup JPanel
      JPanel optionPanel = new JPanel();
      optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));
      optionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
      JPanel solveControls = new JPanel();
      solveControls.setLayout(new GridLayout(0, 2));


      //The popup options
      JComboBox algoOptions = new JComboBox(new String[]{"Prims", "Kruskals"});
      JCheckBox threadBox = new JCheckBox();

      solveControls.add(new JLabel("Image"));
      solveControls.add(new JLabel(getImageInfo("name")));
      solveControls.add(new JLabel("Algorithm"));
      solveControls.add(algoOptions);
      JButton help = new JButton("Help");
      solveControls.add(help);
      optionPanel.add(solveControls);

      //Add functionality to the help link
      help.addActionListener(e1 -> {
        String helpText = "ALGORITHMS\n\n" +
                "Note: Neither of these\n" +
                "algorithms are very good\n" +
                "with mazes larger than 500\n" +
                "by 500\n\n" +
                "PRIMS: Usually the fastest\n" +
                "Kruskals: In this application\n" +
                "it is only a proof of concept.\n" +
                "In general kruskals algorithm\n" +
                "does not deal with dense graphs\n" +
                "such as this maze very well.\n" +
                "I recommend that you use Prims\n" +
                "algorithm";


        JPanel helpPanel = new JPanel();
        helpPanel.setLayout(new BoxLayout(helpPanel, BoxLayout.Y_AXIS));
        JTextArea helpTextPanel = new JTextArea();
        helpTextPanel.setEditable(false);
        helpTextPanel.setText(helpText);
        helpTextPanel.setBackground(backgroundCol);
        helpPanel.add(helpTextPanel);

        JButton exit = new JButton("exit");
        helpPanel.add(exit);
        helpPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        cancelButtons.add(exit);
        makePopup(helpPanel, cancelButtons, new Dimension(400, 550));
      });

      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(new FlowLayout());
      JButton solveButton = new JButton("Solve");
      JButton cancel = new JButton("Cancel"); //Cancel functionality is added to the button in the make popup method

      //Start the solve process
      solveButton.addActionListener(e12 -> {
        String algorithm = (String) algoOptions.getSelectedItem();

        //All of these algorithms search for neighbours on load and do not multi thread
        makeAlgoWorkingScreen(algoMainArea, algorithm, "Loading", false, application);
      });

      cancelButtons.clear();
      cancelButtons.add(cancel);
      cancelButtons.add(solveButton);

      buttonPanel.add(cancel);
      buttonPanel.add(solveButton);

      optionPanel.add(buttonPanel);

      makePopup(optionPanel, cancelButtons, new Dimension(350, 200));
    });

    solve.addActionListener(e -> {
      ArrayList<JButton> cancelButtons = new ArrayList<>();

      //Make a popup JPanel
      JPanel optionPanel = new JPanel();
      optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));
      optionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
      JPanel solveControls = new JPanel();
      solveControls.setLayout(new GridLayout(0, 2));


      //The popup options
      JComboBox algoOptions = new JComboBox(new String[]{"AStar", "Breadth First", "Depth First", "Dijkstra"});
      JComboBox neighbourOptions = new JComboBox(new String[]{"Loading", "Solving"});
      JCheckBox threadBox = new JCheckBox();

      solveControls.add(new JLabel("Image"));
      solveControls.add(new JLabel(getImageInfo("name")));
      solveControls.add(new JLabel("Algorithm"));
      solveControls.add(algoOptions);
      solveControls.add(new JLabel("Neighbours"));
      solveControls.add(neighbourOptions);
      solveControls.add(new JLabel("Multi Threading"));
      solveControls.add(threadBox);
      JButton help = new JButton("Help");
      solveControls.add(help);
      optionPanel.add(solveControls);

      //Add functionality to the help link
      help.addActionListener(e1 -> {
        String helpText = "ALGORITHMS\n\n" +
                "AStar: This is the most efficient\n" +
                "search and is recommended in most circumstances.\n\n" +
                "Breath First: This search is guaranteed to find\n" +
                "the shortest route. However it can be memory\n" +
                "intensive and slow\n\n" +
                "Depth First: The most basic algorithm. Usually\n" +
                "gets the job done, but can be memory intensive,\n" +
                "slow and meandering\n\n" +
                "Dijkstra: Similar to AStar but slightly inferior\n" +
                "performance\n\n\n" +
                "NEIGHBOURS\n\n" +
                "During Loading: Find all of the nodes in the maze\n" +
                "before starting to solve. May lead to faster solve\n" +
                "times but is more memory intensive.\n\n" +
                "Solving: Find only the necessary nodes while\n" +
                "solving the maze. This has better memory performance\n" +
                "but may be slower.\n\n" +
                "MULTI THREADING\n" +
                "The maze will be solved using two concurrent threads.\n" +
                "It is recommend that this is only used for large mazes as\n" +
                "the performance tends to be poor on small mazes.";


        JPanel helpPanel = new JPanel();
        helpPanel.setLayout(new BoxLayout(helpPanel, BoxLayout.Y_AXIS));
        JTextArea helpTextPanel = new JTextArea();
        helpTextPanel.setEditable(false);
        helpTextPanel.setText(helpText);
        helpTextPanel.setBackground(backgroundCol);
        helpPanel.add(helpTextPanel);

        JButton exit = new JButton("exit");
        helpPanel.add(exit);
        helpPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        cancelButtons.add(exit);
        makePopup(helpPanel, cancelButtons, new Dimension(400, 550));
      });

      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(new FlowLayout());
      JButton solveButton = new JButton("Solve");
      JButton cancel = new JButton("Cancel"); //Cancel functionality is added to the button in the make popup method

      //Start the solve process
      solveButton.addActionListener(e12 -> {
        String algorithm = (String) algoOptions.getSelectedItem();
        String params = (String) neighbourOptions.getSelectedItem();
        Boolean multiThread = threadBox.isSelected();

        makeAlgoWorkingScreen(algoMainArea, algorithm, params, multiThread, application);
      });

      cancelButtons.clear();
      cancelButtons.add(cancel);
      cancelButtons.add(solveButton);

      buttonPanel.add(cancel);
      buttonPanel.add(solveButton);

      optionPanel.add(buttonPanel);

      makePopup(optionPanel, cancelButtons, new Dimension(350, 200));
    });

    main.add(control);

    algoMainArea.add(main);
    refresh();
  }

  /**
   * Make the screen that shows a spinner while the algorithm is solving.
   */
  public void makeAlgoWorkingScreen(JPanel mainArea, String algorithm, String params, Boolean multiThread, Application application) {
    mainArea.removeAll();

    //Add the spinner
    mainArea.add(new SpinnerPanel());
    refresh();

    Thread solveThread = application.solve(algorithm, params, multiThread, 0, null);

    //Create another thread that will only wait for algorithm to finish
    Thread algoWait = new Thread() {
      @Override
      public void run() {
        super.run();
        try {
          solveThread.start();
          solveThread.join();
          makeAlgoSolvedScreen(mainArea, application);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    };

    algoWait.start();
  }

  /**
   * Show the completed image on screen
   */
  public void makeAlgoSolvedScreen(JPanel algoMainArea, Application currentApplication) {
    algoMainArea.removeAll();

    System.out.println("Making algorithm solved screen");
    JPanel main = new JPanel();
    main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

    main.add(new Scroll(currentApplication.getImage()));

    //Add the control panel
    JPanel control = new JPanel();
    control.setLayout(new FlowLayout());

    control.setPreferredSize(new Dimension(500, 100));
    control.setBackground(backgroundCol);

    JButton loadOther = new JButton("Load Another Image");
    JButton reset = new JButton("Reset This Image");
    JButton save = new JButton("Save");

    control.add(loadOther);
    control.add(reset);
    control.add(save);

    //Bind the functionality
    loadOther.addActionListener(e -> makeLoadScreen("Algorithm"));
    reset.addActionListener(e -> resetImage());
    save.addActionListener(e -> saveImage());

    main.add(control);

    algoMainArea.add(main);

    refresh();
  }

  /**
   * Save the image
   */
  private void saveImage() {
    JFileChooser save = new JFileChooser();
    int ret = save.showSaveDialog(window);
    if (ret == JFileChooser.APPROVE_OPTION) {
      String fileName = save.getSelectedFile().getName();
      String directory = save.getCurrentDirectory().toString();
      System.out.println(fileName);
      System.out.println(directory);
      System.out.println("Concat: " + directory + "/" + fileName);
      application.saveImage(directory + "/" + fileName);
    } else if (ret == JFileChooser.CANCEL_OPTION) {
      System.out.println("You pressed cancel");
    }
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
    window.revalidate();
    window.repaint();
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

  public void makePopup(JPanel panelToDisplay, ArrayList<JButton> exitButtons, Dimension panelDimensions) {
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
