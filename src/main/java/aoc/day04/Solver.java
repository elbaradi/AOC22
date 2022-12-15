package aoc.day04;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Solver {

  private static final String INPUT_PATHNAME_TEMPLATE = "src/resources/input_day_##.txt";
  private static final String DAY = "04";

  public static void main(String[] args) {

    List<String> lines = getRawInput();
    List<Range[]> pairs = getElfPairs(lines);

    // Problem #1
    long numberOfPairsWhereOneRangeContainsTheOtherFully =
        pairs.stream()
            .filter(
                ranges ->
                    ranges[0].containsFully(ranges[1]) || ranges[1].containsFully(ranges[0])
            )
            .count();

    System.out.println("Solution Problem #1: " + numberOfPairsWhereOneRangeContainsTheOtherFully);

    // Problem #2
    long numberOfPairsWhereOneRangeContainsTheOtherPartially =
        pairs.stream()
            .filter(
                ranges -> ranges[0].containsPartially(ranges[1])
            )
            .count();

    System.out.println("Solution Problem #2: " + numberOfPairsWhereOneRangeContainsTheOtherPartially);

  }

  private static List<Range[]> getElfPairs(List<String> lines) {
    List<Range[]> pairs = new ArrayList<>();

    for (String line : lines) {
      String[] parts = line.split("[,-]");
      Range[] pair = new Range[] {
          new Range(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])),
          new Range(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]))
      };
      pairs.add(pair);
    }

    return pairs;
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
