package aoc.day08;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Stream;

class Solver {

  private static final String INPUT_PATHNAME_TEMPLATE = "src/resources/input_day_##.txt";
  private static final String DAY = "08";

  public static void main(String[] args) {

    List<String> lines = getRawInput();
    int[][] grid = parseGrid(lines);

    // Problem #1
    int[][] visibleTrees = getVisibleTrees(grid);

    long totalVisibleTrees =
        Stream.of(visibleTrees)
            .flatMapToInt(Arrays::stream)
            .sum();

    System.out.println("Solution Problem #1: " + totalVisibleTrees);

    // Problem #2
    int[][] scenicScores = getScenicScores(grid);

    OptionalInt highestScenicScore =
        Stream.of(scenicScores)
            .flatMapToInt(Arrays::stream)
            .max();

    System.out.println("Solution Problem #2: " + highestScenicScore.getAsInt());
  }

  private static int[][] getScenicScores(int[][] grid) {
    int width = grid[0].length;
    int length = grid.length;

    int[][] scenicScores = new int[length][width];

    for (int y = 0; y < length; y++) {
      for (int x = 0; x < width; x++) {
        scenicScores[y][x] = getScenicScoreOf(grid, y, x);
      }
    }

    return scenicScores;
  }

  private static int getScenicScoreOf(int[][] grid, int y, int x) {
    int width = grid[0].length;
    int length = grid.length;

    // Initialize storage for the count of trees
    // that can be seen from this tree in each direction [N,S,W,E]
    int[] dists = new int[4];

    // Return zero score if this tree at edge of grid
    if (y == 0 || y == length - 1 || x == 0 || x == width - 1) {
      return 0;
    }

    // Count the visible trees to the north
    while (y - ++dists[0] > 0 && grid[y][x] > grid[y - dists[0]][x]) ;

    // Count the visible trees to the south
    while (y + ++dists[1] < length - 1 && grid[y][x] > grid[y + dists[1]][x]) ;

    // Count the visible trees to the west
    while (x - ++dists[2] > 0 && grid[y][x] > grid[y][x - dists[2]]) ;

    // Count the visible trees to the east
    while (x + ++dists[3] < width - 1 && grid[y][x] > grid[y][x + dists[3]]) ;

    // Score = N * S * W * E
    return Arrays.stream(dists).reduce(1, Math::multiplyExact);
  }

  private static int[][] getVisibleTrees(int[][] grid) {
    int width = grid[0].length;
    int length = grid.length;

    int[][] visibleTrees = new int[length][width];

    int highestTreeInLineOfView;
    for (int y = 0; y < length; y++) {

      // Check visibility from the west
      highestTreeInLineOfView = -1;
      for (int x = 0; x < width; x++) {
        if (grid[y][x] > highestTreeInLineOfView) {
          highestTreeInLineOfView = grid[y][x];
          visibleTrees[y][x] = 1;
        }
      }

      // Check visibility from the east
      highestTreeInLineOfView = -1;
      for (int x = width - 1; x >= 0; x--) {
        // Tallest tree that can be seen from the west,
        // from the east no trees can be seen behind it
        if (visibleTrees[y][x] == 1) {
          break;
        }

        if (grid[y][x] > highestTreeInLineOfView) {
          highestTreeInLineOfView = grid[y][x];
          visibleTrees[y][x] = 1;
        }
      }
    }

    for (int x = 0; x < width; x++) {

      // Check visibility from the north
      highestTreeInLineOfView = -1;
      for (int y = 0; y < length; y++) {
        if (grid[y][x] > highestTreeInLineOfView) {
          highestTreeInLineOfView = grid[y][x];
          visibleTrees[y][x] = 1;
        }
      }

      // Check visibility from the south
      highestTreeInLineOfView = -1;
      for (int y = length - 1; y >= 0; y--) {
        if (grid[y][x] > highestTreeInLineOfView) {
          highestTreeInLineOfView = grid[y][x];
          visibleTrees[y][x] = 1;
        }
      }
    }

    return visibleTrees;
  }

  private static int[][] parseGrid(List<String> lines) {
    int length = lines.size();
    int width = lines.get(0).length();

    int[][] grid = new int[length][width];

    for (int i = 0; i < length; i++) {
      String[] characters = lines.get(i).split("");
      for (int j = 0; j < width; j++) {
        grid[i][j] = Integer.parseInt(characters[j]);
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
      throw new RuntimeException(e);
    }
    return lines;
  }

  public static String getInputPathname() {
    return INPUT_PATHNAME_TEMPLATE.replace("##", DAY);
  }

}
