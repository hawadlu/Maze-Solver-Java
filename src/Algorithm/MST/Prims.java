package Algorithm.MST;

import Algorithm.AlgorithmRunner;
import Algorithm.AlgorithmWorker;
import Algorithm.SolveAlgorithm;
import Utility.Node;
import Utility.Segment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Prims extends AlgorithmRunner {
  ArrayList<Segment> mstEdges = new ArrayList<>();

  public void solve(SolveAlgorithm solve) {
    System.out.println("Solving Prims");

    AlgorithmWorker worker = new PrimsWorker(solve, solve.entry, solve.exit, this, "t1", this);
    worker.start();
    try {
      worker.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public ArrayList<Segment> getSegments() {
    return mstEdges;
  }

}

class PrimsWorker extends AlgorithmWorker {
  Prims prims;

  public PrimsWorker(SolveAlgorithm solve, Node start, Node destination, AlgorithmRunner runner, String threadId, Prims prims) {
    super(solve, start, destination, runner, threadId);
    this.prims = prims;
  }

  @Override
  public void run() {
    solve.entry.visit(this);

    if (!solve.scanAll) solve.findNeighbours(solve.entry, false);

    //Add the start segment
    PriorityQueue<Segment> toProcess = new PriorityQueue<>(solve.entry.getSegments());

    while (!toProcess.isEmpty()) {
      Segment currentSegment = toProcess.poll();

      if (currentSegment.exit.isVisited() == null) {
        currentSegment.exit.visit(this);

        solve.findNeighbours(currentSegment.exit, false);

        //Add all segments
        toProcess.addAll(currentSegment.exit.getSegments());

        //Add this segment to the list of edges
        this.prims.mstEdges.add(currentSegment);
      }
    }
    System.out.println("Completed prims algorithm");
  }
}
