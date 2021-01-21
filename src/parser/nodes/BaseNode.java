package parser.nodes;

import parser.interfaces.Exec;

import java.util.ArrayList;

/**
 * This class contains a lost of all the statements in the program and will sequentially execute them.
 */
public class BaseNode implements Exec {
  final ArrayList<Exec> statements;

  /**
   * Create a new base node.
   * @param statements all of the statements that will be executed by the program.
   */
  public BaseNode(ArrayList<Exec> statements) {
    this.statements = statements;
  }

  /**
   * Run through each of the stored statements and execute them.
   */
  @Override
  public Object execute() {
    System.out.println("Starting execution");
    for (Exec statement: statements){
      statement.execute();
    }

//    if (!parser.handler.done) parser.executionError(parser.handler.getPlayer() + " Base node reached the end without solving");
    return false;
  }

  /**
   * Go through each of the stored statements and call the
   * appropriate method to validate them.
   */
  @Override
  public void validate() {
    for (Exec statement: statements) {
      statement.validate();
    }
  }

  /**
   * Go through each of the attached statements and call
   * the appropriate toString method.
   */
  @Override
  public String toString() {
    StringBuilder string = new StringBuilder();
    for (Exec statement : statements) string.append(statement).append("\n");
    return string.toString();
  }

  /**
   * Get the type of node would be returned if the execute method is called
   */
  @Override
  public String getExecType() {
    return "BaseNode";
  }
}
