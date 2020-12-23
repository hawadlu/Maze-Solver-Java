package Parser.ProgramNodes;

public class NumberNode implements Exec {
  double value;

  public NumberNode(double number) {
    this.value = number;
  }

  @Override
  public void Execute() {

  }

  @Override
  public String toString() {
    return super.toString();
  }
}
