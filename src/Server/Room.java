package Server;

import Image.ImageFile;

/**
 * Deals with the individual games hosted on the server.
 */
public class Room {
  ClientHandler playerOne, playTwo;
  String imageJson;

  public Room(ClientHandler clientHandler) {
    this.playerOne = clientHandler;
  }

  /**
   * Add another player to the room.
   * @param clientHandler the clientHandler for the new player
   */
  public void join(ClientHandler clientHandler) {
    this.playTwo = clientHandler;
  }

  public void setImageFile(String imageJson) {
    this.imageJson = imageJson;
  }


}
