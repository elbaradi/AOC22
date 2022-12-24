package aoc.day24;

class Blizzard {

  private final int width;
  private final int height;
  private final int lcm;

  final Character[][] grid;

  Blizzard(int width, int height) {
    this.width = width;
    this.height = height;
    lcm = findLcm(width, height);

    grid = new Character[height][width];
  }

  private int findLcm(int width, int height) {

    int leastCommonMultiple = width * height;
    int biggerNumber = Math.max(width, height);

    for (int i = biggerNumber; i < leastCommonMultiple; i += biggerNumber) {
      if (i % width == 0 && i % height == 0) {
        leastCommonMultiple = i;
        break;
      }
    }

    return leastCommonMultiple;
  }

  void addUp(int x, int y) {
    grid[y][x] = '^';
  }

  void addDown(int x, int y) {
    grid[y][x] = 'v';
  }

  void addLeft(int x, int y) {
    grid[y][x] = '<';
  }

  void addRight(int x, int y) {
    grid[y][x] = '>';
  }

  void addOpen(int x, int y) {
    grid[y][x] = '.';
  }

  int getWidth() {
    return width;
  }

  int getHeight() {
    return height;
  }

  int getLcm() {
    return lcm;
  }

}
