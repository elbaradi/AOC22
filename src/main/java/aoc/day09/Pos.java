package aoc.day09;

import java.util.Objects;

import static java.lang.Math.abs;

public class Pos {
  private final int x;
  private final int y;

  Pos(int x, int y) {
    this.x = x;
    this.y = y;
  }

  Pos(Pos o) {
    this.x = o.x;
    this.y = o.y;
  }

  @Override
  public String toString() {
    return "(" + x + "," + y + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    if (!(o instanceof Pos other))
      return false;
    return this.x == other.x && this.y == other.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);  // use Objects.hash() method to generate hash code
  }

  public static Pos add(Pos one, Pos two) {
    return new Pos(one.x + two.x, one.y + two.y);
  }

  public boolean isTouching(Pos o) {
    return abs(this.x - o.x) <= 1 && abs(this.y - o.y) <= 1;
  }

  public boolean isAtLeastTwoSpacesAbove(Pos o) {
    return this.y - o.y <= -2;
  }

  public boolean isAtLeastTwoSpacesBelow(Pos o) {
    return this.y - o.y >= 2;
  }

  public boolean isAtLeastTwoSpacesLeftFrom(Pos o) {
    return this.x - o.x <= -2;
  }

  public boolean isAtLeastTwoSpacesRightFrom(Pos o) {
    return this.x - o.x >= 2;
  }

}
