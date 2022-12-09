package aoc.day09;

import java.util.ArrayList;
import java.util.List;

public class Rope {

  private final List<Pos> knots = new ArrayList<>();
  private final int nKnots;
  private final List<Pos> tailHistory = new ArrayList<>();

  Rope(Pos start, int knots) {
    nKnots = knots;
    for (int i = 0; i < knots; i++)
      this.knots.add(new Pos(start));
    tailHistory.add(this.knots.get(nKnots - 1));
  }

  public List<Pos> getTailHistory() {
    return tailHistory;
  }

  public void moveRope(Character direction, int steps) {
    switch (direction) {
      case 'U' -> updateRope(new Pos(0, -1), steps);
      case 'D' -> updateRope(new Pos(0, 1), steps);
      case 'L' -> updateRope(new Pos(-1, 0), steps);
      case 'R' -> updateRope(new Pos(1, 0), steps);
      default -> throw new IllegalArgumentException("Non-valid direction!" + direction);
    }
  }

  private void updateRope(Pos direction, int steps) {
    for (int i = 0; i < steps; i++) {
      knots.set(0, Pos.add(knots.get(0), direction));
      for (int n = 1; n < nKnots; n++) {
        knots.set(n, calculateTailMove(knots.get(n - 1), knots.get(n)));
      }
      tailHistory.add(knots.get(nKnots - 1));
    }
  }

  private Pos calculateTailMove(Pos head, Pos tail) {
    if (tail.isTouching(head)) {
      return tail;
    }
    else if (head.isAtLeastTwoSpacesAbove(tail))
      return Pos.add(head, new Pos(0, 1));
    else if (head.isAtLeastTwoSpacesBelow(tail))
      return Pos.add(head, new Pos(0, -1));
    else if (head.isAtLeastTwoSpacesLeftFrom(tail))
      return Pos.add(head, new Pos(1, 0));
    else if (head.isAtLeastTwoSpacesRightFrom(tail))
      return Pos.add(head, new Pos(-1, 0));
    else
      throw new IllegalArgumentException("Not possible!");
  }

}
