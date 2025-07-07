package Menu;

import main.Main;
import main.PieceType;
import main.SoundManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MenuPanel extends JPanel {
    SoundManager soundManager = new SoundManager(this);
    // Window size
    public MenuPanel() {
        setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));
        setFocusable(true);
        requestFocusInWindow();
        //addKeyListener(keyHandler);

        //this.loadImages();
        this.loadFonts();
        soundManager.loadSounds();
        //soundManager.startMenuMusic();

        // Refreshrate. Might have to improve that
        new Timer(16, e -> update()).start(); // ~60 FPS
    }

    private void update(){
        repaint();
    }

    Font gameFont;
    private void loadFonts(){
        try {
            File fontFile = new File("res/PressStart2P.ttf");
            gameFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(80f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            gameFont = new Font("Monospaced", Font.BOLD, 80); // fallback
        }
    }


    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;

        drawBackground(g2d);
        drawUI(g2d);
    }


    private void drawBackground(Graphics2D g2d){
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
    }

    private void drawUI(Graphics2D g2d){
        g2d.setFont(gameFont);
        g2d.setColor(Color.WHITE);
        drawText(g2d, "Test");

    }

    private void drawButton(Graphics2D g2d){

    }

    private void drawLabel(Graphics2D g2d){

    }


    void drawText(Graphics2D g2d, String text){
        // Get font metrics for positioning
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() - textHeight) / 2 + fm.getAscent(); // ascent = baseline offset

        g2d.drawString(text, x, y);
    }
}