package Parser.ProgramNodes.ConditionNodes;

import Parser.Parser;
import Parser.ProgramNodes.Exec;
import Parser.ProgramNodes.MethodNodes.MazeActionNode;
import Parser.ProgramNodes.MethodNodes.MethodNode;
import Utility.Node;
import Parser.Handler;

import java.util.Comparator;

public class ComparatorNode implements Exec {
  Comparator<Node> comparator;
  Exec callToInit;
  private Handler handler;


  public ComparatorNode(Exec parseMazeCall, Handler handler) {
    this.callToInit = parseMazeCall;
    this.handler = handler;
  }

  @Override
  public void validate() {
    //Check that the supplied exec is of the correct type
    if (callToInit instanceof MazeActionNode) {
      MethodNode node = (MethodNode) ((MazeActionNode) callToInit).getMethodNode();

      //Check the method that is being called
      if (!node.getName().equals("getNeighbourCount") && !node.getName().equals("getCost")) {
        Parser.fail(node.getName() + " is not a valid method for initialising comparators.\n" +
                "Use getNeighbourCount(node) or getCost(node)", null);
      }

    } else Parser.fail("Comparator must be initialised with a maze method.", null);

    //Check that the supplied exec is valid
    callToInit.validate();
  }

  @Override
  public Object execute() {
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

      comparator = (nodeOne, nodeTwo) -> {

        if (methodName.equals("getCost")) return Double.compare(nodeOne.getCost(), nodeTwo.getCost());
        else if (methodName.equals("getNeighbourCount"))
          return Double.compare(nodeOne.getNeighbours().size(), nodeTwo.getNeighbours().size());
        else if (methodName.equals("distanceToDestination"))
          return Double.compare(handler.getDistanceToDestination(nodeOne), handler.getDistanceToDestination(nodeTwo));

        return 0;
      };

      return comparator;
    }
    return null;
  }

  @Override
  public String toString() {
    return "Comparator: " + callToInit;
  }
}
