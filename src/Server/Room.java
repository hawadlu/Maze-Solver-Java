package Server;

/**
 * Deals with the individual games hosted on the server.
 */
public class Room {
  ClientHandler playerOne, playTwo;

  public Room(ClientHandler clientHandler) {
    this.playerOne = clientHandler;
  }

  public void join(ClientHandler clientHandler) {
    this.playTwo = clientHandler;
  }
}
