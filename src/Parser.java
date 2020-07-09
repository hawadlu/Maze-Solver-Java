import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class contains all of the methods needed to parse the program.
 */
public class Parser {
    /**
     * Parse the program
     * @param fileIn the file to parse
     */
    public void parseProgram(File fileIn) throws FileNotFoundException {
        Scanner programScanner = new Scanner(fileIn);
        printScanner(programScanner);
    }

    /**
     * Run the program
     */
    public void execute() {
    }

    /**
     * Get the path that was created by the program
     * @return the path
     */
    public ArrayList<MazeNode> getPath() {
        return new ArrayList<>();
    }

    /**
     * Print the scanner for debugging
     * @param s the scanner
     */
    private void printScanner(Scanner s) {
        while (s.hasNext()) System.out.println(s);
    }
}
