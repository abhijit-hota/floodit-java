import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

/**
 * FloodIt
 */
public class FloodIt {

    private static int[][] grid;
    private static List<Entry<Integer, Integer>> flooded = new ArrayList<Entry<Integer, Integer>>() {
        {
            Map.entry(1, 1);
        }
    };
    private static int moves = 0;

    private static void printGrid() {
        for (int i = 1; i < grid.length - 1; i++) {
            for (int j = 1; j < grid.length - 1; j++) {
                int elem = grid[i][j];
                if (flooded.contains(Map.entry(i, j))) {
                    Output.print("███", Output.Color.fromOrdinal(elem));
                } else {
                    Output.print(" " + Integer.toString(elem) + " ", Output.Color.fromOrdinal(elem));
                }
            }
            System.out.println();
        }
    }

    private static int[][] createGrid(int size) {
        int[][] tempGrid = new int[size + 2][size + 2];

        for (int i = 0; i <= size + 1; i++) {
            for (int j = 0; j <= size + 1; j++) {
                if (i == 0 || i == size + 1 || j == 0 || j == size + 1) {
                    tempGrid[i][j] = -1;
                } else {
                    tempGrid[i][j] = (int) Math.floor(Math.random() * 6);
                }
            }
        }
        return tempGrid;
    }

    private static void traverse(int x, int y, int move) {
        for (int[] offset : new int[][] { { -1, 0 }, { 0, -1 }, { 0, 1 }, { 1, 0 } }) {
            int i = x + offset[0];
            int j = y + offset[1];
            if (grid[i][j] == move && !flooded.contains(Map.entry(i, j))) {
                flooded.add(Map.entry(i, j));
                traverse(i, j, move);
            }
        }
    }

    private static void playMove(int move) {

        for (Entry<Integer, Integer> coord : new ArrayList<>(flooded)) {
            int x = coord.getKey(), y = coord.getValue();
            // Change the flooded bit
            grid[x][y] = move;
            // And traverse it and its neighbours recursively
            traverse(x, y, move);
        }

    }

    private static void initialise(int n) {
        grid = createGrid(n);
        moves = 0;
        flooded.clear();
        flooded.add(Map.entry(1, 1));

        traverse(1, 1, grid[1][1]);
        Output.clearScreen();
    }

    private static int parseMoveFromInput(String str) {
        try {
            int nextMove = Integer.parseInt(str);
            if (nextMove > 6 || grid[1][1] == nextMove) {
                return -1;
            }
            return nextMove;
        } catch (Exception e) {
            return -1;
        }

    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        initialise(n);

        Scanner sc = new Scanner(System.in);

        while (true) {
            Output.clearScreen();
            printGrid();
            if (flooded.size() >= (n * n)) {
                break;
            }

            while (true) {
                System.out.println("Moves: " + moves + " | Flooded: " + flooded.size());
                System.out.print("Enter move: ");

                String str = sc.nextLine();
                int nextMove = parseMoveFromInput(str);

                if (nextMove != -1) {
                    moves++;
                    playMove(nextMove);
                    break;
                }
                Output.clearLastLines(2);
            }
        }
        // Output.clearLastLines(1);
        System.out.println();
        System.out.println("You won in " + Integer.toString(moves) + " move(s).");
        System.out.print("Enter 's' to start again or any other key to quit. ");
        String cmd = sc.nextLine();
        if (cmd.equals("s")) {
            main(args);
        }
        sc.reset();
        sc.close();
    }

}