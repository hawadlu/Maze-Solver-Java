package Parser.ProgramNodes;

/**
 * Literally an empty statement.
 */
public class EmptyNode implements Exec {
  @Override
  public Object execute() {
    return null;
  }

  @Override
  public String toString() {
    return "Empty statement";
  }

  @Override
  public void validate() {
    //No validation required. This statement will always be empty (there are no variables).
  }

  @Override
  public String getExecType() {
    //todo implement me.
    return null;
  }
}
