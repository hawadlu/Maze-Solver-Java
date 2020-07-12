package customExceptions;

import javax.swing.*;

/**
 * Simple class for handling error popups
 */
public class ErrorPopup {
    public void showPopup(JPanel parentComponent, String message) {
        if (parentComponent == null) System.out.println(message);
        else JOptionPane.showMessageDialog(parentComponent, message);
    }
}
