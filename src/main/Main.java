package main;

import Menu.MenuPanel;

import javax.swing.JFrame;

public class Main{

    public static final int WIDTH = 1920;

    public static final int HEIGHT = 1080;

    public static void openWindow() {
        // Create a new JFrame (window)
        JFrame frame = new JFrame("ChessBrawl");
        
        // Set default close operation so the app exits when window is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Remove window decorations
        frame.setUndecorated(true);

        // Create main.MenuPanel instance
        //frame.add(new GamePanel());
        frame.add(new MenuPanel());

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
