package parser.nodes.conditions;

import parser.Handler;
import parser.interfaces.Exec;

import java.util.ArrayList;

/**
 * This class is attached to if nodes and contains the statements that will be run if the
 * else branch is triggered.
 */
public class ElseNode implements Exec {
  final ArrayList<Exec> statements;
  Handler handler;

  /**
   * Create a new object.
   * @param statements the list of statements that should be executed.
   */
  public ElseNode(ArrayList<Exec> statements, Handler handler) {
    this.statements = statements;
    this.handler = handler;
  }

  /**
   * Go through each statement in the list and execute it.
   */
  @Override
  public Object execute(boolean DEBUG_ON) {
    if (DEBUG_ON) System.out.println(handler.getPlayer() + " " + getExecType());

    for (Exec statement: statements) {
      statement.execute(DEBUG_ON);
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
