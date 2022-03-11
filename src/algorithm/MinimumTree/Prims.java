package algorithm.MinimumTree;

import algorithm.SolveAlgorithm;
import algorithm.Solvers.SolveRunner;
import Utility.Segment;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * This class implements prims algorithm to
 * find a minimum spanning tree.
 */
public class Prims extends SolveRunner {
  ArrayList<Segment> mstEdges = new ArrayList<>();

  /**
   * Execute prims algorithm.
   * @param solve the solve object being used.
   */
  public void solve(SolveAlgorithm solve) {
    Thread visitor = new Thread(); //This thread is never run but used to track the visit status.

    System.out.println("Solving Prims");

    solve.entry.visit(visitor);

    if (!solve.scanAll) solve.findNeighbours(solve.entry, false);

    //Add the start segment
    PriorityQueue<Segment> toProcess = new PriorityQueue<>(solve.entry.getSegments(solve.getNodeMap()));

    while (!toProcess.isEmpty()) {
      Segment currentSegment = toProcess.poll();

      if (currentSegment.exit.isVisited() == null) {
        currentSegment.exit.visit(visitor);

        solve.findNeighbours(currentSegment.exit, false);

        //Add all segments
        toProcess.addAll(currentSegment.exit.getSegments(solve.getNodeMap()));

        //Add this segment to the list of edges
        this.mstEdges.add(currentSegment);
      }
    }
    System.out.println("Completed prims algorithm");
  }

  /**
   * @return an arraylist containing the segments found by the algorithm.
   */
  public ArrayList<Segment> getSegments() {
    return mstEdges;
  }

}
