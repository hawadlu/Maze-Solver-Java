package Parser.ProgramNodes;

import Parser.Parser;
import Parser.ProgramNodes.MethodNodes.MethodNode;
import Parser.ProgramNodes.VariableNodes.GetVariableNode;
import Parser.ProgramNodes.VariableNodes.VariableActionNode;
import Parser.ProgramNodes.MathNodes.Number;


import java.util.ArrayList;

public class PrintNode implements Exec{
  ArrayList<Object> printVals = new ArrayList<>();

  public void append(StringBuilder str) {
    printVals.add(str);
  }

  public void append(Exec exec) {
    printVals.add(exec);
  }

  public void append(Number num) {
    printVals.add(" ");
    printVals.add(num);
  }

  /**
   * Makes a string, executing any nodes along the way.
   * @return
   */
  public Object makeString(Parser parser) {
    StringBuilder toReturn = new StringBuilder();

    for (Object obj: printVals) {
      if (obj instanceof String) toReturn.append(obj);
      else if (obj instanceof Number) toReturn.append(((Number) obj).calculate(parser));
      else if (obj instanceof PrintNode) toReturn.append(((PrintNode) obj).makeString(parser));
      else if (obj instanceof GetVariableNode) toReturn.append(parser.variables.get(((GetVariableNode) obj).getInfo(parser)));
      else if (obj instanceof MethodNode) toReturn.append(((MethodNode) obj).execute(parser));
      else if (obj instanceof VariableActionNode) toReturn.append(((VariableActionNode) obj).execute(parser));
      else toReturn.append(obj);
    }

    return toReturn.toString();
  }

  @Override
  public Object execute(Parser parser) {
    StringBuilder toPrint = new StringBuilder();

    for (Object obj: printVals) {
      if (obj instanceof String) toPrint.append(obj);
      else if (obj instanceof Number) toPrint.append(((Number) obj).calculate(parser));
      else if (obj instanceof PrintNode) toPrint.append(((PrintNode) obj).makeString(parser));
      else if (obj instanceof GetVariableNode) toPrint.append(((GetVariableNode) obj).getInfo(parser));
      else if (obj instanceof MethodNode) toPrint.append(((MethodNode) obj).execute(parser));
      else if (obj instanceof VariableActionNode) toPrint.append(((VariableActionNode) obj).execute(parser));
      else toPrint.append(obj);
    }

    System.out.println(toPrint);
    return null;
  }

  @Override
  public String toString() {
    return printVals.toString();
  }
}
