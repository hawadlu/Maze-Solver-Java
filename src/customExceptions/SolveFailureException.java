package customExceptions;

import javax.swing.*;

public class SolveFailureException extends Throwable {
    public SolveFailureException(String s, JPanel parentComponent) {
        System.out.println("Solve failure!\n" + s);
        new ErrorPopup().showPopup(parentComponent, s);
    }
}
