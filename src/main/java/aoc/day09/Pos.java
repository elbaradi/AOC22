package aoc.day09;

import java.util.Objects;

import static java.lang.Math.abs;

public class Pos {
  private int x;
  private int y;

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

  public Pos add(Pos other) {
    this.x += other.x;
    this.y += other.y;

    return this;
  }

  public boolean isTouching(Pos o) {
    return abs(this.x - o.x) <= 1 && abs(this.y - o.y) <= 1;
  }

  public boolean isAbove(Pos o) {
    return this.y - o.y <= -1;
  }

  public boolean isBelow(Pos o) {
    return this.y - o.y >= 1;
  }

  public boolean isLeftFrom(Pos o) {
    return this.x - o.x <= -1;
  }

  public boolean isRightFrom(Pos o) {
    return this.x - o.x >= 1;
  }

}
