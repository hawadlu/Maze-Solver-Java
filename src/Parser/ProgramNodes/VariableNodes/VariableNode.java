package Parser.ProgramNodes.VariableNodes;

import Parser.Handler;
import Parser.ProgramNodes.Exec;
import Parser.ProgramNodes.MathNodes.NumberNode;
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
  private Handler handler;

  public VariableNode(String type, String name, Handler handler) {
    this.name = name.replaceAll(" ", "");
    this.type = type.replaceAll(" ", "");
    this.handler = handler;
  }

  public VariableNode(String[] info, Exec toEvaluate, Handler handler) {
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
    this.handler = handler;
  }

  public Object getValue() {
    return this.value;
  }

  public String getType() {
    return this.type;
  }

  @Override
  public void validate() {
    //Add the variable to the map in the handler.
    addToVariableMap();

   if (toEvaluate != null) {
     toEvaluate.validate();
   }
   if (value instanceof Exec) {
     ((Exec) value).validate();
   }
  }

  @Override
  public Object execute() {
    //Add the variable to the map in the handler.
    addToVariableMap();

    //Evaluate the Exec node that will assign the value
    if (toEvaluate != null) {
      if (type.equals("Node") || type.equals("List") || type.equals("Comparator") || type.equals("Number")) value = toEvaluate.execute();
    } else {
      if (type.equals("Stack")) value = new Stack<>();
      else if (type.equals("Queue")) value = new ArrayDeque<>();
      else if (type.equals("PriorityQueue")) value = new PriorityQueue<>();
    }
    return null;
  }

  /**
   * Add this variable to the variable map.
   * This will first check if the variable already exits in the map,
   * if it does not, it is added.
   */
  private void addToVariableMap() {
    //Put this into the var map if required
    if (!handler.hasVariable(this.name)) {
      handler.addVariable(this.name, this);
    }
  }

  @Override
  public String toString() {
    return print();
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
  public Object callMethod(MethodNode method) {
    if (value instanceof Collection) {
      if (method.getName().equals("add")) add(method);
      else if (method.getName().equals("isEmpty")) return isEmpty();
      else if (method.getName().equals("getNext")) return getNext();
      else if (method.getName().equals("getSize")) return getSize();
      else if (method.getName().equals("assignComparator")) assignComparator(method);
    }

    return null;
  }

  /**
   * Assign a comparator to the value if required.
   * @param method the method
   */
  private void assignComparator(MethodNode method) {
    if (!(value instanceof PriorityQueue));

    //Get the comparator out of the variable map
    String varName = (String) method.getParameters().get(0);
    Comparator<Node> comparator = (Comparator<Node>) handler.getFromMap(varName).value;

    value = new PriorityQueue<>(comparator);
  }

  /**
   * @return the size of the collection.
   */
  private NumberNode getSize() {
    return new NumberNode(((Collection) value).size());
  }

  /**
   * @return the next value in the collection.
   */
  private Object getNext() {
    if (type.equals("Stack")) return ((Stack) value).pop();
    else if (type.equals("Queue")) return ((ArrayDeque) value).poll();
    else if (type.equals("PriorityQueue")) return ((PriorityQueue) value).poll();
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
   */
  private void add(MethodNode method) {
    if (type.equals("Stack")) {
      Stack tmp = (Stack) value;

      Object toAdd = method.execute();

      //Cast the variable to a node if required
      if (toAdd instanceof VariableNode)  toAdd = (Node) ((VariableNode) toAdd).getValue();

      tmp.push(toAdd);
      value = tmp;
    } else if (type.equals("Queue") || type.equals("PriorityQueue")) {
      Queue tmp = null;
      if (this.type.equals("Queue")) tmp = (ArrayDeque) value;
      else if (this.type.equals("PriorityQueue")) {
        tmp = (PriorityQueue) value;
      }

      Object toAdd = method.execute();

      //Cast the variable to a node if required
      if (toAdd instanceof VariableNode)  toAdd = (Node) ((VariableNode) toAdd).getValue();

      tmp.offer(toAdd);
      value = tmp;
    }
  }

  /**
   * Update the value in this variable
   * @param newVal the new value
   */
  public void update(Exec newVal) {
    this.value = newVal.execute();

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
    if (value == null) return "Name: " + name + " Type: " + type;
    else return "Name: " + name + " Type: " + type + " value: " + value;
  }
}
