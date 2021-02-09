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
   * Send a request to the server.
   * @param request the request to send.
   */
  public void sendRequest(String request) {
    try {
      dataOut.writeUTF(request);
      dataOut.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Empty the dataIn buffer to get any response from the server
   * @return anything in the dataIn buffer.
   */
  public String getResponse() {
    String message = null;
    System.out.println("Waiting for server response");
    try {
      message = dataIn.readUTF();
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("Server response: " + message);
    return message;
  }
}
