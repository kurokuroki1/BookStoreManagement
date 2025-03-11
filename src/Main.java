public class Main {
    public static void main(String[] args) {
        BookUI ui = new BookUI();
        try {
            ui.start();
        } catch (Exception e) {
            System.err.println("Critical error: " + e.getMessage());
        }
        }
    }
