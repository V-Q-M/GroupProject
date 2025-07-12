package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

import Allies.*;
import enemies.Enemy;
import entities.Projectile;

public class GamePanel extends JPanel{

  // Game variables
  private boolean DEBUG_MODE = false;
  public boolean gamePaused = false;
  public int score = 0;

  boolean gameStart = true;
  public boolean swapSoon = false;
  public int castleHealth = 100;
  public boolean gameOver = false;

  private Font gameFont;
  private Font gameFontTiny;

  // Textures of the player pieces
  public BufferedImage rookImage;
  public BufferedImage rookHurtImage;
  public BufferedImage knightImage;
  public BufferedImage knightHurtImage;
  public BufferedImage bishopImage;
  public BufferedImage bishopHurtImage;
  public BufferedImage kingImage;
  public BufferedImage kingHurtImage;
  public BufferedImage queenImage;
  public BufferedImage queenHurtImage;
  public BufferedImage pawnImage;
  public BufferedImage pawnHurtImage;
  // Enemy textures
  public BufferedImage enemyRookImage;
  public BufferedImage enemyRookHurtImage;
  public BufferedImage enemyKingImage;
  public BufferedImage enemyKingHurtImage;
  public BufferedImage enemyPawnImage;
  public BufferedImage enemyPawnHurtImage;

  // Projectile textures
  public BufferedImage cannonBallImage;
  public BufferedImage explosionImage;
  public BufferedImage queenParticleImageUp;
  public BufferedImage queenParticleImageDown;
  public BufferedImage queenParticleImageLeft;
  public BufferedImage queenParticleImageRight;
  public BufferedImage bishopParticleImageUpLeft;
  public BufferedImage bishopParticleImageUpRight;
  public BufferedImage bishopParticleImageDownLeft;
  public BufferedImage bishopParticleImageDownRight;
  public BufferedImage knightParticleImage;

  // text for localization
  private String startingText = "Starting in: ";
  private String swappingSoonText = "Swapping soon!";
  private String scoreText = "Score:";
  private String gameOverText = "Game Over!";
  private String quitGameText = "Quit Game?";
  private String restartText = "Restart?";
  private String resumeText = "Resume?";

  private boolean almostLost = false;

  // uses the enum
  public PieceType selectedPieceType;
  public int pieceWidth;
  public int pieceHeight;


  // Builds the background
  private BufferedImage tileImage;
  private BufferedImage bottomBarImage;

  public int PIECE_HEIGHT = 4 * 32;

  // Im scaling 32x32 Textures so that they look nicer
  public final int SCALE = 8;

  // carries particle effects
  public final List<Projectile> projectiles = new ArrayList<>();
  public final List<Projectile> effects = new ArrayList<>();
  // Carry enemy projectiles separate so that the player doesn't have to loop through all projectiles
  public final List<Projectile> enemyBalls = new ArrayList<>();
  // carries enemies
  public final List<Enemy>  enemies = new ArrayList<>();
  // carries wall and allies
  public final List<Ally> allies = new ArrayList<>();

  // Necessary managers
  KeyHandler keyHandler = new KeyHandler(this);
  CollisionHandler collisionHandler = new CollisionHandler(this);
  SoundManager soundManager = new SoundManager(this);

  // Start position at ca. center
  int startX = PIECE_HEIGHT*4;
  int startY = PIECE_HEIGHT*4;

  // Rest of managers and player
  Player player = new Player(this, keyHandler, soundManager, collisionHandler, startX, startY);
  EnemyManager enemyManager = new EnemyManager(this);
  public EntityManager entityManager = new EntityManager(this, keyHandler, soundManager, player);

  // Upgrades. Available in the shop
  private final boolean turretUpgradeUnlocked = true;
  private final boolean kingUpgradeUnlocked = true;
  private final boolean queenUpgradeUnlocked = false;

  public GamePanel() {
    // Window size
    setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));
    setFocusable(true);
    requestFocusInWindow();
    addKeyListener(keyHandler);

    // setup
    this.loadImages();
    this.loadFonts();
    soundManager.loadSounds();
    getSettings();

    // Builds the pawnwall on the left
    buildWall();

    player.selectPiece(PieceType.BISHOP);

    // Refreshrate. Might have to improve that
    new Timer(16, e -> update()).start(); // ~60 FPS
  }

  private void getSettings(){
    if (SettingsManager.musicOff){
      soundManager.stopMusic();
      System.out.println("MUSIC OFF");
    } else {
      soundManager.startMusic();
      System.out.println("MUSIC ON");
    }
    if (SettingsManager.languageGerman){
      startingText = "Starte in: ";
      swappingSoonText = "Wechsele bald!";
      scoreText = "Punkte:";
      gameOverText = "Verloren!";
      quitGameText = "Spiel verlassen?";
      restartText = "Neustarten?";
      resumeText = "Fortfahren?";
    }

    if (SettingsManager.debugMode){
      DEBUG_MODE = true;
    }
  }

  // The pawn wall on the left, including the two turrets /rooks
  private void buildWall(){
    for (int i = 0; i < 8; i++){
      allies.add(new AllyPawn(this, soundManager, collisionHandler, PIECE_HEIGHT, i * PIECE_HEIGHT, PIECE_HEIGHT, PIECE_HEIGHT, false));
    }
    if (turretUpgradeUnlocked){
      //int randomNum = (int) (Math.random()*7);
      allies.add(new AllyRook(this, soundManager, collisionHandler, 0, 0, PIECE_HEIGHT, PIECE_HEIGHT));
      allies.add(new AllyRook(this, soundManager, collisionHandler, 0, 7 * PIECE_HEIGHT, PIECE_HEIGHT, PIECE_HEIGHT));
    }
  }

  // Image loader. Very simple. Might expand to ImageAtlas
  private void loadImages() {
    try {
      tileImage = ImageIO.read(getClass().getResourceAsStream("/background/earth.png"));
      bottomBarImage = ImageIO.read(getClass().getResourceAsStream("/background/BottomBar.png"));

      rookImage = ImageIO.read(getClass().getResourceAsStream("/chess-pieces/white/rook.png"));
      rookHurtImage = ImageIO.read(getClass().getResourceAsStream("/chess-pieces/white/rook_hurt.png"));
      knightImage = ImageIO.read(getClass().getResourceAsStream("/chess-pieces/white/knight.png"));
      knightHurtImage = ImageIO.read(getClass().getResourceAsStream("/chess-pieces/white/knight_hurt.png"));
      bishopImage = ImageIO.read(getClass().getResourceAsStream("/chess-pieces/white/bishop.png"));
      bishopHurtImage = ImageIO.read(getClass().getResourceAsStream("/chess-pieces/white/bishop_hurt.png"));
      kingImage = ImageIO.read(getClass().getResourceAsStream("/chess-pieces/white/king.png"));
      kingHurtImage = ImageIO.read(getClass().getResourceAsStream("/chess-pieces/white/king_hurt.png"));
      queenImage = ImageIO.read(getClass().getResourceAsStream("/chess-pieces/white/queen.png"));
      queenHurtImage = ImageIO.read(getClass().getResourceAsStream("/chess-pieces/white/queen_hurt.png"));
      pawnImage = ImageIO.read(getClass().getResourceAsStream("/chess-pieces/white/pawn.png"));
      pawnHurtImage = ImageIO.read(getClass().getResourceAsStream("/chess-pieces/white/pawn_hurt.png"));

      enemyRookImage = ImageIO.read(getClass().getResourceAsStream("/chess-pieces/black/rook.png"));
      enemyRookHurtImage = ImageIO.read(getClass().getResourceAsStream("/chess-pieces/black/rook_hurt.png"));
      enemyKingImage = ImageIO.read(getClass().getResourceAsStream("/chess-pieces/black/king.png"));
      enemyKingHurtImage = ImageIO.read(getClass().getResourceAsStream("/chess-pieces/black/king_hurt.png"));
      enemyPawnImage = ImageIO.read(getClass().getResourceAsStream("/chess-pieces/black/pawn.png"));
      enemyPawnHurtImage = ImageIO.read(getClass().getResourceAsStream("/chess-pieces/black/pawn_hurt.png"));

      cannonBallImage = ImageIO.read(getClass().getResourceAsStream("/particles/cannonball2.png"));
      explosionImage = ImageIO.read(getClass().getResourceAsStream("/particles/explosion.png"));
      queenParticleImageUp = ImageIO.read(getClass().getResourceAsStream("/particles/queenParticlesUp.png"));
      queenParticleImageDown = ImageIO.read(getClass().getResourceAsStream("/particles/queenParticlesDown.png"));
      queenParticleImageLeft = ImageIO.read(getClass().getResourceAsStream("/particles/queenParticlesLeft.png"));
      queenParticleImageRight = ImageIO.read(getClass().getResourceAsStream("/particles/queenParticlesRight.png"));
      bishopParticleImageUpLeft = ImageIO.read(getClass().getResourceAsStream("/particles/bishopLanceUpLeft.png"));
      bishopParticleImageUpRight = ImageIO.read(getClass().getResourceAsStream("/particles/bishopLanceUpRight.png"));
      bishopParticleImageDownLeft = ImageIO.read(getClass().getResourceAsStream("/particles/bishopLanceDownLeft.png"));
      bishopParticleImageDownRight = ImageIO.read(getClass().getResourceAsStream("/particles/bishopLanceDownRight.png"));
      knightParticleImage = ImageIO.read(getClass().getResourceAsStream("/particles/knightParticles.png"));

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
      gameFont = baseFont.deriveFont(80f);
      gameFontTiny = baseFont.deriveFont(20f);
    } catch (FontFormatException | IOException e) {
      e.printStackTrace();
      gameFont = new Font("Monospaced", Font.BOLD, 80); // fallback
      gameFontTiny = new Font("Monospaced", Font.PLAIN, 20); // fallback
    }
  }


  // A simple bobbing animation
  int animationFrame = 1;
  int animationCounter = 0;
  public void simpleAnimation() {
    animationCounter++;

    if (animationCounter > 45) {
      animationCounter = 0;
    }

    if (animationCounter <= 15) {
      animationFrame = 1;
    } else if (animationCounter <= 30) {
      animationFrame = 2;
    } else {
      animationFrame = 3;
    }
  }

  public void update() {
    score+=1;

    if (castleHealth <= 20) {
      almostLost = true;
    } else {
      almostLost = false;
    }

    if (!gameOver && !gamePaused) {
      simpleAnimation();

      if (!player.isDead) {
        player.playerUpdate();
      }

      for (Projectile projectile : projectiles){
        projectile.update();
      }
      projectiles.removeIf(projectile -> projectile.isDead);

      // Effects are separate to avoid bugs
      for (Projectile effect : effects){
        effect.update();
      }
      effects.removeIf(effect -> effect.isDead);

      // Enemy projectiles are seperate to avoid looping through all projectiles for player
      for (Projectile enemyBall : enemyBalls){
        enemyBall.update();
      }
      enemyBalls.removeIf(enemyBall -> enemyBall.isDead);

      for (Enemy enemy : enemies) {
        enemy.update();
      }
      enemies.removeIf(enemy -> enemy.isDead);

      for (Ally ally : allies){
        ally.update();
      }
      allies.removeIf(ally -> ally.isDead);

      enemyManager.updateSpawner();
      gameUpdate();
    }
    if (keyHandler.escapePressed){
      keyHandler.escapePressed = false;
      gamePaused = !gamePaused;
    }
    repaint();
  }

  int gameStartCounter = 0;
  String startMessage = startingText + 3;
  private void startMessagePopUP(){
    if (gameStartCounter > 180) {
      gameStart = false;
      gameStartCounter = 0;
    } else if (gameStartCounter > 150) {
      startMessage = "";
      gameStartCounter++;
    } else if (gameStartCounter > 120) {
      startMessage = startingText + 1;
      gameStartCounter++;
    } else if (gameStartCounter > 90){
      startMessage = "";
      gameStartCounter++;
    } else if (gameStartCounter > 60){
      startMessage = startingText + 2;
      gameStartCounter++;
    } else if (gameStartCounter > 30){
      startMessage = "";
      gameStartCounter++;
    } else {
      gameStartCounter++;
    }
  }


  private void gameUpdate(){
    if (gameStart) {
      startMessagePopUP();
    }

    // Prepare for game over
    if (castleHealth <= 0){
      gameOver = true;
      castleHealth = 0;
      swapSoon = false;
      soundManager.stopMusic();
      repaint();
    }

  }

  // Carefull. Render method
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D)g;

    drawBackground(g2d);
    drawPlayer(g2d);
    drawAllies(g2d);
    drawEnemies(g2d);
    drawEntities(g2d);
    drawHealthBars(g2d);
    drawUI(g2d);
  }

  private void drawBackground(Graphics2D g2d){
    //  Draw tiled background, scaled 10x
    if (tileImage != null) {
      int sw = tileImage.getWidth() * SCALE;
      int sh = tileImage.getHeight() * SCALE;
      for (int y = 0; y < getHeight(); y += sh) {
        for (int x = 0; x < getWidth(); x += sw) {
          g2d.drawImage(tileImage, x, y, sw, sh, this);
        }
      }
    }
  }

  // Helper method for building health-bars
  private void createHealthBar(Graphics2D g2d, int x, int y, int width, int height, int health, int maxHealth){
    g2d.setColor(Color.red);
    g2d.fillRect(x, y - height * 2, width, height);
    int greenWidth= (int) (width * health / maxHealth);
    g2d.setColor(Color.green);
    g2d.fillRect(x, y - height * 2, greenWidth, height);
  }

  int animationOffset = 2;
  private void drawPlayer(Graphics2D g2d){
    // Draw selectedPiece at current position
    if (player.skin != null && !player.isDead) {
      if (animationFrame == 2){
        g2d.drawImage(player.skin, player.x - animationOffset, player.y + animationOffset, pieceWidth + animationOffset * 2, pieceHeight, this);
      } else if (animationFrame == 3){
        g2d.drawImage(player.skin, player.x + animationOffset, player.y - animationOffset, pieceWidth - animationOffset * 2, pieceHeight, this);
      } else {
        g2d.drawImage(player.skin, player.x, player.y, pieceWidth, pieceHeight, this);
      }
      // Draw hitbox
      if (DEBUG_MODE) {
        g2d.setColor(Color.red);
        g2d.drawRect(player.x , player.y, pieceWidth, pieceHeight);
      }
    }
  }

  private void drawAllies(Graphics2D g2d){
    for (Ally ally : allies) {
      if (animationFrame == 2){
        g2d.drawImage(ally.skin, ally.x - animationOffset, ally.y + animationOffset, ally.width + animationOffset * 2, ally.height, this);
      } else if (animationFrame == 3){
        g2d.drawImage(ally.skin, ally.x + animationOffset, ally.y - animationOffset, ally.width - animationOffset * 2, ally.height, this);
      } else {
        g2d.drawImage(ally.skin, ally.x, ally.y, ally.width, ally.height, this);
      }
      // Draw hitbox
      if (DEBUG_MODE){
        g2d.setColor(Color.red);
        g2d.drawRect(ally.x, ally.y, ally.width, ally.height);
      }
    }
  }

  private void drawEntities(Graphics2D g2d){
    for (Projectile projectile : projectiles) {
      g2d.drawImage(projectile.skin, projectile.x, projectile.y, projectile.width, projectile.height, this);
      if (DEBUG_MODE){
        g2d.drawRect(projectile.x, projectile.y, projectile.width, projectile.height);
      }
    }
    for (Projectile projectile : effects){
      g2d.drawImage(projectile.skin, projectile.x, projectile.y, projectile.width, projectile.height, this);
      if (DEBUG_MODE){
        g2d.drawRect(projectile.x, projectile.y, projectile.width, projectile.height);
      }
    }
    for (Projectile projectile : enemyBalls){
      g2d.drawImage(projectile.skin, projectile.x, projectile.y, projectile.width, projectile.height, this);
      if (DEBUG_MODE){
        g2d.drawRect(projectile.x, projectile.y, projectile.width, projectile.height);
      }
    }
  }

  private void drawEnemies(Graphics2D g2d){
    for (Enemy enemy : enemies) {
      if (animationFrame == 2){
        g2d.drawImage(enemy.skin, enemy.x - animationOffset, enemy.y - animationOffset, enemy.width + animationOffset * 2, enemy.height, this);
      } else if (animationFrame == 3){
        g2d.drawImage(enemy.skin, enemy.x + animationOffset, enemy.y + animationOffset, enemy.width - animationOffset * 2, enemy.height, this);
      } else {
        g2d.drawImage(enemy.skin, enemy.x, enemy.y, enemy.width, enemy.height, this);
      }
      // draw hitbox
      if (DEBUG_MODE){
        g2d.setColor(Color.red);
        g2d.drawRect(enemy.x, enemy.y, enemy.width, enemy.height);
      }
    }
  }

  // Renders the healthbars
  void drawHealthBars(Graphics2D g2d){
    // Personal choice - only show health-bar when not at full health
    for (Enemy enemy : enemies) {
      if (enemy.health != enemy.maxHealth) {
        createHealthBar(g2d, enemy.x, enemy.y, enemy.width, 15, enemy.health, enemy.maxHealth);
      }
    }

    for (Ally ally : allies){
      if (ally.health != ally.maxHealth){
        createHealthBar(g2d, ally.x, ally.y, ally.width, 15, ally.health, ally.maxHealth);
      }
    }

    int playerHealth = 100;
    int playerMaxHealth = 100;
    switch(selectedPieceType){
      case ROOK -> {
        playerHealth = player.rookHealth;
        playerMaxHealth = player.ROOK_BASE_HEALTH;
      }
      case KNIGHT -> {
        playerHealth = player.knightHealth;
        playerMaxHealth = player.KNIGHT_BASE_HEALTH;
      }
      case BISHOP -> {
        playerHealth = player.bishopHealth;
        playerMaxHealth = player.BISHOP_BASE_HEALTH;
      }
      case QUEEN -> {
        playerHealth = player.queenHealth;
        playerMaxHealth = player.QUEEN_BASE_HEALTH;
      }
      case KING -> {
        playerHealth = player.kingHealth;
        playerMaxHealth = player.KING_BASE_HEALTH;
      }
    }

    // player.Player health-bar always on top
    if (!player.isDead) {
      createHealthBar(g2d, player.x, player.y, pieceWidth, 20, playerHealth, playerMaxHealth);
    }

    // Castle healthbar
    createHealthBar(g2d, 350, 60, 1200, 20, castleHealth, 100);
  }


  private int pauseMenuIndex = 100000;
  void drawUI(Graphics2D g2d) {
    g2d.setFont(gameFont);

    drawBottomBar(g2d);
    drawAbilityBar(g2d);

    if (gameOver) {
      drawGameOverScreen(g2d);
    }

    if (gamePaused) {
      drawPauseMenu(g2d);
    }

    if (gameStart && !gamePaused) {
      g2d.setColor(Color.WHITE);
      drawText(g2d,0,0, gameFont, startMessage);
    }
    if (swapSoon && !gamePaused){
      g2d.setColor(Color.YELLOW);
      drawText(g2d,0,0, gameFont, swappingSoonText);
    }

    if (almostLost){
      g2d.setColor(new Color(200, 0 ,0, 40));
      g2d.fillRect(0,0, Main.WIDTH, Main.HEIGHT);
    }
  }

  private void drawBottomBar(Graphics2D g2d){
    g2d.drawImage(bottomBarImage,0, Main.HEIGHT - 56,  this);

    g2d.setColor(Color.WHITE);
    drawText(g2d, 10, Main.HEIGHT -12, gameFontTiny, scoreText + score);
  }

  private void drawGameOverScreen(Graphics2D g2d){
    g2d.setColor(new Color(0,0,0,200));
    g2d.fillRect(0,0,Main.WIDTH, Main.HEIGHT);

    g2d.setColor(Color.RED);
    drawText(g2d,0,420, gameFont, gameOverText);

    if(pauseMenuIndex % 2 == 0){
      g2d.setColor(Color.YELLOW);
    } else {
      g2d.setColor(Color.WHITE);
    }
    drawText(g2d,0,550, gameFont, quitGameText);
    if(pauseMenuIndex % 2 == 1){
      g2d.setColor(Color.YELLOW);
    } else {
      g2d.setColor(Color.WHITE);
    }
    drawText(g2d, 0, 680, gameFont, restartText);
    if(keyHandler.goingDown){
      keyHandler.goingDown = false;
      soundManager.playClip(soundManager.buttonHoverClip);
      pauseMenuIndex++;
    } else if (keyHandler.goingUp){
      keyHandler.goingUp = false;
      soundManager.playClip(soundManager.buttonHoverClip);
      pauseMenuIndex--;
    }
    if(keyHandler.enterPressed){
      keyHandler.enterPressed = false;
      if (pauseMenuIndex % 2 == 0){
        soundManager.playClip(soundManager.buttonClickClip);
        soundManager.stopMusic();
        Main.returnToMenu(this);
      } else if (pauseMenuIndex % 2 == 1){
        soundManager.playClip(soundManager.buttonClickClip);
        soundManager.stopMusic();
        Main.startMainGame(null, this);
      }
    }
  }

  private void drawPauseMenu(Graphics2D g2d){
    g2d.setColor(new Color(0,0,0,200));
    g2d.fillRect(0,0,Main.WIDTH, Main.HEIGHT);

    if(pauseMenuIndex % 2 == 0){
      g2d.setColor(Color.YELLOW);
    } else {
      g2d.setColor(Color.WHITE);
    }
    drawText(g2d,0,0, gameFont, quitGameText);
    if(pauseMenuIndex % 2 == 1){
      g2d.setColor(Color.YELLOW);
    } else {
      g2d.setColor(Color.WHITE);
    }
    drawText(g2d, 0, 700, gameFont, resumeText);
    if(keyHandler.goingDown){
      keyHandler.goingDown = false;
      soundManager.playClip(soundManager.buttonHoverClip);
      pauseMenuIndex++;
    } else if (keyHandler.goingUp){
      keyHandler.goingUp = false;
      soundManager.playClip(soundManager.buttonHoverClip);
      pauseMenuIndex--;
    }
    if(keyHandler.enterPressed){
      keyHandler.enterPressed = false;
      if (pauseMenuIndex % 2 == 0){
        soundManager.playClip(soundManager.buttonClickClip);
        soundManager.stopMusic();
        Main.returnToMenu(this);
      } else if (pauseMenuIndex % 2 == 1){
        soundManager.playClip(soundManager.buttonClickClip);
        gamePaused = false;
      }
    }
  }

  private void drawAbilityBar(Graphics2D g2d){
    // A bit of a dumb solution. If the piece is dead it gets drawn beneath the bar
    // making it appear greyed out. If its alive its drawn above the bar, looking active
    int xPos = 653;
    int yPos = Main.HEIGHT- 110;
    int size = 96;

    if (player.rookAlive){
      g2d.drawImage(rookImage, xPos, yPos, size, size, this);
    } else {
      g2d.setColor(Color.RED);
      g2d.fillRect(xPos, yPos, size, size);
    }
    if (player.knightAlive){
      g2d.drawImage(knightImage, xPos + 129, yPos, size, size, this);
    } else {
      g2d.setColor(Color.RED);
      g2d.fillRect(xPos + 129, yPos, size, size);
    }
    if (player.kingAlive){
      g2d.drawImage(kingImage, xPos + 259, yPos, size, size, this);
    } else {
      g2d.setColor(Color.RED);
      g2d.fillRect(xPos + 259, yPos, size, size);
    }
    if (player.queenAlive){
      g2d.drawImage(queenImage, xPos + 387, yPos, size, size, this);
    } else {
      g2d.setColor(Color.RED);
      g2d.fillRect(xPos + 387, yPos, size, size);
    }
    if (player.bishopAlive){
      g2d.drawImage(bishopImage, xPos + 516, yPos, size, size, this);
    } else {
      g2d.setColor(Color.RED);
      g2d.fillRect(xPos + 516, yPos , size, size);
    }
  }

  // Helper method for rendering formatted text
  private void drawText(Graphics2D g2d,int xOverride, int yOverride, Font gameFont, String text){
    g2d.setFont(gameFont);
    // Get font metrics for positioning
    FontMetrics fm = g2d.getFontMetrics();
    int textWidth = fm.stringWidth(text);
    int textHeight = fm.getHeight();

    int x = (getWidth() - textWidth) / 2;
    int y = (getHeight() - textHeight) / 2 + fm.getAscent(); // ascent = baseline offset

    if (xOverride != 0 && yOverride != 0){
      g2d.drawString(text, xOverride, yOverride);
    } else if (xOverride != 0){
      g2d.drawString(text, xOverride, y);
    } else if (yOverride != 0){
      g2d.drawString(text, x, yOverride);
    }
    else {
      g2d.drawString(text, x, y);
    }
  }
}
