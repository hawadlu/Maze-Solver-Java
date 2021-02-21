package tests;

import application.Application;
import image.ImageProcessor;
import utility.Exceptions.GenericError;
import utility.Location;
import utility.Node;
import dispatcher.Dispatcher;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests that don't really fit into any of the other classes.
 */
public class AssortedTests {

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

        ImageProcessor processor = new ImageProcessor();

        processor.scanAll(application.getImageFile());
        ConcurrentHashMap<Location, Node> nodes = processor.getNodes();
        int expectedSize = 12;
        assertEquals(expectedSize, nodes.size(), "Size is: " + nodes.size() + ",  expected size: " + expectedSize);
    }


    //TEST THE WORKER THREAD
    @Test
    public void testWorkerThread() throws InterruptedException {
        String algorithm = "Breadth First";


        Application application = new Application();
        File image = new File("Images/TwoK Imperfect.png");
        try {
            application.parseImageFile(image);
        } catch (GenericError genericError) {
            genericError.printStackTrace();
            System.out.println("Failed to parse image");
        }

        Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
        dispatcher.solve(algorithm);

        System.out.println("Thread finished");
    }

    //TEST SAVING
    @Test
    public void testSave() throws InterruptedException {
        String algorithm = "Breadth First";

        Application application = new Application();
        File image = new File("Images/Small Imperfect.png");
        try {
            application.parseImageFile(image);
        } catch (GenericError genericError) {
            genericError.printStackTrace();
            System.out.println("Failed to parse image");
        }

        Dispatcher dispatcher = new Dispatcher(application.getImageFile(), 1);
        dispatcher.solve(algorithm);

        System.out.println("Thread finished");

        application.saveImage("Images/Solved/Test.png");
    }
}


