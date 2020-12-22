package Parser;

import Parser.ProgramNodes.ExecNode;
import Parser.ProgramNodes.StatementNode;
import Utility.Exceptions.ParserFailure;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is responsible for parsing the files.
 */
public class Parser {
  Scanner fileScanner = null;
  ExecNode baseNode;

  public Parser(File toRead) {
    try {
      fileScanner = new Scanner(toRead);

      fileScanner.useDelimiter(Regex.delimiter);

      //Start the scanner
//      baseNode = parseProgram(fileScanner);

      System.out.println("Statement: " + Regex.statement);
      System.out.println("Assignment: " + Regex.assignment);
      System.out.println("Method call: " + Regex.methodCall);
      System.out.println("Maze: " + Regex.mazeCall);
      System.out.println("letterChar: " + Regex.letterChar);
      System.out.println("Scentence: " + Regex.scentence);
      System.out.println("Print: " + Regex.print);
      System.out.println("Print concat: " + Regex.printConcatenate);
      System.out.println("Declare var: " + Regex.declareVar);
      System.out.println("Use var: " + Regex.useVar);
      System.out.println("Reassign var: " + Regex.reassignVar);
      System.out.println("Line: " + Regex.line);
      System.out.println("While: " + Regex.whileLoop);
      System.out.println("For: " + Regex.forLoop);
      System.out.println("If: " + Regex.ifStmt);

      System.out.println();

      printScanner(fileScanner);

      fileScanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (ParserFailure e) {
      System.out.println("parser error");
      System.out.println(e.getMessage());
      fileScanner.close();
    }
  }

  private ExecNode parseProgram(Scanner fileScanner) {
    ArrayList<StatementNode> statements = new ArrayList<>();

    //Add statements to the list until the scanner is empty
    while (fileScanner.hasNext()) {
//      if (fileScanner.hasNext(Regex.statement)) {
//
//      }
    }

    return new StatementNode();
  }

  private void printScanner(Scanner s) {
    while (s.hasNext()) {
      System.out.println(s.next());
    }
  }

  private void fail(String message) {
    String msg = "Parse error: " + message + "\n   @ ...";
    for (int i = 0; i < 5 && fileScanner.hasNext(); i++) {
      msg += " " + fileScanner.next();
    }
    throw new ParserFailure(msg + "...");
  }
}
