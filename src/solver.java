import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * This program loads a png image of a maze.
 * It will attempt to solve it and output a solved image file.
 */

public class solver {
    BufferedImage imgFile;

    /**
     * Loads the image file
     */
    public void loadImage() {
        //Getting the filename
        String fileName = getUserInput("Please enter the file name: ");
        System.out.println(fileName);

        //Checking if the file exists
        try {
            imgFile = ImageIO.read(new File(fileName));
            System.out.println("Image loaded");
        } catch (Exception e){
            System.out.println("File does not exist");
            loadImage();
        }
    }


    /**
     * Gets user input and returns a string
     */
    public String getUserInput(String question) {
        Scanner userInput = new Scanner(System.in); //Scans the user input
        System.out.print(question);

        return userInput.nextLine();
    }

    // Main
    public static void main(String[] arguments) {
        solver slvr = new solver();
        slvr.loadImage();
    }
}

