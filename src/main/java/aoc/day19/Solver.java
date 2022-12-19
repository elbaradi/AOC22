package aoc.day19;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Math.min;

class Solver {

  private static final String INPUT_PATHNAME_TEMPLATE = "src/resources/input_day_##.txt";
  private static final String DAY = "19";

  public static void main(String[] args) {

    List<String> lines = getRawInput();
    List<Blueprint> blueprints = getBlueprints(lines);

    // Problem #1
    Map<Integer, Integer> mostGeodesByIds = blueprints.stream().collect(Collectors.toMap(
        Blueprint::id, blueprint -> blueprint.getMostGeodes(24)
    ));

    int sumOfQualityLevels = mostGeodesByIds.entrySet().stream().mapToInt(entry -> entry.getKey() * entry.getValue()).sum();

    System.out.println("Solution Problem #1: " + sumOfQualityLevels);

    // Problem #2
    List<Blueprint> uneatenBlueprints = blueprints.subList(0, min(3, blueprints.size()));
    mostGeodesByIds = uneatenBlueprints.stream().collect(Collectors.toMap(
        Blueprint::id, blueprint -> blueprint.getMostGeodes(32)
    ));

    long productOfMostGeodes = mostGeodesByIds.values().stream().reduce(1, (a, b) -> a * b);
    System.out.println("Solution Problem #2: " + productOfMostGeodes);

  }

  private static List<Blueprint> getBlueprints(List<String> lines) {

    List<Blueprint> blueprints = new ArrayList<>();

    for (String line: lines)
      blueprints.add(Blueprint.newInstance(line));

    return blueprints;
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
