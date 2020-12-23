package Parser.ProgramNodes;

/**
 * Perform a given action on a variable
 */
public class VariableAction implements Exec {
  String name;
  Exec action;
  public VariableAction(String varName, Exec action) {
    this.name = varName;
    this.action = action;
  }

  @Override
  public void Execute() {

  }

  @Override
  public String toString() {
    return super.toString();
  }
}
