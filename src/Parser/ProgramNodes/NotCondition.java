package Parser.ProgramNodes;

public class NotCondition implements Condition {
  Condition condition;

  public NotCondition(Condition condition) {
    this.condition = condition;
  }

  //todo make this return true if the condition is not true.
  @Override
  public boolean evaluate() {
    return false;
  }

  @Override
  public String toString() {
    return super.toString();
  }
}
