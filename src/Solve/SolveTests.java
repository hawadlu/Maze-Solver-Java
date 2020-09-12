package Solve;

import customExceptions.ParserFailureException;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;
import Parser.Parser;


/**
 * Test the various solve algorithms for the mazes
 */
public class SolveTests {
    final String standardDir = "Images/"; //Most images are stored here


    // =================================================================
    // Tests
    // =================================================================

    //Test reading the mazes in
//    @Test
//    public void readImages() {
//        ArrayList<String> paths = getFiles(".png");
//        System.out.println("Collected files");
//
//        for (String path: paths) {
//            try {
//                new Image.ImageFile(ImageIO.read(new File("Images/" + path)), path, new JPanel());
//            } catch (IOException | InvalidColourException | InvalidMazeException e) {
//                System.out.println("Path: " + path);
//                if (path.contains("invalid") || path.contains("Invalid")) System.out.println("Deliberate invalid");
//                e.printStackTrace();
//            }
//        }
//    }

    @Test
    public void parseProg() throws FileNotFoundException, ParserFailureException {
        new Parser().setupParser(new File("Test Programs/Test1.txt"), null);
    }

    /**
     * Get all of the files in a specified directory that contain a specific word.
     * @return Arraylist of matching files (int the format dir/file)
     */
    public ArrayList<String> getFiles(String pattern) {
        // Creates an array in which we will store the names of files and directories
        String[] pathNames;

        // Creates a new File instance by converting the given pathname string
        // into an abstract pathname
        File f = new File(standardDir);

        // Populates the array with names of files and directories
        pathNames = f.list();

        // For each pathname in the pathNames array
        for (String pathname : Objects.requireNonNull(pathNames)) {
            // Print the names of files and directories
            System.out.println(pathname);
        }

        //Add all the files to the arraylist
        ArrayList<String> toReturn = new ArrayList<>();

        for (String str: pathNames) {
            if (Pattern.compile(Pattern.quote(pattern), Pattern.CASE_INSENSITIVE).matcher(str).find()) toReturn.add(str);
        }
        return toReturn;
    }

}
