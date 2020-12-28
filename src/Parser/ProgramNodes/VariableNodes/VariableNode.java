package Parser.ProgramNodes.VariableNodes;

import Parser.Parser;
import Parser.ProgramNodes.Exec;
import Parser.ProgramNodes.MethodNodes.MethodNode;
import Utility.Node;

import java.util.*;

/**
 * This class holds the variable info including the type, name and value object
 */
public class VariableNode implements Exec {
  String type = null, name = null;
  Exec toEvaluate;

  Object value = null;

  public VariableNode(String type, String name) {
    this.name = name.replaceAll(" ", "");;
    this.type = type.replaceAll(" ", "");;
  }

  /**
   * This is a sort of copy constructor. It takes the value and type from another variable.
   * Used primarily for the temp variables in a for each loop.
   * Because of this it does not error check any of the parameters, it is assumed that
   * this has already been done.
   * @param name the var name
   * @param type the var type
   * @param value the value to set
   */
  public VariableNode(String name, String type, Object value) {
    this.name = name;
    this.type = type;
    this.value = value;
  }

  public VariableNode(String[] info, Exec toEvaluate) {
    ArrayList<String> tmp1 = new ArrayList<>(Arrays.asList(info));
    ArrayList<String> tmp2 = new ArrayList<>(Arrays.asList(info));

    for (String str: tmp1) {
      if (!str.matches("\\s*\\w+\\s*")) {
        tmp2.remove(str);
      }
    }


    this.name = tmp2.get(1).replaceAll(" ", "");
    this.type = tmp2.get(0).replaceAll(" ", "");;
    this.toEvaluate = toEvaluate;
  }

  public Object getValue() {
    return this.value;
  }

  public String getType() {
    return this.type;
  }

  @Override
  public Object execute(Parser parser) {
    //Put this into the var map if required
    if (!parser.variables.containsKey(this)) {
      parser.variables.put(this.name, this);
    }

    //Evaluate the Exec node that will assign the value
    if (toEvaluate != null) {
      if (type.equals("Node") || type.equals("List")) value = toEvaluate.execute(parser);
    } else {
      if (type.equals("Stack")) value = new Stack<>();
      else if (type.equals("Queue")) value = new ArrayDeque<>();
    }
    return null;
  }

  @Override
  public String toString() {
    return type + " " + name + " " + toEvaluate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    VariableNode that = (VariableNode) o;
    return type.equals(that.type) && name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, name);
  }

  /**
   * Execute a method call on this variable
   * @param method the method
   * @return the result of the method if required
   */
  public Object callMethod(MethodNode method, Parser parser) {
    if (value instanceof Collection) {
      if (method.getName().equals("add")) add(method, parser);
      else if (method.getName().equals("isEmpty")) return isEmpty();
      else if (method.getName().equals("getNext")) return getNext();
      else if (method.getName().equals("getSize")) return getSize();
    }

    return null;
  }

  /**
   * @return the size of the collection.
   */
  private double getSize() {
    return ((Collection) value).size();
  }

  /**
   * @return the next value in the collection.
   */
  private Object getNext() {
    if (type.equals("Stack")) return ((Stack) value).pop();
    else if (type.equals("Queue")) return ((ArrayDeque) value).poll();
    return null;
  }

  /**
   * Check if the collection stored in the value object is empty
   */
  private boolean isEmpty() {
      return ((Collection) value).isEmpty();
  }

  /**
   * Add a new value to this variable.
   * @param method the method to execute in order to get the value
   * @param parser the parser object.
   */
  private void add(MethodNode method, Parser parser) {
    if (type.equals("Stack")) {
      Stack tmp = (Stack) value;

      Object toAdd = method.execute(parser);

      //Cast the variable to a node if required
      if (toAdd instanceof VariableNode)  toAdd = (Node) ((VariableNode) toAdd).getValue();

      tmp.push(toAdd);
      value = tmp;
    } else if (type.equals("Queue")) {
      ArrayDeque tmp = (ArrayDeque) value;

      Object toAdd = method.execute(parser);

      //Cast the variable to a node if required
      if (toAdd instanceof VariableNode)  toAdd = (Node) ((VariableNode) toAdd).getValue();

      tmp.offer(toAdd);
      value = tmp;
    }
  }

  /**
   * Update the value in this variable
   * @param newVal the new value
   * @param parser the parser
   */
  public void update(Exec newVal, Parser parser) {
    this.value = newVal.execute(parser);

    //Check if the newly assigned value is of the correct type
    if (this.value instanceof VariableNode) {
      this.value = ((VariableNode) this.value).value;
    }
  }

  /**
   * Set the new value of this node, without execution.
   * @param newVal the new value.
   */
  public void setValue(Object newVal) {
    this.value = newVal;
  }

  public String print() {
    return "Name: " + name + " Type: " + type + " value: " + value;
  }
}
