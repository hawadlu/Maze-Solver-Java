import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * This program loads a png image of a maze.
 * It will attempt to solve it and output a solved image file.
 */

public class solver {
    /**
     * Loads the image file
     */
    public void loadImage() {
        BufferedImage imgFile;

        //Getting the filename
        String filePath = getUserInput("Please enter the file name: ");

        //Checking if the file exists
        try {
            imgFile = ImageIO.read(new File(filePath));
            System.out.println("Image loaded");

            //Calls the method to save the image
            saveImage(imgFile, filePath);
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

    /**
     * Strips the image type then adds the 'solved' suffix
     */
    public String insertSuffix(String filePath) {
        StringBuilder finalPath = new StringBuilder();
        String[] filePathArr = filePath.split("");

        for (int i = 0; i < filePathArr.length; i++) {
            if (filePathArr[i].equals(".")) {
                finalPath.append(" solved.");
            } else {
                finalPath.append(filePathArr[i]);
            }
        }
        return finalPath.toString();
    }

    /**
     * Saves solved images in the images folder
     */
    public void saveImage(BufferedImage img, String filePath) {
        //Saving the image
        String fileName = insertSuffix(filePath);
        //System.out.println("Dir status: " + Files.exists(Paths.get(filePath)));
        try {
            ImageIO.write(img, "png", new File(fileName));
            System.out.println("Image saved as " + fileName);
        } catch (Exception e) {
            System.out.println("Unable to save image: " + e);
        }
    }

    // Main
    public static void main(String[] arguments) {
        solver slvr = new solver();
        slvr.loadImage();
    }
}

