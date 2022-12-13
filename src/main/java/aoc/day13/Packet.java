package aoc.day13;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

public class Packet implements Comparable<Packet> {
  private List<Packet> list;
  private Optional<Integer> integer;

  private Packet() {}

  public Packet(String str) {
    str = str.replace("[", "[,");
    str = str.replace("]",",]");
    String[] elements = str.split(",");
    Stack<Packet> stack = new Stack<>();
    this.list = new ArrayList<>();
    this.integer = Optional.empty();
    stack.push(this);

    for (String element : elements) {
      if (element.startsWith("[")) {
        // element is a list, create a new Packet with an empty list
        Packet packet = new Packet();
        packet.list = new ArrayList<>();
        packet.integer = Optional.empty();
        stack.push(packet);
      } else if (element.endsWith("]")) {
        // element is the closing bracket of a list, pop the top Packet from the stack
        Packet packet = stack.pop();
        // add the popped Packet to the list of the previous Packet
        stack.peek().list.add(packet);
      } else {
        // element is a number, create a new Packet with the integer value set to that number
        Packet packet = new Packet();
        packet.list = new ArrayList<>();
        if (element.isEmpty())
          packet.integer = Optional.empty();
        else
          packet.integer = Optional.of(Integer.parseInt(element));
        // add the Packet to the list of the previous Packet
        stack.peek().list.add(packet);
      }
    }
  }


  @Override
  public int compareTo(Packet o) {
    if (o.list.isEmpty() && o.integer.isEmpty()) {
      if (this.list.isEmpty() && this.integer.isEmpty()) {
        return 0;
      }
      return 1;
    }
    else if (this.list.isEmpty() && this.integer.isEmpty())
      return -1;
    else if (this.list.isEmpty() && !o.list.isEmpty())  // Mixed type
      return -o.compareTo(new Packet(String.format(integer.get().toString())));
    else if (!this.list.isEmpty() && o.list.isEmpty())  // Mixed type
      return this.compareTo(new Packet(String.format(o.integer.get().toString())));
    else if (this.list.isEmpty())
      return this.integer.get() - o.integer.get();
    else {
      int result;
      for (int i = 0; i < this.list.size(); i++) {
        if (i >= o.list.size())
          return 1;
        result = this.list.get(i).compareTo(o.list.get(i));
        if (result != 0)
          return result;
      }
      return this.list.size() - o.list.size();
    }
  }

}
