package Parser.ProgramNodes.MethodNodes;

import Parser.Parser;

import java.util.ArrayList;

/**
 * This class is used to validate the parameters that have been passed to a method at runtime.
 */
public class ParameterValidator {
  ArrayList<Object> suppliedParameters;
  String[] expectedTypes;
  String methodName;
  int argumentCount;

  public ParameterValidator(String methodName, ArrayList<Object> suppliedParameters) {
    this.methodName = methodName;
    this.suppliedParameters = suppliedParameters;

    setExpectedCost();
    setExpectedClasses();
  }

  public ParameterValidator(String methodName) {
    this.methodName = methodName;

    setExpectedCost();
    setExpectedClasses();
  }

  /**
   * Set the expected number of arguments of the method
   */
  private void setExpectedCost() {
    if (methodName.equals("getStart")) argumentCount = 0;
    else if (methodName.equals("setCost")) argumentCount = 2;
    else if (methodName.equals("getCost")) argumentCount = 1;
    else if (methodName.equals("visit")) argumentCount = 1;
    else if (methodName.equals("isDone")) argumentCount = 1;
    else if (methodName.equals("finish")) argumentCount = 0;
    else if (methodName.equals("getNeighbours")) argumentCount = 1;
    else if (methodName.equals("isVisited")) argumentCount = 1;
    else if (methodName.equals("getDistance")) argumentCount = 2;
    else if (methodName.equals("setParent")) argumentCount = 2;
    else if (methodName.equals("distanceToDestination")) argumentCount = 1;
    else if (methodName.equals("getParent")) argumentCount = 1;
  }

  /**
   * Set the expected classes of a method
   */
  private void setExpectedClasses() {
    if (methodName.equals("setCost")) expectedTypes = new String[]{"Node", "Number"};
    else if (methodName.equals("getCost")) expectedTypes = new String[]{"Node"};
    else if (methodName.equals("visit")) expectedTypes = new String[]{"Node"};
    else if (methodName.equals("isDone")) expectedTypes = new String[]{"Node"};
    else if (methodName.equals("getNeighbours")) expectedTypes = new String[]{"Node"};
    else if (methodName.equals("isVisited")) expectedTypes = new String[]{"Node"};
    else if (methodName.equals("getDistance")) expectedTypes = new String[]{"Node", "Node"};
    else if (methodName.equals("setParent")) expectedTypes = new String[]{"Node", "Node"};
    else if (methodName.equals("distanceToDestination")) expectedTypes = new String[]{"Node"};
    else if (methodName.equals("getParent")) expectedTypes = new String[]{"Node"};
  }

  /**
   * Check that the supplied types match the required types
   */
  public void validate(Parser parser) {
    //Check to see if the number of arguments are as expected
    if (suppliedParameters != null) {
      if (suppliedParameters.size() != argumentCount) {
        parser.executionError(methodName + " expects " + argumentCount + " arguments, but found " + suppliedParameters.size());
      }

      validateTypes(parser);
    } else if (argumentCount > 0) parser.executionError(methodName + " expects " + argumentCount + " arguments, but found none.");
  }


  /**
   * Validate the types that have been supplied to a method.
   * @param parser the parser.
   */
  private void validateTypes(Parser parser) {
    //Check that the supplied arguments are as expected
    for (int i = 0; i < suppliedParameters.size(); i++) {
      String varName  = (String) suppliedParameters.get(i);
      if (parser.variables.get(varName).getType() != expectedTypes[i]) {
        parser.executionError(methodName + " expected type " + expectedTypes[i] + " found " + parser.variables.get(varName).getType());
      }
    }
  }
}
