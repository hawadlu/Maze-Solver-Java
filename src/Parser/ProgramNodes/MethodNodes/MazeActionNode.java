package Parser.ProgramNodes.MethodNodes;

import Parser.Parser;
import Parser.ProgramNodes.Exec;
import Parser.ProgramNodes.MathNodes.Number;
import Parser.ProgramNodes.MathNodes.NumberNode;
import Utility.Node;

/**
 * This class deals with calls to maze actions
 */
public class MazeActionNode implements Exec {
  MethodNode methodNode;

  public MazeActionNode(MethodNode methodNode) {
    this.methodNode = methodNode;
  }

  public MethodNode getMethodNode() {
    return methodNode;
  }

  @Override
  public Object execute(Parser parser) {
    if (methodNode.name.equals("getStart")) {
      return parser.handler.getStart();
    } else if (methodNode.name.equals("visit")) {
      String varName = (String) methodNode.parameters.get(0);
      Node toUpdate = (Node) parser.variables.get(varName).getValue();
      parser.handler.visit(toUpdate);
    } else if (methodNode.name.equals("isDone")) {
      String varName = (String) methodNode.parameters.get(0);
      Node toUpdate = (Node) parser.variables.get(varName).getValue();
      return parser.handler.checkDone(toUpdate);
    } else if (methodNode.name.equals("getNeighbours")){
      String varName = (String) methodNode.parameters.get(0);
      Node toUpdate = (Node) parser.variables.get(varName).getValue();
      return parser.handler.getNeighbours(toUpdate);
    } else if (methodNode.name.equals("isVisited")) {
      String varName = (String) methodNode.parameters.get(0);
      Node toUpdate = (Node) parser.variables.get(varName).getValue();
      return parser.handler.isVisited(toUpdate);
    } else if (methodNode.name.equals("setParent")) {
      String child = (String) methodNode.parameters.get(0);
      String parent = (String) methodNode.parameters.get(1);

      Node childNode = (Node) parser.variables.get(child.replaceAll(" ", "")).getValue();
      Node parentNode = (Node) parser.variables.get(parent.replaceAll(" ", "")).getValue();
      return parser.handler.setParent(childNode, parentNode);
    } else if (methodNode.name.equals("finish")) {
      String varName = (String) methodNode.parameters.get(0);
      Node toUpdate = (Node) parser.variables.get(varName).getValue();
      parser.handler.reportDone(toUpdate);
    } else if (methodNode.name.equals("setCost")) {
      String nodeName = (String) methodNode.parameters.get(0);
      Node toUpdate = (Node) parser.variables.get(nodeName).getValue();

      String varName = (String) methodNode.parameters.get(1);
      Number num = (Number) parser.variables.get(varName).getValue();
      double cost = num.calculate();

      parser.handler.setCost(toUpdate, cost);
    } else if (methodNode.name.equals("getCost")) {
      String nodeName = (String) methodNode.parameters.get(0);
      Node toUpdate = (Node) parser.variables.get(nodeName).getValue();
      return new NumberNode(parser.handler.getCost(toUpdate));
    } else if (methodNode.name.equals("getDistance")) {
      String nodeOneName = (String) methodNode.parameters.get(0);
      String nodeTwoName = (String) methodNode.parameters.get(1);

      Node nodeOne = (Node) parser.variables.get(nodeOneName).getValue();
      Node nodeTwo = (Node) parser.variables.get(nodeTwoName).getValue();

      return new NumberNode(parser.handler.getDistance(nodeOne, nodeTwo));
    } else parser.executionError("Unrecognised method " + methodNode.name);
    return null;
  }

  @Override
  public String toString() {
    return "Maze action " + methodNode;
  }
}
