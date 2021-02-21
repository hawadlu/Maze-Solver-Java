package parser;

import java.util.regex.Pattern;

public class Regex {
  //Punctuation
  final public static Pattern openParen = Pattern.compile("\\(");
  final public static Pattern closeParen = Pattern.compile("\\)");
  final public static Pattern doubleQuote = Pattern.compile("\\s*\"");
  final public static Pattern equals = Pattern.compile("=");
  final public static Pattern colon = Pattern.compile(":");
  final public static Pattern semiColon = Pattern.compile(";");
  final public static Pattern dot = Pattern.compile("\\.");
  final public static Pattern not = Pattern.compile("!");
  final public static Pattern openCurly = Pattern.compile("\\{");
  final public static Pattern closeCurly = Pattern.compile("}");
  final public static Pattern plus = Pattern.compile("\\s*\\+");
  final public static Pattern number = Pattern.compile("\\s*-*[0-9]+(.\\d+)*");
  final public static Pattern comma = Pattern.compile(",");

  final public static Pattern name = Pattern.compile("\\s*\\w+"); // \w Matches any letter, digit or underscore. Equivalent to [a-zA-Z0-9_].
  final public static Pattern sentencePunctuation = Pattern.compile("\\s|.|,");
  final public static Pattern printConcatenate = Pattern.compile("(\s\\+)");
  final public static Pattern sentence = Pattern.compile(doubleQuote + "[" + name + "" + sentencePunctuation +"]*" + doubleQuote);
  final public static Pattern methodCall = Pattern.compile("\\w+\\((\\w+,*\\s*)*\\)");
  final public static Pattern mazeCall = Pattern.compile("\\s*Maze");
  final public static Pattern declareVar = Pattern.compile("\\w+\\s\\w+[\\s=\\s]*");
  final public static Pattern useVar = Pattern.compile("^((?!Maze)\\w+\\.+)");
  final public static Pattern reassignVar = Pattern.compile("\\w+\\s=\\s");
  final public static Pattern comment = Pattern.compile("\\*\\*\\*");
  final public static Pattern comparatorAssignment = Pattern.compile("->");
  final public static Pattern lessThan = Pattern.compile("\\s*lt");
  final public static Pattern greaterThan = Pattern.compile("\\s*gt");
  final public static Pattern equalTo = Pattern.compile("\\s*eq");
  final public static Pattern comparatorMethod = Pattern.compile("distanceToDestination|getNeighbourCount|getCost");

  //Math action
  final public static Pattern mathPlus = Pattern.compile("\\s*plus");
  final public static Pattern mathMinus = Pattern.compile("\\s*minus");
  final public static Pattern mathDivide = Pattern.compile("\\s*divide");
  final public static Pattern mathMultiply = Pattern.compile("\\s*multiply");
  final public static Pattern mathPower = Pattern.compile("\\s*power");
  final public static Pattern mathRoot= Pattern.compile("\\s*root");
  final public static Pattern math = Pattern.compile(mathPlus + "|" + mathMinus + "|" + mathDivide + "|" + mathMultiply + "|" + mathPower + "|" + mathRoot + "|" + number);



  //Data types
  final public static Pattern node = Pattern.compile("MazeNode");
  final public static Pattern list = Pattern.compile("List");
  final public static Pattern stack = Pattern.compile("Stack");
  final public static Pattern queue = Pattern.compile("Queue");
  final public static Pattern priorityQueue = Pattern.compile("PriorityQueue");
  final public static Pattern comparator = Pattern.compile("Comparator");
  final public static Pattern numberDeclaration = Pattern.compile("Number");

  //Loops
  final public static Pattern whileLoop = Pattern.compile("\\s*while\\s*");
  final public static Pattern forLoop = Pattern.compile("\\s*for\\s*");

  //If statements
  final public static Pattern ifStmt = Pattern.compile("\\s*if\\s*");
  final public static Pattern elseIf = Pattern.compile("\\s*else if\\s*");
  final public static Pattern ifElse = Pattern.compile("\\s*else\\s*");

  //Assignment statements
  final public static Pattern declaration = Pattern.compile("\\s*([" + node + "|" + list + "|" + stack + "|" + queue + "|" + priorityQueue + "|" + comparator + "|" + numberDeclaration + "]+\\s\\w+\\s*)");

  //Statements in the program
  final public static Pattern print = Pattern.compile("\\s*print");

  //The collective program statement
  final public static Pattern statement = Pattern.compile("[\\n\\s]*(" + print + "|" + declaration + "|" + name + "|" + whileLoop + "|" + forLoop + "|" + comment + ")");

  final public static Pattern delimiter = Pattern.compile("\\s*(?=[:{}(),;!=.\"+]|(" + comment + ")|(" + comparatorAssignment + "))|(?<=[:{}(),;!=.\"+]|(" + comment + ")|(" + comparatorAssignment + "))");

}
