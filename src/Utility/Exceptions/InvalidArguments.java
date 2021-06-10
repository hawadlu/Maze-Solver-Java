package utility.Exceptions;

/**
 * This class is used to represent errors thrown when incorrect arguments
 * are provided to a function.
 *
 * This is primarily used when launching the server.
 */
public class InvalidArguments extends GenericError {
    /**
     * @param specifics a string containing specific information about the maze.
     */
    public InvalidArguments(String specifics) {
        System.out.println("Invalid arguments: " + specifics);
    }
}
