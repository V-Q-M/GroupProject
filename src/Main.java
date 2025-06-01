import javax.swing.JFrame;

public class Main{

    public static void openWindow() {
        // Create a new JFrame (window)
        JFrame frame = new JFrame("My Swing Window");
        
        // Set default close operation so the app exits when window is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // Create your GamePanel instance
        GamePanel gamePanel = new GamePanel();

        frame.add(gamePanel);
        frame.pack();

        // Set the window size (width, height)
        frame.setSize(400, 300);
        
        // Optional: center the window on the screen
        frame.setLocationRelativeTo(null);
        
        // Make the window visible
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Open the window
        openWindow();
    }
}
