package aoc.day15;

import java.util.ArrayList;
import java.util.List;

public class Range {
  int start;
  int end;

  Range(int start, int end) {
    this.start = start;
    this.end = end;
  }

  public static List<Range> minus(Range one, Range two) {
    List<Range> result = new ArrayList<>();

    if (two.end < one.start || two.start > one.end)
      result.add(one);
    else {
      if (two.start > one.start)
        result.add(new Range(one.start, two.start - 1));
      if (two.end < one.end)
        result.add(new Range(two.end + 1, one.end));
    }

    return result;
  }

  public static List<Range> minus(List<Range> one, Range two) {
    List<Range> result = new ArrayList<>();

    one.forEach(
        range -> result.addAll(minus(range, two))
    );

    return result;
  }

}
