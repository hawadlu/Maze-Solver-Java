package parser.nodes.VariableNodes;

import parser.Handler;
import parser.Parser;
import parser.nodes.Exec;
import parser.nodes.MathNodes.Number;
import parser.nodes.MathNodes.NumberNode;
import parser.nodes.MethodNodes.MethodNode;
import parser.nodes.Value;
import Utility.Node;

import java.util.*;

/**
 * This class holds the variable info including the type, name and value object
 */
public class VariableNode implements Exec, Value {
  private String type = null, name = null;
  private Exec toEvaluate;
  private Object value = null;
  private Handler handler;
  private boolean isCollection = false;

  public VariableNode(String type, String name, Handler handler) {
    this.name = name.replaceAll(" ", "");
    this.type = type.replaceAll(" ", "");
    this.handler = handler;

    if (type.equals("List") || type.equals("Stack") || type.equals("Queue") || type.equals("PriorityQueue")) isCollection = true;
  }

  public VariableNode(String[] info, Exec toEvaluate, Handler handler) {
    ArrayList<String> tmp1 = new ArrayList<>(Arrays.asList(info));
    ArrayList<String> tmp2 = new ArrayList<>(Arrays.asList(info));

    for (String str : tmp1) {
      if (!str.matches("\\s*\\w+\\s*")) {
        tmp2.remove(str);
      }
    }


    this.name = tmp2.get(1).replaceAll(" ", "");
    this.type = tmp2.get(0).replaceAll(" ", "");
    ;
    this.toEvaluate = toEvaluate;
    this.handler = handler;

    if (type.equals("List") || type.equals("Stack") || type.equals("Queue") || type.equals("PriorityQueue")) isCollection = true;
  }

  public Object getValue() {
    return this.value;
  }

  /**
   * Set the new value of this node, without execution.
   *
   * @param newVal the new value.
   */
  public void setValue(Object newVal) {
    this.value = newVal;
  }

  @Override
  public String getType() {
    return this.type;
  }

  @Override
  public void validate() {
    //Add the variable to the map in the handler.
    addToVariableMap();

    if (toEvaluate != null) {
      toEvaluate.validate();

      String expectedType = getType();
      String suppliedType;

      //Only run this is it is a value type
      if (toEvaluate instanceof Value) {
        suppliedType = ((Value) toEvaluate).getType();
      } else {
        suppliedType = toEvaluate.getExecType();
      }

      if (suppliedType.equals("Collection")) {
        if (!isCollection) Parser.fail(name + " expects type " + getType() + " found " + suppliedType, "Execution", null, handler.getPopup());
      } else if (!expectedType.equals(suppliedType)) {
        Parser.fail(name + " expects type " + getType() + " found " + suppliedType, "Execution", null, handler.getPopup());
      }
    }

    if (value != null) {
      if (value instanceof Exec) {
        ((Exec) value).validate();
      }

      if (value instanceof Value) {
        String expectedType = getType();
        String suppliedType;

        //Only run this is it is a value type
        if (toEvaluate instanceof Value) {
          suppliedType = ((Value) toEvaluate).getType();
        } else {
          suppliedType = toEvaluate.getExecType();
        }

        if (suppliedType.equals("Collection")) {
          if (!isCollection) Parser.fail(name + " expects type " + getType() + " found " + suppliedType, "Execution", null, handler.getPopup());
        } else if (!expectedType.equals(suppliedType)) {
          Parser.fail(name + " expects type " + getType() + " found " + suppliedType, "Execution", null, handler.getPopup());
        }
      }
    }
  }

  @Override
  public Object execute() {
    //Add the variable to the map in the handler.
    addToVariableMap();

    //Evaluate the Exec node that will assign the value
    if (toEvaluate != null) {
      if (type.equals("List")) {
        Collection tmp = (Collection) toEvaluate.execute();
        value = new ArrayList<>();

        for (Object obj : tmp) ((ArrayList) value).add(obj);

      } else if (type.equals("MazeNode") || type.equals("Comparator") || type.equals("Number")) {
        value = toEvaluate.execute();
      }
    } else {
      if (type.equals("Stack")) value = new Stack<>();
      else if (type.equals("Queue")) value = new ArrayDeque<>();
      else if (type.equals("PriorityQueue")) value = new PriorityQueue<>();
      else if (type.equals("List")) value = new ArrayList<>();
    }

    //Revalidate
    validate();

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
  public String getExecType() {
    //Null because executing a variable never does anything.
    return null;
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
   *
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
      else if (method.getName().equals("get")) return get(method);
    }

    return null;
  }

  /**
   * Get a variable at a specified index out of the list.
   * Get the parameter object. If it is a number use that,
   * if it is a variable use that to extract the number from
   * the variable map.
   *
   * @param method the method object.d
   * @return the value at the specified index.
   */
  private Object get(MethodNode method) {
    Object parameter = method.getParameters().get(0);

    int index = 0;

    if (parameter instanceof GetVariableNode) {
      VariableNode var = ((GetVariableNode) parameter).extractVariable();
      index = (int) ((Number) var.value).calculate();
    } else if (parameter instanceof Number) {
      index = (int) ((Number) parameter).calculate();
    } else if (parameter instanceof VariableNode) {
      System.out.println();
    } else if (parameter instanceof String) {
      VariableNode var = handler.getFromMap(parameter);
      index = (int) ((Number) var.getValue()).calculate();
    }

    return ((ArrayList) value).get(index);
  }

  /**
   * Assign a comparator to the value if required.
   *
   * @param method the method
   */
  private void assignComparator(MethodNode method) {
    if (!(value instanceof PriorityQueue)) ;

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
   *
   * @param method the method to execute in order to get the value
   */
  private void add(MethodNode method) {
    //Cast to a collection and check the size
    if (value instanceof Collection && ((Collection) value).size() > 2097152) {
      Parser.fail("Collection '" + name + "' exceeded maximum size of 2097152.", "Execution", null, handler.getPopup());
    }

    if (type.equals("Stack")) {
      Object toAdd = method.execute();

      //Cast the variable to a node if required
      if (toAdd instanceof VariableNode) toAdd = (Node) ((VariableNode) toAdd).getValue();

      ((Stack) value).add(toAdd);
    } else if (type.equals("Queue") || type.equals("PriorityQueue")) {
      Object toAdd = method.execute();

      //Cast the variable to a node if required
      if (toAdd instanceof VariableNode) toAdd = (Node) ((VariableNode) toAdd).getValue();

      //Verify that the value is not null
      if (toAdd == null) Parser.fail("Null Pointer Exception when adding to '" + name + "'", "Execution", null, handler.getPopup());

      if (value instanceof PriorityQueue) ((PriorityQueue<Node>) value).add((Node) toAdd);
      else if (value instanceof ArrayDeque) ((ArrayDeque<Node>) value).add((Node) toAdd);
    } else if (type.equals("List")) {
      Object toAdd = method.execute();

      //Cast the variable to a node if required
      if (toAdd instanceof VariableNode) toAdd = (Node) ((VariableNode) toAdd).getValue();

      //Verify that the value is not null
      if (toAdd == null) Parser.fail("Null Pointer Exception when adding to '" + name + "'", "Execution", null, handler.getPopup());

      ((ArrayList) value).add(toAdd);
    }
  }

  /**
   * Update the value in this variable
   *
   * @param newVal the new value
   */
  public void update(Object newVal) {
    if (newVal instanceof Exec) this.value = ((Exec) newVal).execute();
    else if (newVal instanceof Number) this.value = new NumberNode(((Number) newVal).calculate());

    //Check if the newly assigned value is of the correct type
    if (this.value instanceof VariableNode) {
      this.value = ((VariableNode) this.value).value;
    }
  }

  public String print() {
    if (value == null) return "Name: " + name + " Type: " + type;
    else return "Name: " + name + " Type: " + type + " value: " + value;
  }

  /**
   * Get the name of the variable.
   *
   * @return the name.
   */
  public String getName() {
    return name;
  }

  /**
   * Check if this variable is of the collection type.
   * True if type == Stack, Queue or PriorityQueue.
   *
   * @return a boolean to indicate if this is a collection.
   */
  public boolean isCollection() {
    return this.type.equals("PriorityQueue") || this.type.equals("Queue") || this.type.equals("Stack") || this.type.equals("List");
  }
}
