package Utility;

import java.util.Objects;

/**
 * This class is used to represent the space between two nodes in the maze.
 */
public class Segment implements Comparable {
  public Node entry, exit;
  public int segmentLen;

  /**
   * Create a new segment.
   *
   * @param entry Node at the entry.
   * @param exit Node at the exit.
   */
  public Segment(Node entry, Node exit) {
    this.entry = entry;
    this.exit = exit;

    this.segmentLen = (int) Node.calculateEuclideanDistance(entry, exit);
  }

  /**
   * Check object equality.
   * @param o the object to check.
   * @return a boolean indicating object equality.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Segment segment = (Segment) o;
    return (entry.equals(segment.entry) && exit.equals(segment.exit) || (entry.equals(segment.exit) && exit.equals(segment.entry)));
  }

  /**
   * @return a hashcode representation of the object
   */
  @Override
  public int hashCode() {
    return Objects.hash(entry, exit);
  }

  /**
   * Compare two objects to see which should come first.
   * @param o the object to compare to.
   * @return an int indicating how the objects compared
   */
  @Override
  public int compareTo(Object o) {
    Segment other = (Segment) o;
    return Integer.compare(this.segmentLen, other.segmentLen);
  }
}
