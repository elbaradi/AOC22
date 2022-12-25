package aoc.day05;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

class Solver {

  private static final String INPUT_PATHNAME_TEMPLATE = "src/resources/input_day_##.txt";
  private static final String DAY = "05";

  public static void main(String[] args) {

    List<String> lines = getRawInput();

    int numStacks = getNumberOfStacks(lines);
    List<int[]> moves = parseMoves(lines);

    // Problem #1
    List<Stack<String>> mutableStacks = parseStacks(lines, numStacks);

    moves.forEach(move -> useCrateMover9000(move, mutableStacks));
    String topCrateOfStacks = mutableStacks.stream()
        .filter(stack -> !stack.isEmpty())
        .map(Stack::peek)
        .reduce("", String::concat);

    System.out.println("Solution Problem #1: " + topCrateOfStacks);

    // Problem #2
    List<Stack<String>> moreMutableStacks = parseStacks(lines, numStacks);

    moves.forEach(move -> useCrateMover9001(move, moreMutableStacks));
    topCrateOfStacks = moreMutableStacks.stream()
        .filter(stack -> !stack.isEmpty())
        .map(Stack::peek)
        .reduce("", String::concat);

    System.out.println("Solution Problem #1: " + topCrateOfStacks);

  }

  private static void useCrateMover9001(int[] move, List<Stack<String>> mutableStacks) {

    Stack<String> crane = new Stack<>();

    for (int i = 0; i < move[0]; i++) {
      String crate = mutableStacks.get(move[1]).pop();
      crane.push(crate);
    }

    while (!crane.isEmpty()) {
      String crate = crane.pop();
      mutableStacks.get(move[2]).push(crate);
    }

  }

  private static void useCrateMover9000(int[] move, List<Stack<String>> mutableStacks) {

    for (int i = 0; i < move[0]; i++) {
      String crate = mutableStacks.get(move[1]).pop();
      mutableStacks.get(move[2]).push(crate);
    }

  }

  private static List<Stack<String>> parseStacks(List<String> lines, int numStacks) {

    List<Stack<String>> stacks = new ArrayList<>();

    int breakIndex = lines.indexOf("");
    List<String> rawStacks = lines.subList(0, breakIndex - 1);
    Collections.reverse(rawStacks);

    for (int i = 0; i < numStacks; i++) {
      stacks.add(new Stack<>());
    }

    for (String line : rawStacks) {
      for (int i = 0; 4 * i + 1 < line.length(); i++) {
        if (line.charAt(4 * i + 1) != ' ')
          stacks.get(i).push(String.valueOf(line.charAt(4 * i + 1)));
      }
    }

    Collections.reverse(rawStacks);

    return stacks;
  }

  private static List<int[]> parseMoves(List<String> lines) {

    List<int[]> moves = new ArrayList<>();

    int breakIndex = lines.indexOf("");
    List<String> rawMoves = lines.subList(breakIndex + 1, lines.size());

    for (String s : rawMoves) {
      String[] parts = s.split("move | from | to ");
      moves.add(
          new int[] {
              Integer.parseInt(parts[1]),
              Integer.parseInt(parts[2]) - 1,
              Integer.parseInt(parts[3]) - 1
          }
      );
    }

    return moves;
  }

  private static int getNumberOfStacks(List<String> lines) {

    int numberOfStacks;

    int breakIndex = lines.indexOf("");
    String[] stackIndices = lines.get(breakIndex - 1).stripLeading().split(" {3}");
    numberOfStacks = stackIndices.length;

    return numberOfStacks;
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
