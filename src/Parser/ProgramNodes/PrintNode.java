package Parser.ProgramNodes;

import Parser.Handler;
import Parser.ProgramNodes.MathNodes.Number;
import Parser.ProgramNodes.MethodNodes.MethodNode;
import Parser.ProgramNodes.VariableNodes.GetVariableNode;
import Parser.ProgramNodes.VariableNodes.VariableActionNode;

import java.util.ArrayList;

public class PrintNode implements Exec{
  ArrayList<Object> printValues = new ArrayList<>();
  private Handler handler;

  public PrintNode(Handler handler) {
    this.handler = handler;
  }

  public void append(StringBuilder str) {
    printValues.add(str);
  }

  public void append(Exec exec) {
    printValues.add(exec);
  }

  public void append(Number num) {
    printValues.add(" ");
    printValues.add(num);
  }

  /**
   * Makes a string, executing any nodes along the way.
   * @return
   */
  public Object makeString() {
    StringBuilder toReturn = new StringBuilder();

    for (Object obj: printValues) {
      if (obj instanceof String) toReturn.append(obj);
      else if (obj instanceof Number) toReturn.append(((Number) obj).calculate());
      else if (obj instanceof PrintNode) toReturn.append(((PrintNode) obj).makeString());
      else if (obj instanceof GetVariableNode) toReturn.append(((GetVariableNode) obj).getInfo());
      else if (obj instanceof MethodNode) toReturn.append(((MethodNode) obj).execute());
      else if (obj instanceof VariableActionNode) toReturn.append(((VariableActionNode) obj).execute());
      else toReturn.append(obj);
    }

    return toReturn.toString();
  }

  @Override
  public void validate() {
    //todo implement me
  }

  @Override
  public Object execute() {
    StringBuilder toPrint = new StringBuilder();

    for (Object obj: printValues) {
      if (obj instanceof String) toPrint.append(obj);
      else if (obj instanceof Number) toPrint.append(((Number) obj).calculate());
      else if (obj instanceof PrintNode) toPrint.append(((PrintNode) obj).makeString());
      else if (obj instanceof GetVariableNode) toPrint.append(((GetVariableNode) obj).getInfo());
      else if (obj instanceof MethodNode) toPrint.append(((MethodNode) obj).execute());
      else if (obj instanceof VariableActionNode) toPrint.append(((VariableActionNode) obj).execute());
      else toPrint.append(obj);
    }

    System.out.println(toPrint);
    return null;
  }

  @Override
  public String toString() {
    return printValues.toString();
  }
}
