package Utility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This class implements an arraylist that is comprised purely of locations.
 *
 * It is used so that the server can determine when a list of locations is being sent.
 * The standard instanceof method does not all 'instanceOf Arraylist<Location>' so
 * this is a way of getting around that while still being clear about what the list contains.
 */
public class LocationList extends ArrayList implements Serializable {
  ArrayList<Location> locations = new ArrayList<>();

  @Override
  public int size() {
    return locations.size();
  }

  @Override
  public boolean add(Object o) {
    return locations.add((Location) o);
  }

  @Override
  public Location get(int index) {
    return locations.get(index);
  }

  @Override
  public Location remove(int index) {
    return locations.remove(index);
  }

  @Override
  public String toString() {
    return "Location list of size: " + size();
  }

  /**
   * @return the underlying arraylist
   */
  public ArrayList<Location> getUnderlyingList() {
    return locations;
  }
}
