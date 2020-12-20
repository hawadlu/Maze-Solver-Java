package Algorithm.MinimumTree;

import Algorithm.Solvers.SolveRunner;
import Algorithm.SolveAlgorithm;
import Utility.Segment;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Prims extends SolveRunner {
  ArrayList<Segment> mstEdges = new ArrayList<>();

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

  public ArrayList<Segment> getSegments() {
    return mstEdges;
  }

}
