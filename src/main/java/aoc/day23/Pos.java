package aoc.day23;

import java.util.Objects;

import static aoc.day23.Direction.S;
import static aoc.day23.Direction.W;
import static aoc.day23.Direction.E;
import static aoc.day23.Direction.N;
import static java.lang.Math.abs;

class Pos {
  private long x;
  private long y;

  Pos(long x, long y) {
    this.x = x;
    this.y = y;
  }

  Pos(Pos o) {
    this(o.x, o.y);
  }

  public long x() {
    return x;
  }

  public long y() {
    return y;
  }

  public Pos add(Pos o) {
    this.x += o.x;
    this.y += o.y;

    return this;
  }

  public static Pos add(Pos one, Pos two) {
    return new Pos(one.x + two.x, one.y + two.y);
  }
  public static Pos subtract(Pos one, Pos two) {
    return new Pos(one.x - two.x, one.y - two.y);
  }
  public static Pos direction(Pos start, Pos end) {
    Pos vector = Pos.subtract(start, end);

    Pos dir = new Pos(0,0);
    if (vector.y > 0)
      dir.add(N);
    if (vector.y < 0)
      dir.add(S);
    if (vector.x > 0)
      dir.add(W);
    if (vector.x < 0)
      dir.add(E);

    return dir;
  }

  public boolean isAdjacent(Pos o) {
    return abs(this.x - o.x) <= 1 && abs(this.y - o.y) <= 1 && !this.equals(o);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Pos pos = (Pos) o;
    return x == pos.x && y == pos.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public String toString() {
    return "(" + x + "," + y + ")";
  }

}
