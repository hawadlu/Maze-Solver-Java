package Utility.Exceptions;

public class InvalidMaze extends GenericError{
    public InvalidMaze(String specifics) {
        System.out.println("Invalid maze: " + specifics);
    }
}
