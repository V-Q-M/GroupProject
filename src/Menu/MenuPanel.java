package Menu;

import main.FileManager;
import main.Main;
import main.SettingsManager;
import main.SoundManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MenuPanel extends JPanel {
    MenuKeyHandler keyHandler = new MenuKeyHandler(this);
    SoundManager soundManager = new SoundManager(this);
    BufferedImage backgroundImg;

    Font gameFont;
    Font gameFontSmall;
    Font gameFontTiny;

    private int buttonIndexY = 100000;


    // Tracking the yellow hover effect
    private boolean hoveringPlay = false;
    private boolean hoveringShop = false;
    private boolean hoveringQuit = false;
    private boolean hoveringSettings = false;
    private boolean hoveringHelp = false;

    private boolean hoveringMusicSettingButton = false;
    private boolean hoveringLanguageSettingButton = false;
    private boolean hoveringDebugSettingButton = false;

    private boolean hoveringShopItemOne = false;
    private boolean hoveringShopItemTwo = false;
    private boolean hoveringShopItemThree = false;

    // If one of these is on, it shows that side menu
    private boolean showingShop = false;
    private boolean showingHelp = false;
    private boolean showingSettings = false;

    // Carries the text values of the main menu
    private String playText = "Play";
    private String shopText = "Shop";
    private String quitText = "Quit";
    private String settingsText = "Settings";
    private String helpText = "Help";
    // Same for side menus
    private String musicOnText = "Music on";
    private String musicOffText = "Music off";
    private String languageEnglishText = "Language English";
    private String languageGermanText = "Language German";
    private String debugOffText = "Hitboxes off";
    private String debugOnText = "Hitboxes on (developer mode)";

    // Window size
    public MenuPanel() {
        setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(keyHandler);

        this.loadImages();
        this.loadFonts();
        soundManager.loadSounds();
        //soundManager.startMenuMusic();
        initializeSettings();
        readSettings();

        // Refreshrate. Might have to improve that
        new Timer(16, e -> update()).start(); // ~60 FPS
    }

    private void initializeSettings() {
        Path tempFilePath = Paths.get(System.getProperty("java.io.tmpdir"), "settings.txt");

        try (BufferedReader reader = Files.newBufferedReader(tempFilePath)) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            // Use the settings if needed
            System.out.println("Loaded settings:");
            lines.forEach(System.out::println);

        } catch (IOException e) {
            // File not found or failed to read — create default settings
            String[] defaultSettings = { "music off", "language german", "debug on" };
            FileManager.writeLinesToTempFile(defaultSettings);

            System.out.println("Default settings written to temp file.");
        }
    }

    // Reads the settings from a txt file and overwrites the default values
    private void readSettings(){
        String[] line = FileManager.readLinesFromTempFile();
        if (line != null) {
            System.out.println(line[0]);
            if (line[0].equals("music off")) {
                soundManager.stopMusic();
                SettingsManager.musicOff = true;
            } else {
                soundManager.startMenuMusic();
                SettingsManager.musicOff = false;
            }
            if (line[1].equals("language german")) {
                SettingsManager.languageGerman = true;
                playText = "Start";
                shopText = "Shop";
                quitText = "Verlassen";
                settingsText = "Einstellungen";
                helpText = "Hilfe";

                musicOnText = "Musik an";
                musicOffText = "Musik aus";
                languageEnglishText = "Sprache Englisch";
                languageGermanText = "Sprache Deutsch";
                debugOffText = "Hitboxen aus";
                debugOnText = "Hitboxen an (Entwicklermodus)";
            } else {
                SettingsManager.languageGerman = false;
                playText = "Play";
                shopText = "Shop";
                quitText = "Quit";
                settingsText = "Settings";
                helpText = "Help";

                musicOnText = "Music on";
                musicOffText = "Music off";
                languageEnglishText = "Language English";
                languageGermanText = "Language German";
                debugOffText = "Hitboxes off";
                debugOnText = "Hitboxes on (developer mode)";
            }
            if (line[2].equals("debug on")) {
                SettingsManager.debugMode = true;
            } else {
                SettingsManager.debugMode = false;
            }
        }
    }




    private void update(){
        updateMenuState();
        repaint();
    }

    private void updateMenuState(){
        if (showingShop){
            updateShopMenu();
        } else if (showingSettings){
            updateSettingsMenu();
        } else if (showingHelp){
            updateHelpMenu();
        } else {
            updateMainMenu();
        }
    }

    private void updateShopMenu(){
        if (keyHandler.escapePressed){
            keyHandler.escapePressed = false;
            showingShop = false;
            soundManager.playClip(soundManager.buttonClickClip);
        }
        else if (keyHandler.goingUp){
            keyHandler.goingUp = false;
            buttonIndexY--;
            soundManager.playClip(soundManager.buttonHoverClip);
        } else if (keyHandler.goingDown){
            keyHandler.goingDown = false;
            buttonIndexY++;
            soundManager.playClip(soundManager.buttonHoverClip);
        }

        // Enter performs action on the button
        if (keyHandler.enterPressed || keyHandler.spacePressed) {
            keyHandler.enterPressed = false;
            keyHandler.spacePressed = false;
            soundManager.playClip(soundManager.buttonClickClip);

            if (buttonIndexY % 3 == 0) {
                System.out.println("Item01");
            } else if (buttonIndexY % 3 == 1) {
                System.out.println("Item02");

            } else if (buttonIndexY % 3 == 2) {
                System.out.println("Item03");
            }
        }
        // Hover effect
        resetButtons();
        // Color buttons correctly
        if (buttonIndexY % 3 == 0) {
            hoveringShopItemOne = true;
        }
        else if (buttonIndexY % 3 == 1) {
            hoveringShopItemTwo = true;
        }
        else if (buttonIndexY % 3 == 2) {
            hoveringShopItemThree = true;
        }
    }


    private void updateSettingsMenu(){
        if (keyHandler.escapePressed){
            keyHandler.escapePressed = false;
            showingSettings = false;
            soundManager.playClip(soundManager.buttonClickClip);
        }
        else if (keyHandler.goingUp){
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
            //String newSettings[] = {"music off", "language german", "debug on"};

            String newSettings[] = new String[3];

            if (buttonIndexY % 3 == 0) {
                System.out.println("MusicSetting");
                SettingsManager.musicOff = !SettingsManager.musicOff;
            } else if (buttonIndexY % 3 == 1) {
                System.out.println("LanguageSetting");
                SettingsManager.languageGerman = !SettingsManager.languageGerman;

            } else if (buttonIndexY % 3 == 2) {
                System.out.println("DebugSetting");
                SettingsManager.debugMode = !SettingsManager.debugMode;
            }

            SettingsManager.writeSettings();
            readSettings();
            System.out.println("APPLY SETTINGS");


        }
        // Hover effect
        resetButtons();
        // Color buttons correctly
        if (buttonIndexY % 3 == 0) {
            hoveringMusicSettingButton = true;
        }
        else if (buttonIndexY % 3 == 1) {
            hoveringLanguageSettingButton = true;
        }
        else if (buttonIndexY % 3 == 2) {
            hoveringDebugSettingButton = true;
        }
    }

    private void updateHelpMenu(){
        if (keyHandler.escapePressed){
            keyHandler.escapePressed = false;
            showingHelp = false;
            soundManager.playClip(soundManager.buttonClickClip);
        }
        else if (keyHandler.goingRight){
            keyHandler.goingRight = false;
            currentHelpPage++;
            soundManager.playClip(soundManager.buttonClickClip);
        } else if (keyHandler.goingLeft){
            keyHandler.goingLeft = false;
            currentHelpPage--;
            soundManager.playClip(soundManager.buttonClickClip);
        }
    }

    private void updateMainMenu(){
        // Pressing a key increments or decrements index

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

            if (buttonIndexY % 5 == 0) {
                System.out.println(playText);
                soundManager.stopMusic();
                Main.startMainGame(this, null);
            } else if (buttonIndexY % 5 == 1) {
                System.out.println(shopText);
                showingShop = true;
            } else if (buttonIndexY % 5 == 2) {
                System.out.println(settingsText);
                showingSettings = true;
            } else if (buttonIndexY % 5 == 3) {
                System.out.println(helpText);
                showingHelp = true;
            } else if (buttonIndexY % 5 == 4) {
                System.out.println(quitText);
                System.exit(0);
            }
        }

        // Hover effect
        resetButtons(); // Resets hover effect
        // Color buttons correctly
        if (buttonIndexY % 5 == 0) {
            hoveringPlay = true;
        } else if (buttonIndexY % 5 == 1) {
            hoveringShop = true;
        } else if (buttonIndexY % 5 == 2) {
            hoveringSettings = true;
        } else if (buttonIndexY % 5 == 3) {
            hoveringHelp = true;
        } else if (buttonIndexY % 5 == 4) {
            hoveringQuit = true;
        }
    }

    // Helper method that removes hovering effects
    private void resetButtons(){
        hoveringShop = false;
        hoveringPlay = false;
        hoveringQuit = false;
        hoveringSettings = false;
        hoveringHelp = false;

        hoveringMusicSettingButton = false;
        hoveringLanguageSettingButton = false;
        hoveringDebugSettingButton = false;

        hoveringShopItemOne = false;
        hoveringShopItemTwo = false;
        hoveringShopItemThree = false;

    }

    private void loadImages() {
        try (InputStream is = getClass().getResourceAsStream("/background/BackgroundMenu.png")) {
            if (is == null) {
                throw new IOException("Image resource not found: /background/BackGroundMenu.png");
            }
            backgroundImg = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not load images");
        }
    }

    private void loadFonts() {
        try {
            InputStream fontStream = getClass().getResourceAsStream("/fonts/PressStart2P.ttf");
            if (fontStream == null) {
                throw new IOException("Font file not found in resources.");
            }

            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            gameFont = baseFont.deriveFont(70f);
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
        g2d.drawImage(backgroundImg, 0, 0, Main.WIDTH, Main.HEIGHT, this);
    }

    private void drawShop(Graphics2D g2d) {
        // Background
        g2d.setColor(new Color(0,0,0,220));
        g2d.fillRect(100,100, Main.WIDTH - 200, Main.HEIGHT - 200);


        g2d.setFont(gameFont);
        g2d.setColor(Color.WHITE);
        drawText(g2d,0,230, "Shop");

        // Wall upgrade Button
        g2d.setFont(gameFontSmall);
        if(hoveringShopItemOne){
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.WHITE);
        }
        drawText(g2d,150,350, "Wall upgrade [PURCHASED]");
        g2d.setColor(Color.WHITE);
        g2d.setFont(gameFontTiny);
        drawText(g2d,155,400, "Purchase two turrets defending their rows with cannonballs");

        // King upgrade Button
        g2d.setFont(gameFontSmall);
        if(hoveringShopItemTwo){
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.WHITE);
        }
        drawText(g2d,150,500, "King upgrade [PURCHASED]");
        g2d.setColor(Color.WHITE);
        g2d.setFont(gameFontTiny);
        drawText(g2d,155,550, "Purchase two more guards for the king - allowing him to attack 3 rows at once");

        g2d.setFont(gameFontSmall);
        if (hoveringShopItemThree){
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.WHITE);
        }
        drawText(g2d,150,650, "Queen upgrade [LOCKED]");
        g2d.setColor(Color.WHITE);
        g2d.setFont(gameFontTiny);
        drawText(g2d,155,700, "Purchase an ancient talisman for the queen - allowing her to heal on enemies");
        drawText(g2d, 0, 950, "Press ESCAPE to return to the main menu");
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
        if(SettingsManager.musicOff){
            drawText(g2d,0,400, musicOffText);
        } else {
            drawText(g2d,0,400, musicOnText);
        }

        // Language button
        if(hoveringLanguageSettingButton){
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.WHITE);
        }
        if(SettingsManager.languageGerman){
            drawText(g2d,0,500, languageGermanText);
        } else {
            drawText(g2d,0,500, languageEnglishText);
        }
        // Debugmode button
        if(hoveringDebugSettingButton){
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.WHITE);
        }
        if(SettingsManager.debugMode){
            drawText(g2d,0,600, debugOnText);
        } else {
            drawText(g2d,0,600, debugOffText);
        }
        g2d.setColor(Color.WHITE);
        g2d.setFont(gameFontTiny);
        drawText(g2d, 0, 950, "Press ESCAPE to return to the main menu");
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

            drawText(g2d, 155, 950, "Press <- to view previous page");
            drawText(g2d, 1250, 950, "Press -> to view next page");

        } else if (currentHelpPage % 3 == 1){
            g2d.setFont(gameFontSmall);
            drawText(g2d, 155, 320, "Piece Overview:");

            drawText(g2d,155,405, "The Rook:");
            g2d.setFont(gameFontTiny);
            drawText(g2d,155,450, "Having mastered artillery - the rook has proven to be a reliable range option,");
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

            drawText(g2d, 155, 950, "Press <- to view previous page");
            drawText(g2d, 1250, 950, "Press -> to view next page");
        } else {
            g2d.setFont(gameFontSmall);
            drawText(g2d, 155, 320, "The Queen:");
            g2d.setFont(gameFontTiny);
            drawText(g2d, 155, 365, "Having been secretly trained in the art of sword fighting - the queen is as mobile");
            drawText(g2d, 155, 410, "as she is deadly, striking down those, who dare cross her way.");

            g2d.setFont(gameFontSmall);
            drawText(g2d, 155, 500, "The King:");
            g2d.setFont(gameFontTiny);
            drawText(g2d, 155, 545, "Having served his people for many years - the King utilizes his royal guard ");
            drawText(g2d, 155, 590, "to defeat those who dare attack him.");

            drawText(g2d, 0, 950, "Press ESCAPE to return to the main menu");
        }
        g2d.setFont(gameFontSmall);

    }

    int leftSpace = 50;
    private void drawMainMenu(Graphics2D g2d){
        g2d.setFont(gameFont);

        // Title
        g2d.setColor(Color.WHITE);
        drawText(g2d,0,180, "Chess");
        drawText(g2d,0,260, "Defense");

        // Play button
        if(hoveringPlay){
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.WHITE);
        }
        drawText(g2d,leftSpace,500, playText);

        g2d.setFont(gameFontSmall);
        // Shop button
        if(hoveringShop){
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.WHITE);
        }
        drawText(g2d,leftSpace,580, shopText);

        if(hoveringSettings){
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.WHITE);
        }
        drawText(g2d, leftSpace, 660, settingsText);

        if(hoveringHelp){
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.WHITE);
        }
        drawText(g2d,leftSpace,740, helpText);

        // Quit button
        if(hoveringQuit){
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.WHITE);
        }
        drawText(g2d,leftSpace,820, quitText);

    }

    // Helper method for drawing formatted text
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