package parser.nodes.conditions;

import parser.interfaces.Exec;

import java.util.ArrayList;

/**
 * This class is attached to if nodes and contains the statements that will be run if the
 * else branch is triggered.
 */
public class ElseNode implements Exec {
  final ArrayList<Exec> statements;

  /**
   * Create a new object.
   * @param statements the list of statements that should be executed.
   */
  public ElseNode(ArrayList<Exec> statements) {
    this.statements = statements;
  }

  /**
   * Go through each statement in the list and execute it.
   */
  @Override
  public Object execute() {
    for (Exec statement: statements) {
      statement.execute();
    }
    return null;
  }

  /**
   * Go through each statement in the list and validate it.
   */
  @Override
  public void validate() {
    for (Exec statement: statements) statement.validate();
  }

  /**
   * Return a string representation of the object.
   */
  @Override
  public String toString() {
    StringBuilder internals = new StringBuilder();
    for (Exec statement: statements) internals.append("\t").append(statement).append("\n");

    return "else {\n" + internals + "}\n";
  }

  /**
   * Get the type that would be returned when the execute method is called.
   */
  @Override
  public String getExecType() {
    return "ElseNode";
  }
}
