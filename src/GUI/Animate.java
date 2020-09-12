package GUI;

import GUI.ImagePanel;
import Image.ImageFile;
import Image.ImageManipulation;
import Solve.MazeNode;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class animates the progress of the solver
 */
public class Animate {
    ArrayList<MazeNode> route;
    ImageFile imageFile;
    final JFrame liveSolver;

    Animate(ArrayList<MazeNode> route, ImageFile imageFile, String player) {
        this.route = route;
        Collections.reverse(this.route);
        this.imageFile = imageFile;

        //Create a frame to show the maze
        liveSolver = new JFrame(player);
        liveSolver.setSize(400, 420);
        liveSolver.setVisible(true);
        liveSolver.setResizable(false);

        //Add the image to the view
        ImagePanel image = new ImagePanel(imageFile, 380, 380, null);
        image.setSize(390, 390);

        liveSolver.add(image);
    }

    /**
     * Step through the solved maze
     * @throws InterruptedException problem!
     * @param colour the colour of the line
     */
    public void play(byte colour) throws InterruptedException {
        MazeNode prev = null;
        imageFile.initSolvedArr();
        imageFile.initWhiteSquares();
        while (!route.isEmpty()) {
            MazeNode currentNode = route.remove(0);

            if (prev == null) imageFile.setRGB(currentNode.getX(), currentNode.getY(), colour);
            else ImageManipulation.draw(imageFile, prev.getX(), prev.getY(), currentNode.getX(), currentNode.getY(), colour);
            prev = currentNode;

            liveSolver.repaint();

            Thread.sleep(200);
        }
    }
}
