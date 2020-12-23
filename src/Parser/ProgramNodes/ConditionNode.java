package Parser.ProgramNodes;

public class ConditionNode implements Condition{
  Exec method;

  public ConditionNode(Exec method) {
    this.method = method;
  }

  @Override
  public boolean evaluate() {
    return false;
  }

  @Override
  public String toString() {
    return super.toString();
  }
}
