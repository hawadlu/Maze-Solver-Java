package Parser.ProgramNodes.LoopNodes;

import Parser.Parser;
import Parser.ProgramNodes.Exec;
import Parser.ProgramNodes.VariableNodes.VariableNode;
import Utility.Node;

import java.util.ArrayList;
import java.util.Collection;

public class ForNode implements Exec {
  String varName, collectionName;
  ArrayList<Exec> statements;
  VariableNode varNode;

  public ForNode(String varName, String collectionName, ArrayList<Exec> statements) {
    this.varName = varName;
    this.collectionName = collectionName;
    this.statements = statements;
  }

  @Override
  public Object execute(Parser parser) {
    //make variable node
    String type = "Node";
    Object value = parser.variables.get(collectionName).getValue();

    this.varNode = new VariableNode(varName, type);
    parser.variables.put(varName, varNode);

    //Get and iterate through a list of the nodes
    for (Node node: getValueList(value)) {
      System.out.println(node + " middle 1");
      this.varNode.setValue(node);

      //Go through the statements
      for (Exec statement: statements) {
        statement.execute(parser);
      }

      System.out.println(node + " middle 2");
    }

    //Remove the variable
    parser.variables.remove(varName);

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
}
