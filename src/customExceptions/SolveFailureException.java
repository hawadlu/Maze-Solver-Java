package customExceptions;

public class SolveFailureException extends Throwable {
    public SolveFailureException(String s) {
        System.out.println("Solve failure!\n" + s);
    }
}
