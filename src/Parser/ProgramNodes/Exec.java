package Parser.ProgramNodes;

public interface Exec {
  Object execute();
  void validate();
  String toString();
}
