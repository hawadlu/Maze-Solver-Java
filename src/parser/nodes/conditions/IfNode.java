package parser.nodes.conditions;

import parser.Handler;
import parser.interfaces.Condition;
import parser.interfaces.Exec;

import java.util.ArrayList;

/**
 * This class is used to run if statements.
 * It contains a condition and a block of statements to run if the condition is true.
 * It also contains an else node which may be run if the condition is false.
 * Note that the else node is not required and may therefore be null.
 */
public class IfNode implements Exec {

  final ArrayList<Exec> statements;
  final Condition ifCondition;
  ElseNode elseNode;
  Handler handler;

  /**
   * Create the object.
   * @param condition the condition that must be true for the statements to run.
   * @param statements a list of all the statements.
   */
  public IfNode(Condition condition, ArrayList<Exec> statements, Handler handler) {
    this.ifCondition = condition;
    this.statements = statements;
    this.handler = handler;
  }

  /**
   * Add an else node to the if statement.
   * @param elseNode the else node to be added.
   */
  public void addElse(ElseNode elseNode) {
    this.elseNode = elseNode;
  }

  /**
   * Go through each of the attached statements (including else) and validate them.
   */
  @Override
  public void validate() {
    for (Exec statement: statements) statement.validate();

    if (elseNode != null) elseNode.validate();
  }

  /**
   * Check the condition, if it is true run the statements in the else block.
   * If the condition is false and the elseNode != null run the statements in the else block.
   */
  @Override
  public Object execute(boolean DEBUG) {
    if (DEBUG) System.out.println(handler.getPlayer() + " " + getExecType());

    if (ifCondition.evaluate(DEBUG)) {
      for (Exec statement: statements) {
        statement.execute(DEBUG);
      }
    } else if (this.elseNode != null) {
      elseNode.execute(DEBUG);
    }
    return null;
  }

  /**
   * Return a string representation of the object.
   */
  @Override
  public String toString() {
    StringBuilder toReturn = new StringBuilder();
    StringBuilder internals = new StringBuilder();
    for (Exec statement: statements) internals.append("\t").append(statement).append("\n");

    toReturn.append("if (").append(ifCondition).append("){\n").append(internals).append("}\n");

    if (elseNode != null) toReturn.append(elseNode);

    return toReturn.toString();
  }

  /**
   * Get the type that would be returned when the execute method is called.
   */
  @Override
  public String getExecType() {
    return "IfNode";
  }
}
