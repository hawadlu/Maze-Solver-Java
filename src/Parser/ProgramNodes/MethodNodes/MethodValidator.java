package Parser.ProgramNodes.MethodNodes;

import Parser.Handler;
import Parser.Parser;

import java.util.ArrayList;

/**
 * Class used to validate the number of arguments and their types.
 */
public class MethodValidator {
    String[] expectedTypes;
    ArrayList<Object> parameters;
    String methodName;
    private Handler handler;

    MethodValidator(String methodName, String[] expectedTypes, ArrayList<Object> parameters, Handler handler) {
        this.methodName = methodName;
        this.expectedTypes = expectedTypes;
        this.parameters = parameters;
        this.handler = handler;
    }

    /**
     * Validate the method using the supplied information
     */
    public void validate() {
        //Check that the number of arguments matches what is expected
        if (parameters.size() != expectedTypes.length) Parser.fail(methodName + " expects " + expectedTypes.length + " argument(s). Found " + parameters.size(), null);

        //Check that the supplied types match the expected types
        for (int i = 0; i < parameters.size(); i++) {
            if (parameters.get(i) instanceof String) {
                String parameterName = (String) parameters.get(i);
                String expectedType = expectedTypes[i];

                //If the variable is contained in the map, locate it and check the type
                if (handler.hasVariable(parameterName)) {
                    if (!handler.getFromMap(parameterName).getType().equals(expectedType)) {
                        Parser.fail(methodName + " expects type(s) " + printExpectedTypes() + " found " + printParameterTypes(), null);
                    }
                }
            }
        }
    }

    /**
     * Format the expected types array for printing;
     */
    private String printParameterTypes() {
        StringBuilder toReturn = new StringBuilder();

        toReturn.append("[");

        for (int i = 0; i < parameters.size(); i++) {
            toReturn.append(handler.getFromMap(parameters.get(i)).getType());
            if (i < parameters.size() - 1) toReturn.append(", ");
            else toReturn.append(" ");
        }

        toReturn.append("]");

        return toReturn.toString();
    }

    /**
     * Format the expected types array for printing;
     */
    private String printExpectedTypes() {
        StringBuilder toReturn = new StringBuilder();

        toReturn.append("[");

        for (int i = 0; i < expectedTypes.length; i++) {
            toReturn.append(expectedTypes[i]);
            if (i < expectedTypes.length - 1) toReturn.append(", ");
            else toReturn.append(" ");
        }

        toReturn.append("]");

        return toReturn.toString();
    }
}