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

  // uses the enum
  public PieceType selectedPieceType;
  public int pieceWidth;
  public int pieceHeight;

  // Ability Cooldowns
  private final int ROOK_ABILITY_COOLDOWN = 60;
  private final int KNIGHT_ABILITY_COOLDOWN = 120;
  private final int QUEEN_ABILITY_COOLDOWN = 40;
  private final int KING_ABILITY_COOLDOWN = 240;

  // Initializes it
  public int abilityCoolDown = ROOK_ABILITY_COOLDOWN;

  // Builds the background
  private BufferedImage tileImage;

  // This will hold the actual player.Player piece
  private BufferedImage selectedPiece;
  public int PIECE_HEIGHT = 4 * 32;

  // Im scaling 32x32 Textures so that they look nicer
  final int SCALE = 8;

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
  private boolean turretUpgradeUnlocked = true;
  private boolean kingUpgradeUnlocked = true;
  private boolean queenUpgradeUnlocked = false;

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
    applySettings();

    // Builds the pawnwall on the left
    buildWall();

    // Default piece
    selectPiece(PieceType.BISHOP);

    // Refreshrate. Might have to improve that
    new Timer(16, e -> update()).start(); // ~60 FPS
  }

  // Reads the settings from a txt file and overwrites the default values
  private void applySettings(){
    String[] line = readLinesFromResource("settings.txt");
    System.out.println(line[0]);
    if (line[0].equals("music off")) {
      soundManager.stopMusic();
      System.out.println("STOP");
    } else {
      soundManager.startMusic();
      System.out.println("PLAY");
    }
    if (line[1].equals("language german")) {
      startingText = "Starte in: ";
      swappingSoonText = "Wechsele bald!";
      scoreText = "Punkte:";
      gameOverText = "Verloren!";
      quitGameText = "Spiel verlassen?";
      restartText = "Neustarten?";
      resumeText = "Fortfahren?";
    }
    if (line[2].equals("debug on")){
      DEBUG_MODE = true;
    }
  }

  // Helper method for reading files
  public String[] readLinesFromResource(String resourceName) {
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName);
         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

      List<String> lines = new ArrayList<>();
      String line;

      while ((line = reader.readLine()) != null) {
        lines.add(line);
      }

      return lines.toArray(new String[0]);

    } catch (IOException | NullPointerException e) {
      JOptionPane.showMessageDialog(this, "Failed to read resource: " + resourceName);
      e.printStackTrace();
      return null;
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

  public void selectPiece(PieceType changePiece) {
    selectedPieceType = changePiece;
    player.swapCounter = 0;
    soundManager.playClip(soundManager.swapClip);
    swapSoon = false;
    switch (changePiece) {
      case PieceType.ROOK -> {
        selectedPiece = rookImage;
        abilityCoolDown = ROOK_ABILITY_COOLDOWN;
      }
      case PieceType.QUEEN -> {
        selectedPiece = queenImage;
        abilityCoolDown = QUEEN_ABILITY_COOLDOWN;
      }
      case PieceType.KING -> {
        selectedPiece = kingImage;
        abilityCoolDown = KING_ABILITY_COOLDOWN;
      }
      case PieceType.KNIGHT -> {
        selectedPiece = knightImage;
        abilityCoolDown = KNIGHT_ABILITY_COOLDOWN;
      }
      case PieceType.BISHOP -> {
        selectedPiece = bishopImage;
        abilityCoolDown = ROOK_ABILITY_COOLDOWN;
      }
    }
    pieceWidth = selectedPiece.getWidth() * SCALE;
    pieceHeight = selectedPiece.getHeight() * SCALE;
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
    if (selectedPiece != null && !player.isDead) {
      if (animationFrame == 2){
        g2d.drawImage(selectedPiece, player.x - animationOffset, player.y + animationOffset, pieceWidth + animationOffset * 2, pieceHeight, this);
      } else if (animationFrame == 3){
        g2d.drawImage(selectedPiece, player.x + animationOffset, player.y - animationOffset, pieceWidth - animationOffset * 2, pieceHeight, this);
      } else {
        g2d.drawImage(selectedPiece, player.x, player.y, pieceWidth, pieceHeight, this);
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


  private int pauseMenuIndex;
  void drawUI(Graphics2D g2d) {
    g2d.setFont(gameFont);

    drawScore(g2d);
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
  }

  private void drawScore(Graphics2D g2d){
    g2d.setColor(Color.WHITE);
    drawText(g2d, 120, 42, gameFontTiny, scoreText + score);
  }

  private void drawGameOverScreen(Graphics2D g2d){
    g2d.setColor(new Color(0,0,0,200));
    g2d.fillRect(0,0,Main.WIDTH, Main.HEIGHT);

    g2d.setColor(Color.RED);
    drawText(g2d,0,320, gameFont, gameOverText);

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
    drawText(g2d, 0, 700, gameFont, restartText);
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
    int xPos = 666;
    int yPos = Main.HEIGHT - 132;

    if (!player.rookAlive){
      g2d.drawImage(rookImage, xPos, yPos, 100, 100, this);
    }
    if (!player.knightAlive){
      g2d.drawImage(knightImage, xPos + 132, yPos, 100, 100, this);
    }
    if (!player.kingAlive){
      g2d.drawImage(kingImage, xPos + 264, yPos, 100, 100, this);
    }
    if (!player.queenAlive){
      g2d.drawImage(queenImage, xPos + 402, yPos, 100, 100, this);
    }
    if (!player.bishopAlive){
      g2d.drawImage(bishopImage, xPos + 534, yPos, 100, 100, this);
    }

    g2d.setColor(new Color(50,50,50,200));
    g2d.fillRect(xPos, yPos, Main.WIDTH - 1290, 100);

    if (player.rookAlive){
      g2d.drawImage(rookImage, xPos, yPos, 100, 100, this);
    }
    if (player.knightAlive){
      g2d.drawImage(knightImage, xPos + 132, yPos, 100, 100, this);
    }
    if (player.kingAlive){
      g2d.drawImage(kingImage, xPos + 264, yPos, 100, 100, this);
    }
    if (player.queenAlive){
      g2d.drawImage(queenImage, xPos + 402, yPos, 100, 100, this);
    }
    if (player.bishopAlive){
      g2d.drawImage(bishopImage, xPos + 534, yPos, 100, 100, this);
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
