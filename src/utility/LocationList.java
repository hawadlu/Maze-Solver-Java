package Utility;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class implements an arraylist that is comprised purely of locations.
 *
 * It is used so that the server can determine when a list of locations is being sent.
 * The standard instanceof method does not all 'instanceOf Arraylist<Location>' so
 * this is a way of getting around that while still being clear about what the list contains.
 */
public class LocationList extends ArrayList implements Serializable {
  ArrayList<Location> locations = new ArrayList<>();

  /**
   * @return an int representing the length of the list.
   */
  @Override
  public int size() {
    return locations.size();
  }

  /**
   * Add an object to the list.
   * @param o the object to add to the list.
   * @return a boolean to indicate a successful operation.
   */
  @Override
  public boolean add(Object o) {
    return locations.add((Location) o);
  }

  /**
   * Get an object from the list at a specified index.
   * @param index the index of the object to get
   * @return the object at the specified index.
   */
  @Override
  public Location get(int index) {
    return locations.get(index);
  }

  /**
   * Remove an object from the list at the specified index.
   * @param index the index of the object to remove.
   * @return the object at the specified index.
   */
  @Override
  public Location remove(int index) {
    return locations.remove(index);
  }

  /**
   * @return a string representation of the list.
   */
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
