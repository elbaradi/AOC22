package aoc.day18;

import java.util.HashSet;
import java.util.Set;

public class Block {

  private static final int SIDES = 6;

  private final Pos3D pos;
  private final Set<Block> connected = new HashSet<>();

  Block(String string) {
    pos = new Pos3D(string);
  }

  public Pos3D getPos() {
    return pos;
  }

  public void addConnected(Block o) {
    connected.add(o);
  }

  public int getUnconnectedSides() {
    return SIDES - connected.size();
  }

}
