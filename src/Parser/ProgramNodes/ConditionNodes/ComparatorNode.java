package Parser.ProgramNodes.ConditionNodes;

import Parser.Parser;
import Parser.ProgramNodes.Exec;
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
    //todo turn this into a java comparator that can be used later.
    return null;
  }

  @Override
  public String toString() {
    return "Comparator: " + comparator;
  }
}
