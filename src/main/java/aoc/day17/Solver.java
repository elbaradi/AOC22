package aoc.day17;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static aoc.day17.Direction.DOWN;
import static aoc.day17.Direction.LEFT;
import static aoc.day17.Direction.RIGHT;

class Solver {

  private static final String INPUT_PATHNAME_TEMPLATE = "src/resources/input_day_##.txt";
  private static final String DAY = "17";
  static final int ROCK_SPAWN_X = 3;
  static final long ROCK_SPAWN_Y_DELTA = 4;
  static final int CHAMBER_WIDTH = 7;
  static final Pos BOTTOM_LEFT = new Pos(0, 0);


  public static void main(String[] args) {

    List<String> lines = getRawInput();
    List<Pos> directions = getDirections(lines);

    // Problem #1
    long small = 2022L;
    long heightBlockSmallStack = dropRocks(directions, small);

    System.out.println("Solution Problem #1: " + heightBlockSmallStack);

    // Problem #2
    Cycle cycle = findCycle(directions);

    final long big = 1_000_000_000_000L;
    long nonCycleLength = (big - cycle.start()) % cycle.length() + cycle.start();
    long nonCycleHeight = dropRocks(directions, nonCycleLength);
    long numberOfCycles = (big - nonCycleLength) / cycle.length();

    long heightBlockBigStack = nonCycleHeight + cycle.height() * numberOfCycles;
    System.out.println("Solution Problem #2: " + heightBlockBigStack);
  }

  // Floyd's turtle and hare-- Maarten's suggestion; thanks!
  private static Cycle findCycle(List<Pos> directions) {

    State turtle = new State(directions);
    State hare = new State(directions);

    turtle.dropRock();
    hare.dropRock().dropRock();

    while (!turtle.equals(hare)) {
      turtle.dropRock();
      hare.dropRock().dropRock();
    }

    return new Cycle(turtle.getCount(), hare.getCount() - turtle.getCount(), turtle.getHeight() - hare.getHeight());
  }

  private static long dropRocks(List<Pos> directions, long count) {

    State rocks = new State(directions);

    for (long i = 0; i < count; i++)
      rocks.dropRock();

    return -rocks.getHeight();
  }

  private static List<Pos> getDirections(List<String> lines) {

    List<Pos> directions = new ArrayList<>();
    String[] parts = lines.get(0).split("");

    for(String part : parts) {
      if (part.equals("<"))
        directions.add(LEFT);
      else if (part.equals(">"))
        directions.add(RIGHT);
      else
        throw new IllegalArgumentException("Invalid input char");
      directions.add(DOWN);
    }

    return directions;
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
