import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;


/**
 * Test the various solve algorithms for the mazes
 */
public class SolveTests {
    String standardDir = "Images/"; //Most images are stored here


    // =================================================================
    // Tests
    // =================================================================

    //Test the smaller mazes
    @Test
    public void test01() {
        //tiny();
        //small();
        //medium();
        //large();
        huge();
        oneK();
    }

    //test the bigger mazes
    @Test
    public void test02() {
        uneven();
        twoK();
        longs();
        fourK();
        sixK();
        eightK();
        tenK();
    }



    public void tiny() {
        ArrayList<String> files = getFiles(standardDir, "tiny");

        //Go through each images
        for (String imageFile: files) {
            //go through each possible way of solving it
            for (int i = 1; i < 5; i++) {
                String[] args = new String[]{(standardDir + imageFile), "n", "1", String.valueOf(i)};
                Solver.main(args);
            }
        }
    }

    public void small() {
        ArrayList<String> files = getFiles(standardDir, "small");

        //Go through each images
        for (String imageFile: files) {
            //go through each possible way of solving it
            for (int i = 1; i < 5; i++) {
                String[] args = new String[]{(standardDir + imageFile), "n", "1", String.valueOf(i)};
                Solver.main(args);
            }
        }
    }

    public void medium() {
        ArrayList<String> files = getFiles(standardDir, "medium");

        //Go through each images
        for (String imageFile: files) {
            //go through each possible way of solving it
            for (int i = 1; i < 5; i++) {
                String[] args = new String[]{(standardDir + imageFile), "n", "1", String.valueOf(i)};
                Solver.main(args);
            }
        }
    }

    public void large() {
        ArrayList<String> files = getFiles(standardDir, "large");

        //Go through each images
        for (String imageFile: files) {
            //go through each possible way of solving it
            for (int i = 1; i < 5; i++) {
                String[] args = new String[]{(standardDir + imageFile), "n", "1", String.valueOf(i)};
                Solver.main(args);
            }
        }
    }

    public void huge() {
        ArrayList<String> files = getFiles(standardDir, "huge");

        //Go through each images
        for (String imageFile: files) {
            //Exclude uneven
            if (!imageFile.contains("Uneven")) {
                //go through each possible way of solving it
                for (int i = 1; i < 5; i++) {
                    String[] args = new String[]{(standardDir + imageFile), "n", "1", String.valueOf(i)};
                    Solver.main(args);
                }
            }
        }
    }

    public void uneven() {
        ArrayList<String> files = getFiles(standardDir, "uneven");

        //Go through each images
        for (String imageFile: files) {
            //go through each possible way of solving it
            for (int i = 1; i < 5; i++) {
                String[] args = new String[]{(standardDir + imageFile), "y", "1", String.valueOf(i)};
                Solver.main(args);
            }
        }
    }

    public void oneK() {
        ArrayList<String> files = getFiles(standardDir, "oneK");

        //Go through each images
        for (String imageFile: files) {
            //go through each possible way of solving it
            for (int i = 1; i < 5; i++) {
                String[] args = new String[]{(standardDir + imageFile), "n", "1", String.valueOf(i)};
                Solver.main(args);
            }
        }
    }

    public void twoK() {
        ArrayList<String> files = getFiles(standardDir, "twoK");

        //Go through each images
        for (String imageFile: files) {
            //go through each possible way of solving it
            for (int i = 1; i < 5; i++) {
                String[] args = new String[]{(standardDir + imageFile), "y", "1", String.valueOf(i)};
                Solver.main(args);
            }
        }
    }

    public void longs() {
        ArrayList<String> files = getFiles(standardDir, "long");

        //Go through each images
        for (String imageFile: files) {
            //go through each possible way of solving it
            for (int i = 1; i < 5; i++) {
                String[] args = new String[]{(standardDir + imageFile), "y", "2", String.valueOf(i)};
                Solver.main(args);
            }
        }
    }

    public void fourK() {
        ArrayList<String> files = getFiles(standardDir, "fourK");

        //Go through each images
        for (String imageFile: files) {
            //go through each possible way of solving it
            for (int i = 1; i < 5; i++) {
                String[] args = new String[]{(standardDir + imageFile), "y", "2", String.valueOf(i)};
                Solver.main(args);
            }
        }
    }

    public void sixK() {
        ArrayList<String> files = getFiles(standardDir, "sixK");

        //Go through each images
        for (String imageFile: files) {
            //go through each possible way of solving it
            for (int i = 1; i < 5; i++) {
                String[] args = new String[]{(standardDir + imageFile), "y", "2", String.valueOf(i)};
                Solver.main(args);
            }
        }
    }

    public void eightK() {
        ArrayList<String> files = getFiles(standardDir, "eightK");

        //Go through each images
        for (String imageFile: files) {
            //go through each possible way of solving it
            for (int i = 1; i < 5; i++) {
                String[] args = new String[]{(standardDir + imageFile), "y", "2", String.valueOf(i)};
                Solver.main(args);
            }
        }
    }

    public void tenK() {
        ArrayList<String> files = getFiles(standardDir, "tenK");

        //Go through each images
        for (String imageFile: files) {
            String[] args = new String[]{(standardDir + imageFile), "y", "2", "4"};
            Solver.main(args);
        }
    }

    /**
     * Get all of the files in a specified directory that contain a specific word.
     * @return Arraylist of matching files (int the format dir/file)
     */
    public ArrayList<String> getFiles(String dir, String pattern) {
        // Creates an array in which we will store the names of files and directories
        String[] pathnames;

        // Creates a new File instance by converting the given pathname string
        // into an abstract pathname
        File f = new File(dir);

        // Populates the array with names of files and directories
        pathnames = f.list();

        // For each pathname in the pathnames array
        for (String pathname : Objects.requireNonNull(pathnames)) {
            // Print the names of files and directories
            System.out.println(pathname);
        }

        //Add all the files to the arraylist
        ArrayList<String> toReturn = new ArrayList<>();

        for (String str: pathnames) {
            if (Pattern.compile(Pattern.quote(pattern), Pattern.CASE_INSENSITIVE).matcher(str).find()) toReturn.add(str);
        }
        return toReturn;
    }

}
