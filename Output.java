import java.util.EnumMap;

public class Output {
    public enum Color {
        RED,
        GREEN,
        YELLOW,
        BLUE,
        PURPLE,
        CYAN,
        WHITE,
        RESET;

        public static Color fromOrdinal(int n) {
            return values()[n];
        }
    }

    public static final EnumMap<Color, String> colors = new EnumMap<>(Color.class) {
        {

            // https://stackoverflow.com/questions/5762491/

            // Text Color
            put(Color.RED, "\033[0;91m");
            put(Color.GREEN, "\033[0;92m");
            put(Color.YELLOW, "\033[0;93m");
            put(Color.BLUE, "\033[0;94m");
            put(Color.PURPLE, "\033[0;95m");
            put(Color.CYAN, "\033[0;96m");
            put(Color.WHITE, "\033[0;97m");
            put(Color.RESET, "\033[0m");
        }
    };

    public static void print(String str, Color color) {
        String ansiValue = colors.get(color);
        String ansiReset = colors.get(Color.RESET);

        System.out.print(ansiValue + str + ansiReset);
    }

    public static void clearScreen() {
        // https: //stackoverflow.com/questions/2979383/how-to-clear-the-console

        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void clearLastLines(int n) {
        // https://stackoverflow.com/a/22083329/12172493
        for (int i = 0; i < n; i++) {
            System.out.print(String.format("\033[%dA", 1)); // Move up
            System.out.print("\033[2K"); // Erase line content
        }
    }

}
