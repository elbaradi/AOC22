package aoc.day12;

public class Direction {
  public static final Pos DOWN = new Pos(0, 1);
  public static final Pos UP = new Pos(0, -1);
  public static final Pos RIGHT = new Pos(1, 0);
  public static final Pos LEFT = new Pos(-1, 0);

  private Direction() {}
}
