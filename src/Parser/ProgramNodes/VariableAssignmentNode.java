package Parser.ProgramNodes;

/**
 * Class used to assign values to variables during execution
 */
public class VariableAssignmentNode implements Exec {
  String varName;
  Exec newVal;

  public VariableAssignmentNode(String varName, Exec newVal) {
    this.varName = varName;
    this.newVal = newVal;
  }

  @Override
  public void Execute() {

  }

  @Override
  public String toString() {
    return super.toString();
  }
}
