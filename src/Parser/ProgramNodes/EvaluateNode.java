package Parser.ProgramNodes;

import Parser.Parser;
import Parser.ProgramNodes.MathNodes.Number;
import Parser.ProgramNodes.VariableNodes.GetVariableNode;

/**
 * Evaluate and return a number
 */
public class EvaluateNode implements Exec, Number {
  Exec toEvaluate;

  public EvaluateNode(Exec toEvaluate) {
    this.toEvaluate = toEvaluate;
  }


  @Override
  public Object execute(Parser parser) {
    return null;
  }

  @Override
  public double calculate() {
    //todo, will probably have to cast to an number node (instanceof)
    return 0;
  }

  @Override
  public String toString() {
    //todo implement me
    return super.toString();
  }
}
