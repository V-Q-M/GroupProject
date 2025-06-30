package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

import enemies.Enemy;
import entities.CannonBall;
import Allies.Player;
import entities.Projectile;

public class GamePanel extends JPanel{

  private boolean DEBUG_MODE = false;

  // Textures of the pieces
  public BufferedImage rookImage;
  public BufferedImage knightImage;
  public BufferedImage bishopImage;
  public BufferedImage kingImage;
  public BufferedImage queenImage;
  public BufferedImage pawnImage;

  public BufferedImage enemyRookImage;
  public BufferedImage enemyKnightImage;
  public BufferedImage enemyBishopImage;
  public BufferedImage enemyKingImage;
  public BufferedImage enemyQueenImage;
  public BufferedImage enemyPawnImage;
  // uses the enum
  public PieceType selectedPieceType;
  public int pieceWidth;
  public int pieceHeight;

  private final int ROOK_ABILITY_COOLDOWN = 60;
  private final int QUEEN_ABILITY_COOLDOWN = 40;

  public int abilityCoolDown = ROOK_ABILITY_COOLDOWN;

  // Part of the background
  private BufferedImage tileImage;

  // This will hold the actual player.Player piece
  private BufferedImage selectedPiece;

  // Im scaling 32x32 Textures so that they look nicer
  final int SCALE = 8;

  // List to track cannon balls
  public final List<CannonBall> balls = new ArrayList<>();
  // carries particle effects
  public final List<Projectile> projectiles = new ArrayList<>();
  // carries enemies
  public final List<Enemy>  enemies = new ArrayList<>();

  // Gamelogic here
  boolean gameStart = true;
  public boolean swapSoon = false;
  public int castleHealth = 100;
  //public int castleHealth = 10;
  public boolean gameOver = false;

  KeyHandler keyHandler = new KeyHandler(this);
  CollisionHandler collisionHandler = new CollisionHandler(this);
  SoundManager soundManager = new SoundManager(this);

  int startX = 100;
  int startY = 100;

  Player player = new Player(this, keyHandler, soundManager, collisionHandler, startX, startY);
  EnemyManager enemyManager = new EnemyManager(this);
  public EntityManager entityManager = new EntityManager(this, keyHandler, soundManager, player);

  public GamePanel() {
    // Window size
    setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));
    setFocusable(true);
    requestFocusInWindow();
    addKeyListener(keyHandler);

    this.loadImages();
    this.loadFonts();
    soundManager.loadSounds();
    //soundManager.startMusic();
    // Default piece
    selectPiece(PieceType.ROOK);
    // Refreshrate. Might have to improve that
    new Timer(16, e -> update()).start(); // ~60 FPS
                                          //
  }

  // Image loader. Very simple. Might expand to ImageAtlas
  private void loadImages() {
    try {
      rookImage =
          ImageIO.read(new File("res/chess-pieces-png/color/white/rook.png"));
      tileImage = ImageIO.read(new File("res/earth.png"));
      knightImage =
          ImageIO.read(new File("res/chess-pieces-png/color/white/knight.png"));
      bishopImage =
          ImageIO.read(new File("res/chess-pieces-png/color/white/bishop.png"));
      kingImage =
          ImageIO.read(new File("res/chess-pieces-png/color/white/king.png"));
      queenImage =
          ImageIO.read(new File("res/chess-pieces-png/color/white/queen.png"));
      pawnImage =
          ImageIO.read(new File("res/chess-pieces-png/color/white/pawn.png"));
      enemyRookImage=
              ImageIO.read(new File("res/chess-pieces-png/color/black/rook.png"));
      enemyKnightImage=
              ImageIO.read(new File("res/chess-pieces-png/color/black/knight.png"));
      enemyBishopImage=
              ImageIO.read(new File("res/chess-pieces-png/color/black/bishop.png"));
      enemyKingImage=
              ImageIO.read(new File("res/chess-pieces-png/color/black/king.png"));
      enemyQueenImage=
              ImageIO.read(new File("res/chess-pieces-png/color/black/queen.png"));
      enemyPawnImage =
              ImageIO.read(new File("res/chess-pieces-png/color/black/pawn.png"));

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

  // SelectPiece. Should prompt the user to pick one eventually
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
        abilityCoolDown = ROOK_ABILITY_COOLDOWN;
      }
      case PieceType.KNIGHT -> {
        selectedPiece = knightImage;
        abilityCoolDown = ROOK_ABILITY_COOLDOWN;
      }
      case PieceType.BISHOP -> {
        selectedPiece = bishopImage;
        abilityCoolDown = ROOK_ABILITY_COOLDOWN;
      }
    }
    pieceWidth = selectedPiece.getWidth() * SCALE;
    pieceHeight = selectedPiece.getHeight() * SCALE;
  }

  public void update() {
    if (!gameOver) {
      if (!player.isDead) {
        player.playerUpdate();
      }
      entityUpdate();

      // Update every enemy
      for (Enemy enemy : enemies) {
        if (!enemy.isDead) {
          enemy.update();
        }
      }
      enemyManager.updateSpawner();
      gameUpdate();
    }
    repaint();
  }

  int gameStartCounter = 0;
  String startMessage = "Starting in: 3";
  private void gameUpdate(){
    if (gameStart) {
      if (gameStartCounter > 180) {
        gameStart = false;
        gameStartCounter = 0;
      } else if (gameStartCounter > 150) {
        startMessage = "";
        gameStartCounter++;
      } else if (gameStartCounter > 120) {
        startMessage = "Starting in: 1";
        gameStartCounter++;
      } else if (gameStartCounter > 90){
        startMessage = "";
        gameStartCounter++;
      } else if (gameStartCounter > 60){
        startMessage = "Starting in: 2";
        gameStartCounter++;
      } else if (gameStartCounter > 30){
        startMessage = "";
        gameStartCounter++;
      } else {
        gameStartCounter++;
      }
    }
    if (castleHealth <= 0){
      gameOver = true;
      castleHealth = 0;
      soundManager.stopMusic();
    }

  }
  private void entityUpdate(){
    // Update cannon balls
    balls.removeIf(ball -> {
      ball.moveBall(ball.speed);
      return ball.hasHit || ball.y + ball.height < 0
                         || ball.y - ball.height >= Main.HEIGHT
                         || ball.x + ball.width < 0
                         || ball.x - ball.width >= Main.WIDTH;
    });
    // update projectiles (queens effect)
    for (Iterator<Projectile> iterator = projectiles.iterator(); iterator.hasNext(); ) {
      Projectile projectile = iterator.next();
      projectile.moveParticle(projectile.speed);
      projectile.decay++;
      if (projectile.decay > 20) {
        iterator.remove();
      }
    }
  }


  // Carefull. Render method
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D)g;

    drawBackground(g2d);
    drawPlayer(g2d);
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

  private void createHealthBar(Graphics2D g2d, int x, int y, int width, int height, int health){
    g2d.setColor(Color.red);
    g2d.fillRect(x, y - height * 2, width, height);
    int greenWidth= (int) (width * health / 100);
    g2d.setColor(Color.green);
    g2d.fillRect(x, y - height * 2, greenWidth, height);
  }

  private void drawPlayer(Graphics2D g2d){
    // Draw selectedPiece at current position
    if (selectedPiece != null && !player.isDead) {
      int x = player.x;
      int y = player.y;
      g2d.drawImage(selectedPiece, x, y, pieceWidth, pieceHeight, this);
      // Draw hitbox
      if (DEBUG_MODE) {
        g2d.setColor(Color.red);
        g2d.drawRect(player.x , player.y, pieceWidth, pieceHeight);
      }
    }
  }

  private void drawEntities(Graphics2D g2d){
    // Draw all cannon balls
    g2d.setColor(Color.WHITE);
    for (CannonBall b : balls) {
      g2d.fillRect(b.x, b.y, b.width, b.height);
      g2d.drawRect(b.x, b.y, b.width, b.height);
    }
    for (Projectile p : projectiles) {
      g2d.fillRect(p.x, p.y, p.width, p.height);
      g2d.drawRect(p.x, p.y, p.width, p.height);
    }
  }

  private void drawEnemies(Graphics2D g2d){
    for (Enemy enemy : enemies) {
      if (!enemy.isDead) {
        g2d.drawImage(enemy.skin, enemy.x, enemy.y, enemy.width, enemy.height, this);
        if (DEBUG_MODE){
          g2d.setColor(Color.red);
          g2d.drawRect(enemy.x, enemy.y, enemy.width, enemy.height);
        }
      }
    }
  }

  void drawHealthBars(Graphics2D g2d){
    // Personal choice - only show health-bar when not at full health
    for (Enemy enemy : enemies) {
      if (!enemy.isDead && enemy.health != 100) {
        createHealthBar(g2d, enemy.x, enemy.y, enemy.width, 15, enemy.health);
      }
    }
    // player.Player health-bar always on top
    if (!player.isDead && player.health != 100) {
        createHealthBar(g2d, player.x, player.y, pieceWidth, 20, player.health);
    }

    // Castle healthbar
    createHealthBar(g2d, 350, 60, 1200, 20, castleHealth);
  }


  void drawUI(Graphics2D g2d) {
    g2d.setFont(gameFont);

    if (gameOver) {
      g2d.setColor(Color.RED);
      drawText(g2d, "Game Over!");
    }

    if (gameStart) {
      g2d.setColor(Color.WHITE);
      drawText(g2d, startMessage);
    }
    if (swapSoon){
      g2d.setColor(Color.YELLOW);
      drawText(g2d, "Swapping soon!");
    }
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
