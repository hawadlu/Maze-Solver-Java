package Server;

import Image.ImageFile;
import Utility.LocationList;

import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class
ClientHandler extends Thread {
  ObjectInputStream dataIn;
  ObjectOutputStream dataOut;
  Socket connectedSocket;
  Server server;
  int currentRoom;
  boolean ready = false;
  String username;

  //The chance of a collision within a room is very small, but for large scale deployment a better solution may be needed.
  int id = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);

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

            //Send the room id
            sendMessage("room: " + currentRoom);

            //Tell the user to create the wait screen
            sendMessage(Requests.wait);
          } else if (((String) message).contains(Requests.joinRoom)) {
            String[] tmp = ((String) message).split(":");

            int id = Integer.parseInt(tmp[1]);
            this.currentRoom = id;
            server.joinRoom(this, id);

            //send the user names to both users.
            server.rooms.get(currentRoom).sendUserNames();

            //Send the room id to the current player
            sendMessage("room: " + currentRoom);

            //Return the image file
            sendMessage(server.rooms.get(currentRoom).getImage());

            //Tell the room to make the setup screen for both players
            server.rooms.get(currentRoom).setup();
          } else if (((String) message).contains(Requests.ready)) {
            //Mark this client as ready
            this.ready = true;

            //Check if the other clients are ready
            server.rooms.get(currentRoom).checkReadiness();
          } else if (((String) message).matches(Requests.username.pattern())) {
            //set the clients username
            username = ((String) message).replace("user: ", "");
          }
        } else if (message instanceof ImageFile) {
          server.rooms.get(currentRoom).setImageFile((ImageFile) message);
        } else if (message instanceof LocationList) {
          //Tell the room to send the list to the other player
          server.rooms.get(currentRoom).sendLocationList(this, (LocationList) message);
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

    sendMessage("room:" + invite);
    return invite;
  }

  /**
   * Send a message back to the client.
   */
  public void sendMessage(Object message) {
    System.out.println("Sending message to client: " + message.toString());

    try {
      dataOut.writeObject(message);
      dataOut.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ClientHandler that = (ClientHandler) o;
    return id == that.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
