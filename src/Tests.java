import Utility.Thread.AlgorithmWorkerThread;
import Application.Application;
import Algorithm.SolveAlgorithm;
import Utility.Exceptions.GenericError;
import Utility.Image.ImageProcessor;
import Utility.Location;
import Utility.Node;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {

    //TESTS OF NODE SCANNING
    /**
     * Test the scanning of nodes to ensure that the correct number are found
     */
    @Test
    public void testScanNode() {
        Application application = new Application();
        File image = new File("Images/Tiny.png");
        try {
            application.parseImageFile(image);
        } catch (GenericError genericError) {
            genericError.printStackTrace();
            System.out.println("Failed to parse image");
        }

        ImageProcessor processor = new ImageProcessor(application);

        processor.scanAll();
        HashMap<Location, Node> nodes = processor.getNodes();
        int expectedSize = 12;
        assertEquals(expectedSize, nodes.size(), "Size is: " + nodes.size() + ",  expected size: " + expectedSize);
    }




    //TESTS OF THE DEPTH FIRST SEARCH

    /**
     * Test DFS search, find neighbours during loading.
     */
    @Test
    public void testDFSSmallScanOnLoad() {
        Application application = new Application();
        File image = new File("Images/Tiny.png");
        try {
            application.parseImageFile(image);
        } catch (GenericError genericError) {
            genericError.printStackTrace();
            System.out.println("Failed to parse image");
        }

        SolveAlgorithm solve = new SolveAlgorithm(application);
        solve.Scan("During Loading");
        solve.Solve("Depth First");
        ArrayList<Node> path = solve.getPath();
        assert !path.isEmpty();

        //Create the image
        application.getImageFile().createSolvedImage(path);
        BufferedImage solvedImage = application.getImage();
        System.out.println("Created image");
    }

    /**
     * Test DFS search, find neighbours during loading.
     */
    @Test
    public void testDFSMediumScanOnLoad() {
        Application application = new Application();
        File image = new File("Images/Large Imperfect.png");
        try {
            application.parseImageFile(image);
        } catch (GenericError genericError) {
            genericError.printStackTrace();
            System.out.println("Failed to parse image");
        }

        SolveAlgorithm solve = new SolveAlgorithm(application);
        solve.Scan("During Loading");
        solve.Solve("Depth First");
        ArrayList<Node> path = solve.getPath();
        assert !path.isEmpty();

        //Create the image
        application.getImageFile().createSolvedImage(path);
        BufferedImage solvedImage = application.getImage();
        System.out.println("Created image");
    }

    /**
     * Test DFS search, find neighbours during loading.
     */
    @Test
    public void testDFSHugeScanOnLoad() {
        Application application = new Application();
        File image = new File("Images/Huge Imperfect.png");
        try {
            application.parseImageFile(image);
        } catch (GenericError genericError) {
            genericError.printStackTrace();
            System.out.println("Failed to parse image");
        }

        SolveAlgorithm solve = new SolveAlgorithm(application);
        solve.Scan("During Loading");
        solve.Solve("Depth First");
        ArrayList<Node> path = solve.getPath();
        assert !path.isEmpty();

        //Create the image
        application.getImageFile().createSolvedImage(path);
        BufferedImage solvedImage = application.getImage();
        System.out.println("Created image");
    }

    /**
     * Test DFS search, find neighbours during loading.
     */
    @Test
    public void testDFSMaxScanOnLoad() {
        Application application = new Application();
        File image = new File("Images/OneK Imperfect.png");
        try {
            application.parseImageFile(image);
        } catch (GenericError genericError) {
            genericError.printStackTrace();
            System.out.println("Failed to parse image");
        }

        SolveAlgorithm solve = new SolveAlgorithm(application);
        solve.Scan("During Loading");
        solve.Solve("Depth First");
        ArrayList<Node> path = solve.getPath();
        assert !path.isEmpty();

        //Create the image
        application.getImageFile().createSolvedImage(path);
        BufferedImage solvedImage = application.getImage();
        System.out.println("Created image");
    }

    /**
     * Test DFS search, find neighbours during loading.
     */
    @Test
    public void testDFSSmallScanOnSolve() {
        Application application = new Application();
        File image = new File("Images/Tiny.png");
        try {
            application.parseImageFile(image);
        } catch (GenericError genericError) {
            genericError.printStackTrace();
            System.out.println("Failed to parse image");
        }

        SolveAlgorithm solve = new SolveAlgorithm(application);
        solve.Scan("During Solving");
        solve.Solve("Depth First");
        ArrayList<Node> path = solve.getPath();
        assert !path.isEmpty();

        //Create the image
        application.getImageFile().createSolvedImage(path);
        BufferedImage solvedImage = application.getImage();
        System.out.println("Created image");
    }

    /**
     * Test DFS search, find neighbours during loading.
     */
    @Test
    public void testDFSMediumScanOnSolve() {
        Application application = new Application();
        File image = new File("Images/Large Imperfect.png");
        try {
            application.parseImageFile(image);
        } catch (GenericError genericError) {
            genericError.printStackTrace();
            System.out.println("Failed to parse image");
        }

        SolveAlgorithm solve = new SolveAlgorithm(application);
        solve.Scan("During Solving");
        solve.Solve("Depth First");
        ArrayList<Node> path = solve.getPath();
        assert !path.isEmpty();

        //Create the image
        application.getImageFile().createSolvedImage(path);
        BufferedImage solvedImage = application.getImage();
        System.out.println("Created image");
    }

    /**
     * Test DFS search, find neighbours during loading.
     */
    @Test
    public void testDFSHugeScanOnSolve() {
        Application application = new Application();
        File image = new File("Images/Huge Imperfect.png");
        try {
            application.parseImageFile(image);
        } catch (GenericError genericError) {
            genericError.printStackTrace();
            System.out.println("Failed to parse image");
        }

        SolveAlgorithm solve = new SolveAlgorithm(application);
        solve.Scan("During Solving");
        solve.Solve("Depth First");
        ArrayList<Node> path = solve.getPath();
        assert !path.isEmpty();

        //Create the image
        application.getImageFile().createSolvedImage(path);
        BufferedImage solvedImage = application.getImage();
        System.out.println("Created image");
    }

    /**
     * Test DFS search, find neighbours during loading.
     */
    @Test
    public void testDFSMaxScanOnSolve() {
        Application application = new Application();
        File image = new File("Images/OneK Imperfect.png");
        try {
            application.parseImageFile(image);
        } catch (GenericError genericError) {
            genericError.printStackTrace();
            System.out.println("Failed to parse image");
        }

        SolveAlgorithm solve = new SolveAlgorithm(application);
        solve.Scan("During Solving");
        solve.Solve("Depth First");
        ArrayList<Node> path = solve.getPath();
        assert !path.isEmpty();

        //Create the image
        application.getImageFile().createSolvedImage(path);
        BufferedImage solvedImage = application.getImage();
        System.out.println("Created image");
    }

    //TEST THE WORKER THREAD
    @Test
    public void testWorkerThread() throws InterruptedException {
        Application application = new Application();
        File image = new File("Images/TwoK Imperfect.png");
        try {
            application.parseImageFile(image);
        } catch (GenericError genericError) {
            genericError.printStackTrace();
            System.out.println("Failed to parse image");
        }

        AlgorithmWorkerThread thread = new AlgorithmWorkerThread("Depth First", "During Loading", application);
        thread.start();

        thread.join(); //Wait for the other thread to finish

        System.out.println("Thread finished");
    }
}
