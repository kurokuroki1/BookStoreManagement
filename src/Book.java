public class Book {
    private final String ISBN;
    private String title;
    private double price;
    private String author;
    private int stock;

    public Book(String ISBN, String title, double price, String author, int stock) {
        this.ISBN = ISBN;
        this.title = title;
        this.price = price;
        this.author = author;
        this.stock = stock;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getAuthor() {
        return author;
    }
    public String getTitle() {
        return title;
    }
    public double getPrice() {
        return price;
    }
    public int getStock() {
        return stock;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString(){
        return String.format("ISBN: %-10s | Title: %-30s | Author: %-20s | Price: $%.2f | Stock: %d",
                ISBN, title, author, price, stock);

    }
}
