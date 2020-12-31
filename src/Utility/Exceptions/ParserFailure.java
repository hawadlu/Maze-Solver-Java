package Utility.Exceptions;

import javax.swing.*;

public class ParserFailure extends RuntimeException {

  /**
   * Throw a new error.
   * @param frame the window to display the message in.
   * @param msg the message to display
   */
  public ParserFailure(JFrame frame, String msg) {
    super(msg);
    JOptionPane.showMessageDialog(frame, msg);
  }
}