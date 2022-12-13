package aoc.day13;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Solver {

  private static final String INPUT_PATHNAME_TEMPLATE = "src/resources/input_day_##.txt";
  private static final String DAY = "13";

  public static void main(String[] args) {

    List<String> lines = getRawInput();
    Map<Integer, List<Packet>> pairs = getPairs(lines);

    // Problem #1
    int summedIndices = pairs.entrySet().stream().filter(
            entry -> isInTheRightOrder(entry.getValue())
        )
        .mapToInt(Map.Entry::getKey)
        .sum();

    System.out.println("Solution Problem #1: " + summedIndices);

    // Problem #2
    Packet firstDividerPacket = new Packet("[[2]]");
    Packet secondDividerPacket = new Packet("[[6]]");

    List<Packet> allPackets = pairs.values().stream().flatMap(List::stream).collect(Collectors.toList());
    allPackets.add(firstDividerPacket);
    allPackets.add(secondDividerPacket);

    List<Packet> sortedPackets = allPackets.stream().sorted().toList();

    int indexFirst = sortedPackets.indexOf(firstDividerPacket) + 1;
    int indexSecond = sortedPackets.indexOf(secondDividerPacket) + 1;

    System.out.println("Solution Problem #2: " + (indexFirst * indexSecond));

  }

  private static boolean isInTheRightOrder(List<Packet> pair) {
    return pair.get(1).compareTo(pair.get(0)) > 0;
  }

  private static Map<Integer, List<Packet>> getPairs(List<String> lines) {
    Map<Integer, List<Packet>> pairs = new HashMap<>();

    List<Packet> pair = new ArrayList<>();
    for (int i = 0; i < lines.size(); i++) {
      if (lines.get(i).isEmpty()) {
        pairs.put((i + 1) / 3, pair);
        pair = new ArrayList<>();
      } else
        pair.add(new Packet(lines.get(i)));
    }
    pairs.put((lines.size() + 1) / 3, pair);

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
