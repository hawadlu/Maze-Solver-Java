package Utility;

import java.util.Objects;

public class Segment implements Comparable {
  public Node entry, exit;
  public int segmentLen;

  public Segment(Node entry, Node exit) {
    this.entry = entry;
    this.exit = exit;

    this.segmentLen = (int) Node.calculateEuclideanDistance(entry, exit);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Segment segment = (Segment) o;
    return (entry.equals(segment.entry) && exit.equals(segment.exit) || (entry.equals(segment.exit) && exit.equals(segment.entry)));
  }

  @Override
  public int hashCode() {
    return Objects.hash(entry, exit);
  }

  @Override
  public int compareTo(Object o) {
    Segment other = (Segment) o;
    return Integer.compare(this.segmentLen, other.segmentLen);
  }
}
