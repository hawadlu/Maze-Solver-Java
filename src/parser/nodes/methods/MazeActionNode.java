package parser.nodes.methods;

import parser.Handler;
import parser.Parser;
import parser.nodes.EvaluateNode;
import parser.interfaces.Exec;
import parser.nodes.math.NumberNode;
import parser.nodes.variables.GetVariableNode;
import Utility.Node;
import parser.interfaces.Number;


/**
 * This class deals with calls to maze actions
 */
public class MazeActionNode implements Exec {
  final MethodNode methodNode;
  private final Handler handler;

  public MazeActionNode(MethodNode methodNode, Handler handler) {
    this.methodNode = methodNode;
    this.handler = handler;
  }

  @Override
  public Object execute() {
    switch (methodNode.getName()) {
      case "getStart":
        return handler.getStart();
      case "visit": {
        String varName = (String) methodNode.getParameters().get(0);
        Node toUpdate = (Node) handler.getFromMap(varName).getValue();
        handler.visit(toUpdate);
        break;
      }
      case "isDone": {
        String varName = (String) methodNode.getParameters().get(0);
        Node toUpdate = (Node) handler.getFromMap(varName).getValue();
        return handler.checkDone(toUpdate);
      }
      case "getNeighbours": {
        Node toUpdate = null;
        Object parameter = methodNode.getParameters().get(0);

        if (parameter instanceof String) {
          toUpdate = (Node) handler.getFromMap((String) parameter).getValue();
        } else if (parameter instanceof GetVariableNode) {
          toUpdate = (Node) handler.getFromMap((String) parameter).getValue();
        } else if (parameter instanceof EvaluateNode || parameter instanceof MazeActionNode || parameter instanceof MethodNode) {
          toUpdate = (Node) ((Exec) parameter).execute();
        }

        //Make sure that the above code succeeded
        if (toUpdate != null) return handler.getNeighbours(toUpdate);
        else Parser.fail("Failed to invoke method getNeighbours", "Execution", null, handler.getPopup());

        break;
      }
      case "isVisited": {
        String varName = (String) methodNode.getParameters().get(0);
        Node toUpdate = (Node) handler.getFromMap(varName).getValue();
        return handler.isVisited(toUpdate);
      }
      case "setParent":
        String child = (String) methodNode.getParameters().get(0);
        String parent = (String) methodNode.getParameters().get(1);

        Node childNode = (Node) handler.getFromMap(child.replaceAll(" ", "")).getValue();
        Node parentNode = (Node) handler.getFromMap(parent.replaceAll(" ", "")).getValue();
        handler.setParent(childNode, parentNode);
        break;
      case "finish": {
        String varName = (String) methodNode.getParameters().get(0);
        Node toUpdate = (Node) handler.getFromMap(varName).getValue();
        handler.reportDone(toUpdate);
        break;
      }
      case "setCost": {
        String nodeName = (String) methodNode.getParameters().get(0);
        Node toUpdate = (Node) handler.getFromMap(nodeName).getValue();

        String varName = (String) methodNode.getParameters().get(1);
        Number num = (Number) handler.getFromMap(varName).getValue();
        double cost = num.calculate();

        handler.setCost(toUpdate, cost);
        break;
      }
      case "getCost": {
        String nodeName = (String) methodNode.getParameters().get(0);
        Node toUpdate = (Node) handler.getFromMap(nodeName).getValue();
        return new NumberNode(handler.getCost(toUpdate));
      }
      case "getDistance":
        String nodeOneName = (String) methodNode.getParameters().get(0);
        String nodeTwoName = (String) methodNode.getParameters().get(1);

        Node nodeOne = (Node) handler.getFromMap(nodeOneName).getValue();
        Node nodeTwo = (Node) handler.getFromMap(nodeTwoName).getValue();

        return new NumberNode(handler.getDistance(nodeOne, nodeTwo));
      case "distanceToDestination":
        Node node = (Node) handler.getFromMap((String) methodNode.getParameters().get(0)).getValue();

        return new NumberNode(handler.getDistanceToDestination(node));
      case "fail":
        Parser.fail(methodNode.execute().toString(), "Execution", null, handler.getPopup());
        break;
      default:
        Parser.fail("Unrecognised method '" + methodNode.getName() + "'", "Execution", null, handler.getPopup());
        break;
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

  @Override
  public String getExecType() {
    return switch (methodNode.getName()) {
      case "getStart" -> "MazeNode";
      case "getNeighbours" -> "Collection";
      case "getCost", "distanceToDestination", "getDistance" -> "Number";
      default -> null; //Used by case "visit", "isDone", "isVisited", "setParent", "finish", "setCost" -> null;
    };
  }
}
