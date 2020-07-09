package customExceptions;

import javax.swing.*;

/**
 * Simple class for handling error popups
 */
public class ErrorPopup {
    public void showPopup(JPanel parentComponent, String message) {
        JOptionPane.showMessageDialog(parentComponent, message);
    }
}
