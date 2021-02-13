package Server;

import Image.ImageFile;

/**
 * Deals with the individual games hosted on the server.
 */
public class Room {
  private ClientHandler playerOne, playerTwo;
  private ImageFile imageFile;

  public Room(ClientHandler clientHandler) {
    this.playerOne = clientHandler;
  }

  /**
   * Add another player to the room.
   * @param clientHandler the clientHandler for the new player
   */
  public void join(ClientHandler clientHandler) {
    this.playerTwo = clientHandler;
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

  /**
   * Check the readiness of each of the clients
   */
  public void checkReadiness() {
    if (playerOne.ready) playerTwo.sendMessage(Requests.otherReady);
    if (playerTwo.ready) playerOne.sendMessage(Requests.otherReady);
    if (playerOne.ready && playerTwo.ready) System.out.println("Start");//start
  }

  /**
   * Tell both of the players to make the setup screen
   */
  public void setup() {
    playerOne.sendMessage(Requests.makeSetup);
    playerTwo.sendMessage(Requests.makeSetup);
  }
}
