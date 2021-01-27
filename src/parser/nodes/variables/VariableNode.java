package parser.nodes.variables;

import parser.Handler;
import parser.Parser;
import parser.interfaces.Exec;
import parser.interfaces.Number;
import parser.nodes.math.NumberNode;
import parser.nodes.methods.MethodNode;
import parser.interfaces.Value;
import Utility.Node;

import java.util.*;

/**
 * This class holds the variable info including the type, name and value object
 */
@SuppressWarnings("unchecked")
public class VariableNode implements Exec, Value {
  private final String type;
  private final String name;
  private Exec toEvaluate;
  private Object value = null;
  private final Handler handler;
  private boolean isCollection = false;

  /**
   * Create the object.
   * @param type The type of object that is being stored.
   * @param name The name of the variable.
   * @param handler The maze handler.
   */
  public VariableNode(String type, String name, Handler handler) {
    this.name = name.replaceAll(" ", "");
    type = type.replaceAll(" ", "");
    this.handler = handler;

    if (type.equals("List") || type.equals("Stack") || type.equals("Queue") || type.equals("PriorityQueue")) isCollection = true;

    if (type.equals("Node")) this.type = "MazeNode";
    else this.type = type;
  }

  /**
   * Create the object.
   * @param info A list of information from which the variable name and type are extracted.
   * @param toEvaluate The value of this variable.
   * @param handler The maze handler.
   */
  public VariableNode(String[] info, Exec toEvaluate, Handler handler) {
    ArrayList<String> tmp1 = new ArrayList<>(Arrays.asList(info));
    ArrayList<String> tmp2 = new ArrayList<>(Arrays.asList(info));

    for (String str : tmp1) {
      if (!str.matches("\\s*\\w+\\s*")) {
        tmp2.remove(str);
      }
    }


    this.name = tmp2.get(1).replaceAll(" ", "");
    String type = tmp2.get(0).replaceAll(" ", "");
    this.toEvaluate = toEvaluate;
    this.handler = handler;

    if (type.equals("List") || type.equals("Stack") || type.equals("Queue") || type.equals("PriorityQueue")) isCollection = true;

    if (type.equals("Node")) this.type = "MazeNode";
    else this.type = type;
  }

  /**
   * @return the value stored in this variable.
   */
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

  /**
   * @return a string representation of the type at execution.
   */
  @Override
  public String getType() {
    return this.type;
  }

  /**
   * Validate the contents of the variable.
   *
   * Add the variable to the map.
   *
   * If applicable evaluate toEvaluate otherwise evaluate the value.
   */
  @Override
  public void validate() {
    //Add the variable to the map in the handler.
    addToVariableMap();

    if (toEvaluate != null) {
      toEvaluate.validate();

      validateType();
    }

    if (value != null) {
      if (value instanceof Exec) {
        ((Exec) value).validate();
      }

      if (value instanceof Value) {
        validateType();
      }
    }
  }

  /**
   * Check that the expected type matches the supplied type.
   */
  private void validateType() {
    String expectedType = getType();
    String suppliedType;

    //Only run this is it is a value type
    if (toEvaluate instanceof Value) {
      suppliedType = ((Value) toEvaluate).getType();
    } else {
      suppliedType = toEvaluate.getExecType();
    }

    if (suppliedType.equals("Collection")) {
      if (!isCollection)
        Parser.fail(name + " expects type " + getType() + " found " + suppliedType, "Execution", null, handler.getPopup());
    } else if (!expectedType.equals(suppliedType)) {
      Parser.fail(name + " expects type " + getType() + " found " + suppliedType, "Execution", null, handler.getPopup());
    }
  }

  /**
   * Add the variable to the variable map.
   *
   * If toEvaluate is not null run it.
   * Otherwise check the variable type and initialise the value.
   */
  @Override
  public Object execute(boolean DEBUG) {
    if (DEBUG) System.out.println(handler.getPlayer() + " " + getExecType());

    //Add the variable to the map in the handler.
    addToVariableMap();

    //Evaluate the Exec node that will assign the value
    if (toEvaluate != null) {
      if (type.equals("List")) {
        Collection<Node> tmp = (Collection<Node>) toEvaluate.execute(DEBUG);

        value = new ArrayList<>();

        for (Node node : tmp) {
          //noinspection unchecked
          ((ArrayList<Node>) value).add(node);
        }

      } else if (type.equals("MazeNode") || type.equals("Comparator") || type.equals("Number")) {
        value = toEvaluate.execute(DEBUG);
      }
    } else {
      switch (type) {
        case "Stack" -> value = new Stack<>();
        case "Queue" -> value = new ArrayDeque<>();
        case "PriorityQueue" -> value = new PriorityQueue<>();
        case "List" -> value = new ArrayList<>();
      }
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

  /**
   * Return a string representation of the object.
   */
  @Override
  public String toString() {
    return print();
  }

  /**
   * Get the type that would be returned when the execute method is called.
   */
  @Override
  public String getExecType() {

    //Null because executing a variable never does anything.
    return null;
  }

  /**
   * Check the equality of two variables.
   * @param o the variable node to compare.
   * @return a boolean indicating object equality.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    VariableNode that = (VariableNode) o;
    return type.equals(that.type) && name.equals(that.name);
  }

  /**
   * Get a hashcode of the variable.
   * @return a hashcode of the object.
   */
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
  public Object callMethod(MethodNode method, boolean DEBUG) {
    if (value instanceof Collection) {
      switch (method.getName()) {
        case "add":
          add(method, DEBUG);
          break;
        case "isEmpty":
          return isEmpty();
        case "getNext":
          return getNext();
        case "getSize":
          return getSize();
        case "assignComparator":
          assignComparator(method);
          break;
        case "get":
          return get(method, DEBUG);
      }
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
  private Object get(MethodNode method, boolean DEBUG) {
    Object parameter = method.getParameters().get(0);

    int index = 0;

    if (parameter instanceof GetVariableNode) {
      VariableNode var = ((GetVariableNode) parameter).extractVariable();
      index = (int) ((Number) var.value).calculate(DEBUG);
    } else if (parameter instanceof Number) {
      index = (int) ((Number) parameter).calculate(DEBUG);
    } else if (parameter instanceof VariableNode) {
      System.out.println();
    } else if (parameter instanceof String) {
      VariableNode var = handler.getFromMap((String) parameter);
      index = (int) ((Number) var.getValue()).calculate(DEBUG);
    }

    return ((ArrayList<Node>) value).get(index);
  }

  /**
   * Assign a comparator to the value if required.
   *
   * @param method the method
   */
  private void assignComparator(MethodNode method) {

    //Get the comparator out of the variable map
    String varName = (String) method.getParameters().get(0);
    Comparator<Node> comparator = (Comparator<Node>) handler.getFromMap(varName).value;

    value = new PriorityQueue<>(comparator);
  }

  /**
   * @return the size of the collection.
   */
  private NumberNode getSize() {
    return new NumberNode(((Collection<Node>) value).size());
  }

  /**
   * @return the next value in the collection.
   */
  private Object getNext() {
    return switch (type) {
      case "Stack" -> ((Stack<Node>) value).pop();
      case "Queue" -> ((ArrayDeque<Node>) value).poll();
      case "PriorityQueue" -> ((PriorityQueue<Node>) value).poll();
      default -> null;
    };
  }

  /**
   * Check if the collection stored in the value object is empty
   */
  private boolean isEmpty() {
    return ((Collection<Node>) value).isEmpty();
  }

  /**
   * Add a new value to this variable.
   *
   * @param method the method to execute in order to get the value
   */
  private void add(MethodNode method, boolean DEBUG) {
    //Cast to a collection and check the size
    if (value instanceof Collection && ((Collection<Node>) value).size() > 2097152) {
      Parser.fail("Collection '" + name + "' exceeded maximum size of 2097152.", "Execution", null, handler.getPopup());
    }

    switch (type) {
      case "Stack" -> {
        Object toAdd = method.execute(DEBUG);

        //Cast the variable to a node if required
        if (toAdd instanceof VariableNode) toAdd = ((VariableNode) toAdd).getValue();

        ((Stack<Node>) value).add((Node) toAdd);
      }
      case "Queue", "PriorityQueue" -> {
        Object toAdd = method.execute(DEBUG);

        //Cast the variable to a node if required
        if (toAdd instanceof VariableNode) toAdd = ((VariableNode) toAdd).getValue();

        //Verify that the value is not null
        if (toAdd == null)
          Parser.fail("Null Pointer Exception when adding to '" + name + "'", "Execution", null, handler.getPopup());

        if (value instanceof PriorityQueue) ((PriorityQueue<Node>) value).add((Node) toAdd);
        else if (value instanceof ArrayDeque) {
          assert toAdd != null;
          ((ArrayDeque<Node>) value).add((Node) toAdd);
        }
      }
      case "List" -> {
        Object toAdd = method.execute(DEBUG);

        //Cast the variable to a node if required
        if (toAdd instanceof VariableNode) toAdd = ((VariableNode) toAdd).getValue();

        //Verify that the value is not null
        if (toAdd == null)
          Parser.fail("Null Pointer Exception when adding to '" + name + "'", "Execution", null, handler.getPopup());

        ((ArrayList<Node>) value).add((Node) toAdd);
      }
    }
  }

  /**
   * Update the value in this variable
   *
   * @param newVal the new value
   */
  public void update(Object newVal, boolean DEBUG) {
    if (newVal instanceof Exec) this.value = ((Exec) newVal).execute(DEBUG);
    else if (newVal instanceof Number) this.value = new NumberNode(((Number) newVal).calculate(DEBUG));

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
