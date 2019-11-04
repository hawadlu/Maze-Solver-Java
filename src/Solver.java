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

public class Solver {
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

            //Calls method to solve the image
            solve(imgFile, filePath);
        } catch (Exception e){
            System.out.println("File does not exist");
            loadImage();
        }
    }

    /**
     * Solves the maze
     */
    public void solve(BufferedImage imgFile, String filePath) {
        //Creating the start and end nodes
        MazeNode start, end;


        //Performing a one pass over the maze to find all the nodes
        for (int height = 0; height < imgFile.getWidth(); height++) {
            for (int width = 0; width < imgFile.getHeight(); width++) {
                //Gets the 0-255 value for red. Colour is either white or black so can use R, G or B.
                int colour  = imgFile.getRGB(width, height) & 0x00ff0000 >> 16;

                //Marking the start
                if (height == 0 && colour == 255) {
                    start = new MazeNode(width, height);
                    imgFile.setRGB(width, height, 845909); //Mark the start green

                //marking the end
                } else if (height == imgFile.getHeight() - 1 && colour == 255) {
                    end = new MazeNode(width, height);
                    imgFile.setRGB(width, height, 16711680);
                }
            }
        }

        //Calls the method to save the image
        saveImage(imgFile, filePath);
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
        Solver slvr = new Solver();
        slvr.loadImage();
    }
}

