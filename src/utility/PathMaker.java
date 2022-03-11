package Utility;

import image.ImageFile;

import java.util.ArrayList;


/**
 * class containing methods needed to generate node paths
 */
public class PathMaker {
  /**
   * Fill the image with the necessary segments
   *
   * @param segments the arraylist of segments
   * @param image the imageFile to fill.
   */
  public static void makePath(ArrayList<Segment> segments, ImageFile image) {
    image.fillSegmentPath(segments);
  }

  /**
   * Fill in the articulation points on the image
   *
   * @param artPts an arraylist containing all of the articulation points.
   * @param image the imageFile to use
   */
  public static void makeNodePath(ArrayList<Node> artPts, ImageFile image) {
    image.fillNodePath(artPts, false);
  }

  /**
   * Create the path from the start to the finish
   */
  public static void makePath(Node[] join, Node entry, Node exit, ImageFile image) {
    Node currentNode;

    if (join == null) {
      if (exit.getParent() != null) currentNode = exit;
      else currentNode = entry;

      image.fillNodePath(generatePathArraylist(currentNode), true);
    } else {
      image.fillNodePath(generatePathArraylist(join[0]), true);
      image.fillNodePath(generatePathArraylist(join[1]), true);
      ArrayList<Node> tmp = new ArrayList<>();
      tmp.add(join[0]);
      tmp.add(join[1]);
      image.fillNodePath(tmp, true);
    }
  }

  /**
   * Generate a list of nodes.
   *
   * Go from parent to parent until parent is null.
   * @param currentNode the node to start with.
   * @return an arraylist of nodes.
   */
  public static ArrayList<Node> generatePathArraylist(Node currentNode) {
    ArrayList<Node> path = new ArrayList<>();

    while (currentNode != null) {
      try {
        path.add(currentNode);
      } catch (OutOfMemoryError e) {
        System.out.println(e);
      }
      currentNode = currentNode.getParent();
    }
    return path;
  }

  /**
   * Generate a list of node locations.
   *
   * Go from parent to parent until parent is null.
   * @param currentNode the node to start with.
   * @return an arraylist of node locations.
   */
  public static LocationList generatePathLocationArraylist(Node currentNode) {
    LocationList locations = new LocationList();

    while (currentNode != null) {
      try {
        locations.add(currentNode.getLocation());
      } catch (OutOfMemoryError e) {
        System.out.println(e);
      }
      currentNode = currentNode.getParent();
    }
    return locations;
  }
}
