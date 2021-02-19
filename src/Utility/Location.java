package Utility;

import java.io.Serializable;
import java.util.Objects;

/**
 * Simple class used to represent coordinates.
 */
public class Location implements Serializable {
  public final int x, y;

  /**
   *
   * @param x
   * @param y
   */
  public Location(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Copy constructor.
   * @param location The old location.
   */
  public Location(Location location) {
    this.x = location.x;
    this.y = location.y;
  }

  /**
   *
   * @param o
   * @return
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Location location = (Location) o;
    return x == location.x &&
            y == location.y;
  }

  /**
   *
   * @return
   */
  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  /**
   *
   * @return
   */
  @Override
  public String toString() {
    return "x: " + this.x + " y: " + this.y;
  }

  /**
   * Estimate the size of this object.
   * Used primarily for testing purposes.
   * @return the estimated size (bytes)
   */
  public double estimateSize() {
    //There are two ints, so return 8;
    return 8;
  }
}
