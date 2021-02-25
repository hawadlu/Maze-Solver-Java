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
   * @param args String array containing the arguments required to start the server
   *             the first element is the port and the second is a boolean to indicate
   *             if we should stop after binding (for testing purposes)
   */
  public Server(String[] args) {
    this.socketNum = Integer.parseInt(args[0]);
  }

  /**
   * Create a new server
   * @param socket the port to connect to.
   */
  public Server(int socket) {
    this.socketNum = socket;

    this.bind();

    this.listen();
  }

    /**
     * Bind and listen for incoming connections
     */
    public void start() {
        this.bind();
        this.listen();
    }


    /**
   * Bind to the requested port.
   */
  public void bind() {
    try {
      socket = new ServerSocket(socketNum);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Listen for incoming connections.
   */
  public void listen() {
      //Create a new thread that will listen for connections
      Server server = this;
      new Thread() {
          @Override
          public void run() {
              super.run();
              System.out.println("Server listening");

              while (true) {
                  try {
                      Socket accept = socket.accept();

                      System.out.println("Client connected");

                      //Create a new thread to handle the connection to this client
                      ClientHandler client = new ClientHandler(server, accept);

                      clients.add(client);
                      client.start();
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
          }
      }.start();
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
     * @return The number of clients that are currently connected to the database
     */
    public int getClientCount() {
      return clients.size();
    }


    /**
   * Start the server
   */
  public static void main(String[] args) {
    new Server(args).start();
  }
}
