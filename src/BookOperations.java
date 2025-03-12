import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BookOperations {
    private Scanner scanner;
    private BookManager bookManager;
    public BookOperations() {
        scanner = new Scanner(System.in);
        bookManager = new BookManager();
    }
    public void handleAddBook() {
        try {
            System.out.println("\n====== Add a new book ======");
            String title = BookUtli.getNonEmptyInput("Enter title: ");
            String author = BookUtli.getNonEmptyInput("Enter author: ");
            double price = BookUtli.getDoubleInput("Enter Price: $", 0.01);
            int stock = BookUtli.getIntInput("Enter quantity to add: ", 0, Integer.MAX_VALUE);

            String id = bookManager.addBook(title, price, author, stock);
            System.out.println("Book added successfully with ID: " + id);
        } catch (Exception e) {
            System.err.println("Error adding book: " + e.getMessage());
        }
    }

    public void handleModifyBook() {
        try {
            System.out.println("\n====== Modify Book Details ======");
            System.out.println("Enter ISBN of the book you would like to modify: ");
            String isbn =  BookUtli.getNonEmptyInput("Enter ISBN of the book you would like to modify: ");

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
                int choice = BookUtli.getIntInput("Enter your choice (1-5): ", 1, 5);

                if (choice == 5) {
                    System.out.println("Modification cancelled.");
                    break; // Exit the loop if 'Cancel' is selected
                }

                switch (choice) {
                    case 1:
                        String newTitle = BookUtli.getNonEmptyInput("Enter new title: ");
                        bookManager.updateBookTitle(isbn, newTitle);
                        break;
                    case 2:
                        String newAuthor = BookUtli.getNonEmptyInput("Enter new author: ");
                        bookManager.updateBookAuthor(isbn, newAuthor);
                        break;
                    case 3:
                        double newPrice = BookUtli.getDoubleInput("Enter new price: $", 0.01);
                        bookManager.updateBookPrice(isbn, newPrice);
                        break;
                    case 4:
                        int newStock = BookUtli.getIntInput("Enter new stock quantity: ", 0, Integer.MAX_VALUE);
                        bookManager.updateBookStock(isbn, newStock);
                        break;
                }

                // After each modification, ask if the user wants to modify other stuff
                String response = BookUtli.getNonEmptyInput("\nDo you want to modify other details? (yes/no): ").toLowerCase();
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

    public void handleDisplayBooks() {
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

    public void handleRemoveBook() {
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
        String isbn = BookUtli.getNonEmptyInput("Enter the ISBN of the book to modify: ");

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

            int choice = BookUtli.getIntInput("Enter your choice (1-3): ", 1, 3);

            switch(choice) {
                case 1: // Reduce stock
                    if(currentStock > 0) {
                        int reduceBy = BookUtli.getIntInput("Enter quantity to remove (1-" + currentStock + "): ", 1, currentStock);
                        book.setStock(currentStock - reduceBy);
                        System.out.println("Stock reduced. New stock level: " + book.getStock());
                    } else {
                        System.out.println("The book is already out of stock. Would you like to remove it completely?");
                        String response = BookUtli.getNonEmptyInput("Enter 'yes' to remove or 'no' to cancel: ").toLowerCase();
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

    public void handleOutOfStock() {
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

    public void handleSearchBook() {
        System.out.println("\n====== Search Book ======");
        try {
            System.out.println("How would you like to search?");
            System.out.println("1. By ISBN");
            System.out.println("2. By Title");
            System.out.println("3. By Author");
            System.out.println("4. Cancel");

            int searchOption = BookUtli.getIntInput("Enter your choice (1-4): ", 1, 4);

            if (searchOption == 4) {
                System.out.println("Search cancelled.");
                return;
            }

            String searchTerm = BookUtli.getNonEmptyInput("Enter search term: ");
            HashMap<String, Book> booksMap = bookManager.getAllBooks();

            if (booksMap.isEmpty()) {
                System.out.println("No books available in the system.");
                return;
            }

            boolean found = false;
            System.out.println("\nSearch Results:");
            System.out.println("=====================================");

            // Implement case-sensitive search
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
    public void handleSortBooks() {
        System.out.println("\n====== Sort Books ======");
        try {
            HashMap<String, Book> booksMap = bookManager.getAllBooks();

            if (booksMap.isEmpty()) {
                System.out.println("No books available to sort.");
                return;
            }

            System.out.println("How would you like to sort the books?");
            System.out.println("1. By Title (A-Z)");
            System.out.println("2. Cancel");

            int sortOption = BookUtli.getIntInput("Enter your choice (1-4): ", 1, 4);


            // Convert HashMap to array for bubble sort
            Book[] booksArray = booksMap.values().toArray(new Book[0]);
            String[] isbnArray = booksMap.keySet().toArray(new String[0]);

            // Bubble sort implementation
            for (int i = 0; i < booksArray.length - 1; i++) {
                for (int j = 0; j < booksArray.length - i - 1; j++) {
                    boolean swapNeeded = false;

                    if (sortOption == 1) { // By Title A-Z
                        swapNeeded = booksArray[j].getTitle().compareTo(booksArray[j + 1].getTitle()) > 0;
                    } else if (sortOption == 2) {
                        System.out.println("Sorting cancelled.");
                        return;
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
