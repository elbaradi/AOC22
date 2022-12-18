package aoc.day18;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static aoc.day18.Direction.DOWN;
import static aoc.day18.Direction.IN;
import static aoc.day18.Direction.LEFT;
import static aoc.day18.Direction.OUT;
import static aoc.day18.Direction.RIGHT;
import static aoc.day18.Direction.UP;

class Solver {

  private static final String INPUT_PATHNAME_TEMPLATE = "src/resources/input_day_##.txt";
  private static final String DAY = "18";

  public static void main(String[] args) {

    List<String> lines = getRawInput();
    Set<Block> blocks = getBlocks(lines);

    // Problem #1
    int surfaceAreaByUnconnectedSides = blocks.stream().map(Block::getUnconnectedSides).reduce(0, Integer::sum);

    System.out.println("Solution Problem #1: " + surfaceAreaByUnconnectedSides);

    // Problem #2
    Set<Surface> exteriorSurfaces = findExteriorSurfaces(blocks);

    int exteriorSurfaceArea = exteriorSurfaces.size();

    System.out.println("Solution Problem #2: " + exteriorSurfaceArea);

  }

  private static Set<Surface> findExteriorSurfaces(Set<Block> blocks) {

    Set<Pos3D> obs = blocks.stream().map(Block::getPos).collect(Collectors.toSet());
    Set<Surface> exterior = new HashSet<>();
    Set<Pos3D> visited = new HashSet<>();
    Deque<Pos3D> visiting = new ArrayDeque<>();

    // Find search area
    long minX = obs.stream().mapToLong(Pos3D::x).min().getAsLong() - 1;
    long minY = obs.stream().mapToLong(Pos3D::y).min().getAsLong() - 1;
    long minZ = obs.stream().mapToLong(Pos3D::z).min().getAsLong() - 1;

    long maxX = obs.stream().mapToLong(Pos3D::x).max().getAsLong() + 1;
    long maxY = obs.stream().mapToLong(Pos3D::y).max().getAsLong() + 1;
    long maxZ = obs.stream().mapToLong(Pos3D::z).max().getAsLong() + 1;

    // Bottom-left-back corner of search area
    Pos3D start = new Pos3D(minX, minY, minZ);
    visiting.add(start);

    // Search
    while (!visiting.isEmpty()) {
      start = visiting.removeFirst();

      Pos3D up = Pos3D.add(start, UP);
      Pos3D down = Pos3D.add(start, DOWN);
      Pos3D left = Pos3D.add(start, LEFT);
      Pos3D right = Pos3D.add(start, RIGHT);
      Pos3D in = Pos3D.add(start, IN);
      Pos3D out = Pos3D.add(start, OUT);

      if (up.y() <= maxY) {
        if (obs.contains(up))
          exterior.add(new Surface(up, DOWN));
        else if (!visiting.contains(up))
          visiting.add(up);
      }

      if (down.y() >= minY) {
        if (obs.contains(down))
          exterior.add(new Surface(down, UP));
        else if (!visiting.contains(down))
          visiting.add(down);
      }

      if (right.x() <= maxX) {
        if (obs.contains(right))
          exterior.add(new Surface(right, LEFT));
        else if (!visiting.contains(right))
          visiting.add(right);
      }

      if (left.x() >= minX) {
        if (obs.contains(left))
          exterior.add(new Surface(left, RIGHT));
        else if (!visiting.contains(left))
          visiting.add(left);
      }

      if (out.z() <= maxZ) {
        if (obs.contains(out))
          exterior.add(new Surface(out, IN));
        else if (!visiting.contains(out))
          visiting.add(out);
      }

      if (in.z() >= minZ) {
        if (obs.contains(in))
          exterior.add(new Surface(in, OUT));
        else if (!visiting.contains(in))
          visiting.add(in);
      }

      visited.add(start);
      while (!visiting.isEmpty() && visited.contains(visiting.peekFirst()))
        visiting.removeFirst();

    }

    return exterior;
  }

  private static Set<Block> getBlocks(List<String> lines) {

    Set<Block> blocks = new HashSet<>();

    for (String line: lines)
      blocks.add(new Block(line));

    blocks.forEach(
        firstBlock -> blocks.stream()
            .filter(secondBlock ->
                        firstBlock.getPos().isTouching(secondBlock.getPos()))
            .forEach(firstBlock::addConnected)
    );

    return blocks;
  }

  private static List<String> getRawInput() {
    List<String> lines = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(getInputPathname()))) {
      reader.lines().forEach(lines::add);
    }
    catch (IOException e) {
      throw new RuntimeException("Retrieving input went wrong!");
    }
    return lines;
  }

  public static String getInputPathname() {
    return INPUT_PATHNAME_TEMPLATE.replace("##", DAY);
  }

}
