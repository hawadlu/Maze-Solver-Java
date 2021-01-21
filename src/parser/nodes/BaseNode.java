package parser.nodes;

import java.util.ArrayList;

/**
 * This class contains a lost of all the statements in the program and will sequentially execute them.
 */
public class BaseNode implements Exec {
  ArrayList<Exec> statements;
  public BaseNode(ArrayList<Exec> statements) {
    this.statements = statements;
  }

  @Override
  public Object execute() {
    System.out.println("Starting execution");
    for (Exec statement: statements){
      statement.execute();
    }

//    if (!parser.handler.done) parser.executionError(parser.handler.getPlayer() + " Base node reached the end without solving");
    return false;
  }

  @Override
  public void validate() {
    for (Exec statement: statements) {
      statement.validate();
    }
  }

  @Override
  public String toString() {
    StringBuilder string = new StringBuilder();
    for (Exec statement : statements) string.append(statement).append("\n");
    return string.toString();
  }

  @Override
  public String getExecType() {
    //todo implement me.
    return null;
  }
}
