package Parser.ProgramNodes.VariableNodes;

import Parser.Parser;
import Parser.ProgramNodes.Exec;

/**
 * Class for getting a referenced variable at runtime
 */
public class GetVariableNode implements Exec {
  String varName;

  public GetVariableNode(String varName) {
    this.varName = varName;
  }

  public String getInfo(Parser parser) {
    return parser.variables.get(varName).print();
  }

  @Override
  public Object execute(Parser parser) {
    return parser.variables.get(varName);
  }

  @Override
  public String toString() {
    return "get " + varName;
  }
}
