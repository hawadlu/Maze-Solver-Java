package Parser.ProgramNodes.LoopNodes;

import Parser.Handler;
import Parser.ProgramNodes.Exec;
import Parser.ProgramNodes.VariableNodes.VariableNode;
import Utility.Node;

import java.util.ArrayList;
import java.util.Collection;

public class ForNode implements Exec {
  String varName, collectionName;
  ArrayList<Exec> statements;
  VariableNode varNode;
  private Handler handler;


  public ForNode(String varName, String collectionName, ArrayList<Exec> statements, Handler handler) {
    this.varName = varName;
    this.collectionName = collectionName;
    this.statements = statements;
    this.handler = handler;
  }

  @Override
  public void validate() {
    //todo implement me
  }

  @Override
  public Object execute() {
    //make variable node
    String type = "MazeNode";
    Object value = handler.getFromMap(collectionName).getValue();

    this.varNode = new VariableNode(type, varName, handler);
    handler.addVariable(varName, varNode);

    //Get and iterate through a list of the nodes
    for (Node node: getValueList(value)) {
      this.varNode.setValue(node);

      //Go through the statements
      for (Exec statement: statements) {
        statement.execute();
      }

    }

    //Remove the variable
    handler.removeFromMap(varName);

    return null;
  }

  /**
   * Get the list.
   * @param value the object to get from.
   * @return the list.
   */
  private ArrayList<Node> getValueList(Object value) {
    ArrayList<Node> toReturn = new ArrayList<>();

    for (Object node: (Collection) value) toReturn.add((Node) node);

    return toReturn;
  }

  @Override
  public String toString() {
    StringBuilder internals = new StringBuilder();
    for (Exec statement : statements) {
      internals.append("\t").append(statement).append("\n");
    }

    return "For (" + varName + " : " + collectionName + ") {\n" + internals + "\n}\n";
  }

  @Override
  public String getExecType() {
    //todo implement me.
    return null;
  }
}
