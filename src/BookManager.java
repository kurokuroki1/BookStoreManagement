import java.util.HashMap;


public class BookManager {
    private HashMap<String, Book> bookHashMap;
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
                    String existingISBN = existingBook.getID();
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

    public void updateBookStock(String isbn, int newStock) {
        Book book = bookHashMap.get(isbn);
        if (book != null) {
            book.setStock(newStock);
        }
    }
    public boolean removeBook(String isbn) {
        if (bookHashMap.containsKey(isbn)) {
            bookHashMap.remove(isbn);
            return true;
        }
        return false;
    }

}
