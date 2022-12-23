package aoc.day23;

class Direction {

  public static final Pos S = new Pos(0, 1);
  public static final Pos N = new Pos(0, -1);
  public static final Pos E = new Pos(1, 0);
  public static final Pos W = new Pos(-1, 0);

  public static final Pos NW = Pos.add(N, W);
  public static final Pos NE = Pos.add(N, E);
  public static final Pos SW = Pos.add(S, W);
  public static final Pos SE = Pos.add(S, E);


  private Direction() {}
}
