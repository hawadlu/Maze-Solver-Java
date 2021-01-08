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
    this.varName = varName;
    this.handler = handler;
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
