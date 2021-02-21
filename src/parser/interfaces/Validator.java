package parser.interfaces;

/**
 * This interface is used to delineate any classes
 * (in the parser) that can be validated.
 */
public interface Validator {

  /**
   * Validate to make sure that all elements are correct.
   */
  void validate();
}
