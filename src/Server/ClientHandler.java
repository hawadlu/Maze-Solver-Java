package Server;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
  ObjectInputStream dataIn;
  ObjectOutputStream dataOut;
  Socket connectedSocket;
  Server server;
  int currentRoom;

  public ClientHandler(Server server, Socket connection) {
    try {
      this.dataIn = new ObjectInputStream(connection.getInputStream());
      this.dataOut = new ObjectOutputStream(connection.getOutputStream());
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
        if (message.equals(Requests.createRoom)) {
          this.currentRoom = createRoom();
        }
        else if (message.contains(Requests.joinRoom)) {
          String[] tmp = message.split(":");

          int id = Integer.parseInt(tmp[1]);
          this.currentRoom = id;
          server.joinRoom(this, id);
        } else if (message.contains(Requests.setImage)) {
          message.replaceAll(Requests.setImage + ": ", "");
          server.rooms.get(currentRoom).setImageFile(message);
        }
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
   * @return the room id
   */
  private int createRoom() {
    int invite = server.createRoom(this);

    sendMessage("" + invite);
    return invite;
  }

  /**
   * Send a message back to the client.
   */
  private void sendMessage(String message) {
    System.out.println("Sending message to client: " + message);

    try {
      dataOut.writeUTF(message);
      dataOut.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
