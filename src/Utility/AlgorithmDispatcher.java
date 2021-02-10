package Utility;

import GUI.GUI;
import Game.Player;
import Image.*;
import Server.LocalClient;
import Server.Requests;
import Utility.Exceptions.InvalidImage;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;


/**
 * This thread takes is the one that processes the algorithms.
 * Using multiple threads allows multiple algorithms to run simultaneously.
 */
public class AlgorithmDispatcher {
  private ImageFile imageFile;
  private ArrayList<Player> players = new ArrayList<>();
  private boolean live = false; //used to indicate if this should run in live solve mode.
  private GUI gui;
  private JPanel screen = new JPanel();
  private LocalClient client;


  /**
   * Create a single player dispatcher for running an algorithm.
   * This is primarily used by the tests
   *
   * @param imageFile
   * @param playersToCreate the number of players that should be created.
   *
   */
  public AlgorithmDispatcher(ImageFile imageFile, int playersToCreate) {
    this.imageFile = new ImageFile(imageFile);

    for (int i = 0; i < playersToCreate; i++) {
      Player newPlayer = new Player("Player " + i, "Algorithm", this);
      this.players.add(newPlayer);
    }
  }

  /**
   * Create a new dispatcher that is used for online multiplayer.
   * @param client the object that connects to the server.
   * @param playersToCreate the number of players to create
   */
  public AlgorithmDispatcher(LocalClient client, int playersToCreate) {
    this.client = client;

    for (int i = 0; i < playersToCreate; i++) {
      Player newPlayer = new Player("Player " + i, "Algorithm", this);
      this.players.add(newPlayer);
    }
  }

  /**
   * Set the gui object.
   * @param gui the gui to be used.
   */
  public void initialiseGUI(GUI gui) {
    this.gui = gui;
  }

  /**
   * Create a single player parser.
   * @param customAlgo the file containing the custom algorithm.
   * @param imageFile the imageFile to be used.
   */
  public AlgorithmDispatcher(File customAlgo, ImageFile imageFile) {
    this.imageFile = new ImageFile(imageFile);

    Player newPlayer = new Player("Player One", customAlgo, this);
    newPlayer.initialiseHandler();

    if (this.players.size() == 0) this.players.add(newPlayer);
    else this.players.set(0, newPlayer);
  }

  public long getExecTime() {
    return players.get(0).getExecTime();
  }

  public double getMazeSize() {
    return imageFile.getDimensions().height * imageFile.getDimensions().width;
  }

  public ImageFile getImageFile() {
    return this.imageFile;
  }

  /**
   * Get the screen currently being displayed by a given player.
   * @param index the index of the player to get the screen from.
   * @return a JComponent containing the screen.
   */
  public Component getPlayerScreen(int index) {
    return players.get(index).getScreen();
  }

  /**
   * Call the solve method in the player.
   * Primarily used for testing
   *
   * @param algorithm the algorithm to use.
   * @param params look for neighbours on load or on solve.
   * @param multiThreading should the algorithm run multithreaded.
   */
  public void solve(String algorithm, String params, boolean multiThreading) {
    players.get(0).solve(algorithm, params, multiThreading);
  }

  /**
   * Call the solve method in the player.
   * Primarily used for testing
   *
   * @param algorithm the algorithm to use.
   */
  public void solve(String algorithm) {
    players.get(0).solve(algorithm);
  }

  /**
   * Call the solve method on the player.
   * This is used when the player is using a custom algorithm.
   */
  public void solve() {
    players.get(0).solve();
  }

  /**
   * Get the player to create an imageFile and fill it in.
   * This is used when two algorithms are running at once.
   *
   * @return the resultant imageFile
   */
  public ImageFile fillPlayerPath() {
    return players.get(0).fillPath();
  }

  /**
   * Check if this is running in live mode.
   * @return a boolean to indicate if live or not.
   */
  public boolean isLive() {
    return live;
  }

  /**
   * Tell the gui to reset the screen.d
   * @param tab the tab to set it to.
   */
  public void makeLoadScreen(String tab) {
    gui.makeLoadScreen(tab);
  }

  /**
   * Create a component that renders two players in one screen.
   * @param width the width of the screen.
   * @param height the height of the screen.
   */
  public void makeGameScreen(int width, int height) {
    this.screen.removeAll();
    this.screen.setLayout(new GridBagLayout());

    //Define the required dimensions
    Dimension playerDimensions = new Dimension(width / 2, (int) (height * 0.75));
    Dimension controlDimensions = new Dimension(width, height / 4);

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;

    //Set the player screens
    players.get(0).makeGameScreen();
    players.get(1).makeGameScreen();

    JPanel playerOne = players.get(0).getScreen();
    JPanel playerTwo = players.get(1).getScreen();
    JPanel control = new JPanel();

    playerOne.setPreferredSize(playerDimensions);
    playerTwo.setPreferredSize(playerDimensions);
    control.setPreferredSize(controlDimensions);

    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.gridwidth = 1;
    constraints.gridheight = 3;

    this.screen.add(playerOne, constraints);

    constraints.gridx = 1;

    this.screen.add(playerTwo, constraints);

    constraints.gridx = 0;
    constraints.gridy = 4;
    constraints.gridwidth = 2;
    constraints.gridheight = 1;

    //setup the control panel
    control.setLayout(new FlowLayout(FlowLayout.LEADING));

    //Create the allowable delays
    String[] delays = new String[]{"5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60", "65", "70", "75", "80", "85", "90", "95", "100"};

    JLabel delayTitle = new JLabel("Set delay (ms)");
    JComboBox<String> delay = new JComboBox<>(delays);

    final JButton[] solve = {new JButton("Waiting for node scan to finish")};
    solve[0].setEnabled(false);

    //Bind functionality to the solve button
    solve[0].addActionListener(e -> {
      //make the solving screen
      makeSolvingScreen(playerDimensions);

      this.live = true;

      //Create a new thread for each of the players
      Thread pOne = new Thread(() -> {
        players.get(0).solve();
      });

      Thread pTwo = new Thread(() -> {
        players.get(1).solve();
      });

      pOne.start();
      pTwo.start();
    });

    JPanel delayPanel = new JPanel();
    delayPanel.setLayout(new BoxLayout(delayPanel, BoxLayout.Y_AXIS));
    delayPanel.add(delayTitle);
    delayPanel.add(delay);

    control.add(delayPanel);
    control.add(solve[0]);

    //Create a new thread and have the players perform a node scan
    Thread scannerThread = new Thread(() -> {
      System.out.println("Started scan");

      players.get(0).scanAll();
      players.get(1).scanAll();

      //Update the control panel
      solve[0].setText("Solve");
      solve[0].setEnabled(true);

      System.out.println("Finished node scan");
    });

    this.screen.add(control, constraints);

    scannerThread.start();
  }

  /**
   * Make the screen that is used for live solving
   * @param playerDimensions the size of each player screen.
   */
  private void makeSolvingScreen(Dimension playerDimensions) {
    this.screen.removeAll();

    players.get(0).makeSolvingScreen();
    players.get(1).makeSolvingScreen();

    players.get(0).getScreen().setPreferredSize(playerDimensions);
    players.get(1).getScreen().setPreferredSize(playerDimensions);

    this.screen.add(players.get(0).getScreen());
    this.screen.add(players.get(1).getScreen());

    this.screen.revalidate();
    this.screen.repaint();
  }

  /**
   * @return the arraylist of players
   */
  public ArrayList<Player> getPlayers() {
    return players;
  }

  /**
   *
   * @param live
   */
  public void setLive(boolean live) {
    this.live = live;
  }

  public JPanel getScreen() {
    return screen;
  }

  public void setScreen(JPanel screen) {
    this.screen = screen;
  }

  /**
   * Make the screen that is used to join/create an online game.
   * @param width
   * @param height
   */
  public void makeOnlineStartScreen(int width, int height) {
    Dimension playerDimensions = new Dimension(width / 2, (int) (height * 0.75));

    this.screen.removeAll();

    JButton createRoom = new JButton("Create New Game");
    createRoom.addActionListener(e -> {
      try {
        this.imageFile = new ImageFile(GUI.UIFileChooser());
      } catch (InvalidImage invalidImage) {
        invalidImage.printStackTrace();
      }

      //Send the request to create the room.
      System.out.println("Sending create room request");
      client.send(Requests.createRoom);
      Object response = client.getResponse();

      //Send the image file
      System.out.println("Sending set image request");
      client.send(imageFile);
//      client.send(Requests.setImage + ":" + imageFile.makeJson());

      //Set the player screens
      makeSolvingScreen(playerDimensions);
    });

    JButton joinRoom = new JButton("Join Game");

    //Send the request to  join the room
    joinRoom.addActionListener(e -> {
      //Ask the user to enter the room id
      String roomId = JOptionPane.showInputDialog("Enter Room Id");

      client.send(Requests.joinRoom + ":" + roomId);

      this.imageFile = (ImageFile) client.getResponse();

      makeSolvingScreen(playerDimensions);
    });


    screen.add(createRoom);
    screen.add(joinRoom);
  }
}
