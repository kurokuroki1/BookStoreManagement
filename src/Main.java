import java.util.Scanner;

public class Main {
    private Scanner scanner;
    private BookOperations bookOperations;

    public Main() {
        scanner = new Scanner(System.in);
        this.bookOperations = new BookOperations();
    }

    public static void main(String[] args) {
        Main ui = new Main();
        ui.runProgram();
    }

    private void runProgram() {
        boolean flag = true;
        System.out.println("\033[1;32m╔══════════════════════════════════════════════════╗");
        System.out.println("║                                                  ║");
        System.out.println("║          BOOK INVENTORY MANAGEMENT SYSTEM        ║");
        System.out.println("║                                                  ║");
        System.out.println("╚══════════════════════════════════════════════════╝\033[0m");

        while (flag) {
            System.out.println("\033[1;34m┌────────── MAIN MENU ──────────────────────┐\033[0m");
            System.out.println("\033[1;36m│  1. \033[0m\033[1;37mAdd a New Book                        \033[1;36m│\033[0m");
            System.out.println("\033[1;36m│  2. \033[0m\033[1;37mModify Book Details                   \033[1;36m│\033[0m");
            System.out.println("\033[1;36m│  3. \033[0m\033[1;37mDisplay All Books                     \033[1;36m│\033[0m");
            System.out.println("\033[1;36m│  4. \033[0m\033[1;37mSearch for a Book                     \033[1;36m│\033[0m");
            System.out.println("\033[1;36m│  5. \033[0m\033[1;37mSort Books                            \033[1;36m│\033[0m");
            System.out.println("\033[1;36m│  6. \033[0m\033[1;37mRemove a Book                         \033[1;36m│\033[0m");
            System.out.println("\033[1;36m│  7. \033[0m\033[1;37mList Out of Stock Books               \033[1;36m│\033[0m");
            System.out.println("\033[1;36m│  8. \033[0m\033[1;37mExit System                           \033[1;36m│\033[0m");
            System.out.println("\033[1;34m└───────────────────────────────────────────┘\033[0m");

            try {
                // Read user input as a string
                System.out.print("Enter your choice: ");
                String inputStr = scanner.nextLine().trim();

                if (!inputStr.matches("\\d+")) {
                    throw new NumberFormatException("Invalid input. Please enter a number between 1-8 only.");
                }

                int option = Integer.parseInt(inputStr);

                // Check range
                if (option < 1 || option > 8) {
                    throw new IllegalArgumentException("Please enter a number between 1-8 only.");
                }

                // Process valid option
                switch (option) {
                    case 1: bookOperations.AddBook(); break;
                    case 2: bookOperations.ModifyBook(); break;
                    case 3: bookOperations.DisplayBooks(); break;
                    case 4: bookOperations.SearchBook(); break;
                    case 5: bookOperations.SortBooks(); break;
                    case 6: bookOperations.RemoveBook(); break;
                    case 7: bookOperations.OutOfStock(); break;
                    case 8: 
                        flag = false; 
                        System.out.println("Exiting... Thank you!"); 
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Detail: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred.");
                if (e.getMessage() != null) {
                    System.out.println("Details: " + e.getMessage());
                }
            }
        }
    }
}
