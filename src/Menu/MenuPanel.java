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
    MenuKeyHandler keyHandler = new MenuKeyHandler(this);
    SoundManager soundManager = new SoundManager(this);
    BufferedImage backgroundImg;

    private int buttonIndexX = 0;
    private int buttonIndexY = 0;
    // Window size
    public MenuPanel() {
        setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(keyHandler);

        this.loadImages();
        this.loadFonts();
        soundManager.loadSounds();
        soundManager.startMenuMusic();

        // Refreshrate. Might have to improve that
        new Timer(16, e -> update()).start(); // ~60 FPS
    }

    private void update(){
        updateMenuState();
        repaint();
    }

    private boolean hoveringPlay = false;
    private boolean hoveringShop = false;
    private boolean hoveringQuit = false;
    private boolean hoveringSettings = false;
    private boolean hoveringHelp = false;
    private boolean shopMode = false;

    private void updateMenuState(){
        // Pressing a key increments or decrements index

        if (keyHandler.goingRight){
            keyHandler.goingRight = false;
            soundManager.playClip(soundManager.buttonHoverClip);
            buttonIndexX++;
        } else if (keyHandler.goingLeft){
            keyHandler.goingLeft = false;
            soundManager.playClip(soundManager.buttonHoverClip);
            buttonIndexX--;
        }
        if (keyHandler.goingUp){
            keyHandler.goingUp = false;
            soundManager.playClip(soundManager.buttonHoverClip);
            buttonIndexY--;
        } else if (keyHandler.goingDown){
            keyHandler.goingDown = false;
            soundManager.playClip(soundManager.buttonHoverClip);
            buttonIndexY++;
        }

        // Enter performs action on the button
        if (keyHandler.enterPressed){
            keyHandler.enterPressed = false;
            soundManager.playClip(soundManager.buttonClickClip);
            if (buttonIndexY % 2 == 0) {
                if (buttonIndexX % 3 == 0) {
                    System.out.println("Shop");
                    shopMode = true;
                } else if (buttonIndexX % 3 == 1) {
                    System.out.println("Play");
                    Main.startMainGame(this);
                } else if (buttonIndexX % 3 == 2) {
                    System.out.println("Quit");
                    System.exit(0);
                }
            } else if (buttonIndexY % 2 == 1){
                if (buttonIndexX % 2 == 0) {
                    System.out.println("Settings");
                } else if (buttonIndexX % 2 == 1) {
                    System.out.println("Help");
                }
            }
        }

        // Hover effect
        resetButtons(); // Resets hover effect
        // Color buttons correctly
        if (buttonIndexY % 2 == 0) {
            if (buttonIndexX % 3 == 0) {
                hoveringShop = true;
            }
            if (buttonIndexX % 3 == 1) {
                hoveringPlay = true;
            }
            if (buttonIndexX % 3 == 2) {
                hoveringQuit = true;
            }
        } else if (buttonIndexY % 2 == 1){
            if (buttonIndexX % 2 == 0) {
                hoveringSettings = true;
            }
            if (buttonIndexX % 2 == 1) {
                hoveringHelp = true;
            }
        }
    }

    private void resetButtons(){
        hoveringShop = false;
        hoveringPlay = false;
        hoveringQuit = false;
        hoveringSettings = false;
        hoveringHelp = false;
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
    Font gameFontSmall;
    private void loadFonts(){
        try {
            File fontFile = new File("res/PressStart2P.ttf");
            gameFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(80f);
            gameFontSmall = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(40f);
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

        // Title background
        g2d.setColor(new Color(0, 0, 0, 220));
        g2d.fillRect(650, 15, Main.WIDTH - 1300, 210);

        // Upper row background
        g2d.setColor(new Color(0, 0, 0, 230));
        g2d.fillRect(0, 450, Main.WIDTH, 180);

        // Lower row background
        g2d.setColor(new Color(0, 0, 0, 220));
        g2d.fillRect(0, 680, Main.WIDTH, 80);

        // Title
        g2d.setColor(Color.WHITE);
        drawText(g2d,0,120, "Chess");
        drawText(g2d,0,220, "Defense");

        if(hoveringPlay){
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.WHITE);
        }
        drawText(g2d,0,0, "Play");

        if(hoveringShop){
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.WHITE);
        }
        drawText(g2d,300,0, "Shop");

        if(hoveringQuit){
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.WHITE);
        }
        drawText(g2d,Main.WIDTH/2 + 400,0, "Quit");

        g2d.setFont(gameFontSmall);
        if(hoveringSettings){
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.WHITE);
        }
        drawText(g2d, 545, 745, "Settings");

        if(hoveringHelp){
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.WHITE);
        }
        drawText(g2d,Main.WIDTH/2 + 215,745, "Help");



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