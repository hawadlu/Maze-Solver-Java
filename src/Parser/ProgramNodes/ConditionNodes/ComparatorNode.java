package Parser.ProgramNodes.ConditionNodes;

import Parser.Parser;
import Parser.ProgramNodes.Exec;
import Parser.ProgramNodes.MethodNodes.MazeActionNode;
import Parser.ProgramNodes.MethodNodes.MethodNode;
import Utility.Location;
import Utility.Node;

import java.util.Comparator;

public class ComparatorNode implements Exec {
  Comparator<Node> comparator;
  Exec callToInit;

  public ComparatorNode(Exec parseMazeCall) {
    this.callToInit = parseMazeCall;
  }

  @Override
  public Object execute(Parser parser) {
    comparator = (nodeOne, nodeTwo) -> {
      //todo implement more ways to compare

      //Check what the user wants to use as a compare
      if (callToInit instanceof MazeActionNode) {
        MazeActionNode action = (MazeActionNode) callToInit;

        //Get the method node
        MethodNode method = action.getMethodNode();

        //Look at the method
        String methodName = method.getName();

        if (methodName.equals("getCost")) return Double.compare(nodeOne.getCost(), nodeTwo.getCost());
      }

      return 0;
    };

    return comparator;
  }

  @Override
  public String toString() {
    return "Comparator: " + callToInit;
  }
}
