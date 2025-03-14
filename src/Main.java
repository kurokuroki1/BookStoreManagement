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
        boolean flag = true;

        System.out.println("Welcome to the Book Inventory System!");

        while (flag) {
            System.out.println("Press 1 to add a new book");
            System.out.println("Press 2 to modify the book");
            System.out.println("Press 3 to display all books");
            System.out.println("Press 4 to search for a book");
            System.out.println("Press 5 to sort the books");
            System.out.println("Press 6 to remove a book");
            System.out.println("Press 7 to list out of stock books");
            System.out.println("Press 8 to exit");

            try {
                // Read user input as a string
                System.out.println("Enter your choice: ");
                String inputStr = ui.scanner.nextLine().trim();
                if (!inputStr.matches("\\d+")) {
                    System.out.println("Invalid input. Please enter a number between 1-8 only.");
                    continue;
                }

                int option = Integer.parseInt(inputStr);
                // Check range
                if (option < 1 || option > 8) {
                    System.out.println("Please enter a number between 1-8 only.");
                    continue;
                }

                // Process valid option
                switch (option) {
                    case 1: ui.bookOperations.AddBook(); break;
                    case 2: ui.bookOperations.ModifyBook(); break;
                    case 3: ui.bookOperations.DisplayBooks(); break;
                    case 4: ui.bookOperations.SearchBook(); break;
                    case 5: ui.bookOperations.SortBooks(); break;
                    case 6: ui.bookOperations.RemoveBook(); break;
                    case 7: ui.bookOperations.OutOfStock(); break;
                    case 8: flag = false; System.out.println("Exiting... Thank you!"); break;
                }
            } catch (Exception e) {
                System.out.println("An error occurred while processing your request.");
                if (e.getMessage() != null) {
                    System.out.println("Details: " + e.getMessage());
                }
            }
        }
    }
}
