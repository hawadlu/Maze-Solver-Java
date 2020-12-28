package Parser.ProgramNodes;

import Parser.Parser;

public interface Exec {
  Object execute(Parser parser);
  String toString();
}
