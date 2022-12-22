package aoc.day22;

import aoc.day22.Direction.Facing;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import static aoc.day22.BlockType.WALL;
import static aoc.day22.Direction.DOWN;
import static aoc.day22.Direction.Facing.EAST;
import static aoc.day22.Direction.Facing.NORTH;
import static aoc.day22.Direction.Facing.SOUTH;
import static aoc.day22.Direction.Facing.WEST;
import static aoc.day22.Direction.LEFT;
import static aoc.day22.Direction.RIGHT;
import static aoc.day22.Direction.UP;

class Player {
  Facing facing = EAST;
  Pos pos;

  Player(Pos start) {
    pos = new Pos(start);
  }

  void followInstruction(Instruction ins, Map<Pos, BlockType> map, Set<Pos> blocks, boolean mapIsCube) {
    switch (facing) {
      case NORTH -> walk(ins.steps(), UP, map, blocks, mapIsCube);
      case EAST -> walk(ins.steps(), RIGHT, map, blocks, mapIsCube);
      case SOUTH -> walk(ins.steps(), DOWN, map, blocks, mapIsCube);
      case WEST -> walk(ins.steps(), LEFT, map, blocks, mapIsCube);
    }

    if (ins.turn().isEmpty())
      return;

    boolean turnRight = ins.turn().equals("R");
    switch (facing) {
      case NORTH -> facing = turnRight ? EAST : WEST;
      case EAST -> facing = turnRight ? SOUTH : NORTH;
      case SOUTH -> facing = turnRight ? WEST : EAST;
      case WEST -> facing = turnRight ? NORTH : SOUTH;
    }

  }

  private void walk(int steps, Pos dir, Map<Pos, BlockType> map, Set<Pos> inBounds, boolean mapIsCube) {

    if (mapIsCube) {
      spaghettiWalk(steps, dir, map, inBounds);
      return;
    }

    Pos move = new Pos(pos);
    final boolean movesAlongYAxis = dir.x() == 0;
    final boolean movesToMax = dir.y() + dir.x() < 0;
    final long xPos = pos.x();
    final long yPos = pos.y();

    for (int i = 0; i < steps; i++) {
      Pos nextMove = Pos.add(move, dir);
      if (!inBounds.contains(nextMove)) {
        if (movesAlongYAxis) {
          if (movesToMax)
            nextMove = new Pos(xPos, inBounds.stream()
                .filter(e -> e.x() == xPos)
                .map(Pos::y)
                .max(Comparator.naturalOrder()).get());
          else
            nextMove = new Pos(xPos, inBounds.stream()
                .filter(e -> e.x() == xPos)
                .map(Pos::y)
                .min(Comparator.naturalOrder()).get());
        }
        else {
          if (movesToMax)
            nextMove = new Pos(inBounds.stream()
                                   .filter(e -> e.y() == yPos)
                                   .map(Pos::x)
                                   .max(Comparator.naturalOrder()).get(), yPos);
          else
            nextMove = new Pos(inBounds.stream()
                                   .filter(e -> e.y() == yPos)
                                   .map(Pos::x)
                                   .min(Comparator.naturalOrder()).get(), yPos);
        }
      }
      if (isWall(nextMove, map))
        break;
      else
        move = nextMove;
    }

    pos = move;
  }

  /*
  * [ ][1][2] -> [ ][1][ ]
  * [ ][3][ ] -> [4][3][2]
  * [4][5][ ] -> [ ][5][ ]
  * [6][ ][ ] -> [ ][6][ ]
  * */
  private void spaghettiWalk(int steps, Pos dir, Map<Pos, BlockType> map, Set<Pos> inBounds) {

    Pos move = new Pos(pos);

    for (int i = 0; i < steps; i++) {
      Pos nextMove = Pos.add(move, dir);
      if (!inBounds.contains(nextMove)) {
        if (section(move) == 2 && dir == DOWN) { // section 2, moving down to 3
          nextMove = new Pos(100, move.x() - 50);
          dir = LEFT;
        } else if (section(move) == 2 && dir == RIGHT) { // section 2, moving right to 5
          nextMove = new Pos(100, 151 - move.y());
          dir = LEFT;
        } else if (section(move) == 2 && dir == UP) { // section 2, moving up to 6
          nextMove = new Pos(move.x() - 100, 200);
          dir = UP;
        } else if (section(move) == 1 && dir == UP) { // section 1, moving up to 6
          nextMove = new Pos(1, move.x() + 100);
          dir = RIGHT;
        } else if (section(move) == 1 && dir == LEFT) { // section 1, moving left to 4
          nextMove = new Pos(1, 151 - move.y());
          dir = RIGHT;
        } else if (section(move) == 3 && dir == LEFT) { // section 3, moving left to 4
          nextMove = new Pos(move.y() - 50, 101);
          dir = DOWN;
        } else if (section(move) == 3 && dir == RIGHT) { // section 3, moving right to 2
          nextMove = new Pos(move.y() + 50, 50);
          dir = UP;
        } else if (section(move) == 4 && dir == UP) { // section 4, moving up to 3
          nextMove = new Pos(51, move.x() + 50);
          dir = RIGHT;
        } else if (section(move) == 4 && dir == LEFT) { // section 4, moving left to 1
          nextMove = new Pos(51, 151 - move.y());
          dir = RIGHT;
        } else if (section(move) == 5 && dir == RIGHT) { // section 5, moving right to 2
          nextMove = new Pos(150, 151 - move.y());
          dir = LEFT;
        } else if (section(move) == 5 && dir == DOWN) { // section 5, moving down to 6
          nextMove = new Pos(50, move.x() + 100);
          dir = LEFT;
        } else if (section(move) == 6 && dir == LEFT) { // section 6, moving left to 1
          nextMove = new Pos(move.y() - 100, 1);
          dir = DOWN;
        } else if (section(move) == 6 && dir == RIGHT) { // section 6, moving right to
          nextMove = new Pos(move.y() - 100, 150);
          dir = UP;
        } else if (section(move) == 6 && dir == DOWN) { // section 6, moving down to 2
          nextMove = new Pos(move.x() + 100, 1);
          dir = DOWN;
        }
      }
      if (isWall(nextMove, map)) {
        break;
      }
      else {
        move = nextMove;
        if (dir.equals(UP))
          facing = NORTH;
        else if (dir.equals(RIGHT))
          facing = EAST;
        else if (dir.equals(DOWN))
          facing = SOUTH;
        else if (dir.equals(LEFT))
          facing = WEST;
      }
    }

    pos = move;
  }

  private boolean isWall(Pos block, Map<Pos, BlockType> map) {
    return map.get(block).equals(WALL);
  }

  private int section(Pos move) {
    return switch ((int) ((int) ((move.x() - 1) / 50) + (3 * ((move.y() - 1) / 50)))) {
      case 1 -> 1;
      case 2 -> 2;
      case 4 -> 3;
      case 6 -> 4;
      case 7 -> 5;
      case 9 -> 6;
      default -> throw new IllegalArgumentException();
    };
  }

}
