package Menu;

import main.Main;
import main.PieceType;
import main.SoundManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MenuPanel extends JPanel {
    SoundManager soundManager = new SoundManager(this);
    BufferedImage backgroundImg;
    // Window size
    public MenuPanel() {
        setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));
        setFocusable(true);
        requestFocusInWindow();
        //addKeyListener(keyHandler);

        this.loadImages();
        this.loadFonts();
        soundManager.loadSounds();
        soundManager.startMenuMusic();

        // Refreshrate. Might have to improve that
        new Timer(16, e -> update()).start(); // ~60 FPS
    }

    private void update(){
        repaint();
    }

    private void loadImages() {
        try {
            backgroundImg =
                    ImageIO.read(new File("res/BackGroundMenu.png"));

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not load images");
        }
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
        //g2d.setColor(Color.DARK_GRAY);
        //g2d.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
        g2d.drawImage(backgroundImg, 0, 0, Main.WIDTH, Main.HEIGHT, this);
    }

    private void drawUI(Graphics2D g2d){
        g2d.setFont(gameFont);

        g2d.setColor(new Color(0, 0, 0, 220));
        g2d.fillRect(0, 450, Main.WIDTH, 180);

        g2d.setColor(new Color(0, 0, 0, 220));
        g2d.fillRect(650, 15, Main.WIDTH - 1300, 210);


        g2d.setColor(Color.WHITE);
        drawText(g2d,0,0, "Play");
        drawText(g2d,300,0, "Shop");
        drawText(g2d,Main.WIDTH/2 + 400,0, "Quit");
        drawText(g2d,0,120, "Chess");
        drawText(g2d,0,220, "Defense");


    }

    private void drawButton(Graphics2D g2d){

    }

    private void drawLabel(Graphics2D g2d){

    }


    void drawText(Graphics2D g2d, int x, int y, String text){
        // Get font metrics for positioning
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        int force_x = (getWidth() - textWidth) / 2;
        int force_y = (getHeight() - textHeight) / 2 + fm.getAscent(); // ascent = baseline offset
        if (x == 0 && y == 0){
            g2d.drawString(text, force_x, force_y);
        } else if (x == 0){
            g2d.drawString(text, force_x, y);
        } else if (y == 0){
            g2d.drawString(text, x, force_y);
        }
        else {
            g2d.drawString(text, x, y);
        }
    }
}