package Menu;

import main.Main;
import main.PieceType;
import main.SoundManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class MenuPanel extends JPanel {
    MenuKeyHandler keyHandler = new MenuKeyHandler(this);
    SoundManager soundManager = new SoundManager(this);
    BufferedImage backgroundImg;

    private int buttonIndexX = 100000;
    private int buttonIndexY = 100000;
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
    private boolean showingShop = false;
    private boolean showingHelp = false;
    private boolean showingSettings = false;




    private void updateMenuState(){
        if (showingShop){
            shopMenu();
        } else if (showingSettings){
            settingsMenu();
        } else if (showingHelp){
            helpMenu();
        } else {
            mainMenu();
        }
    }


    private void shopMenu(){
        if (keyHandler.escapePressed){
            keyHandler.escapePressed = false;
            showingShop = false;
            soundManager.playClip(soundManager.buttonClickClip);
        }
    }

    private void settingsMenu(){
        if (keyHandler.escapePressed){
            keyHandler.escapePressed = false;
            showingSettings = false;
            soundManager.playClip(soundManager.buttonClickClip);
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
        if (keyHandler.enterPressed || keyHandler.spacePressed) {
            keyHandler.enterPressed = false;
            keyHandler.spacePressed = false;
            soundManager.playClip(soundManager.buttonClickClip);

            if (buttonIndexY % 3 == 0) {
                System.out.println("MusicSetting");
            } else if (buttonIndexY % 3 == 1) {
                System.out.println("LanguageSetting");
            } else if (buttonIndexY % 3 == 2) {
                System.out.println("DebugSetting");
            }
        }
        // Hover effect
        resetButtons();
        // Color buttons correctly
        if (buttonIndexY % 3 == 0) {
            hoveringMusicSettingButton = true;
        }
        if (buttonIndexY % 3 == 1) {
            hoveringLanguageSettingButton = true;
        }
        if (buttonIndexY % 3 == 2) {
            hoveringDebugSettingButton = true;
        }
    }

    private void helpMenu(){
        if (keyHandler.escapePressed){
            keyHandler.escapePressed = false;
            showingHelp = false;
            soundManager.playClip(soundManager.buttonClickClip);
        }
        if (keyHandler.enterPressed){
            keyHandler.enterPressed = false;
            currentHelpPage++;
            soundManager.playClip(soundManager.buttonClickClip);
        }
    }

    private void mainMenu(){
        // Pressing a key increments or decrements index

        if (keyHandler.goingRight){
            keyHandler.goingRight = false;
            buttonIndexX++;
            soundManager.playClip(soundManager.buttonHoverClip);
        } else if (keyHandler.goingLeft){
            keyHandler.goingLeft = false;
            buttonIndexX--;
            soundManager.playClip(soundManager.buttonHoverClip);
        }
        if (keyHandler.goingUp){
            keyHandler.goingUp = false;
            buttonIndexY--;
            soundManager.playClip(soundManager.buttonHoverClip);
        } else if (keyHandler.goingDown){
            keyHandler.goingDown = false;
            buttonIndexY++;
            soundManager.playClip(soundManager.buttonHoverClip);
        }

        // Enter performs action on the button
        if (keyHandler.enterPressed || keyHandler.spacePressed){
            keyHandler.enterPressed = false;
            keyHandler.spacePressed = false;
            soundManager.playClip(soundManager.buttonClickClip);

            if (buttonIndexY % 2 == 0) {
                if (buttonIndexX % 3 == 0) {
                    System.out.println("Shop");
                    showingShop = true;
                } else if (buttonIndexX % 3 == 1) {
                    System.out.println("Play");
                    soundManager.stopMusic();
                    Main.startMainGame(this, null);
                } else if (buttonIndexX % 3 == 2) {
                    System.out.println("Quit");
                    System.exit(0);
                }
            } else if (buttonIndexY % 2 == 1){
                if (buttonIndexX % 2 == 0) {
                    System.out.println("Settings");
                    showingSettings = true;
                } else if (buttonIndexX % 2 == 1) {
                    System.out.println("Help");
                    showingHelp = true;
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

        hoveringMusicSettingButton = false;
        hoveringLanguageSettingButton = false;
        hoveringDebugSettingButton = false;

    }

    private void loadImages() {
        try (InputStream is = getClass().getResourceAsStream("/BackGroundMenu.png")) {
            if (is == null) {
                throw new IOException("Image resource not found: /BackGroundMenu.png");
            }
            backgroundImg = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not load images");
        }
    }

    Font gameFont;
    Font gameFontSmall;
    Font gameFontTiny;
    private void loadFonts() {
        try {
            InputStream fontStream = getClass().getResourceAsStream("/PressStart2P.ttf");
            if (fontStream == null) {
                throw new IOException("Font file not found in resources.");
            }

            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            gameFont = baseFont.deriveFont(80f);
            gameFontSmall = baseFont.deriveFont(40f);
            gameFontTiny = baseFont.deriveFont(20f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            gameFont = new Font("Monospaced", Font.BOLD, 80); // fallback
            gameFontSmall = new Font("Monospaced", Font.PLAIN, 40); // fallback
        }
    }


    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;

        drawBackground(g2d);

        if (showingShop){
            drawShop(g2d);
        } else if (showingSettings){
            drawSettings(g2d);
        } else if (showingHelp){
            drawHelp(g2d);
        } else {
            drawMainMenu(g2d);
        }
    }

    private void drawBackground(Graphics2D g2d){
        //g2d.setColor(Color.DARK_GRAY);
        //g2d.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
        g2d.drawImage(backgroundImg, 0, 0, Main.WIDTH, Main.HEIGHT, this);
    }

    private boolean hoveringMusicSettingButton = false;
    private boolean hoveringLanguageSettingButton = false;
    private boolean hoveringDebugSettingButton = false;

    private String musicOnText = "Music on";
    private String musicOffText = "Music off";
    private String languageText = "Language English";
    private String debugText = "Debugmode off";

    private void drawShop(Graphics2D g2d) {
        // Background
        g2d.setColor(new Color(0,0,0,220));
        g2d.fillRect(100,100, Main.WIDTH - 200, Main.HEIGHT - 200);


        g2d.setFont(gameFont);
        g2d.setColor(Color.WHITE);
        drawText(g2d,0,230, "Shop");

        // Music button
        g2d.setFont(gameFontSmall);
        if(hoveringMusicSettingButton){
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.WHITE);
        }
        drawText(g2d,150,350, "Wall upgrade [PURCHASED]");
        g2d.setColor(Color.WHITE);
        g2d.setFont(gameFontTiny);
        drawText(g2d,155,400, "Purchase two turrets defending their rows with cannonballs");

        // Language button
        g2d.setFont(gameFontSmall);
        if(hoveringLanguageSettingButton){
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.WHITE);
        }
        drawText(g2d,150,500, "King upgrade [PURCHASED]");
        g2d.setColor(Color.WHITE);
        g2d.setFont(gameFontTiny);
        drawText(g2d,155,550, "Purchase two more guards for the king - allowing him to attack 3 rows at once");

        g2d.setFont(gameFontSmall);
        drawText(g2d,150,650, "Queen upgrade [PURCHASED]");
        g2d.setColor(Color.WHITE);
        g2d.setFont(gameFontTiny);
        drawText(g2d,155,700, "Purchase an ancient talisman for the queen - allowing her to heal when defeating enemies");
    }

    private void drawSettings(Graphics2D g2d){
        // Background
        g2d.setColor(new Color(0,0,0,220));
        g2d.fillRect(100,100, Main.WIDTH - 200, Main.HEIGHT - 200);


        g2d.setFont(gameFont);
        g2d.setColor(Color.WHITE);
        drawText(g2d,0,230, "Settings");

        g2d.setFont(gameFontSmall);
        // Music button
        if(hoveringMusicSettingButton){
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.WHITE);
        }
        drawText(g2d,0,400, musicOnText);

        // Language button
        if(hoveringLanguageSettingButton){
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.WHITE);
        }
        drawText(g2d,0,500, languageText);

        // Debugmode button
        if(hoveringDebugSettingButton){
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.WHITE);
        }
        drawText(g2d,0,600, debugText);
    }

    private int currentHelpPage = 0;
    private void drawHelp(Graphics2D g2d){
        // Background
        g2d.setColor(new Color(0,0,0,220));
        g2d.fillRect(100,100, Main.WIDTH - 200, Main.HEIGHT - 200);

        g2d.setFont(gameFont);
        g2d.setColor(Color.WHITE);
        drawText(g2d,0,230, "Help");

        if (currentHelpPage % 3 == 0){
            g2d.setFont(gameFontSmall);
            drawText(g2d, 155, 320, "Welcome to Chess Defense!");
            g2d.setColor(Color.WHITE);
            g2d.setFont(gameFontTiny);
            drawText(g2d,155,375, "The enemy Chess Pieces have started an assault on your castle and you must hold");
            drawText(g2d,155,425, "them off for as long as possible! Will you be strong enough?");
            g2d.setFont(gameFontSmall);
            drawText(g2d, 155, 540, "Controls:");
            g2d.setFont(gameFontTiny);
            drawText(g2d,155,595, "Use the WASD or Arrow-Keys to move your pieces. ");
            drawText(g2d,155,645, "Press SPACE to perform an attack. Each piece has it's own unique attack.");
            drawText(g2d,155,695, "Press ESC to open the Pause menu.");

            drawText(g2d,155,800, "Remember. Every few seconds your piece gets automatically switched.");
            drawText(g2d,155,845, "Be prepared.");

            drawText(g2d, 0, 950, "Press ENTER to view next page");
        } else if (currentHelpPage % 3 == 1){
            g2d.setFont(gameFontSmall);
            drawText(g2d, 155, 320, "Piece Overview:");

            drawText(g2d,155,405, "The Rook:");
            g2d.setFont(gameFontTiny);
            drawText(g2d,155,450, "Having mastered artillery - the rook has proven a reliable range option,");
            drawText(g2d,155,500, "capable of defeating his enemies from a safe distance.");
            g2d.setFont(gameFontSmall);
            drawText(g2d, 155, 600, "The Knight:");
            g2d.setFont(gameFontTiny);
            drawText(g2d,155,645, "Having lost his sword - the knight relies on his mighty stead to strike fear");
            drawText(g2d, 155, 695, "into his enemies.");

            g2d.setFont(gameFontSmall);
            drawText(g2d, 155, 790, "The Bishop:");
            g2d.setFont(gameFontTiny);
            drawText(g2d,155,835, "Having found strength in his faith - the Bishop is ready to take up arms and ");
            drawText(g2d, 155, 885, "fight for his faith.");

            drawText(g2d, 0, 950, "Press ENTER to view next page");
        } else {
            g2d.setFont(gameFontSmall);
            drawText(g2d, 155, 320, "The Queen:");
            g2d.setFont(gameFontTiny);
        }
        g2d.setFont(gameFontSmall);

    }

    private void drawMainMenu(Graphics2D g2d){
        g2d.setFont(gameFont);

        // Title background
        g2d.setColor(new Color(0, 0, 0, 220));
        g2d.fillRect(650, 15, Main.WIDTH - 1300, 215);

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

        // Play button
        if(hoveringPlay){
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.WHITE);
        }
        drawText(g2d,0,0, "Play");

        // Shop button
        if(hoveringShop){
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.WHITE);
        }
        drawText(g2d,300,0, "Shop");

        // Quit button
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