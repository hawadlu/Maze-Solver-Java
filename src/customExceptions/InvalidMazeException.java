package customExceptions;

import javax.swing.*;

public class InvalidMazeException extends Throwable {
    public InvalidMazeException(String s, JPanel parentComponent) {
        System.out.println("Invalid maze!\n" + s);
        new ErrorPopup().showPopup(parentComponent, s);
    }
}
