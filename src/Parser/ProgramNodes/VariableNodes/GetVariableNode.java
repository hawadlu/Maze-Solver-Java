package Parser.ProgramNodes.VariableNodes;

import Parser.Handler;
import Parser.ProgramNodes.Exec;

/**
 * Class for getting a referenced variable at runtime
 */
public class GetVariableNode implements Exec {
  String varName;
  private Handler handler;

  public GetVariableNode(String varName, Handler handler) {
    this.varName = varName.replaceAll(" ", "");
    this.handler = handler;
  }

  /**
   * @return the corresponding variable from the variable map.
   */
  public VariableNode extractVariable() {
    return handler.getFromMap(varName);
  }

  public String getVarName() {
    return varName;
  }

  public String getInfo() {
    return handler.getFromMap(varName).print();
  }

  @Override
  public void validate() {
    //todo implement me
  }

  @Override
  public Object execute() {
    return handler.getFromMap(varName);
  }

  @Override
  public String toString() {
    return "get " + varName;
  }
}
