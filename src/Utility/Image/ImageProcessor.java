package Utility.Image;

import Application.Application;
import Utility.Location;
import Utility.Node;

import java.util.HashMap;

public class ImageProcessor {
    Application application;

    public ImageProcessor(Application application) {
        this.application = application;
    }

    /**
     * Scan the maze for nodes.
     * @return All of the nodes in the maze
     */
    public HashMap<Location, Node> scanMaze() {
        //Do a 1 time pass over the image to locate all of the nodes
        ImageFile image = application.getImageFile();
        for (int height = 0; height < image.getDimensions().height; height++) {
            for (int width = 0; width < image.getDimensions().width; width++) {
                //Check iof the square is white
                if (image.isWhite(new Location(width, height))) {
                    //Check if this is a node
                }
            }
        }
        return null;
    }

    /**
     * Find all of the neighbours of this particular node.
     * @param origin the origin node.
     * @return map of the neighbours.
     */
    public HashMap<Location, Node> scanMaze(Node origin) {
        //todo implement me
        return null;
    }
}
