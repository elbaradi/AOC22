package aoc.day17;

import aoc.day17.Rock.Type;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static aoc.day17.Direction.DOWN;
import static aoc.day17.Direction.LEFT;
import static aoc.day17.Direction.RIGHT;
import static aoc.day17.Rock.getRockType;
import static aoc.day17.Rock.spawnRock;
import static aoc.day17.Solver.BOTTOM_LEFT;
import static aoc.day17.Solver.CHAMBER_WIDTH;
import static aoc.day17.Solver.ROCK_SPAWN_X;
import static aoc.day17.Solver.ROCK_SPAWN_Y_DELTA;
import static java.lang.Math.min;

public class State {

  private Type rockType;
  private Set<Pos> rocks = new HashSet<>();
  private long height;
  private int index;
  private int count;
  private final List<Pos> directions;
  private final int repeat;

  State(List<Pos> directions) {
    rocks.addAll(Set.of(new Pos(1, 0), new Pos(2, 0), new Pos(3, 0), new Pos(4, 0), new Pos(5, 0), new Pos(6, 0), new Pos(7, 0)));
    index = 0;
    count = 0;
    rockType = getRockType(count);
    this.directions = directions;
    repeat = this.directions.size();
  }

  public long getHeight() {
    return height;
  }

  public long getCount() {
    return count;
  }

  public Pos getNextMove() {
    Pos move = directions.get(index);
    this.index += 1;
    this.index = index % repeat;

    return move;
  }

  public void increaseCount() {
    this.count++;
    this.rockType = getRockType(this.count);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof State state)) {
      return false;
    }
    return index == state.index && rockType == state.rockType && relativeFloor().equals(state.relativeFloor());
  }

  @Override
  public int hashCode() {
    return Objects.hash(rockType, rocks, index);
  }

  public void updateFloor(Rock rock) {
    height = min(height, rock.getHighest());

    Set<Pos> reachable = new HashSet<>();
    Set<Pos> visited = new HashSet<>();
    Deque<Pos> visiting = new ArrayDeque<>();

    Pos start = new Pos(1, height - 1);
    visiting.add(start);

    while(!visiting.isEmpty()) {
      start = visiting.removeFirst();

      Pos down = Pos.add(start, DOWN);
      Pos left = Pos.add(start, LEFT);
      Pos right = Pos.add(start, RIGHT);

      if (rocks.contains(down))
        reachable.add(down);
      else if (!visiting.contains(down))
        visiting.add(down);

      if (left.x() != BOTTOM_LEFT.x()) {
        if (rocks.contains(left))
          reachable.add(left);
        else if (!visiting.contains(left))
          visiting.add(left);
      }

      if (right.x() != CHAMBER_WIDTH + 1) {
        if (rocks.contains(right))
          reachable.add(right);
        else if (!visiting.contains(right))
          visiting.add(right);
      }

      visited.add(start);
      while (!visiting.isEmpty() && visited.contains(visiting.peekFirst()))
        visiting.removeFirst();
    }

    rocks = reachable;
  }

  private Set<Pos> relativeFloor() {
    long baseline = getLowest();

    return rocks.stream().map(block -> block.add(new Pos(0, -baseline))).collect(Collectors.toSet());
  }

  private long getLowest() {
    return rocks.stream().map(Pos::y).max(Long::compareTo).get();
  }

  public State dropRock() {
    Rock rock = spawnRock(new Pos(ROCK_SPAWN_X, getHeight() - ROCK_SPAWN_Y_DELTA), this.rockType);
    while (true) {
      Pos move = getNextMove();
      if (move.equals(DOWN) && rock.hasLanded(rocks))
        break;
      else
        rock.move(move, rocks);
    }
    updateFloor(rock);
    increaseCount();

    return this;
  }

}
