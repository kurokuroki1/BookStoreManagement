import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BookManager {
    private final HashMap<String, Book> bookHashMap;
    private int nextBookID;

    public BookManager() {
        bookHashMap = new HashMap<>();
        nextBookID = 1;
    }
    public String generateBookID() {
        String isbn = String.format("%06d", nextBookID++);
        return isbn;
    }
    public String addBook(String title, double price, String author, int stock) {
        try {
            for (Book existingBook : bookHashMap.values()) {
                if (existingBook.getTitle().equalsIgnoreCase(title) &&
                        existingBook.getAuthor().equalsIgnoreCase(author) &&
                        existingBook.getPrice() == price) {

                    // Book already exists, just update its stock
                    String existingISBN = existingBook.getISBN();
                    int currentStock = existingBook.getStock();
                    existingBook.setStock(currentStock + stock);
                    return existingISBN;
                }
            }

            String ISBN = generateBookID();
            Book newbook = new Book(ISBN, title, price, author, stock);
            bookHashMap.put(ISBN, newbook);
            return ISBN;
        } catch (Exception e) {
            System.err.println("Error while adding new book " + e.getMessage());
            return null;
        }
    }
    public Book getBook(String isbn) {
        return bookHashMap.get(isbn);

    }
    public HashMap<String, Book> getAllBooks() {
        return new HashMap<>(bookHashMap);
    }

    public void updateBookTitle(String isbn, String newTitle) {
        Book book = bookHashMap.get(isbn);
        if (book != null) {
            book.setTitle(newTitle);
        }
    }

    public void updateBookAuthor(String isbn, String newAuthor) {
        Book book = bookHashMap.get(isbn);
        if (book != null) {
            book.setAuthor(newAuthor);
        }
    }

    public void updateBookPrice(String isbn, double newPrice) {
        Book book = bookHashMap.get(isbn);
        if (book != null) {
            book.setPrice(newPrice);
        }
    }
    public boolean removeBook(String isbn) {
        if (bookHashMap.containsKey(isbn)) {
            bookHashMap.remove(isbn);
            return true;
        }
        return false;
    }

    static int binarySearchExactMatch(List<String> sortedList, String target) {
        int left = 0;
        int right = sortedList.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            String midValue = sortedList.get(mid);

            int comparison = midValue.compareTo(target);

            if (comparison == 0) {
                return mid; // Found exact match
            } else if (comparison < 0) {
                left = mid + 1; // Look in right half
            } else {
                right = mid - 1; // Look in left half
            }
        }

        return -1; // Not found
    }

    // Find strings containing the search term, using binary search as a starting point
    public static List<String> findMatchingStrings(List<String> sortedList, String searchTerm) {
        List<String> matches = new ArrayList<>();


        // If the search term is alphabetically "less than" the first item, start from beginning
        if (sortedList.isEmpty() || searchTerm.compareToIgnoreCase(sortedList.get(0)) < 0) {
            // Start from the beginning
            for (String item : sortedList) {
                if (item.contains(searchTerm)) {
                    matches.add(item);
                }
            }
            return matches;
        }

        // If the search term is alphabetically "greater than" the last item, no matches
        if (searchTerm.compareTo(sortedList.get(sortedList.size() - 1)) > 0) {
            return matches; // Empty list
        }

        // Binary search to find a starting point
        int left = 0;
        int right = sortedList.size() - 1;
        int startIndex = 0;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            String midValue = sortedList.get(mid);

            if (midValue.startsWith(searchTerm)) {
                startIndex = mid;
                break;
            } else if (midValue.compareTo(searchTerm) < 0) {
                left = mid + 1;
                startIndex = left;
            } else {
                right = mid - 1;
            }
        }

        // From the starting point, check for matches
        for (int i = 0; i < sortedList.size(); i++) {
            String item = sortedList.get(i);
            if (item.contains(searchTerm)) {
                matches.add(item);
            }
        }

        return matches;
    }
    // Display book details
    static void displayBook(Book book) {
        System.out.printf("\033[1;34m│\033[0m \033[1;36m%-12s\033[0m : \033[1;37m%-26s\033[1;34m│\033[0m%n", "ISBN", book.getISBN());
        System.out.printf("\033[1;34m│\033[0m \033[1;36m%-12s\033[0m : \033[1;37m%-26s\033[1;34m│\033[0m%n", "Title", book.getTitle());
        System.out.printf("\033[1;34m│\033[0m \033[1;36m%-12s\033[0m : \033[1;37m%-26s\033[1;34m│\033[0m%n", "Author", book.getAuthor());
        System.out.printf("\033[1;34m│\033[0m \033[1;36m%-12s\033[0m : \033[1;32m$%-25.2f\033[1;34m│\033[0m%n", "Price", book.getPrice());
        System.out.printf("\033[1;34m│\033[0m \033[1;36m%-12s\033[0m : \033[1;37m%-26d\033[1;34m│\033[0m%n", "Stock", book.getStock());
        System.out.println("\033[1;34m└──────────────────────────────────────────┘\033[0m");

    }



}
