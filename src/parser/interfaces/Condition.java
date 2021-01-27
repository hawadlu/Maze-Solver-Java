package parser.interfaces;

public interface Condition {
  boolean evaluate(boolean DEBUG);
  String getExecType();
}
