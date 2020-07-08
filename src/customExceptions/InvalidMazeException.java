package customExceptions;

public class InvalidMazeException extends Throwable {
    public InvalidMazeException(String s) {
        System.out.println("Invalid maze!\n" + s);
    }
}
