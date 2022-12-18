package aoc.day18;

public class Direction {
  public static final Pos3D UP = new Pos3D(0, 1, 0);
  public static final Pos3D DOWN = new Pos3D(0, -1, 0);

  public static final Pos3D RIGHT = new Pos3D(1, 0, 0);
  public static final Pos3D LEFT = new Pos3D(-1, 0, 0);

  public static final Pos3D OUT = new Pos3D(0, 0, 1);
  public static final Pos3D IN = new Pos3D(0, 0, -1);


  private Direction() {}
}
