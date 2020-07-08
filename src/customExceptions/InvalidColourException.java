package customExceptions;

public class InvalidColourException extends Throwable {
    public InvalidColourException(String s) {
        System.out.println("Failed!\n" + s);
    }
}
