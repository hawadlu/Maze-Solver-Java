package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class deals with the initial connection of new clients
 * and contains a map of rooms into which the clients are divided.
 */
public class Server {
  int socketNum;
  ServerSocket socket;
  ArrayList<ClientHandler> clients = new ArrayList<>();
  HashMap<Integer, Room> rooms = new HashMap();


  /**
   * Create a new server.
   *
   * @param socketNum the socket to communicate through.
   */
  public Server(int socketNum) {
    this.socketNum = socketNum;
  }

  /**
   * Bind to the requested port.
   */
  public void bind() {
    System.out.println("Attempting to bind to socket " + socketNum);

    try {
      socket = new ServerSocket(socketNum);
      System.out.println("Successfully bound to socket " + socketNum);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Listen for incoming connections.
   */
  public void listen() {
    System.out.println("Server listening");

    while (true) {
      try {
        Socket accept = socket.accept();

        System.out.println("Client connected");

        //Create a new thread to handle the connection to this client
        ClientHandler client = new ClientHandler(this, accept);

        clients.add(client);
        client.start();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Create a new room.
   *
   * @param clientHandler the client that requested that the room be created.
   * @return an int containing the room id.
   */
  public int createRoom(ClientHandler clientHandler) {
    //Generate keys until a free one is found
    int roomKey;
    while (true) {
      roomKey = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);

      if (!rooms.containsKey(roomKey)) break;
    }

    rooms.put(roomKey, new Room(clientHandler));

    return roomKey;
  }

  /**
   * Join a room
   *
   * @param clientHandler the client that wants to join the room.
   * @param roomId the id of the room that the client wants to join.
   */
  public void joinRoom(ClientHandler clientHandler, int roomId) {
    rooms.get(roomId).join(clientHandler);
  }

  /**
   * Close the specified room.
   * @param currentRoom the id of the room to close
   */
    public void closeRoom(int currentRoom) {
      rooms.remove(currentRoom);
    }

  /**
   * Start the server
   */
  public static void main(String[] args) {
    int socket = Integer.parseInt(args[0]);
    Server server = new Server(socket);
    server.bind();
    server.listen();
  }
}
