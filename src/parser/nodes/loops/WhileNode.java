package parser.nodes.loops;

import parser.interfaces.Condition;
import parser.interfaces.Exec;

import java.util.ArrayList;

/**
 * This class implements a while loop.
 */
public class WhileNode implements Exec {
  final ArrayList<Exec> statements;
  final Condition loopCondition;

  /**
   * Create the object.
   * @param condition The condition that should be checked.
   * @param statements The list of statements to execute.
   */
  public WhileNode(Condition condition, ArrayList<Exec> statements) {
    this.loopCondition = condition;
    this.statements = statements;
  }

  /**
   * Call the validate method on each of the statements provided.
   */
  @Override
  public void validate() {
    for (Exec statement: statements) statement.validate();
  }

  /**
   * Run through the list of statements while the provided condition is true.
   *
   * @return does not need to return anything, so it returns null;
   */
  @Override
  public Object execute() {

    while (loopCondition.evaluate()) {
      for (Exec statement : statements) {
        statement.execute();
      }
    }

    return null;
  }

  /**
   * Return a string representation of the object.
   */
  @Override
  public String toString() {
    StringBuilder internals = new StringBuilder();
    for (Exec statement : statements) internals.append("\t").append(statement).append("\n");

    return "while (" + loopCondition + ") {\n" + internals + "\n}\n";
  }

  /**
   * Get the type that would be returned when the execute method is called.
   */
  @Override
  public String getExecType() {
    return "WhileNode";
  }
}
