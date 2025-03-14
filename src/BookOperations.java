import java.util.*;

public class BookOperations {
    private Scanner scanner;
    private BookManager bookManager;
    public BookOperations() {
        scanner = new Scanner(System.in);
        bookManager = new BookManager();
    }
    public void AddBook() {
        try {
            System.out.println("\n====== Add a new book ======");
            String title = BookUtil.getNonEmptyInput("Enter title: ");
            String author = BookUtil.getNonEmptyInput("Enter author: ");
            double price = BookUtil.getDoubleInput("Enter Price: $", 0.01);
            int stock = BookUtil.getIntInput("Enter quantity to add: ", 0, Integer.MAX_VALUE);

            String id = bookManager.addBook(title, price, author, stock);
            System.out.println("Book added successfully with ID: " + id);
        } catch (Exception e) {
            System.err.println("Error adding book: " + e.getMessage());
        }
    }

    public void ModifyBook() {
        try {
            System.out.println("\n====== Modify Book Details ======");
            String isbn =  BookUtil.getNonEmptyInput("Enter ISBN of the book you would like to modify: ");

            HashMap<String, Book> booksMap = bookManager.getAllBooks();
            if (!booksMap.containsKey(isbn)) {
                System.out.println("\u001B[31mBook with ISBN " + isbn + " not found.\u001B[0m");
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
                modOptions.put(4, "Cancel");

                System.out.println("\nWhat would you like to modify?");
                for (Map.Entry<Integer, String> option : modOptions.entrySet()) {
                    System.out.println(option.getKey() + ". " + option.getValue());
                }

                // Get user choice for modification type
                int choice = BookUtil.getIntInput("Enter your choice (1-4): ", 1, 4);

                if (choice == 4) {
                    System.out.println("Modification cancelled.");
                    break;
                }

                switch (choice) {
                    case 1:
                        String newTitle = BookUtil.getNonEmptyInput("Enter new title: ");
                        bookManager.updateBookTitle(isbn, newTitle);
                        break;
                    case 2:
                        String newAuthor = BookUtil.getNonEmptyInput("Enter new author: ");
                        bookManager.updateBookAuthor(isbn, newAuthor);
                        break;
                    case 3:
                        double newPrice = BookUtil.getDoubleInput("Enter new price: $", 0.01);
                        bookManager.updateBookPrice(isbn, newPrice);
                        break;
                }

                // After each modification, ask if the user wants to modify other stuff
                String response = BookUtil.getNonEmptyInput("\nDo you want to modify other details? (yes/no): ").toLowerCase();
                if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y")) {
                    modifyAnother = true;
                } else if (response.equalsIgnoreCase("no") || response.equalsIgnoreCase("n")) {
                    modifyAnother = false;
                } else {
                    System.out.println("Invalid option. Pls enter 'yes',or 'no'");
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

    public void DisplayBooks() {
        try {
            System.out.println("\n====== Display All Books ======");

            // Get all books from the bookManager
            HashMap<String, Book> booksMap = bookManager.getAllBooks();

            // Check if there are any books
            if (booksMap.isEmpty()) {
                System.out.println("No books available in the system.");
                return;
            }
            System.out.println("=====================================");
            System.out.println("              Book Details");
            System.out.println("=====================================");
            // Iterate over the HashMap and print each book's details
            for (Map.Entry<String, Book> entry : booksMap.entrySet()) {
                Book book = entry.getValue();
                BookManager.displayBook(book);
            }
        } catch (Exception e) {
            System.err.println("Error displaying books: " + e.getMessage());
        }
    }

    public void RemoveBook() {
        System.out.println("\n====== Remove Book ======");

        HashMap<String, Book> booksMap = bookManager.getAllBooks();
        if(booksMap.isEmpty()) {
            System.out.println("No books available.");
            return;
        }

        boolean continueRemoving = true;
        while(continueRemoving) {
            // Display available books with their ISBNs for reference
            System.out.println("Available books:");
            booksMap = bookManager.getAllBooks(); // Refresh the book list
            if(booksMap.isEmpty()) {
                System.out.println("No books available.");
                return;
            }

            for (String isbn : booksMap.keySet()) {
                Book book = booksMap.get(isbn);
                System.out.println("ISBN: " + isbn + " - Title: " + book.getTitle() + " - Stock: " + book.getStock());
            }

            // Get and validate the ISBN input
            String isbn = BookUtil.getNonEmptyInput("Enter the ISBN of the book to modify: ");

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

                int choice = BookUtil.getIntInput("Enter your choice (1-3): ", 1, 3);

                switch(choice) {
                    case 1: // Reduce stock
                        if(currentStock > 0) {
                            int reduceBy = BookUtil.getIntInput("Enter quantity to remove (1-" + currentStock + "): ", 1, currentStock);
                            book.setStock(currentStock - reduceBy);
                            System.out.println("Stock reduced. New stock level: " + book.getStock());
                        } else {
                            System.out.println("The book is already out of stock. Would you like to remove it completely?");
                            String response = BookUtil.getNonEmptyInput("Enter 'yes' to remove or 'no' to cancel: ").toLowerCase();
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
                        continueRemoving = false;
                        break;
                }
            } else {
                // ISBN doesn't exist
                System.out.println("Book with ISBN " + isbn + " not found.");

                if (!isbn.matches("\\d+")) {
                    System.out.println("\u001B[31m Note: The ISBN you entered contains non-numeric characters.");
                    System.out.println("ISBN values in this system are numeric. Please try again with a valid ISBN.\u001B[0m");
                }
            }

            // Ask if the user wants to continue removing/reducing books
            if(continueRemoving) {
                String response = BookUtil.getNonEmptyInput("Would you like to remove/reduce another book? (yes/no): ").toLowerCase();
                continueRemoving = response.equals("yes");
            }
        }
    }

    public void OutOfStock() {
        System.out.println("\n====== Out of Stock Books ======");
        try {
            HashMap<String, Book> booksMap = bookManager.getAllBooks();

            if(booksMap.isEmpty()) {
                System.out.println("No books available in the system.");
                return;
            }

            boolean foundOutOfStock = false;
            System.out.println("=====================================");
            System.out.println("              Book Details           ");
            System.out.println("=====================================");

            for(Map.Entry<String, Book> entry : booksMap.entrySet()) {
                String isbn = entry.getKey();
                Book book = entry.getValue();

                if(book.getStock() <= 0){
                    BookManager.displayBook(book);
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

    public void SearchBook() {
        System.out.println("\n====== Search Book ======");
        try {
            System.out.println("How would you like to search?");
            System.out.println("1. By ISBN");
            System.out.println("2. By Title");
            System.out.println("3. By Author");
            System.out.println("4. Cancel");

            int searchOption = BookUtil.getIntInput("Enter your choice (1-4): ", 1, 4);

            if (searchOption == 4) {
                System.out.println("Search cancelled.");
                return;
            }

            String searchTerm = BookUtil.getNonEmptyInput("Enter search term: ");
            HashMap<String, Book> booksMap = bookManager.getAllBooks();

            if (booksMap.isEmpty()) {
                System.out.println("No books available in the system.");
                return;
            }

            // Convert to a list and sort based on the selected search option
            List<Book> bookList = new ArrayList<>(booksMap.values());
            System.out.println("=====================================");
            System.out.println("              Book Details");
            System.out.println("=====================================");
            switch (searchOption) {
                case 1: // ISBN - Exact match binary search
                    List<String> isbnList = new ArrayList<>(booksMap.keySet());
                    Collections.sort(isbnList); // Ensure ISBNs are sorted
                    int isbnIndex = BookManager.binarySearchExactMatch(isbnList, searchTerm);
                    if (isbnIndex >= 0) {
                        BookManager.displayBook(booksMap.get(isbnList.get(isbnIndex)));
                    } else {
                        System.out.println("No book found with ISBN: " + searchTerm);
                    }
                    break;

                case 2: // Title - Partial match binary search
                    List<String> titleList = new ArrayList<>();
                    for (Book book : bookList) {
                        titleList.add(book.getTitle());
                    }
                    Collections.sort(titleList); // Sort titles
                    List<String> matchingTitles = BookManager.findMatchingStrings(titleList, searchTerm);
                    if (!matchingTitles.isEmpty()) {
                        for (String title : matchingTitles) {
                            for (Book book : bookList) {
                                if (book.getTitle().equals(title)) {
                                    BookManager.displayBook(book);
                                }
                            }
                        }
                    } else {
                        System.out.println("No book found with Title containing: " + searchTerm);
                    }
                    break;

                case 3: // Author - Partial match binary search
                    List<String> authorList = new ArrayList<>();
                    for (Book book : bookList) {
                        authorList.add(book.getAuthor());
                    }
                    Collections.sort(authorList); // Sort authors
                    List<String> matchingAuthors = BookManager.findMatchingStrings(authorList, searchTerm);
                    if (!matchingAuthors.isEmpty()) {
                        for (String author : matchingAuthors) {
                            System.out.println("=====================================");
                            for (Book book : bookList) {
                                if (book.getAuthor().equals(author)) {
                                    BookManager.displayBook(book);
                                }
                            }
                        }
                    } else {
                        System.out.println("No book found with Author containing: " + searchTerm);
                    }
                    break;
            }

        } catch (Exception e) {
            System.err.println("Error searching for books: " + e.getMessage());
        }
    }

    public void SortBooks() {
        System.out.println("\n====== Sort Books ======");
        try {
            HashMap<String, Book> booksMap = bookManager.getAllBooks();

            if (booksMap.isEmpty()) {
                System.out.println("No books available to sort.");
                return;
            }

            System.out.println("How would you like to sort the books?");
            System.out.println("1. By Title (A-Z)");
            System.out.println("2. By Author (A-Z)");
            System.out.println("3. Cancel");

            int sortOption = BookUtil.getIntInput("Enter your choice (1-3): ", 1, 3);

            if (sortOption == 3) {
                System.out.println("Sorting cancelled.");
                return;
            }

            // Convert HashMap to an ArrayList for sorting
            List<Book> bookList = new ArrayList<>(booksMap.values());

            // Perform sorting using Bubble Sort
            int n = bookList.size();
            for (int i = 0; i < n - 1; i++) {
                for (int j = 0; j < n - i - 1; j++) {
                    boolean swapCondition = false;

                    if (sortOption == 1) { // Sort by Title
                        swapCondition = bookList.get(j).getTitle().compareToIgnoreCase(bookList.get(j + 1).getTitle()) > 0;
                    } else if (sortOption == 2) { // Sort by Author
                        swapCondition = bookList.get(j).getAuthor().compareToIgnoreCase(bookList.get(j + 1).getAuthor()) > 0;
                    }

                    if (swapCondition) {
                        // Swap books
                        Book temp = bookList.get(j);
                        bookList.set(j, bookList.get(j + 1));
                        bookList.set(j + 1, temp);
                    }
                }
            }

            // Display sorted books
            System.out.println("\n====== Sorted Books ======");
            for (Book book : bookList) {
                BookManager.displayBook(book);
            }

        } catch (Exception e) {
            System.err.println("Error sorting books: " + e.getMessage());
        }
    }

}