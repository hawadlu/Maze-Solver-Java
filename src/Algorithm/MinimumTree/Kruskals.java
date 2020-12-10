package Algorithm.MinimumTree;

import Algorithm.AlgorithmRunner;
import Algorithm.AlgorithmWorker;
import Algorithm.SolveAlgorithm;
import Utility.Node;
import Utility.Segment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class Kruskals {
  //todo investigate why this is slow

  ArrayList<Segment> mstEdges = new ArrayList<>();
  HashSet<Node> unvisited = new HashSet<>();
  HashSet<Node> forest = new HashSet<>();

  public void solve(SolveAlgorithm solve) {
    System.out.println("Solving Kruskals");


    //Scan all of the nodes
    solve.scan("Loading");

    //Make an arraylist of edges
    solve.makeSegments();
    ArrayList<Segment> edges = new ArrayList<>(solve.segments);

    //Add all the nodes to the unvisited set
    unvisited.addAll(solve.getNodes());

    //Create the forest
    for (Node node: unvisited) {
      forest.add(node);
      node.setParent(node);
      node.setCost(0);
    }

    //Sort the arraylist
    edges.sort(Comparator.comparingInt(segment -> segment.segmentLen));

    System.out.println("Sorted edges");

    while (!edges.isEmpty()) {
      Segment currentSegment = edges.remove(0);

      if (union(currentSegment.entry, currentSegment.exit)) {
        mstEdges.add(currentSegment);
      }

    }
    System.out.println("Found MST with " + mstEdges.size() + " edges");

  }

  /**
   * Merge two nodes into the same tree
   * @param start the first node
   * @param end the second node
   * @return true/false was the union successful?
   */
  private boolean union(Node start, Node end) {
    Node startRoot = findRoot(start);
    Node endRoot = findRoot(end);

    //start and end are in the same tree
    if (startRoot == endRoot) {
      return false;
    } else {
      //Merge the trees
      if (startRoot.getCost() < endRoot.getCost()) {
        startRoot.setParent(end);
        forest.remove(start);
      } else {
        endRoot.setParent(start);
        forest.remove(end);

        if (startRoot.getCost() == endRoot.getCost()) {
          startRoot.setCost(startRoot.getCost() + 1);
        }
      }
      return true;
    }
  }

  /**
   * Locate the root of a given tree
   * @param start the tree
   * @return the root node
   */
  private Node findRoot(Node start) {
    if (start.getParent().equals(start)) {
      return start;
    } else {
      return findRoot(start.getParent());
    }
  }

  public ArrayList<Segment> getSegments() {
    return mstEdges;
  }
}

