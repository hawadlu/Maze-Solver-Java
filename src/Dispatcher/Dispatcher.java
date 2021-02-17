package Dispatcher;

import GUI.GUI;
import Game.Player;
import Image.*;
import Server.LocalClient;
import Server.Requests;
import Utility.Exceptions.InvalidImage;
import Utility.LocationList;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;


/**
* todo describe this class
 */
public class Dispatcher {
  Dimension playerDimensions = new Dimension(GUI.width / 2, (int) (GUI.height * 0.75));
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
   */
  public Dispatcher(ImageFile imageFile, int playersToCreate) {
    this.imageFile = new ImageFile(imageFile);

    for (int i = 0; i < playersToCreate; i++) {
      Player newPlayer = new Player("Player " + i, "Algorithm", this);
      this.players.add(newPlayer);
    }
  }

  /**
   * Create a new dispatcher that is used for online multiplayer.
   * Make one player local and the rest passive (updated by the server).
   *
   * @param client          the object that connects to the server.
   * @param playersToCreate the number of players to create
   */
  public Dispatcher(LocalClient client, int playersToCreate) {
    this.client = client;

    //The local player
    this.players.add(new Player("Player " + 0, "Algorithm", this));


    for (int i = 1; i < playersToCreate; i++) {
      this.players.add(new Player("Player" + i, "Algorithm", this, true));
    }
  }

  /**
   * Create a single player parser.
   *
   * @param customAlgo the file containing the custom algorithm.
   * @param imageFile  the imageFile to be used.
   */
  public Dispatcher(File customAlgo, ImageFile imageFile) {
    this.imageFile = new ImageFile(imageFile);

    Player newPlayer = new Player("Player One", customAlgo, this);
    newPlayer.initialiseHandler();

    if (this.players.size() == 0) this.players.add(newPlayer);
    else this.players.set(0, newPlayer);
  }

  /**
   * Set the gui object.
   *
   * @param gui the gui to be used.
   */
  public void initialiseGUI(GUI gui) {
    this.gui = gui;
  }

  /**
   * @return
   */
  public long getExecTime() {
    return players.get(0).getExecTime();
  }

  /**
   * @return
   */
  public double getMazeSize() {
    return imageFile.getDimensions().height * imageFile.getDimensions().width;
  }

  /**
   * @return
   */
  public ImageFile getImageFile() {
    return this.imageFile;
  }

  /**
   * Set the image file, initiate a scan and set the waiting screen.
   *
   * @param imageFile the new imageFile.
   */
  public void setImageFile(ImageFile imageFile) {
    this.imageFile = imageFile;
    new Thread(() -> players.get(0).scanAll()).start();

    makeOnlineWaitingScreen();
  }

  /**
   * Get the screen currently being displayed by a given player.
   *
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
   * @param algorithm      the algorithm to use.
   * @param params         look for neighbours on load or on solve.
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
   *
   * @return a boolean to indicate if live or not.
   */
  public boolean isLive() {
    return live;
  }

  /**
   * @param live
   */
  public void setLive(boolean live) {
    this.live = live;
  }

  /**
   * Tell the gui to reset the screen.d
   *
   * @param tab the tab to set it to.
   */
  public void makeLoadScreen(String tab) {
    gui.makeLoadScreen(tab);
  }

  /**
   * Create a component that renders two players in one screen.
   */
  public void makeGameScreen() {
    //reset the image
    imageFile.reset();

    //reset the players
    players.get(0).reset();
    players.get(1).reset();

    this.screen.removeAll();
    this.screen.setLayout(new GridBagLayout());

    //Define the required dimensions
    Dimension playerDimensions = new Dimension(GUI.width / 2, (int) (GUI.height * 0.75));
    Dimension controlDimensions = new Dimension(GUI.width, GUI.height / 4);

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
   *
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
   * make the online waiting screen
   */
  public void makeOnlineWaitingScreen() {
    //reset the image and players
    this.imageFile.reset();
    players.get(0).reset();
    players.get(1).reset();

    this.screen.removeAll();
    this.screen.setLayout(new GridBagLayout());

    //Define the required dimensions
    Dimension controlDimensions = new Dimension(GUI.width, GUI.height / 4);

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;

    //Set the player screens
    players.get(0).makeOnlineWaitingScreen();
    players.get(1).makeOnlineWaitingScreen();

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
    control.add(new JLabel("Room ID:" + client.getRoom()));

    this.screen.add(control, constraints);

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
   * @return
   */
  public JPanel getScreen() {
    return screen;
  }

  /**
   * @param screen
   */
  public void setScreen(JPanel screen) {
    this.screen = screen;
  }

  /**
   * Make the screen that is used to join/create an online game.
   * <p>
   * This contains button for creating and joining a game along
   * with their associated functionality.
   */
  public void makeOnlineStartScreen() {
    client.setDispatcher(this);

    //Ask for a username
    boolean validUser = false;
    String user = null;

    while (!validUser) {
      user = JOptionPane.showInputDialog("Enter a username");

      if (!user.matches(Pattern.compile("(\\s|[A-z0-9])*").pattern())) {
        JOptionPane.showMessageDialog(null, "Username: '" + user + "' does not match pattern " + Requests.username);
      } else {
        validUser = true;
      }
    }
    client.setLocalUserName(user);

    //Send the username to the server
    client.send("user: " + user);

    this.screen.removeAll();

    JButton createRoom = new JButton("Create New Game");
    createRoom.addActionListener(e -> {
      //Set the local player
      players.get(0).setLocal(true);

      try {
        this.imageFile = new ImageFile(GUI.UIFileChooser());
      } catch (InvalidImage invalidImage) {
        invalidImage.printStackTrace();
      }

      //Send the request to create the room.
      System.out.println("Sending create room request");
      client.send(Requests.createRoom);

      //Send the image file
      System.out.println("Sending set image request");
      client.send(imageFile);

      //Start a new thread and scan the image file
      new Thread(() -> players.get(0).scanAll()).start();
    });

    JButton joinRoom = new JButton("Join Game");

    //Send the request to  join the room
    joinRoom.addActionListener(e -> {
      //Ask the user to enter the room id
      //todo deal with cancel clicked.
      String roomId = JOptionPane.showInputDialog("Enter Room Id");

      client.send(Requests.joinRoom + ":" + roomId);

      //Set the local player
      players.get(0).setLocal(true);
    });

    //Mark both players as online
    players.get(0).setOnline(true);
    players.get(1).setOnline(true);

    screen.add(createRoom);
    screen.add(joinRoom);
  }

  /**
   * Send a message to the server.
   *
   * @param message the message to send
   */
  public void sendMessage(Object message) {
    client.send(message);
  }

  /**
   * Set the opponents in both players to true
   */
  public void setOpponentsReady() {
    players.get(0).setOpponent(true);
    players.get(1).setOpponent(true);
  }

  /**
   * Start the online players.
   * <p>
   * Note: only player 0 is started because the other player
   * is being updated by the server.
   */
  public void startOnline() {
    this.live = true;

    //Create a new thread and start this.
    //Note: Continuing on the same thread causes the LocalClient to stop listening for incoming messages
    Thread solver = new Thread() {
      @Override
      public void run() {
        super.run();
        players.get(0).solve();
      }
    };
    solver.start();
  }

  /**
   * Update the online player.
   *
   * @param locationList the list of locations to draw
   */
  public void updateOnlinePlayer(LocationList locationList) {
    players.get(1).update(locationList);
  }

  /**
   * Set the image processor in the local player.
   *
   * @param imageProcessor the image processor to use
   */
  public void setImageProcessor(ImageProcessor imageProcessor) {
    players.get(0).setImageProcessor(imageProcessor);
  }

  /**
   * Get the username from the client.
   * @param dimension is this player online or local
   * @return the username
   */
  public String getUserName(String dimension) {
    if (dimension.equals("local")) return client.getLocalUserName();
    else if (dimension.equals("online")) return client.getOnlineUserName();
    return null;
  }

  /**
   * Check the status of each of the players to see if they are done.
   * @param player the player that has reported done
   */
  public void checkStatus(Player player) {
    if (player.equals(players.get(0))) {
      if (!players.get(1).isDone()) player.makeDoneDisplay("1st");
      else player.makeDoneDisplay("2nd");
    } else if (player.equals(players.get(1))) {
      if (!players.get(0).isDone()) player.makeDoneDisplay("1st");
      else player.makeDoneDisplay("2nd");
    }
  }

  /**
   * Used by the server to indicate that the other player has finished.
   *
   * In this case it will update player 2 (players.get(1))
   */
  public void otherDone() {
    players.get(1).markDone();
    checkStatus(players.get(1));
  }

  /**
   * Process a requested restart from a player.
   * @param player the player that requested the restart.
   */
  public void requestRestart(Player player) {
    if (player.isOnline()) {
      client.send(Requests.requestRestart);
    } else {
      showRestartRequest();
    }
  }

  /**
   * Show a message saying that a restart has been requested.
   *
   * Process the response.
   */
  public void showRestartRequest() {
    int result;

    if (players.get(0).isOnline()) {
      result = JOptionPane.showConfirmDialog(new JFrame(), client.getOnlineUserName() + " wants a rematch", "Restart",
              JOptionPane.YES_NO_OPTION,
              JOptionPane.QUESTION_MESSAGE);
    } else {
      result = JOptionPane.showConfirmDialog(new JFrame(), "Do you want to restart?", "Restart",
              JOptionPane.YES_NO_OPTION,
              JOptionPane.QUESTION_MESSAGE);
    }

    if (result == JOptionPane.YES_OPTION) {
      if (players.get(0).isOnline()) {
        client.send(Requests.restart);
        makeOnlineWaitingScreen();
      } else {
        makeGameScreen();
      }
    } else if (result == JOptionPane.NO_OPTION) {
      //todo go to a home screen
    } else {
      //continue until a valid option selected
      showRestartRequest();
    }
  }
}
