package aoc.day18;

import java.util.Objects;

import static aoc.day18.Direction.DOWN;
import static aoc.day18.Direction.IN;
import static aoc.day18.Direction.LEFT;
import static aoc.day18.Direction.OUT;
import static aoc.day18.Direction.RIGHT;
import static aoc.day18.Direction.UP;
import static java.lang.Math.abs;

public class Pos3D {
  private long x;
  private long y;
  private long z;

  Pos3D(long x, long y, long z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  Pos3D(String string) {
    String[] coords = string.split(",");
    this.x = Long.parseLong(coords[0]);
    this.y = Long.parseLong(coords[1]);
    this.z = Long.parseLong(coords[2]);
  }

  Pos3D(Pos3D o) {
    this(o.x, o.y, o.z);
  }

  public long x() {
    return x;
  }

  public long y() {
    return y;
  }

  public long z() { return z; }

  public Pos3D add(Pos3D o) {
    this.x += o.x;
    this.y += o.y;
    this.z = o.z;

    return this;
  }

  public static Pos3D add(Pos3D one, Pos3D two) {
    return new Pos3D(one.x + two.x, one.y + two.y, one.z + two.z);
  }
  public static Pos3D subtract(Pos3D one, Pos3D two) {
    return new Pos3D(one.x - two.x, one.y - two.y, one.z - two.z);
  }
  public static Pos3D direction(Pos3D start, Pos3D end) {
    Pos3D vector = Pos3D.subtract(end, start);

    Pos3D dir = new Pos3D(0, 0, 0);
    if (vector.y > 0)
      dir.add(UP);
    if (vector.y < 0)
      dir.add(DOWN);
    if (vector.x > 0)
      dir.add(RIGHT);
    if (vector.x < 0)
      dir.add(LEFT);
    if (vector.z < 0)
      dir.add(IN);
    if (vector.z > 0)
      dir.add(OUT);

    return dir;
  }

  public boolean isTouching(Pos3D o) {
    Pos3D delta = new Pos3D(abs(this.x() - o.x()), abs(this.y() - o.y()), abs(this.z() - o.z()));
    if (delta.x() > 1 || delta.y() > 1 || delta.z() > 1)
      return false;

    return delta.x() + delta.y() + delta.z() == 1;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Pos3D pos = (Pos3D) o;
    return x == pos.x && y == pos.y && z == pos.z;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z);
  }

  @Override
  public String toString() {
    return "(" + x + "," + y + "," + z + ")";
  }

}
