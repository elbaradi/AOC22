package aoc.day10;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Math.abs;

class Solver {

  private static final String INPUT_PATHNAME_TEMPLATE = "src/resources/input_day_##.txt";
  private static final String DAY = "10";

  public static void main(String[] args) {

    List<String> lines = getRawInput();
    List<Integer> instructions = parseInstructions(lines);
    List<Integer> totals = getTotals(instructions);

    // Problem #1
    // summing the values at {20, 60, 100, 140, 180, 220}
    int summed = IntStream.range(0, 6).map(i -> i * 40 + 20).map(i -> i * totals.get(i - 1)).sum();

    System.out.println("Solution Problem #1: " + summed);

    // Problem #2
    System.out.println("Solution Problem #2: ");

    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < 40; j++) {
        if (abs((j) - totals.get(i * 40 + j)) <= 1) {
          System.out.print("#");
        }
        else {
          System.out.print(".");
        }
      }
      System.out.println();
    }
  }

  private static List<Integer> getTotals(List<Integer> instructions) {

    List<Integer> totals = new ArrayList<>();

    int total = 1;
    totals.add(total);

    for (int instruction : instructions) {
      total += instruction;
      totals.add(total);
    }

    return totals;
  }

  private static List<Integer> parseInstructions(List<String> lines) {
    List<String> instructions = new ArrayList<>(lines);
    for (int i = lines.size() - 1; i >= 0; i--)
    {
      if (lines.get(i).startsWith("addx"))
        instructions.add(i, "noop");
    }
    return instructions.stream().map(line -> {
      if (line.equals("noop"))
        return 0;
      else
        return Integer.parseInt(line.split(" ")[1]);
    }).toList();
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
