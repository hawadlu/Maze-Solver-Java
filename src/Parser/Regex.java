package Parser;

import java.util.regex.Pattern;

public class Regex {
  //Punctuation
  public static Pattern openParen = Pattern.compile("\\(");
  public static Pattern closeParen = Pattern.compile("\\)");
  public static Pattern doubleQuote = Pattern.compile("\\s*\"");
  public static Pattern equals = Pattern.compile("=");
  public static Pattern colon = Pattern.compile(":");
  public static Pattern semiColon = Pattern.compile(";");
  public static Pattern dot = Pattern.compile("\\.");
  public static Pattern not = Pattern.compile("!");
  public static Pattern openCurly = Pattern.compile("\\{");
  public static Pattern closeCurly = Pattern.compile("\\}");
  public static Pattern plus = Pattern.compile("\\s*\\+");
  public static Pattern number = Pattern.compile("\\s*[0-9]+");
  public static Pattern comma = Pattern.compile(",");

  public static Pattern name = Pattern.compile("\\s*\\w+"); // \w Matches any letter, digit or underscore. Equivalent to [a-zA-Z0-9_].
  public static Pattern sentencePunctuation = Pattern.compile("\\s|.|,");
  public static Pattern printConcatenate = Pattern.compile("(\s\\+)");
  public static Pattern sentence = Pattern.compile(doubleQuote + "[" + name + "" + sentencePunctuation +"]*" + doubleQuote);
  public static Pattern methodCall = Pattern.compile("\\w+\\((\\w+,*\\s*)*\\)");
  public static Pattern mazeCall = Pattern.compile("\\s*Maze");
  public static Pattern declareVar = Pattern.compile("\\w+\\s\\w+[\\s=\\s]*");
  public static Pattern useVar = Pattern.compile("^((?!Maze)\\w+\\.+)");
  public static Pattern reassignVar = Pattern.compile("\\w+\\s=\\s");
  public static Pattern comment = Pattern.compile("\\*\\*\\*");

  //Math action
  public static Pattern mathPlus = Pattern.compile("\\s*plus");
  public static Pattern mathMinus = Pattern.compile("\\s*minus");
  public static Pattern mathDivide = Pattern.compile("\\s*divide");
  public static Pattern mathMultiply = Pattern.compile("\\s*multiply");
  public static Pattern mathPower = Pattern.compile("\\s*power");
  public static Pattern mathRoot= Pattern.compile("\\s*root");
  public static Pattern math = Pattern.compile(mathPlus + "|" + mathMinus + "|" + mathDivide + "|" + mathMultiply + "|" + mathPower + "|" + mathRoot + "|" + number);



  //Data types
  public static Pattern node = Pattern.compile("Node");
  public static Pattern list = Pattern.compile("List");
  public static Pattern stack = Pattern.compile("Stack");

  //Loops
  public static Pattern whileLoop = Pattern.compile("\\s*while\\s*");
  public static Pattern forLoop = Pattern.compile("\\s*for\\s*");

  //If statements
  public static Pattern ifStmt = Pattern.compile("\\s*if\\s*");

  //Assignment statements
  public static Pattern declaration = Pattern.compile("\\s*([" + node + "|" + list + "|" + stack + "]+\\s\\w+\\s*)");

  //Statements in the program
  public static Pattern print = Pattern.compile("\\s*print");

  //The collective program statement
  public static Pattern statement = Pattern.compile("[\\n\\s]*(" + print + "|" + declaration + "|" + name + "|" + whileLoop + "|" + forLoop + "|" + comment + ")");

  public static Pattern delimiter = Pattern.compile("\\s*(?=[:{}(),;!=\\.\"\\+]|" + comment + ")|(?<=[:{}(),;!=\\.\"\\+]|" + comment + ")");

}
