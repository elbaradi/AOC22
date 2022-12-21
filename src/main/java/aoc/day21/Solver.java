package aoc.day21;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Solver {

  private static final String INPUT_PATHNAME_TEMPLATE = "src/resources/input_day_##.txt";
  private static final String DAY = "21";

  public static void main(String[] args) {

    List<String> lines = getRawInput();
    Map<String, String> monkeyJobs = getMonkeyJobs(lines);

    // Problem #1
    Map<String, Long> results = new HashMap<>();

    whyAreAllTheseMonkeysYelling("root", monkeyJobs, results);

    System.out.println("Solution Problem #1: " + results.get("root"));
    results.clear();

    // Problem #2
    monkeyJobs.put("humn", "unknown");
    whyAreAllTheseMonkeysYelling("root", monkeyJobs, results);

    String replace = monkeyJobs.get("root").replaceFirst("[+\\-*/]", "-");
    monkeyJobs.put("root", replace);
    results.put("root", 0L);

    solveForUnknowns("root", monkeyJobs, results);

    System.out.println("Solution Problem #2: " + results.get("humn"));

  }

  private static void solveForUnknowns(String name, Map<String, String> monkeyJobs, Map<String, Long> results) {
    if (name.equals("humn"))
      return;

    String[] operands = monkeyJobs.get(name).split(" ");

    Long known = getKnown(operands[0], operands[2], results);
    String unknown = getUnknown(operands[0], operands[2], results);
    String operator = operands[1];
    Long result = results.get(name);

    boolean unknownIsFirstTerm = unknown.equals(operands[0]);

    switch (operator) {
      case "+" -> results.put(unknown, result - known);
      case "*" -> results.put(unknown, result / known);
      case "-" -> results.put(unknown, unknownIsFirstTerm ? result + known : known - result);
      case "/" -> results.put(unknown, unknownIsFirstTerm ? result * known : known / result);
      default -> throw new IllegalArgumentException("Operator not valid!");
    }

    solveForUnknowns(unknown, monkeyJobs, results);

  }

  private static String getUnknown(String firstTerm, String secondTerm, Map<String, Long> results) {
    return results.containsKey(firstTerm) ? secondTerm : firstTerm;
  }

  private static Long getKnown(String firstTerm, String secondTerm, Map<String, Long> results) {
    return results.containsKey(firstTerm) ? results.get(firstTerm) : results.get(secondTerm);
  }

  private static void whyAreAllTheseMonkeysYelling(String name, Map<String, String> monkeyJobs, Map<String, Long> results) {
    if (name.equals("humn") && monkeyJobs.get(name).equals("unknown"))
        return;

    // If the monkey's job is simply to yell a number, return that number
    if (monkeyJobs.get(name).matches("\\d+")) {
      results.put(name, Long.parseLong(monkeyJobs.get(name)));
      return;
    }

    // Split the monkey's job into the two operands
    String[] operands = monkeyJobs.get(name).split(" ");

    whyAreAllTheseMonkeysYelling(operands[0], monkeyJobs, results);
    whyAreAllTheseMonkeysYelling(operands[2], monkeyJobs, results);

    if (!results.containsKey(operands[0]) || !results.containsKey(operands[2]))
      return;

    long a = results.get(operands[0]);
    long b = results.get(operands[2]);

    // Perform the appropriate operation and return the result
    switch (operands[1]) {
      case "+" -> results.put(name, a + b);
      case "-" -> results.put(name, a - b);
      case "*" -> results.put(name, a * b);
      case "/" -> results.put(name, a / b);
      default -> throw new IllegalArgumentException("Invalid operator");
    }
  }

  private static Map<String, String> getMonkeyJobs(List<String> lines) {

    Map<String, String> monkeyJobs = new HashMap<>();

    for (String line : lines) {
      String[] parts = line.split(": ");
      monkeyJobs.put(parts[0], parts[1]);
    }

    return monkeyJobs;
  }

  private static List<String> getRawInput() {
    List<String> lines = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(getInputPathname()))) {
      reader.lines().forEach(lines::add);
    }
    catch (IOException e) {
      throw new RuntimeException("Retrieving input went wrong!");
    }
    return lines;
  }

  public static String getInputPathname() {
    return INPUT_PATHNAME_TEMPLATE.replace("##", DAY);
  }

}
