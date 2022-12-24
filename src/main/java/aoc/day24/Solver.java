package aoc.day24;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

class Solver {

  private static final String INPUT_PATHNAME_TEMPLATE = "src/resources/input_day_##.txt";
  private static final String DAY = "24";

  public static void main(String[] args) {

    List<String> lines = getRawInput();

    Blizzard blizzard = getBlizzard(lines);

    Pos start = new Pos(0, -1);
    Pos end = new Pos(blizzard.getWidth() - 1, blizzard.getHeight());

    // Problem #1
    int minutes = findQuickestPath(start, end, 0, blizzard, false);

    System.out.println("Solution Problem #1: " + minutes);

    // Problem #2
    minutes += findQuickestPath(start, end, minutes, blizzard, true);
    minutes += findQuickestPath(start, end, minutes, blizzard, false);
    System.out.println("Solution Problem #2: " + minutes);

  }

  private static int findQuickestPath(Pos start, Pos end, int minutes, Blizzard blizzard, boolean isReverse) {

    Stack<State> stack = new Stack<>();
    State state = !isReverse ? new State(start, end, minutes, blizzard) : new State(end, start, minutes, blizzard);

    // Memo can be done through a set instead of a HashMap<State, Time>,
    // because of the way we travel through states,
    // prioritizing routes that bring us closer to the end.
    // This set saves lookup time over a Map.
    Set<State> memo = new HashSet<>();

    stack.add(state);

    int maxPossibleTime = blizzard.getLcm() * blizzard.getWidth() * blizzard.getHeight() - minutes;
    int quickest = maxPossibleTime + 1;

    while (!stack.isEmpty()) {
      state = stack.pop();

      // relativizedState = { Player, minutes % blizzard cycle length }
      State relativizedState = State.relativize(state);
      boolean stateHasBeenReachedThroughAQuickerPath = memo.contains(relativizedState);
      boolean noWayToImproveQuickestTimeFromState = state.getTime() + state.playerDistanceFromEnd() - minutes >= quickest;

      if (stateHasBeenReachedThroughAQuickerPath || noWayToImproveQuickestTimeFromState)
        continue;

      memo.add(relativizedState);

      State up = !isReverse ? new State(state).moveUp() : new State(state).moveDown();
      State down = !isReverse ? new State(state).moveDown() : new State(state).moveUp();
      State left = !isReverse ? new State(state).moveLeft() : new State(state).moveRight();
      State right = !isReverse ? new State(state).moveRight() : new State(state).moveLeft();
      State wait = new State(state).next();

      if (down.playerHasReachedEnd()) {
        quickest = Math.min(quickest, down.getTime() - minutes);
        continue;
      }

      if (wait.playerAtStart() || wait.isSafe()) stack.add(wait);
      if (left.playerInBounds() && left.isSafe()) stack.add(left);
      if (up.playerInBounds() && up.isSafe()) stack.add(up);
      if (right.playerInBounds() && right.isSafe()) stack.add(right);
      if (down.playerInBounds() && down.isSafe()) stack.add(down);
    }

    if (quickest > maxPossibleTime) throw new IllegalArgumentException("No possible route found");

    return quickest;
  }

  private static Blizzard getBlizzard(List<String> lines) {

    int height = lines.size();
    int width = lines.get(0).length();

    Blizzard blizzard = new Blizzard(width - 2, height - 2);

    for (int y = 1; y < height - 1; y++) {
      for (int x = 1; x < width - 1; x++) {
        char c = lines.get(y).charAt(x);

        switch (c) {
          case '>' -> blizzard.addRight(x - 1, y - 1);
          case '<' -> blizzard.addLeft(x - 1, y - 1);
          case '^' -> blizzard.addUp(x - 1, y - 1);
          case 'v' -> blizzard.addDown(x - 1, y - 1);
          case '.' -> blizzard.addOpen(x - 1, y - 1);
        }
      }
    }

    return blizzard;
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
