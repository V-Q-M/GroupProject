import javax.swing.JFrame;

public class Main{

    public static final int WIDTH = 1600;

    public static final int HEIGHT = 900;

    public static void openWindow() {
        // Create a new JFrame (window)
        JFrame frame = new JFrame("ChessBrawl");
        
        // Set default close operation so the app exits when window is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Remove window decorations
        frame.setUndecorated(true);

        // Create GamePanel instance
        GamePanel gamePanel = new GamePanel();

        frame.add(gamePanel);
        frame.pack();

        // Set the window size (width, height)
        frame.setSize(WIDTH, HEIGHT);
        
        frame.setLocationRelativeTo(null);
        
        // Make the window visible
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        // Open the window
        openWindow();
    }
}
