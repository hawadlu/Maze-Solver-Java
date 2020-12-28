package Parser.ProgramNodes;

import Parser.Parser;

import java.util.ArrayList;

/**
 * This class contains a lost of all the statements in the program and will sequentially execute them.
 */
public class BaseNode implements Exec{
  ArrayList<Exec> statements;
  public BaseNode(ArrayList<Exec> statements) {
    this.statements = statements;
  }

  @Override
  public Object execute(Parser parser) {
    for (Exec statement: statements){
      statement.execute(parser);
    }
    return null;
  }

  @Override
  public String toString() {
    StringBuilder string = new StringBuilder();
    for (Exec statement : statements) string.append(statement).append("\n");
    return string.toString();
  }
}
