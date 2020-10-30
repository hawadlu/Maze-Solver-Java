package Application;

import GUI.GUI;

import java.io.File;
import java.util.ArrayList;

/**
 * This class is the base of the program.
 * Most inter package communication happens through here.
 */
public class Application {
  GUI gui;
  static Application currentApplication;

  /**
   * Get the options that can be used to solve mazes
   * @return arraylist of arrays
   */
  //todo implement me
  public ArrayList<String[]> getSolveOptions() {
    String arr[] = {"Hi", "there"};
    ArrayList tmp = new ArrayList();
    for (int i = 0; i < 4; i++) tmp.add(arr);
    return tmp;
  }
  
  /**
   * Take the image file and parse it into the appropriate format
   * @param imageToParse
   */
  //todo, implement me, use the image classes to do the parsing
  public void parseFile(File imageToParse) {
    //not yet implemented
  }

  /**
   * Setup the gui
   */
  private void setUpGui() {
    this.gui = new GUI(currentApplication);
  }

  public static void main(String[] args) {
    //Create the GUI
    currentApplication = new Application();
    currentApplication.setUpGui();
  }
}
