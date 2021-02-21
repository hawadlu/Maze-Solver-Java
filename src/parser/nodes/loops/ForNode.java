package parser.nodes.loops;

import parser.Handler;
import parser.interfaces.Exec;
import parser.nodes.variables.VariableNode;
import utility.Node;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The class implements a foreach loop.
 */
public class ForNode implements Exec {
  final String varName;
  final String collectionName;
  final ArrayList<Exec> statements;
  VariableNode varNode;
  private final Handler handler;

  /**
   * Create the object.
   * @param varName The name of the variable that will be used inside the loop.
   * @param collectionName The name of the collection to iterate through.
   * @param statements List of statements to be executed.
   * @param handler The maze handler.
   */
  public ForNode(String varName, String collectionName, ArrayList<Exec> statements, Handler handler) {
    this.varName = varName;
    this.collectionName = collectionName;
    this.statements = statements;
    this.handler = handler;
  }

  /**
   * Add the temp variable to the map.
   * Go through each of the statements and call the relevant validate method.
   * Remove the temp variable from the map.
   */
  @Override
  public void validate() {
    //Add the variable to the map
    handler.addVariable(varName, new VariableNode("Node", varName, handler));

    //Validate the statements
    for (Exec statement: statements) statement.validate();

    //Remove the variable
    handler.removeFromMap(varName);
  }

  /**
   * Create a new variable node using the name provided.
   *
   * Go through each item in the provided list and run each of the statements.
   */
  @Override
  public Object execute(boolean DEBUG) {
    if (DEBUG) System.out.println(handler.getPlayer() + " " + getExecType());

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
        if (!handler.getPlayer().isDone()) statement.execute(DEBUG);
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

  /**
   * Return a string representation of the object.
   */
  @Override
  public String toString() {
    StringBuilder internals = new StringBuilder();
    for (Exec statement : statements) {
      internals.append("\t").append(statement).append("\n");
    }

    return "For (" + varName + " : " + collectionName + ") {\n" + internals + "\n}\n";
  }

  /**
   * Get the type that would be returned when the execute method is called.
   */
  @Override
  public String getExecType() {
    return "ForNode";
  }
}
