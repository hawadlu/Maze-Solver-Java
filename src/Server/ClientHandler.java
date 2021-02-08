package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {
  DataInputStream dataIn;
  DataOutputStream dataOut;
  Socket connectedSocket;
  Server server;

  public ClientHandler(Server server, Socket connection) {
    try {
      this.dataIn = new DataInputStream(connection.getInputStream());
      this.dataOut = new DataOutputStream(connection.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.server = server;
    this.connectedSocket = connection;
  }

  @Override
  public void run() {
    super.run();

    System.out.println("Client is running");

    while (true) {
      String message = null;
      try {
        message = (String) dataIn.readUTF();
        System.out.println("Client says: " + message);

        //Deal with the request
        if (message.equals(Requests.createRoom)) createRoom();
      } catch (IOException e) {
        try {
          connectedSocket.close();
        } catch (IOException ioException) {
          ioException.printStackTrace();
        }
        System.out.println("Error, socket has been automatically closed: ");
        e.printStackTrace();
        return;
      }
    }
  }

  /**
   * Create a new room and send a unique invite code back to the client
   */
  private void createRoom() {
    int invite = server.createRoom(this);

    sendMessage("" + invite);
  }

  /**
   * Send a message back to the client.
   */
  private void sendMessage(String message) {
    System.out.println("Sending message to client: " + message);

    try {
      dataOut.writeUTF(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
