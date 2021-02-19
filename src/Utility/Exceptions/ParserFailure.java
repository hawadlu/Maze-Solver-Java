package Utility.Exceptions;

import javax.swing.*;

/**
 * todo comment me
 */
public class ParserFailure extends RuntimeException {

  /**
   * Throw a new error.
   * @param frame the window to display the message in.
   * @param msg the message to display
   * @param showPopup only show the popup if requested
   */
  public ParserFailure(JFrame frame, String msg, boolean showPopup) {
    super(msg);
    if (showPopup) JOptionPane.showMessageDialog(frame, msg);
  }
}
