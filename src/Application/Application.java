package Application;

import GUI.GUI;

/**
 * This class is the base of the program.
 * Most inter package communication happens through here.
 */
public class Application {
  private static GUI gui;

  public static void main(String[] args) {
    //Create the GUI
    gui = new GUI();
  }
}
