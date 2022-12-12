package aoc.day12;

public class Point {

  private final Pos pos;
  private final int steps;

  Point(Pos pos, int steps) {
    this.pos = pos;
    this.steps = steps;
  }

  public Pos getPos() {
    return pos;
  }

  public int getSteps() {
    return steps;
  }

}
