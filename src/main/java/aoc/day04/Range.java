package aoc.day04;

public record Range(int start, int end) {

  public boolean containsFully(Range o) {
    return this.start <= o.start && o.end <= this.end;
  }

  public boolean containsPartially(Range o) {
    return !(this.end < o.start || this.start > o.end);
  }

}
