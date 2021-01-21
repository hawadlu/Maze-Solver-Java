package Parser.ProgramNodes.ConditionNodes;

import Parser.Handler;
import Parser.ProgramNodes.Exec;
import Parser.ProgramNodes.Value;
import Utility.Node;

import java.util.Comparator;

public class ComparatorNode implements Exec, Value {
  Comparator<Node> comparator;
  String methodName;
  private Handler handler;


  public ComparatorNode(String methodName, Handler handler) {
    this.methodName = methodName;
    this.handler = handler;
  }

  @Override
  public void validate() {
    //Check that the supplied exec is of the correct type

  }

  @Override
  public Object execute() {
    //Check to see if the user is using a correct maze method
//    String[] validMethods = {"getCost", "getNeighbourCount", "distanceToDestination"};

    comparator = (nodeOne, nodeTwo) -> {

      if (methodName.equals("getCost")) {
        return Double.compare(nodeOne.getCost(), nodeTwo.getCost());
      } else if (methodName.equals("getNeighbourCount")) {
        return Double.compare(nodeOne.getNeighbours().size(), nodeTwo.getNeighbours().size());
      } else if (methodName.equals("distanceToDestination")) {
        return Double.compare(handler.getDistanceToDestination(nodeOne), handler.getDistanceToDestination(nodeTwo));
      }

      return 0;
    };

    return comparator;
  }

  @Override
  public String toString() {
    return "Comparator: " + methodName;
  }

  @Override
  public String getType() {
    return "Comparator";
  }

  @Override
  public String getExecType() {
    //todo implement me.
    return null;
  }
}
