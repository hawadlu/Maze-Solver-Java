package Server;

import Image.ImageFile;
import Image.ImageProcessor;
import Utility.LocationList;

/**
 * Deals with the individual games hosted on the server.
 */
public class Room {
  private ClientHandler playerOne, playerTwo;
  private ImageFile imageFile;

  /**
   *
   * @param clientHandler
   */
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

  /**
   *
   * @param imageFile
   */
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
   * Check the readiness of each of the clients.
   * Start each of them if they are both ready
   */
  public void checkReadiness() {
    if (playerOne.ready) playerTwo.sendMessage(Requests.otherReady);
    if (playerTwo.ready) playerOne.sendMessage(Requests.otherReady);
    if (playerOne.ready && playerTwo.ready) {
      System.out.println("Start");//start
      playerOne.sendMessage(Requests.start);
      playerTwo.sendMessage(Requests.start);
    }
  }

  /**
   * Tell both of the players to make the setup screen
   */
  public void setup() {
    playerOne.sendMessage(Requests.makeSetup);
    playerTwo.sendMessage(Requests.makeSetup);
  }

  /**
   * Pass the list of locations to the appropriate player.
   *
   * @param currentPlayer the player that is sending the list.
   * @param locationList the list of locations
   */
  public void sendLocationList(ClientHandler currentPlayer, LocationList locationList) {
    if (playerOne.equals(currentPlayer)) playerTwo.sendMessage(locationList);
    else playerOne.sendMessage(locationList);
  }

  /**
   * Send the image processor from one client to the other
   * @param currentPlayer the client that is sending the processor.
   * @param imageProcessor the image processor to send.
   */
  public void sendImageProcessor(ClientHandler currentPlayer, ImageProcessor imageProcessor) {
    if (playerOne.equals(currentPlayer)) playerTwo.sendMessage(imageProcessor);
    else playerOne.sendMessage(imageProcessor);
  }

  /**
   * Send the user names to each player.
   *
   * Player one will receive player twos username and vice versa.
   */
  public void sendUserNames() {
    playerOne.sendMessage("user: " + playerTwo.username);
    playerTwo.sendMessage("user: " + playerOne.username);
  }

  /**
   * Tell the other client that the other has finished.
   * @param client the client that has finished.
   */
  public void markDone(ClientHandler client) {
    if (client.equals(playerOne)) playerTwo.sendMessage(Requests.otherDone);
    else playerOne.sendMessage(Requests.otherDone);
  }

  /**
   * Process a restart request from one player.
   * @param client the player that sent the restart request.
   */
  public void processRestartRequest(ClientHandler client) {
    if (client.equals(playerOne)) playerTwo.sendMessage(Requests.requestRestart);
    else playerOne.sendMessage(Requests.requestRestart);
  }

  /**
   * Reset the room and tell the player that requested the restart to
   * make the restart screen.
   * @param client the client that accepted the restart request.
   */
  public void restart(ClientHandler client) {
    if (client.equals(playerOne)) playerTwo.sendMessage(Requests.wait);
    else playerOne.sendMessage(Requests.wait);

    playerOne.done = false;
    playerTwo.done = false;

    playerOne.ready = false;
    playerTwo.ready = false;
  }

  /**
   * If one player disconnects, send the discxonnect notification to the other player.
   * Then close the room
   * @param client the client that is disconnecting
   */
  public void disconnect(ClientHandler client) {
    if (client.equals(playerOne)) playerTwo.sendMessage(Requests.opponentDisconnect);
    else if (client.equals(playerTwo)) playerOne.sendMessage(Requests.opponentDisconnect);
  }
}
