public class Segment {
    MSTNode nodeStart, nodeEnd;
    int cost;

    Segment(MSTNode start, MSTNode end, int newCost) {
        this.nodeStart = start;
        this.nodeEnd = end;
        this.cost = newCost;
    }
}
