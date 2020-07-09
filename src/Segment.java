public class Segment {
    final MSTNode nodeStart;
    final MSTNode nodeEnd;
    final int cost;

    Segment(MSTNode start, MSTNode end, int newCost) {
        this.nodeStart = start;
        this.nodeEnd = end;
        this.cost = newCost;
    }
}
