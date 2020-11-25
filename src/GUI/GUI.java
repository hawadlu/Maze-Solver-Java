package GUI;

import Application.Application;
import GUI.CustomPanels.Scroll;
import Utility.Exceptions.GenericError;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Stack;

public class GUI {
  Application application;

  public final int width = 1280;
  public final int height = 800;
  public static final Color activeCol = new Color(0, 131, 233);
  public static final Color inactiveCol = new Color(66, 66, 66);
  public static final Color backgroundCol = new Color(211, 211,211);

  //Panels
  static JFrame window;
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
    loadPanel.setBackground(backgroundCol);
    loadPanel.setMinimumSize(new Dimension(width, height));
    JButton loadImage = new JButton("Load Image");
    
    //Bind functionality to the load image button
    loadImage.addActionListener(e -> {
      try {
        application.parseImageFile(UIFileChooser());
      } catch (GenericError genericError) {
        genericError.printStackTrace();
      }
  
      //Load the image to the main screen
      algoMainArea.removeAll();
      algoMainArea.add(makeAlgoSolveScreen()); //Add the algo solve area to the main panel
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

    main.add(new Scroll(getImage()));

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
    solve.addActionListener(e -> {
      //Make a popup JPanel
      JPanel optionPanel = new JPanel();
      optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));
      optionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
      JPanel solveControls = new JPanel();
      solveControls.setLayout(new GridLayout(0, 2));


      //The popup options
      JComboBox algoOptions = new JComboBox(new String[]{"AStar", "Breadth First", "Depth First", "Dijkstra"});
      JComboBox neighbourOptions = new JComboBox(new String[]{"During Loading", "During Solving"});

      solveControls.add(new JLabel("Image"));
      solveControls.add(new JLabel(getImageInfo("name")));
      solveControls.add(new JLabel("Algorithm"));
      solveControls.add(algoOptions);
      solveControls.add(new JLabel("Neighbours"));
      solveControls.add(neighbourOptions);
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
                "During Solving: Find only the necessary nodes while\n" +
                "solving the maze. This has better memory performance\n" +
                "but may be slower.";


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
        makePopup(helpPanel, exit, new Dimension(400, 500));
      });

      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(new FlowLayout());
      JButton solveButton = new JButton("Solve");
      JButton cancel = new JButton("Cancel"); //Cancel functionality is added to the button in the make popup method

      //Start the solve process
      solveButton.addActionListener(e12 -> {
        String algorithm = (String) algoOptions.getSelectedItem();
        String params = (String) neighbourOptions.getSelectedItem();
        application.solve(algorithm, params);

        //todo make a spinner thread
        System.out.println("Solve completed");


      });

      buttonPanel.add(cancel);
      buttonPanel.add(solveButton);

      optionPanel.add(buttonPanel);

      makePopup(optionPanel, cancel, new Dimension(350, 200));
    });

    main.add(control);
    
    return main;
  }

  /**
   * Get a specified piece of information about the image
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
  public static void refresh() {
//    System.out.println("Refreshing gui");
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
   * Get the current maze image from the applciation
   * @return
   */
  public BufferedImage getImage() {
    return application.getImage();
  }

  /**
   * Get the dimensions of the maze
   * @return dimensions
   */
  public Dimension getMazeDimensions() {
    return application.getMazeDimensions();
  }

  public void makePopup(JPanel panelToDisplay, JButton exit, Dimension panelDimensions) {
    System.out.println("Making popup");
    JFrame frame = new JFrame();
    frame.setSize(panelDimensions);
    frame.setBackground(backgroundCol);
    frame.add(panelToDisplay);
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    //Add the button functionality
    exit.addActionListener(e -> {
      frame.dispose();
    });

    frame.setVisible(true);
  }
}
