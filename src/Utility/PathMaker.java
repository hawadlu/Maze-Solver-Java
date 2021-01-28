package Utility;

import Application.Application;
import Image.ImageFile;

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
  public static void makePath(ArrayList<Segment> segments, ImageFile imageFile) {
    imageFile.fillSegmentPath(segments);
  }

  /**
   * Fill in the articulation points on the image
   *
   * @param artPts
   */
  public static void makeNodePath(ArrayList<Node> artPts, ImageFile imageFile) {
    imageFile.fillNodePath(artPts, false);
  }

  /**
   * Create the path from the start to the finish
   */
  public static void makePath(Node[] join, Node entry, Node exit, ImageFile imageFile) {
    Node currentNode;

    if (join == null) {
      if (exit.getParent() != null) currentNode = exit;
      else currentNode = entry;

      imageFile.fillNodePath(generatePathArraylist(currentNode), true);
    } else {
      imageFile.fillNodePath(generatePathArraylist(join[0]), true);
      imageFile.fillNodePath(generatePathArraylist(join[1]), true);
      ArrayList<Node> tmp = new ArrayList<>();
      tmp.add(join[0]);
      tmp.add(join[1]);
      imageFile.fillNodePath(tmp, true);
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
