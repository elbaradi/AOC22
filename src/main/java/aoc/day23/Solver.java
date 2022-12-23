package aoc.day23;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static aoc.day23.Direction.E;
import static aoc.day23.Direction.N;
import static aoc.day23.Direction.S;
import static aoc.day23.Direction.W;

class Solver {

  private static final String INPUT_PATHNAME_TEMPLATE = "src/resources/input_day_##.txt";
  private static final String DAY = "23";

  public static void main(String[] args) {

    List<String> lines = getRawInput();
    Set<Pos> elves = parseElves(lines);

    // Problem #1
    int numElves = elves.size();
    Set<Pos> elvesAfter10Rounds = moveElves(10, elves);
    long smallestRectangleArea = getRectangleArea(elvesAfter10Rounds);

    System.out.println("Solution Problem #1: " + (smallestRectangleArea - numElves));

    // Problem #2
    int roundsUntilAllStationary = moveElvesUntilAllStationary(elves);

    System.out.println("Solution Problem #2: " + roundsUntilAllStationary);

  }

  private static int moveElvesUntilAllStationary(Set<Pos> elvesAtStart) {
    Set<Pos> movingElves = new HashSet<>(elvesAtStart);

    int round = 0;
    while (!movingElves.equals(movingElves = moveOneRound(round, movingElves))) {
      round++;
    }

    return round + 1;
  }

  private static long getRectangleArea(Set<Pos> elves) {

    LongSummaryStatistics xStats = elves.stream().mapToLong(Pos::x).summaryStatistics();
    LongSummaryStatistics yStats = elves.stream().mapToLong(Pos::y).summaryStatistics();

    long width = xStats.getMax() - xStats.getMin() + 1;
    long height = yStats.getMax() - yStats.getMin() + 1;

    return width * height;
  }

  private static Set<Pos> moveElves(int rounds, Set<Pos> elvesAtStart) {

    Set<Pos> movingElves = new HashSet<>(elvesAtStart);

    for (int i = 0; i < rounds; i++) {
      movingElves = moveOneRound(i, movingElves);
    }

    return movingElves;
  }

  private static Set<Pos> moveOneRound(int round, Set<Pos> elvesAtStartOfRound) {

    Set<Pos> movingElves = new HashSet<>();
    Map<Pos, Pos> proposedMoves = new HashMap<>();
    Set<Pos> proposedDuplicates = new HashSet<>();

    for (Pos elf : elvesAtStartOfRound) {
      Set<Pos> adjacentElves = getAdjacentElves(elf, elvesAtStartOfRound);
      Pos proposed = considerDirections(round, elf, adjacentElves);
      if (proposed.equals(elf))
        movingElves.add(elf);
      else {
        if (proposedMoves.containsKey(proposed)) {
          proposedDuplicates.add(proposed);
          movingElves.add(elf);
        }
        else
          proposedMoves.put(proposed, elf);
      }
    }

    for (Map.Entry<Pos,Pos> move : proposedMoves.entrySet()) {
      if (proposedDuplicates.contains(move.getKey()))
        movingElves.add(move.getValue());
      else
        movingElves.add(move.getKey());
    }

    return movingElves;
  }

  private static Set<Pos> getAdjacentElves(Pos elf, Set<Pos> elvesAtStartOfRound) {
    return elvesAtStartOfRound.stream().filter(elf::isAdjacent).collect(Collectors.toSet());
  }

  private static Pos considerDirections(int roundNumber, Pos elf, Set<Pos> adjacentElves) {
    if (adjacentElves.isEmpty())
      return elf;

    Pos[] directions = { N, S, W, E };
    LongSummaryStatistics xStats = adjacentElves.stream().mapToLong(Pos::x).summaryStatistics();
    LongSummaryStatistics yStats = adjacentElves.stream().mapToLong(Pos::y).summaryStatistics();
    boolean[] conditions = {
        yStats.getMin() >= elf.y(), // N is clear
        yStats.getMax() <= elf.y(), // S is clear
        xStats.getMin() >= elf.x(), // W is clear
        xStats.getMax() <= elf.x(), // E is clear
    };

    for (int directionsConsidered = 0; directionsConsidered < 4; directionsConsidered++) {
        boolean directionIsClear = conditions[(roundNumber + directionsConsidered) % 4];
        if (directionIsClear) {
          return Pos.add(elf, directions[(roundNumber + directionsConsidered) % 4]);
        }
    }

    return elf;
  }


  private static Set<Pos> parseElves(List<String> lines) {

    Set<Pos> elves = new HashSet<>();

    int height = lines.size();
    int width = lines.get(0).length();

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (lines.get(y).charAt(x) == '#') {
          elves.add(new Pos(x, y));
        }
      }
    }

    return elves;
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
