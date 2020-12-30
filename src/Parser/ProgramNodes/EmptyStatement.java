package Parser.ProgramNodes;

import Parser.Parser;

/**
 * Literally an empty statement.
 */
public class EmptyStatement implements Exec {
  @Override
  public Object execute(Parser parser) {
    return null;
  }

  @Override
  public String toString() {
    return "Empty statement";
  }
}
