package parser.nodes;

import parser.interfaces.Exec;
import parser.interfaces.Number;
import parser.nodes.methods.MethodNode;
import parser.nodes.variables.GetVariableNode;
import parser.nodes.variables.VariableActionNode;

import java.util.ArrayList;

public class PrintNode implements Exec {
  final ArrayList<Object> printValues = new ArrayList<>();

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
   * @return a concatenated string of all the object in the printValues array.
   */
  public Object makeString() {
    return concatenate();
  }

  /**
   * Concatenate all of the values in printValues.
   * @return a concatenated string.
   */
  private String concatenate() {
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
    System.out.println(concatenate());
    return null;
  }

  @Override
  public String toString() {
    return printValues.toString();
  }

  @Override
  public String getExecType() {
    //todo implement me.
    return null;
  }
}
