package aoc.day09;

import java.util.ArrayList;
import java.util.List;

import static aoc.day09.Directions.*;

public class Rope {

  private final Pos[] knots;
  private final int numberOfKnots;
  private final List<Pos> tailHistory = new ArrayList<>();

  Rope(Pos start, int knots) {
    numberOfKnots = knots;
    this.knots = new Pos[numberOfKnots];
    for (int i = 0; i < numberOfKnots; i++)
      this.knots[i] = new Pos(start);
    tailHistory.add(this.knots[numberOfKnots - 1]);
  }

  public List<Pos> getTailHistory() {
    return tailHistory;
  }

  public void moveRope(String direction, int steps) {
    switch (direction) {
      case "U" -> updateRope(UP, steps);
      case "D" -> updateRope(DOWN, steps);
      case "L" -> updateRope(LEFT, steps);
      case "R" -> updateRope(RIGHT, steps);
      default -> throw new IllegalArgumentException("Non-valid direction '" + direction + "'!");
    }
  }

  private void updateRope(Pos direction, int steps) {
    for (int i = 0; i < steps; i++) {
      knots[0].add(direction);
      for (int n = 1; n < numberOfKnots; n++) {
        knots[n].add(calcTrailingKnotMove(knots[n - 1], knots[n]));
      }
      tailHistory.add(new Pos(knots[numberOfKnots - 1]));
    }
  }

  private Pos calcTrailingKnotMove(Pos leading, Pos trailing) {
    Pos move = new Pos(0, 0);

    if (trailing.isTouching(leading))
      return move;

    if (leading.isAbove(trailing))
      move.add(UP);
    if (leading.isBelow(trailing))
      move.add(DOWN);
    if (leading.isLeftFrom(trailing))
      move.add(LEFT);
    if (leading.isRightFrom(trailing))
      move.add(RIGHT);

    return move;
  }

}
