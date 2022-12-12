package aoc.day12;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;

import static aoc.day12.Direction.DOWN;
import static aoc.day12.Direction.LEFT;
import static aoc.day12.Direction.RIGHT;
import static aoc.day12.Direction.UP;

class Solver {

  private static final String INPUT_PATHNAME_TEMPLATE = "src/resources/input_day_##.txt";
  private static final String DAY = "12";

  public static void main(String[] args) {

    List<String> lines = getRawInput();
    Character[][] grid = parseGrid(lines);

    // Get 'S' and set elevation to 'a'
    Pos start = findStart(grid);
    grid[start.y()][start.x()] = 'a';

    // Get 'E' and set elevation to 'z'
    Pos end = findEnd(grid);
    grid[end.y()][end.x()] = 'z';

    // Problem #1
    // Start: S
    // Finish: E
    int fewestSteps = getFewestSteps(grid, start, pos -> pos.equals(end), false);

    System.out.println("Solution Problem #1: " + fewestSteps);

    // Problem #2
    // Start: any 'a'
    // Finish: E
    // Solving using backward induction:
    // calculating the path in reverse, starting from E
    fewestSteps = getFewestSteps(grid, end, pos -> grid[pos.y()][pos.x()] == 'a', true);

    System.out.println("Solution Problem #2: " + fewestSteps);

  }

  private static int getFewestSteps(Character[][] grid, Pos startPos, Predicate<Pos> finish, boolean isReversed) {

    Set<Pos> visited = new HashSet<>();
    Queue<Point> queue = new LinkedList<>();

    Point start = new Point(startPos, 0);

    visited.add(start.getPos());
    queue.add(start);
    int fewestSteps = Integer.MAX_VALUE;

    while (!queue.isEmpty()) {
      Point point = queue.poll();
      Pos pos = point.getPos();

      if (finish.test(pos)) {
        fewestSteps = point.getSteps();
        break;
      }

      List<Pos> nextSteps = findNextSteps(pos, grid, isReversed);

      for (Pos p : nextSteps) {
        if (!visited.contains(p)) {
          visited.add(p);
          queue.add(new Point(p, point.getSteps() + 1));
        }
      }

    }
    return fewestSteps;
  }

  private static List<Pos> findNextSteps(Pos current, Character[][] grid, boolean isReversed) {

    List<Pos> nextSteps = new ArrayList<>();

    nextSteps.add(Pos.add(current, UP));
    nextSteps.add(Pos.add(current, DOWN));
    nextSteps.add(Pos.add(current, LEFT));
    nextSteps.add(Pos.add(current, RIGHT));

    nextSteps.removeIf(
        next -> isOutOfBounds(next, grid)
                || (isReversed
                    ? hasTooMuchElevationDifference(current, next, grid)
                    : hasTooMuchElevationDifference(next, current, grid))
    );

    return nextSteps;
  }

  private static boolean hasTooMuchElevationDifference(Pos next, Pos current, Character[][] grid) {
    return grid[next.y()][next.x()] - grid[current.y()][current.x()] > 1;
  }

  private static boolean isOutOfBounds(Pos pos, Character[][] grid) {
    return pos.x() < 0 || pos.y() < 0 || pos.x() >= grid[0].length || pos.y() >= grid.length;
  }

  private static Pos findStart(Character[][] grid) {
    int width = grid[0].length;
    int length = grid.length;

    for (int y = 0; y < length; y++) {
      for (int x = 0; x < width; x++) {
        if (grid[y][x] == 'S')
          return new Pos(x, y);
      }
    }

    throw new IllegalArgumentException("No starting point found!");
  }

  private static Pos findEnd(Character[][] grid) {
    int width = grid[0].length;
    int length = grid.length;

    for (int y = 0; y < length; y++) {
      for (int x = 0; x < width; x++) {
        if (grid[y][x] == 'E')
          return new Pos(x, y);
      }
    }

    throw new IllegalArgumentException("No end point found!");
  }

  private static Character[][] parseGrid(List<String> lines) {
    int width = lines.get(0).length();
    int length = lines.size();

    Character[][] grid = new Character[length][width];

    for (int i = 0; i < length; i++) {
      String line = lines.get(i);
      for (int j = 0; j < width; j++) {
        grid[i][j] = line.charAt(j);
      }
    }

    return grid;
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
