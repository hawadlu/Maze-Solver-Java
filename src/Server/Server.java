package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * todo write me
 * todo close the room when a client leaves.
 */
public class Server {
  int socketNum;
  ServerSocket socket;
  ArrayList<ClientHandler> clients = new ArrayList<>();
  HashMap<Integer, Room> rooms = new HashMap();


  /**
   *
   * @param socketNum
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
   *
   * @param args
   */
  public static void main(String[] args) {
    Server server = new Server(5000);
    server.bind();
    server.listen();
  }

  /**
   *
   * @param clientHandler
   * @return
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
   *
   * @param clientHandler
   * @param roomId
   */
  public void joinRoom(ClientHandler clientHandler, int roomId) {
    rooms.get(roomId).join(clientHandler);
  }
}
