package Utility;

import Application.Application;

import java.util.ArrayList;

/**
 * class containing methods needed to generate node paths
 */
public class PathMaker {
  /**
   * Fill the image with the necessary segments
   *
   * @param segments the arraylist of segments
   */
  public static void makePath(ArrayList<Segment> segments, Application application) {
    application.getImageFile().fillSegmentPath(segments);
  }

  /**
   * Fill in the articulation points on the image
   *
   * @param artPts
   */
  public static void makeNodePath(ArrayList<Node> artPts, Application application) {
    application.getImageFile().fillNodePath(artPts, false);
  }

  /**
   * Create the path from the start to the finish
   */
  public static void makePath(Node[] join, Node entry, Node exit, Application application) {
    Node currentNode;

    if (join == null) {
      if (exit.getParent() != null) currentNode = exit;
      else currentNode = entry;

      application.getImageFile().fillNodePath(generatePathArraylist(currentNode), true);
    } else {
      application.getImageFile().fillNodePath(generatePathArraylist(join[0]), true);
      application.getImageFile().fillNodePath(generatePathArraylist(join[1]), true);
      ArrayList<Node> tmp = new ArrayList<>();
      tmp.add(join[0]);
      tmp.add(join[1]);
      application.getImageFile().fillNodePath(tmp, true);
    }
  }

  public static ArrayList<Node> generatePathArraylist(Node currentNode) {
    ArrayList<Node> path = new ArrayList<>();

    while (currentNode != null) {
      path.add(currentNode);
      currentNode = currentNode.getParent();
    }
    return path;
  }
}