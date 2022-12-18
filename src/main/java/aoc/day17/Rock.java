package aoc.day17;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static aoc.day17.Direction.DOWN;
import static aoc.day17.Direction.LEFT;
import static aoc.day17.Direction.RIGHT;
import static aoc.day17.Rock.Type.BAR;
import static aoc.day17.Rock.Type.CORNER;
import static aoc.day17.Rock.Type.DASH;
import static aoc.day17.Rock.Type.PLUS;
import static aoc.day17.Rock.Type.SQUARE;
import static aoc.day17.Solver.BOTTOM_LEFT;
import static aoc.day17.Solver.CHAMBER_WIDTH;

public class Rock {

  private final List<Pos> blocks = new ArrayList<>();

  public Rock(Pos spawn, Type type) {
    switch (type) {
      case DASH -> createDash(spawn);
      case PLUS -> createPlus(spawn);
      case CORNER -> createCorner(spawn);
      case BAR -> createBar(spawn);
      case SQUARE -> createSquare(spawn);
    }
  }

  enum Type {
    DASH,
    PLUS,
    CORNER,
    BAR,
    SQUARE
  }

  public void move(Pos move, Set<Pos> rocks) {
    if (canMove(move, rocks))
      for (Pos block : blocks)
        block.add(move);
  }

  public boolean hasLanded(Set<Pos> rocks) {
    return !canMove(DOWN, rocks) && rocks.addAll(blocks);
  }

  public long getHighest() {
    return blocks.get(3).y();
  }

  private boolean canMove(Pos move, Set<Pos> rocks) {
    if (blocks.get(0).x() == BOTTOM_LEFT.x() + 1 && move.equals(LEFT))
      return false;
    if (blocks.get(2).x() == BOTTOM_LEFT.x() + CHAMBER_WIDTH && move.equals(RIGHT))
      return false;
    if (blocks.get(1).y() == BOTTOM_LEFT.y() - 1 && move.equals(DOWN))
      return false;

    for (Pos block : blocks) {
      if (rocks.contains(Pos.add(block, move)))
        return false;
    }
    return true;
  }

  public static Rock.Type getRockType(int count) {
    return switch (count % 5) {
      case 0 -> DASH;
      case 1 -> PLUS;
      case 2 -> CORNER;
      case 3 -> BAR;
      case 4 -> SQUARE;
      default -> throw new IllegalStateException("Can't have negative count");
    };
  }

  public static Rock spawnRock(Pos spawn, Rock.Type type) {
    return new Rock(spawn, type);
  }

  private void createDash(Pos spawn) {
    this.blocks.addAll(
        List.of(
            new Pos(spawn.x(), spawn.y()),
            new Pos(spawn.x() + 1, spawn.y()),
            new Pos(spawn.x() + 3, spawn.y()),
            new Pos(spawn.x() + 2, spawn.y())
        )
    );
  }

  private void createPlus(Pos spawn) {
    this.blocks.addAll(
        List.of(
            new Pos(spawn.x(), spawn.y() - 1),
            new Pos(spawn.x() + 1, spawn.y()),
            new Pos(spawn.x() + 2, spawn.y() - 1),
            new Pos(spawn.x() + 1, spawn.y() - 2),
            new Pos(spawn.x() + 1, spawn.y() - 1)
        )
    );
  }

  private void createCorner(Pos spawn) {
    this.blocks.addAll(
        List.of(
            new Pos(spawn.x(), spawn.y()),
            new Pos(spawn.x() + 1, spawn.y()),
            new Pos(spawn.x() + 2, spawn.y()),
            new Pos(spawn.x() + 2, spawn.y() - 2),
            new Pos(spawn.x() + 2, spawn.y() - 1)
        )
    );
  }

  private void createBar(Pos spawn) {
    this.blocks.addAll(
        List.of(
            new Pos(spawn.x(), spawn.y() - 1),
            new Pos(spawn.x(), spawn.y()),
            new Pos(spawn.x(), spawn.y() - 2),
            new Pos(spawn.x(), spawn.y() - 3)
        )
    );
  }

  private void createSquare(Pos spawn) {
    this.blocks.addAll(
        List.of(
            new Pos(spawn.x(), spawn.y() - 1),
            new Pos(spawn.x(), spawn.y()),
            new Pos(spawn.x() + 1, spawn.y()),
            new Pos(spawn.x() + 1, spawn.y() - 1)
        )
    );
  }

}
