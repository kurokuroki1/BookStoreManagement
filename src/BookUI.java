import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

    public class BookUI {
    private Scanner scanner;
    private BookManager bookManager;
    public BookUI() {
        scanner = new Scanner(System.in);
        bookManager = new BookManager();
    }

    public void start() {
        boolean flag = true;
        System.out.println("Welcome to the Book Management System!");


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
                String inputStr = scanner.nextLine().trim();
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
                    case 1: handleAddBook(); break;
                    case 2: handleModifyBook(); break;
                    case 3: handleDisplayBooks(); break;
                    case 4: handleSearchBook(); break;
                    case 5: handleSortBooks(); break;
                    case 6: handleRemoveBook(); break;
                    case 7: handleOutofStock(); break;
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
        private void handleAddBook() {
            try {
                System.out.println("\n====== Add a new book ======");
                String title = getNonEmptyInput("Enter title: ");
                String author = getNonEmptyInput("Enter author: ");
                double price = getDoubleInput("Enter Price: $", 0.01, Double.MAX_VALUE);
                int stock = getIntInput("Enter quantity to add: ", 0, Integer.MAX_VALUE);

                String id = bookManager.addBook(title, price, author, stock);
                System.out.println("Book added successfully with ID: " + id);
            } catch (Exception e) {
                System.err.println("Error adding book: " + e.getMessage());
            }
        }
        private double getDoubleInput(String prompt, double min, double max) {
            while (true) {
                System.out.print(prompt);
                try {
                    String input = scanner.nextLine().trim().replace(',', '.');
                    double value = Double.parseDouble(input);
                    if (value >= min && value <= max) return value;
                    System.err.println("Please enter a value between " + min + " and " + max);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid input. Enter a valid number.");
                }
            }
        }

        private int getIntInput(String prompt, int min, int max) {
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

        private String getNonEmptyInput(String prompt) {
            while (true) {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (!input.isEmpty()) return input;
                System.err.println("Input cannot be empty. Please try again.");
            }
        }

        private void handleModifyBook() {
            try {
                System.out.println("\n====== Modify Book Details ======");
                System.out.println("Enter ISBN of the book you would like to modify: ");
                String isbn = scanner.nextLine().trim();

                HashMap<String, Book> booksMap = bookManager.getAllBooks();
                if (!booksMap.containsKey(isbn)) {
                    System.out.println("Book with ISBN " + isbn + " not found.");
                    return;
                }

                Book book = booksMap.get(isbn);

                // Print book details before modification in a custom format
                System.out.println("\nCurrent Book Details:");
                System.out.println("ISBN: " + book.getID());
                System.out.println("Title: " + book.getTitle());
                System.out.println("Author: " + book.getAuthor());
                System.out.println("Price: $" + String.format("%.2f", book.getPrice()));
                System.out.println("Stock Quantity: " + book.getStock());

                boolean modifyAnother = true;  // Flag to check if the user wants to modify more details

                while (modifyAnother) {
                    // Create a HashMap for modification options
                    HashMap<Integer, String> modOptions = new HashMap<>();
                    modOptions.put(1, "Title");
                    modOptions.put(2, "Author");
                    modOptions.put(3, "Price");
                    modOptions.put(4, "Stock");
                    modOptions.put(5, "Cancel");

                    System.out.println("\nWhat would you like to modify?");
                    for (Map.Entry<Integer, String> option : modOptions.entrySet()) {
                        System.out.println(option.getKey() + ". " + option.getValue());
                    }

                    // Get user choice for modification type
                    int choice = getIntInput("Enter your choice (1-5): ", 1, 5);

                    if (choice == 5) {
                        System.out.println("Modification cancelled.");
                        break; // Exit the loop if 'Cancel' is selected
                    }

                    switch (choice) {
                        case 1:
                            String newTitle = getNonEmptyInput("Enter new title: ");
                            bookManager.updateBookTitle(isbn, newTitle);
                            break;
                        case 2:
                            String newAuthor = getNonEmptyInput("Enter new author: ");
                            bookManager.updateBookAuthor(isbn, newAuthor);
                            break;
                        case 3:
                            double newPrice = getDoubleInput("Enter new price: $", 0.01, Double.MAX_VALUE);
                            bookManager.updateBookPrice(isbn, newPrice);
                            break;
                        case 4:
                            int newStock = getIntInput("Enter new stock quantity: ", 0, Integer.MAX_VALUE);
                            bookManager.updateBookStock(isbn, newStock);
                            break;
                    }

                    // After each modification, ask if the user wants to modify other stuff
                    String response = getNonEmptyInput("\nDo you want to modify other details? (yes/no): ").toLowerCase();
                    if (response.equals("no")) {
                        modifyAnother = false;  // Exit the loop if the user says "no"
                    }
                }

                // Retrieve the updated book details
                Book updatedBook = bookManager.getBook(isbn);
                System.out.println("\nBook updated successfully:");
                System.out.println("ISBN: " + updatedBook.getID());
                System.out.println("Title: " + updatedBook.getTitle());
                System.out.println("Author: " + updatedBook.getAuthor());
                System.out.println("Price: $" + String.format("%.2f", updatedBook.getPrice()));
                System.out.println("Stock Quantity: " + updatedBook.getStock());

            } catch (Exception e) {
                System.err.println("Error modifying book: " + e.getMessage());
            }
        }

        private void handleDisplayBooks() {
            try {
                System.out.println("\n====== Display All Books ======");

                // Get all books from the bookManager
                HashMap<String, Book> booksMap = bookManager.getAllBooks();

                // Check if there are any books
                if (booksMap.isEmpty()) {
                    System.out.println("No books available.");
                    return;
                }

                // Iterate over the HashMap and print each book's details
                for (Map.Entry<String, Book> entry : booksMap.entrySet()) {
                    String isbn = entry.getKey();
                    Book book = entry.getValue();
                    System.out.println("ISBN: " + isbn);
                    System.out.println("Title: " + book.getTitle());
                    System.out.println("Author: " + book.getAuthor());
                    System.out.println("Price: $" + book.getPrice());
                    System.out.println("Stock: " + book.getStock());
                    System.out.println("=====================================");
                }
            } catch (Exception e) {
                System.err.println("Error displaying books: " + e.getMessage());
            }
        }

        private void handleRemoveBook() {
            System.out.println("\n====== Remove Book ======");

            HashMap<String, Book> booksMap = bookManager.getAllBooks();
            if(booksMap.isEmpty()) {
                System.out.println("No books available.");
                return;
            }

            // Display available books with their ISBNs for reference
            System.out.println("Available books:");
            for (String isbn : booksMap.keySet()) {
                Book book = booksMap.get(isbn);
                System.out.println("ISBN: " + isbn + " - Title: " + book.getTitle() + " - Stock: " + book.getStock());
            }

            // Get and validate the ISBN input
            String isbn = getNonEmptyInput("Enter the ISBN of the book to modify: ");

            // Check if the ISBN exists in the system
            if(bookManager.getAllBooks().containsKey(isbn)) {
                Book book = bookManager.getBook(isbn);
                int currentStock = book.getStock();

                System.out.println("Book found: " + book.getTitle() + " by " + book.getAuthor());
                System.out.println("Current stock: " + currentStock);

                System.out.println("\nWhat would you like to do?");
                System.out.println("1. Reduce stock");
                System.out.println("2. Remove book completely");
                System.out.println("3. Cancel");

                int choice = getIntInput("Enter your choice (1-3): ", 1, 3);

                switch(choice) {
                    case 1: // Reduce stock
                        if(currentStock > 0) {
                            int reduceBy = getIntInput("Enter quantity to remove (1-" + currentStock + "): ", 1, currentStock);
                            book.setStock(currentStock - reduceBy);
                            System.out.println("Stock reduced. New stock level: " + book.getStock());
                        } else {
                            System.out.println("The book is already out of stock. Would you like to remove it completely?");
                            String response = getNonEmptyInput("Enter 'yes' to remove or 'no' to cancel: ").toLowerCase();
                            if(response.equals("yes")) {
                                bookManager.removeBook(isbn);
                                System.out.println("Book with ISBN " + isbn + " has been completely removed.");
                            } else {
                                System.out.println("Operation cancelled.");
                            }
                        }
                        break;

                    case 2: // Remove completely
                        bookManager.removeBook(isbn);
                        System.out.println("Book with ISBN " + isbn + " has been completely removed.");
                        break;

                    case 3: // Cancel
                        System.out.println("Operation cancelled.");
                        break;
                }
            } else {
                // ISBN doesn't exist
                System.out.println("Book with ISBN " + isbn + " not found.");

                // If the input contains non-numeric characters, provide additional feedback
                if (!isbn.matches("\\d+")) {
                    System.out.println("\u001B[31m Note: The ISBN you entered contains non-numeric characters.");
                    System.out.println("ISBN values in this system are numeric. Please try again with a valid ISBN.\u001B[0m");
                }
            }
        }

        private void handleOutofStock() {
            System.out.println("\n====== Out of Stock Books ======");
            try {
                HashMap<String, Book> booksMap = bookManager.getAllBooks();

                if(booksMap.isEmpty()) {
                    System.out.println("No books available in the system.");
                    return;
                }

                boolean foundOutOfStock = false;
                System.out.println("Books that are out of stock:");

                for(Map.Entry<String, Book> entry : booksMap.entrySet()) {
                    String isbn = entry.getKey();
                    Book book = entry.getValue();

                    if(book.getStock() <= 0) {
                        System.out.println("ISBN: " + isbn);
                        System.out.println("Title: " + book.getTitle());
                        System.out.println("Author: " + book.getAuthor());
                        System.out.println("Price: $" + String.format("%.2f", book.getPrice()));
                        System.out.println("Stock: " + book.getStock());
                        System.out.println("-------------------------------------");
                        foundOutOfStock = true;
                    }
                }

                if(!foundOutOfStock) {
                    System.out.println("All books are currently in stock.");
                }
            } catch (Exception e) {
                System.err.println("Error checking out-of-stock books: " + e.getMessage());
            }
        }

        private void handleSearchBook() {
            System.out.println("\n====== Search Book ======");
            try {
                System.out.println("How would you like to search?");
                System.out.println("1. By ISBN");
                System.out.println("2. By Title");
                System.out.println("3. By Author");
                System.out.println("4. Cancel");

                int searchOption = getIntInput("Enter your choice (1-4): ", 1, 4);

                if (searchOption == 4) {
                    System.out.println("Search cancelled.");
                    return;
                }

                String searchTerm = getNonEmptyInput("Enter search term: ");
                HashMap<String, Book> booksMap = bookManager.getAllBooks();

                if (booksMap.isEmpty()) {
                    System.out.println("No books available in the system.");
                    return;
                }

                boolean found = false;
                System.out.println("\nSearch Results:");
                System.out.println("=====================================");

                // Implement case sensitive search
                for (Map.Entry<String, Book> entry : booksMap.entrySet()) {
                    String isbn = entry.getKey();
                    Book book = entry.getValue();
                    boolean matches = false;

                    switch (searchOption) {
                        case 1: // ISBN
                            matches = isbn.equals(searchTerm);
                            break;
                        case 2: // Title
                            matches = book.getTitle().contains(searchTerm);
                            break;
                        case 3: // Author
                            matches = book.getAuthor().contains(searchTerm);
                            break;
                    }

                    if (matches) {
                        System.out.println("ISBN: " + isbn);
                        System.out.println("Title: " + book.getTitle());
                        System.out.println("Author: " + book.getAuthor());
                        System.out.println("Price: $" + String.format("%.2f", book.getPrice()));
                        System.out.println("Stock: " + book.getStock());
                        System.out.println("-------------------------------------");
                        found = true;
                    }
                }

                if (!found) {
                    System.out.println("No books found matching your search term.");
                }

            } catch (Exception e) {
                System.err.println("Error searching for books: " + e.getMessage());
            }
        }
        private void handleSortBooks() {
            System.out.println("\n====== Sort Books ======");
            try {
                HashMap<String, Book> booksMap = bookManager.getAllBooks();

                if (booksMap.isEmpty()) {
                    System.out.println("No books available to sort.");
                    return;
                }

                System.out.println("How would you like to sort the books?");
                System.out.println("1. By Title (A-Z)");
                System.out.println("2. By Price (Low to High)");
                System.out.println("3. By ISBN");
                System.out.println("4. Cancel");

                int sortOption = getIntInput("Enter your choice (1-4): ", 1, 4);

                if (sortOption == 4) {
                    System.out.println("Sorting cancelled.");
                    return;
                }

                // Convert HashMap to array for bubble sort
                Book[] booksArray = booksMap.values().toArray(new Book[0]);
                String[] isbnArray = booksMap.keySet().toArray(new String[0]);

                // Bubble sort implementation
                for (int i = 0; i < booksArray.length - 1; i++) {
                    for (int j = 0; j < booksArray.length - i - 1; j++) {
                        boolean swapNeeded = false;

                        if (sortOption == 1) { // By Title A-Z
                            swapNeeded = booksArray[j].getTitle().compareTo(booksArray[j + 1].getTitle()) > 0;
                        } else if (sortOption == 2) { // By Price
                            swapNeeded = booksArray[j].getPrice() > booksArray[j + 1].getPrice();
                        } else if (sortOption == 3) {
                            int isbn1 = Integer.parseInt(isbnArray[j]);
                            int isbn2 = Integer.parseInt(isbnArray[j + 1]);
                            swapNeeded = isbn1 > isbn2;
                        }

                        if (swapNeeded) {
                            // Swap books
                            Book temp = booksArray[j];
                            booksArray[j] = booksArray[j + 1];
                            booksArray[j + 1] = temp;

                            // Swap ISBN
                            String tempIsbn = isbnArray[j];
                            isbnArray[j] = isbnArray[j + 1];
                            isbnArray[j + 1] = tempIsbn;
                        }
                    }
                }

                // Display sorted results
                System.out.println("\nSorted Book List:");
                System.out.println("=====================================");

                for (int i = 0; i < booksArray.length; i++) {
                    Book book = booksArray[i];
                    String isbn = isbnArray[i];

                    System.out.println("ISBN: " + isbn);
                    System.out.println("Title: " + book.getTitle());
                    System.out.println("Author: " + book.getAuthor());
                    System.out.println("Price: $" + String.format("%.2f", book.getPrice()));
                    System.out.println("Stock: " + book.getStock());
                    System.out.println("-------------------------------------");
                }

            } catch (Exception e) {
                System.err.println("Error sorting books: " + e.getMessage());
            }
        }
}
