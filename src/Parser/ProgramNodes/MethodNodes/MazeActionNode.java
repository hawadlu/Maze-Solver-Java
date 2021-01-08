package Parser.ProgramNodes.MethodNodes;

import Parser.Handler;
import Parser.ProgramNodes.Exec;
import Parser.ProgramNodes.MathNodes.NumberNode;
import Utility.Node;
import Parser.ProgramNodes.MathNodes.Number;


/**
 * This class deals with calls to maze actions
 */
public class MazeActionNode implements Exec {
  MethodNode methodNode;
  private Handler handler;

  public MazeActionNode(MethodNode methodNode, Handler handler) {
    this.methodNode = methodNode;
    this.handler = handler;
  }

  public MethodNode getMethodNode() {
    return methodNode;
  }

  @Override
  public Object execute() {
    if (methodNode.name.equals("getStart")) {
      return handler.getStart();
    } else if (methodNode.name.equals("visit")) {
      String varName = (String) methodNode.parameters.get(0);
      Node toUpdate = (Node) handler.getFromMap(varName).getValue();
      handler.visit(toUpdate);
    } else if (methodNode.name.equals("isDone")) {
      String varName = (String) methodNode.parameters.get(0);
      Node toUpdate = (Node) handler.getFromMap(varName).getValue();
      return handler.checkDone(toUpdate);
    } else if (methodNode.name.equals("getNeighbours")){
      String varName = (String) methodNode.parameters.get(0);
      Node toUpdate = (Node) handler.getFromMap(varName).getValue();
      return handler.getNeighbours(toUpdate);
    } else if (methodNode.name.equals("isVisited")) {
      String varName = (String) methodNode.parameters.get(0);
      Node toUpdate = (Node) handler.getFromMap(varName).getValue();
      return handler.isVisited(toUpdate);
    } else if (methodNode.name.equals("setParent")) {
      String child = (String) methodNode.parameters.get(0);
      String parent = (String) methodNode.parameters.get(1);

      Node childNode = (Node) handler.getFromMap(child.replaceAll(" ", "")).getValue();
      Node parentNode = (Node) handler.getFromMap(parent.replaceAll(" ", "")).getValue();
      return handler.setParent(childNode, parentNode);
    } else if (methodNode.name.equals("finish")) {
      String varName = (String) methodNode.parameters.get(0);
      Node toUpdate = (Node) handler.getFromMap(varName).getValue();
      handler.reportDone(toUpdate);
    } else if (methodNode.name.equals("setCost")) {
      String nodeName = (String) methodNode.parameters.get(0);
      Node toUpdate = (Node) handler.getFromMap(nodeName).getValue();

      String varName = (String) methodNode.parameters.get(1);
      Number num = (Number) handler.getFromMap(varName).getValue();
      double cost = num.calculate();

      handler.setCost(toUpdate, cost);
    } else if (methodNode.name.equals("getCost")) {
      String nodeName = (String) methodNode.parameters.get(0);
      Node toUpdate = (Node) handler.getFromMap(nodeName).getValue();
      return new NumberNode(handler.getCost(toUpdate));
    } else if (methodNode.name.equals("getDistance")) {
      String nodeOneName = (String) methodNode.parameters.get(0);
      String nodeTwoName = (String) methodNode.parameters.get(1);

      Node nodeOne = (Node) handler.getFromMap(nodeOneName).getValue();
      Node nodeTwo = (Node) handler.getFromMap(nodeTwoName).getValue();

      return new NumberNode(handler.getDistance(nodeOne, nodeTwo));
    } else if (methodNode.name.equals("distanceToDestination")) {
      Node node = (Node) handler.getFromMap((String) methodNode.parameters.get(0)).getValue();

      return new NumberNode(handler.getDistanceToDestination(node));
    }
    return null;
  }

  @Override
  public void validate() {
    methodNode.validate();
  }

  @Override
  public String toString() {
    return "Maze action " + methodNode;
  }
}
