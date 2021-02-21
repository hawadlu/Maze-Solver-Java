package parser.interfaces;

/**
 * This class is used to represent any number
 * that the user wants to store in the parser.
 */
public interface Number {
  /**
   * Create a new number object.
   * @param DEBUG print debugging information while running.
   * @return a number that is the result of the calculation.
   */
  double calculate(boolean DEBUG);
}
