import Application.Application;
import Application.Solve.SolveAlgorithm;
import Utility.Exceptions.GenericError;
import Utility.Image.ImageProcessor;
import Utility.Location;
import Utility.Node;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {

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

        HashMap<Location, Node> nodes = processor.scanAll();
        int expectedSize = 12;
        assertEquals(expectedSize, nodes.size(), "Size is: " + nodes.size() + ",  expected size: " + expectedSize);
    }

    /**
     * Test DFS search, find neighbours during loading.
     */
    @Test
    public void testDFS() {
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
    }
}
