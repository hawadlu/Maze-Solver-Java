package Server;

import Image.ImageFile;

/**
 * Deals with the individual games hosted on the server.
 */
public class Room {
  private ClientHandler playerOne, playTwo;
  private ImageFile imageFile;

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

  public void setImageFile(ImageFile imageFile) {
    this.imageFile = imageFile;
  }

  /**
   * @return the image being used in this room.
   */
  public ImageFile getImage() {
    return imageFile;
  }
}
