package Server;

import Image.ImageFile;
import Utility.AlgorithmDispatcher;
import Utility.LocationList;

import java.io.*;
import java.net.Socket;

/**
 * This class handles interaction from the users computer to the server.
 * <p>
 * It contains methods to connect to, send messages to and constantly listen to messages from the server.
 */
public class LocalClient extends Thread {
  private ObjectOutputStream dataOut;
  private ObjectInputStream dataIn;
  private AlgorithmDispatcher dispatcher;
  private String localUserName;
  private String onlineUserName;
  private int roomId;

  /**
   * Set the dispatcher
   */
  public void setDispatcher(AlgorithmDispatcher dispatcher) {
    this.dispatcher = dispatcher;
  }

  /**
   * Initiate a connection to the server
   */
  public void connect() {
    int port = 5000;
    String host = "localhost";
    try {
      Socket connection = new Socket(host, port);

      dataOut = new ObjectOutputStream(connection.getOutputStream());
      dataIn = new ObjectInputStream(connection.getInputStream());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Send an object via the socket
   *
   * @param toSend the object to send
   */
  public void send(Object toSend) {
    try {
      dataOut.writeObject(toSend);
      dataOut.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    super.run();

    System.out.println("Local client listening for messages");
    //Constantly listen for messages
    while (true) {
      System.out.println("Listening");
      try {
        Object message = dataIn.readObject();
        System.out.println("Message from server: " + message);

        if (message instanceof ImageFile) {
          dispatcher.setImageFile((ImageFile) message);
        } else if (message instanceof LocationList) {
          dispatcher.updateOnlinePlayer((LocationList) message);
        } else if (message instanceof String) {
          if (message.equals(Requests.wait)) {
            dispatcher.makeOnlineWaitingScreen();
          } else if (message.equals(Requests.makeSetup)) {
            dispatcher.setOpponentsReady();
            dispatcher.makeOnlineWaitingScreen();
          } else if (message.equals(Requests.start)) {
            dispatcher.startOnline();
          } else if (((String) message).matches(Requests.room.pattern())) {
            //Remove the actual room id from the string and set it
            this.roomId = Integer.parseInt(((String) message).replace("room: ", ""));
          } else if (((String) message).matches(Requests.username.pattern())) {
            this.onlineUserName = ((String) message).replace("user: ", "");
          } else if (message.equals(Requests.otherDone)) {
            dispatcher.otherDone();
          } else if (message.equals(Requests.requestRestart)) {
            dispatcher.showRestartRequest();
          }
        }
      } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * @return the username.
   */
  public String getLocalUserName() {
    return localUserName;
  }

  /**
   * Set the username.
   * @param localUserName the username to use.
   */
  public void setLocalUserName(String localUserName) {
    this.localUserName = localUserName;
  }

  /**
   * @return the current room id
   */
  public int getRoom() {
    return roomId;
  }

  /**
   * @return the username for the online player
   */
  public String getOnlineUserName() {
    return onlineUserName;
  }

  /**
   * Set the username for the online user.
   * @param onlineUserName the user name
   */
  public void setOnlineUserName(String onlineUserName) {
    this.onlineUserName = onlineUserName;
  }
}
