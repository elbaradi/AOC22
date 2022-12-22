package aoc.day22;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static aoc.day22.BlockType.AIR;
import static aoc.day22.BlockType.WALL;

class Solver {

  private static final String INPUT_PATHNAME_TEMPLATE = "src/resources/input_day_##.txt";
  private static final String DAY = "22";

  public static void main(String[] args) {

    List<String> lines = getRawInput();
    Map<Pos, BlockType> map = parseMap(lines);
    Set<Pos> blocks = new HashSet<>(map.keySet());
    List<Instruction> instructions = parseInstructions(lines);

    // Problem #1
    Pos start = findStartingPos(map);
    Player p = new Player(start);

    instructions.forEach(i -> p.followInstruction(i, map, blocks, false));

    Pos end = p.pos;

    System.out.println("Solution Problem #1: " + (1_000 * end.y() + 4 * end.x() + p.facing.ordinal()));

    // Problem #2
    Player p2 = new Player(start);

    instructions.forEach(i -> p2.followInstruction(i, map, blocks, true));

    end = p2.pos;

    System.out.println("Solution Problem #2: " + (1_000 * end.y() + 4 * end.x() + p2.facing.ordinal()));
  }

  private static Pos findStartingPos(Map<Pos, BlockType> map) {
    return map.entrySet().stream().filter(block -> block.getKey().y() == 1 && block.getValue().equals(AIR)).map(Map.Entry::getKey).min(
        Comparator.comparing(Pos::x)).get();
  }

  private static List<Instruction> parseInstructions(List<String> lines) {

    List<Instruction> instructions = new ArrayList<>();
    List<Integer> steps = new ArrayList<>();
    List<String> turns = new ArrayList<>();

    String instructionString = lines.get(lines.size() - 1);

    steps.addAll(Arrays.stream(instructionString.split("[LR]")).map(Integer::parseInt).toList());
    turns.addAll(Arrays.stream(instructionString.split("\\d+")).toList());
    turns.remove(0);

    for (int i = 0; i < turns.size(); i++) {
      instructions.add(new Instruction(steps.get(i), turns.get(i)));
    }

    instructions.add(new Instruction(steps.get(steps.size() - 1), ""));

    return instructions;
  }

  private static Map<Pos, BlockType> parseMap(List<String> lines) {

    Map<Pos, BlockType> map = new HashMap<>();

    int y = 0;
    for (String line : lines) {
      if (line.isEmpty())
        break;

      for (int x = 0; x < line.length(); x++) {
        if (line.charAt(x) == '.')
          map.put(new Pos(x + 1, y + 1), AIR);
        else if (line.charAt(x) == '#')
          map.put(new Pos(x + 1, y + 1), WALL);
      }
      y++;
    }

    return map;

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
