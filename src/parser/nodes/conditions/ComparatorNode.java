package parser.nodes.conditions;

import Utility.Node;
import parser.Handler;
import parser.nodes.Exec;
import parser.nodes.Value;

import java.util.Comparator;

/**
 * The comparator node is used by priority queues to decide the order
 * in which each object should be dispensed.
 */
public class ComparatorNode implements Exec, Value {
  Comparator<Node> comparator;
  String methodName;
  private Handler handler;

  /**
   * Setup the parameters that will be used later to create the comparator.
   *
   * @param methodName the name of tme method that will be used to create the comparator.
   *
   * @param handler the maze handler object.
   */
  public ComparatorNode(String methodName, Handler handler) {
    this.methodName = methodName;
    this.handler = handler;
  }

  /**
   * Check that the validator is correct.
   */
  @Override
  public void validate() {
    //Check that the supplied exec is of the correct type
    //todo implement me
  }

  /**
   * Create the comparator object based on the method name provided in the constructor.
   *
   * @return the newly created comparator.
   */
  @Override
  public Object execute() {
    //Check to see if the user is using a correct maze method

    comparator = (nodeOne, nodeTwo) -> {

      if (methodName.equals("getCost")) {
        return Double.compare(nodeOne.getCost(), nodeTwo.getCost());
      } else if (methodName.equals("getNeighbourCount")) {
        return Double.compare(nodeOne.getNeighbours().size(), nodeTwo.getNeighbours().size());
      } else if (methodName.equals("distanceToDestination")) {
        return Double.compare(handler.getDistanceToDestination(nodeOne),
                handler.getDistanceToDestination(nodeTwo));
      }

      return 0;
    };

    return comparator;
  }

  /**
   * Create a string representation of the object.
   *
   * @return a string representation of the object.
   */
  @Override
  public String toString() {
    return "Comparator: " + methodName;
  }

  /**
   * Get the type of this object.
   *
   * @return a string containing the object type.
   */
  @Override
  public String getType() {
    return "Comparator";
  }

  /**
   * Get the type of the object that will be returned during execution.
   *
   * @return a string containing the object type.
   */
  @Override
  public String getExecType() {
    //todo implement me.
    return null;
  }
}
