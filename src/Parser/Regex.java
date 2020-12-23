package Parser;

import java.util.regex.Pattern;

public class Regex {
  public static Pattern delimiter = Pattern.compile("\\s*(?=[{}(),;!=\\.\"])|(?<=[{}(),;!=\\.\"])");

  //Punctuation
  public static Pattern openParen = Pattern.compile("\\(");
  public static Pattern closeParen = Pattern.compile("\\)");
  public static Pattern doubleQuote = Pattern.compile("\"");
  public static Pattern equals = Pattern.compile("=");
  public static Pattern semiColon = Pattern.compile(";");
  public static Pattern dot = Pattern.compile("\\.");
  public static Pattern not = Pattern.compile("!");
  public static Pattern openCurly = Pattern.compile("\\{");
  public static Pattern closeCurly = Pattern.compile("\\}");

  public static Pattern name = Pattern.compile("\\w+"); // \w Matches any letter, digit or underscore. Equivalent to [a-zA-Z0-9_].
  public static Pattern sentencePunctuation = Pattern.compile("\\s|.|,");
  public static Pattern printConcatenate = Pattern.compile("(\s\\+)");
  public static Pattern sentence = Pattern.compile(doubleQuote + "[" + name + "" + sentencePunctuation +"]*" + doubleQuote);
  public static Pattern methodCall = Pattern.compile("\\w+\\((\\w+,*\\s*)*\\)");
  public static Pattern mazeCall = Pattern.compile("\\s*Maze");
  public static Pattern declareVar = Pattern.compile("\\w+\\s\\w+[\\s=\\s]*");
  public static Pattern useVar = Pattern.compile("^((?!Maze)\\w+\\.+)");
  public static Pattern reassignVar = Pattern.compile("\\w+\\s=\\s");

  //Data types
  public static Pattern node = Pattern.compile("Node");
  public static Pattern list = Pattern.compile("List");
  public static Pattern stack = Pattern.compile("Stack");

  //Loops
  public static Pattern whileLoop = Pattern.compile("while\\s*");
  public static Pattern forLoop = Pattern.compile("for \\(.*\\)\\s");

  //If statements
  public static Pattern ifStmt = Pattern.compile("if\\s\\(.*\\)\\s");

  //Assignment statements
  public static Pattern declaration = Pattern.compile("([" + node + "|" + list + "|" + stack + "]+\\s\\w+\\s*)");

  //Statements in the program
  public static Pattern print = Pattern.compile("print");

  //The collective program statement
  public static Pattern statement = Pattern.compile("\\n*(" + print + "|" + declaration + "|" + name + "|" + whileLoop + ")");
}
