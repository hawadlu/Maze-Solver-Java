package customExceptions;

import javax.swing.*;

public class InvalidColourException extends Throwable {
    public InvalidColourException(JPanel parentComponent, String s) {
        System.out.println("Failed!\n" + s);
        new ErrorPopup().showPopup(parentComponent, s);
    }
}
