package aoc.day24;

import java.util.Objects;

import static aoc.day24.Direction.DOWN;
import static aoc.day24.Direction.LEFT;
import static aoc.day24.Direction.RIGHT;
import static aoc.day24.Direction.UP;
import static java.lang.Math.abs;

public class State {

  private final Pos player;
  private int minutes;
  private final Blizzard blizzard;
  private final Pos start;
  private final Pos end;

  State(Pos start, Pos end, int minutes, Blizzard blizzard) {
    player = new Pos(start);
    this.minutes = minutes;
    this.blizzard = blizzard;
    this.start = start;
    this.end = end;
  }

  State(State o) {
    player = new Pos(o.player);
    minutes = o.minutes;
    blizzard = o.blizzard;
    start = o.start;
    end = o.end;
  }

  boolean isSafe() {
    if (blizzard.grid[(player.y() + minutes) % blizzard.getHeight()][player.x()] == '^')
      return false;
    if (blizzard.grid[(((player.y() - minutes) % blizzard.getHeight()) + blizzard.getHeight()) % blizzard.getHeight()][player.x()] == 'v')
      return false;
    if (blizzard.grid[player.y()][(player.x() + minutes) % blizzard.getWidth()] == '<')
      return false;
    if (blizzard.grid[player.y()][(((player.x() - minutes) % blizzard.getWidth()) + blizzard.getWidth()) % blizzard.getWidth()] == '>')
      return false;
    return true;
  }

  State next() {
    minutes++;

    return this;
  }

  State moveUp() {
    player.add(UP);
    minutes++;

    return this;
  }

  State moveDown() {
    player.add(DOWN);
    minutes++;

    return this;
  }

  State moveLeft() {
    player.add(LEFT);
    minutes++;

    return this;
  }

  State moveRight() {
    player.add(RIGHT);
    minutes++;

    return this;
  }

  boolean playerInBounds() {
    return player.x() >= 0 && player.x() <= blizzard.getWidth() - 1 && player.y() >= 0 && player.y() <= blizzard.getHeight() - 1;
  }

  boolean playerAtStart() {
    return player.equals(start);
  }

  boolean playerHasReachedEnd() {
    return player.equals(end);
  }

  int playerDistanceFromEnd() { return abs(end.x() - player.x()) + abs(end.y() - player.y()); }

  int getTime() {
    return minutes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof State state)) {
      return false;
    }
    return minutes == state.minutes && player.equals(state.player);
  }

  @Override
  public int hashCode() {
    return Objects.hash(player, minutes);
  }

  static State relativize(State state) {
    State relativized = new State(state);

    relativized.minutes = state.minutes % state.blizzard.getLcm();

    return relativized;
  }

}
