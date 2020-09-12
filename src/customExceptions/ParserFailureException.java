package customExceptions;

import javax.swing.*;

public class ParserFailureException extends Throwable {
    public ParserFailureException(String s, JPanel parentComponent) {
        System.out.println("Parser.Parser failure\n" + s);
        new ErrorPopup().showPopup(parentComponent, s);
    }
}
