package parser.nodes.loops;

import parser.Handler;
import parser.interfaces.Exec;
import parser.nodes.variables.VariableNode;
import Utility.Node;

import java.util.ArrayList;
import java.util.Collection;

public class ForNode implements Exec {
  final String varName;
  final String collectionName;
  final ArrayList<Exec> statements;
  VariableNode varNode;
  private final Handler handler;


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
  @SuppressWarnings("unchecked")
  private ArrayList<Node> getValueList(Object value) {

    return new ArrayList<>((Collection<Node>) value);
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
