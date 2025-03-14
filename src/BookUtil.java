import java.util.Scanner;

public class BookUtil {
    static Scanner scanner = new Scanner(System.in);

    public static double getDoubleInput(String prompt, double min) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                double value = Double.parseDouble(input);
                if (value >= min && value <= Double.MAX_VALUE) return value;
                System.err.println("Value can't be under Zero ");
            } catch (NumberFormatException e) {
                System.err.println("Invalid input. Enter a valid number.");
            }
        }
    }

    public static int getIntInput(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                // Check if input contains any non-digit characters
                if (!input.matches("\\d+")) {
                    throw new NumberFormatException("Input contains non-digit characters");
                }
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) return value;
                System.err.println("Please enter a value between " + min + " and " + max);
            } catch (NumberFormatException e) {
                System.err.println("Invalid input. Enter a valid integer.");
            }
        }
    }

    public static String getNonEmptyInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.err.println("Input cannot be empty. Please try again.");
        }
    }
}
