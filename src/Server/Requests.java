package Server;

import java.awt.*;
import java.util.regex.Pattern;

/**
 * Class containing common requests sent to and from the server
 */
public class Requests {

  public static String joinRoom = "Join Room";
  public static String createRoom = "Create Room";
  public static String setImage = "Set Image";
  public static String ready = "Ready";
  public static String otherReady = "Other Ready";
  public static String wait = "Wait";
  public static String makeSetup = "Make Setup";
  public static String start = "Start";
  public static String done = "Done";
  public static String otherDone = "Other Done";
  public static String requestRestart = "Request Restart";
  public static String restart = "Restart";
  public static String disconnect = "disconnect";
  public static String opponentDisconnect = "opponentDisconnect";
  public static Pattern room = Pattern.compile("room: [0-9]*");
  public static Pattern username = Pattern.compile("user: [A-z0-9]*");
}
