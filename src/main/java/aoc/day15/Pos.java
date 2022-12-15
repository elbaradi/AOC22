package aoc.day15;

import java.util.Objects;

import static aoc.day15.Direction.DOWN;
import static aoc.day15.Direction.LEFT;
import static aoc.day15.Direction.RIGHT;
import static aoc.day15.Direction.UP;

public class Pos {
  private int x;
  private int y;

  Pos(int x, int y) {
    this.x = x;
    this.y = y;
  }
  Pos(String string) {
    String substring = string.substring(string.indexOf('x'));
    String[] coords = substring.split(", ");
    this.x = Integer.parseInt(coords[0].substring(2));
    this.y = Integer.parseInt(coords[1].substring(2));
  }

  Pos(Pos o) {
    this(o.x, o.y);
  }

  public int x() {
    return x;
  }

  public int y() {
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
