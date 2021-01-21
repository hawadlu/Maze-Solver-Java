package parser.nodes;

import parser.interfaces.Exec;
import parser.interfaces.Number;
import parser.nodes.methods.MethodNode;
import parser.nodes.variables.GetVariableNode;
import parser.nodes.variables.VariableActionNode;

import java.util.ArrayList;

/**
 * Class that is used to create a print statement.
 * Prints things to the console.
 */
public class PrintNode implements Exec {
  final ArrayList<Object> printValues = new ArrayList<>();

  /**
   * Add a string to the current string.
   * @param str A string builder object containing the new values.
   */
  public void append(StringBuilder str) {
    printValues.add(str);
  }

  /**
   * Add an Exec to the current string.
   * @param exec the Exec object.
   */
  public void append(Exec exec) {
    printValues.add(exec);
  }

  /**
   * Add a number to the current string.
   * @param num the number to add.
   */
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

  /**
   * Call the validate method on all Exec values in the list.
   */
  @Override
  public void validate() {
    for (Object obj: printValues) {
      if (obj instanceof Exec) ((Exec) obj).validate();
    }
  }

  /**
   * Print out the string.
   * @return does not return anything, null.
   */
  @Override
  public Object execute() {
    System.out.println(concatenate());
    return null;
  }

  /**
   * Return a string representation of the object.
   */
  @Override
  public String toString() {
    return printValues.toString();
  }

  /**
   * Get the type that would be returned when the execute method is called.
   */
  @Override
  public String getExecType() {
    return "PrintNode";
  }
}
