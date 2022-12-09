package aoc.day03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Solver {

  private static final String INPUT_PATHNAME_TEMPLATE = "src/resources/input_day_##.txt";
  private static final String DAY = "03";

  public static void main(String[] args) {

    List<String> lines = getRawInput();

    //  Problem #1
    List<List<String>> backpacks = parseBackpacks(lines);
    int summedPrioritiesOfDuplicateItems =
        backpacks.stream().map(Solver::findDuplicateItem).mapToInt(Solver::getPriority).sum();

    System.out.println("Solution Problem #1: " + summedPrioritiesOfDuplicateItems);

    //  Problem #2
    List<List<String>> groups = parseGroups(lines);

    int summedPrioritiesOfBadges =
        groups.stream().map(Solver::findDuplicateItem).mapToInt(Solver::getPriority).sum();

    System.out.println("Solution Problem #2: " + summedPrioritiesOfBadges);

  }

  private static int getPriority(Character item) {
    if (Character.isLowerCase(item)) {
      return item - 'a' + 1;
    }
    else if (Character.isUpperCase(item)) {
      return item - 'A' + 27;
    }
    else {
      throw new IllegalArgumentException("Item must be a letter!");
    }
  }

  private static Character findDuplicateItem(List<String> containers) {
    if (containers.isEmpty()) {
      throw new IllegalStateException("No container!");
    }

    String[] itemsInFirstContainer = containers.get(0).split("");
    List<String> otherContainers = containers.subList(1, containers.size());

    return Arrays.stream(itemsInFirstContainer)
        .filter(item -> otherContainers.stream().allMatch(
            s -> s.contains(item)
        ))
        .reduce("", String::concat)
        .charAt(0);
  }

  private static List<List<String>> parseBackpacks(List<String> lines) {
    return lines.stream().map(
            line -> {
              List<String> compartments = new ArrayList<>();
              int middle = line.length() / 2;

              compartments.add(line.substring(0, middle));
              compartments.add(line.substring(middle));

              return compartments;
            })
        .toList();
  }

  private static List<List<String>> parseGroups(List<String> lines) {
    return lines.stream()
        .collect(Collectors.groupingBy(s -> lines.indexOf(s) / 3))
        .values()
        .stream().toList();
  }

  private static List<String> getRawInput() {
    List<String> lines = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(getInputPathname()))) {
      reader.lines().forEach(lines::add);
    }
    catch (IOException e) {
      throw new RuntimeException("Couldn't retrieve input!");
    }
    return lines;
  }

  public static String getInputPathname() {
    return INPUT_PATHNAME_TEMPLATE.replace("##", DAY);
  }

}
