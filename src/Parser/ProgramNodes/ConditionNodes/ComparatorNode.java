package Parser.ProgramNodes.ConditionNodes;

import Parser.Parser;
import Parser.ProgramNodes.Exec;
import Parser.ProgramNodes.MethodNodes.MazeActionNode;
import Parser.ProgramNodes.MethodNodes.MethodNode;
import Utility.Node;

import java.util.Arrays;
import java.util.Comparator;

public class ComparatorNode implements Exec {
  Comparator<Node> comparator;
  Exec callToInit;

  public ComparatorNode(Exec parseMazeCall) {
    this.callToInit = parseMazeCall;
  }

  @Override
  public Object execute(Parser parser) {
    //Make sure that the method that the user wants to use is valid
    if (callToInit instanceof MazeActionNode) {
      //Check what the user wants to use as a compare
      MazeActionNode action = (MazeActionNode) callToInit;

      //Get the method node
      MethodNode method = action.getMethodNode();

      //Look at the method
      String methodName = method.getName();

      //Check to see if the user is using a correct maze method
      String[] validMethods = {"getCost", "getNeighbourCount", "distanceToDestination"};
      if (!Arrays.asList(validMethods).contains(methodName)) parser.executionError(methodName + " is not a valid comparator method.");

      comparator = (nodeOne, nodeTwo) -> {

        if (methodName.equals("getCost")) return Double.compare(nodeOne.getCost(), nodeTwo.getCost());
        else if (methodName.equals("getNeighbourCount"))
          return Double.compare(nodeOne.getNeighbours().size(), nodeTwo.getNeighbours().size());
        else if (methodName.equals("distanceToDestination"))
          return Double.compare(parser.handler.getDistanceToDestination(nodeOne), parser.handler.getDistanceToDestination(nodeTwo));
        else parser.executionError(methodName + " is not a valid comparator method");

        return 0;
      };

      return comparator;
    } else {
      parser.executionError("Failed to create parser");
      return null;
    }
  }

  @Override
  public String toString() {
    return "Comparator: " + callToInit;
  }
}
