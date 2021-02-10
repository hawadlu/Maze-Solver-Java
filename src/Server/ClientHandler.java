package Server;

import Image.ImageFile;

import java.io.*;
import java.net.Socket;

public class
ClientHandler extends Thread {
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
      Object message = null;
      try {
        message = dataIn.readObject();

        if (message instanceof String) {

          System.out.println("Client says: " + message);

          //Deal with the request
          if (message.equals(Requests.createRoom)) {
            this.currentRoom = createRoom();
          } else if (((String) message).contains(Requests.joinRoom)) {
            String[] tmp = ((String) message).split(":");

            int id = Integer.parseInt(tmp[1]);
            this.currentRoom = id;
            server.joinRoom(this, id);

            //Return the image file
            sendMessage(server.rooms.get(currentRoom).getImage());
          }
        } else if (message instanceof ImageFile) {
          server.rooms.get(currentRoom).setImageFile((ImageFile) message);
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
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
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
  private void sendMessage(Object message) {
    System.out.println("Sending message to client: " + message);

    try {
      dataOut.writeObject(message);
      dataOut.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
