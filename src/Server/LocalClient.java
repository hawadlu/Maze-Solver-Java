package Server;

import java.io.*;
import java.net.Socket;

/**
 * This class handles interaction from the users computer to the server
 */
public class LocalClient {
  ObjectOutputStream dataOut;
  ObjectInputStream dataIn;

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

  /**
   * Empty the dataIn buffer to get any response from the server
   * @return anything in the dataIn buffer.
   */
  public Object getResponse() {
    Object message = null;
    System.out.println("Waiting for server response");
    try {
      message = dataIn.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }

    System.out.println("Server response: " + message);
    return message;
  }
}
