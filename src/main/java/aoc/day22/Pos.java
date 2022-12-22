package aoc.day22;

import java.util.Objects;

import static aoc.day22.Direction.DOWN;
import static aoc.day22.Direction.LEFT;
import static aoc.day22.Direction.RIGHT;
import static aoc.day22.Direction.UP;

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
      dir.add(UP);
    if (vector.y < 0)
      dir.add(DOWN);
    if (vector.x > 0)
      dir.add(LEFT);
    if (vector.x < 0)
      dir.add(RIGHT);

    return dir;
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
