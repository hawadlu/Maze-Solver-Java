package Parser;

import java.util.regex.Pattern;

public class Regex {
  public static Pattern delimiter = Pattern.compile("\\s+|(?=[{}(),;!=])|(?<=[{}(),;!=])");

  //Puctuation
  public static Pattern openParen = Pattern.compile("\\(");
  public static Pattern closeParen = Pattern.compile("\\)");
  public static Pattern doubleQuote = Pattern.compile("\"");
  public static Pattern equals = Pattern.compile("=");

  public static Pattern letterChar = Pattern.compile("\\w"); // \w Matches any letter, digit or underscore. Equivalent to [a-zA-Z0-9_].
  public static Pattern scentencePunctuation = Pattern.compile("\\s|.|,");
  public static Pattern printConcatenate = Pattern.compile("(\s\\+)");
  public static Pattern scentence = Pattern.compile(doubleQuote + "[" + letterChar + "" + scentencePunctuation +"]*" + doubleQuote);
  public static Pattern methodCall = Pattern.compile("\\w+\\((\\w+,*\\s*)*\\)");
  public static Pattern mazeCall = Pattern.compile("Maze");
  public static Pattern declareVar = Pattern.compile("\\w+\\s\\w+[\\s=\\s]*");
  public static Pattern useVar = Pattern.compile("^((?!Maze)\\w+\\.+)");
  public static Pattern reassignVar = Pattern.compile("\\w+\\s=\\s");
  public static Pattern line = Pattern.compile(".*;");

  //Loops
  public static Pattern whileLoop = Pattern.compile("while\\s\\(.*\\)\\s");
  public static Pattern forLoop = Pattern.compile("for \\(.*\\)\\s");

  //If statements
  public static Pattern ifStmt = Pattern.compile("if\\s\\(.*\\)\\s");

  //Statements in the program
  public static Pattern print = Pattern.compile("print\\(\"[\\w,.\\s\"+\\(\\/\\*\\-)]*\"\\)");
  public static Pattern assignment = Pattern.compile("(" + declareVar + "|" + useVar + "|" + reassignVar + ")(((" + mazeCall + ")|(\\w+)\\.)*(" + methodCall + ")+)*");

  //The collective program statement
  public static Pattern statement = Pattern.compile(line + "|" + whileLoop + "|" + forLoop + "|" + ifStmt);
}
