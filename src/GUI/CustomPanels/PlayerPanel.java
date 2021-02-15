package GUI.CustomPanels;

import Game.Player;
import Server.Requests;
import parser.Parser;
import Image.ImageFile;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

import static GUI.GUI.*;

/**
 * This class holds all of the interactions used when playing the game
 */
public class PlayerPanel extends JPanel {
  JButton parser = null;
  Dimension imageSize;
  Player player;
  Scroll scrollPanel;
  String[] algorithms = {"AStar", "Dijkstra", "Depth First", "Breadth First"};
  JComboBox<String> inbuiltAlgorithms = new JComboBox<>(algorithms);


  public PlayerPanel(Dimension maxSize, Player player) {
    this.setBackground(Color.CYAN);
    this.setBorder(BorderFactory.createLineBorder(Color.GREEN));
    this.setPreferredSize(maxSize);
    this.imageSize = new Dimension(500, 500);
    this.player = player;

    makeSolveLayout();
  }

  /**
   * Make the solve layout for the panel
   */
  private void makeSolveLayout() {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    //The player title
    JLabel title = new JLabel(player.getName());
    title.setAlignmentX(CENTER_ALIGNMENT);
    this.add(title);

    this.add(inbuiltAlgorithms);

    //The button to parse an algorithm
    if (parser == null) {
      parser = new JButton("Load Custom Algorithm");

      parser.addActionListener(e -> {
        this.player.setCustomAlgo(new Parser(GUI.GUI.UIFileChooser(), player));
      });
    }
    this.add(parser);
  }

  /**
   * Update this panel to display the image to be solved
   */
  public void initSolvePanel() {
    this.removeAll();
    this.scrollPanel = new Scroll(player.getImageFile().makeImage());
    this.add(scrollPanel);

    //refresh the gui
    refresh();
  }

  /**
   * Update the image
   */
  public void updateImage(ImageFile displayImage) {
    if (scrollPanel != null) {
      scrollPanel = new Scroll(displayImage.makeImage());

      this.removeAll();
      this.add(scrollPanel);
      this.revalidate();
      this.repaint();
    }
  }

  /**
   * @return the selected algorithm
   */
  public String getAlgorithm() {
    return Objects.requireNonNull(inbuiltAlgorithms.getSelectedItem()).toString();
  }

  /**
   * @param message to display in the panel
   */
  public void displayMessage(String message) {
    this.add(new JLabel("Game message: " + message));

    refresh();
  }

  /**
   * Mark this panel as done.
   * @param message the place in which this player finished
   */
  public void markDone(String message, ImageFile completeImage) {
    this.removeAll();
    this.displayMessage(message);
    this.scrollPanel.updateImage(completeImage.makeImage());

    //todo may have to change this
    refresh();
  }

  /**
   * Get the player.
   * @return the player object.
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Set the player object.
   * @param player The player.
   */
  public void setPlayer(Player player) {
    this.player = player;
  }

  /**
   * Make this screen into a screen of buttons for algorithm solving
   */
  public void setAlgoSolveScreen() {
    this.removeAll();
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    this.add(new Scroll(player.getImageFile().makeImage()));

    //Add the control panel
    JPanel control = new JPanel();
    control.setLayout(new FlowLayout());

    control.setPreferredSize(new Dimension(500, 100));
    control.setBackground(backgroundCol);

    JButton solve = new JButton("solve");
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
      solveControls.add(new JLabel(player.getImageFile().getInfo("name")));
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
      JButton solveButton = new JButton("solve");
      JButton cancel = new JButton("Cancel"); //Cancel functionality is added to the button in the make popup method

      //Start the solve process
      solveButton.addActionListener(e12 -> {

        //This algorithm only works on a single thread and while searching for neighbours on load
        makeAlgoWorkingScreen();
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
      solveControls.add(new JLabel(player.getImageFile().getInfo("name")));
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
      JButton solveButton = new JButton("solve");
      JButton cancel = new JButton("Cancel"); //Cancel functionality is added to the button in the make popup method

      //Start the solve process
      solveButton.addActionListener(e12 -> {
        String algorithm = (String) algoOptions.getSelectedItem();

        //All of these algorithms search for neighbours on load and do not multi thread
        makeAlgoWorkingScreen();

//        player.solve(algorithm, );
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
      JComboBox algoOptions = new JComboBox(new String[]{"Custom", "AStar", "Breadth First", "Depth First", "Dijkstra"});
      JComboBox neighbourOptions = new JComboBox(new String[]{"Loading", "Solving"});
      JCheckBox threadBox = new JCheckBox();

      solveControls.add(new JLabel("Image"));
      solveControls.add(new JLabel(player.getImageFile().getInfo("name")));
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
                "Custom: You may use you own algorithm.\n" +
                "It will run on a single thread and the\n" +
                "neighbours will be initialised prior to\n" +
                "execution.\n\n" +
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
        makePopup(helpPanel, cancelButtons, new Dimension(400, 750));
      });

      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(new FlowLayout());
      JButton solveButton = new JButton("solve");
      JButton cancel = new JButton("Cancel"); //Cancel functionality is added to the button in the make popup method

      //Start the solve process
      solveButton.addActionListener(e12 -> {
        String algorithm = (String) algoOptions.getSelectedItem();
        String params = (String) neighbourOptions.getSelectedItem();
        Boolean multiThread = threadBox.isSelected();

        //Load the users own algorithm if requested
        Parser parser = null;
        if (algorithm.equals("Custom")) {
          //Setup the parser
          parser = new Parser(UIFileChooser(), player);
        }


        //make this into a working screen and then kick off the solver
        makeAlgoWorkingScreen();

        System.out.println("Solving");
        player.solve(algorithm, params, multiThread);
      });

      cancelButtons.clear();
      cancelButtons.add(cancel);
      cancelButtons.add(solveButton);

      buttonPanel.add(cancel);
      buttonPanel.add(solveButton);

      optionPanel.add(buttonPanel);

      makePopup(optionPanel, cancelButtons, new Dimension(350, 200));
    });

    this.add(control);
  }

  /**
   * Make the screen that shows a spinner while the algorithm is solving.
   */
  public void makeAlgoWorkingScreen() {

    //Add the spinner
    this.removeAll();
    this.add(new SpinnerPanel());
    refresh();
  }

  /**
   * Show the completed image on screen
   */
  public void makeAlgoSolvedScreen(ImageFile imageFile) {
    this.removeAll();

    System.out.println("Making algorithm solved screen");
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    this.add(new Scroll(imageFile.makeImage()));

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
    loadOther.addActionListener(e -> player.makeLoadScreen("Algorithm"));
    reset.addActionListener(e -> makeSolveLayout());
    save.addActionListener(e -> saveImage(player.fillPath()));

    this.add(control);

    refresh();
  }

  /**
   * Save the image
   */
  private void saveImage(ImageFile imageFile) {
    JFileChooser save = new JFileChooser();
    int ret = save.showSaveDialog(new JFrame());
    if (ret == JFileChooser.APPROVE_OPTION) {
      String fileName = save.getSelectedFile().getName();
      String directory = save.getCurrentDirectory().toString();
      System.out.println(fileName);
      System.out.println(directory);
      System.out.println("Concat: " + directory + "/" + fileName);
      imageFile.saveImage(directory + "/" + fileName);
    } else if (ret == JFileChooser.CANCEL_OPTION) {
      System.out.println("You pressed cancel");
    }
  }

  /**
   * Make this into a game screen.
   */
  public void makeGameScreen() {
    this.removeAll();

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    //Add the image that is being used
    if (scrollPanel == null) scrollPanel = new Scroll(player.getImageFile().makeImage());

    this.add(scrollPanel);
    scrollPanel.updateImage(player.getImageFile().makeImage());

    JPanel controls = new JPanel();
    controls.setLayout(new BoxLayout(controls, BoxLayout.X_AXIS));

    String[] algorithms = {"AStar", "Dijkstra", "Breadth First", "Depth First"};

    inbuiltAlgorithms = new JComboBox<>(algorithms);

    JButton customAlgo = new JButton("Custom Algorithm");

    //load the custom algorithm
    customAlgo.addActionListener(e -> {
      player.setCustomAlgo(new Parser(GUI.GUI.UIFileChooser(), true, player));
    });

    controls.add(inbuiltAlgorithms);
    controls.add(customAlgo);

    this.add(controls);
  }

  public void makeSolvingScreen() {
    this.removeAll();

    if (this.scrollPanel == null) scrollPanel = new Scroll(player.getImageFile().makeImage());
    this.add(scrollPanel);

    this.revalidate();
    this.repaint();
  }

  /**
   * Make the screen that is displayed when waiting for an online game to start
   */
  public void makeOnlineWaitingScreen() {
    this.removeAll();

    if (this.scrollPanel == null) {
      scrollPanel = new Scroll(player.getImageFile().makeImage());
    }
    this.add(scrollPanel);

    //Only add if all players are ready and this is the local player
    if (player.hasOpponent()) {
      //If this is the local player add setup controls

      if (this.player.isLocal()) {
        //Create the setup panel
        JPanel setup = new JPanel();
        setup.add(inbuiltAlgorithms);

        //Add button for custom algorithms
        JButton custom = new JButton("Custom Algorithm");
        custom.addActionListener(e -> {
          //load and compile the algorithm
          player.setCustomAlgo(new Parser(GUI.GUI.UIFileChooser(), true, player));
        });
        this.add(custom);

        setup.add(custom);

        JButton readyButton = new JButton("Press When Ready");
        readyButton.addActionListener(e -> {
          System.out.println(player.getName() + " is ready.");

          player.sendMessage(Requests.ready);
        });
        setup.add(readyButton);
        this.add(setup);
      } else {
        //This is the online player, just add the image
        this.removeAll();
        this.add(new Scroll(player.getImageFile().makeImage()));
      }
    } else {
      if (!player.isLocal()) {
        this.add(new JLabel("Waiting for others to join"));
      }
    }

    this.revalidate();
    this.repaint();
  }
}
