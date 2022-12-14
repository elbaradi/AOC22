package aoc.day14;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static aoc.day14.Direction.DOWN;
import static aoc.day14.Direction.DOWN_LEFT;
import static aoc.day14.Direction.DOWN_RIGHT;

class Solver {

  private static final String INPUT_PATHNAME_TEMPLATE = "src/resources/input_day_##.txt";
  private static final String DAY = "14";
  private static final Pos SOURCE_OF_SAND = new Pos(500, 0);

  public static void main(String[] args) {

    List<String> lines = getRawInput();
    List<List<Pos>> rockPaths = getRockPaths(lines);

    // Thanks Maarten, for giving me the idea to use sets! @mraasvel
    Set<Pos> rocks = getRocks(rockPaths);

    // Problem #1
    int floor = getLowest(rockPaths);
    Set<Pos> blocks = dropSand(rocks, floor, false);

    // Set of block positions (sand + rocks), minus the set of rock positions.
    int totalSand = blocks.size() - rocks.size();

    System.out.println("Solution Problem #1: " + totalSand);

    // Problem #2
    floor += 2;
    blocks = dropSand(rocks, floor, true);

    totalSand = blocks.size() - rocks.size();

    System.out.println("Solution Problem #2: " + totalSand);

  }

  private static Set<Pos> dropSand(Set<Pos> rocks, int floor, boolean hasFloor) {
    Set<Pos> blocks = new HashSet<>(rocks);
    Pos sand = spawnSand();

    while (true) {
      if (!hasFloor && sand.y() + 1 > floor)
        break;
      else if (hasFloor && sand.y() == floor - 1) {
        blocks.add(sand);
        sand = spawnSand();
      }
      else if (!blocks.contains(Pos.add(sand, DOWN)))
        sand.add(DOWN);
      else if (!blocks.contains(Pos.add(sand, DOWN_LEFT)))
        sand.add(DOWN_LEFT);
      else if (!blocks.contains(Pos.add(sand, DOWN_RIGHT)))
        sand.add(DOWN_RIGHT);
      else {
        blocks.add(sand);
        if (sand.equals(spawnSand()))
          break;
        sand = spawnSand();
      }
    }

    return blocks;
  }

  private static Pos spawnSand() {
    return new Pos(SOURCE_OF_SAND);
  }

  private static Set<Pos> getRocks(List<List<Pos>> rockPaths) {
    Set<Pos> rocks = new HashSet<>();

    rockPaths.forEach(
        path -> rocks.addAll(getRocksFromPath(path))
    );

    return rocks;
  }

  private static Set<Pos> getRocksFromPath(List<Pos> path) {
    Set<Pos> rocks = new HashSet<>();
    rocks.add(new Pos(path.get(0)));

    for (int i = 0; i < path.size() - 1; i++) {
      rocks.addAll(getRockLine(path.get(i), path.get(i + 1)));
    }

    return rocks;
  }

  private static Set<Pos> getRockLine(Pos start, Pos end) {
    Set<Pos> rocks = new HashSet<>();
    Pos dir = Pos.direction(start, end);

    for (Pos rock = new Pos(start); !rock.equals(end); rock.add(dir)) {
      rocks.add(new Pos(rock));
    }

    rocks.add(new Pos(end));

    return rocks;
  }

  private static int getLowest(List<List<Pos>> paths) {
    return paths.stream().flatMap(List::stream).mapToInt(Pos::y).max().getAsInt();
  }

  private static List<List<Pos>> getRockPaths(List<String> lines) {
    List<List<Pos>> rockPaths = new ArrayList<>();

    for (String line : lines) {
      List<Pos> rockPath = new ArrayList<>();
      String[] rockPathPositions = line.split(" -> ");
      for (String position : rockPathPositions)
        rockPath.add(new Pos(position));
      rockPaths.add(rockPath);
    }

    return rockPaths;
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
