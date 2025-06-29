import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GamePanel extends JPanel{

  private boolean DEBUG_MODE = false;

  // Textures of the pieces
  BufferedImage rookImage;
  BufferedImage knightImage;
  BufferedImage bishopImage;
  BufferedImage kingImage;
  BufferedImage queenImage;
  BufferedImage pawnImage;

  BufferedImage enemyRookImage;
  BufferedImage enemyKnightImage;
  BufferedImage enemyBishopImage;
  BufferedImage enemyKingImage;
  BufferedImage enemyQueenImage;
  BufferedImage enemyPawnImage;
  // uses the enum
  PieceType selectedPieceType;
  int pieceWidth;
  int pieceHeight;

  private final int ROOK_ABILITY_COOLDOWN = 60;
  private final int QUEEN_ABILITY_COOLDOWN = 40;

  int abilityCoolDown = ROOK_ABILITY_COOLDOWN;

  // Part of the background
  private BufferedImage tileImage;

  // This will hold the actual Player piece
  private BufferedImage selectedPiece;

  // Im scaling 32x32 Textures so that they look nicer
  final int SCALE = 8;

  // List to track cannon balls
  final List<CannonBall> balls = new ArrayList<>();
  // carries particle effects
  final List<Particle> particles = new ArrayList<>();
  // carries enemies
  final List<Enemy>  enemies = new ArrayList<>();

  KeyHandler keyHandler = new KeyHandler(this);
  CollisionHandler collisionHandler = new CollisionHandler(this);
  SoundManager soundManager = new SoundManager(this);

  int startX = 100;
  int startY = 100;

  Player player = new Player(this, keyHandler, soundManager, collisionHandler, startX, startY);
  EnemyManager enemyManager = new EnemyManager(this);
  EntityManager entityManager = new EntityManager(this, keyHandler, soundManager, player);

  public GamePanel() {
    // Window size
    setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));
    setFocusable(true);
    requestFocusInWindow();
    addKeyListener(keyHandler);

    this.loadImages();
    soundManager.loadSounds();
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

  // SelectPiece. Should prompt the user to pick one eventually
  void selectPiece(PieceType changePiece) {
    selectedPieceType = changePiece;
    player.swapCounter = 0;
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
      }
      case PieceType.KNIGHT -> {
        selectedPiece = knightImage;
      }
      case PieceType.BISHOP -> {
        selectedPiece = bishopImage;
      }
    }
    pieceWidth = selectedPiece.getWidth() * SCALE;
    pieceHeight = selectedPiece.getHeight() * SCALE;
  }

  public void update() {
    if (!player.isDead){
      player.playerUpdate();
    }

    entityUpdate();

    // Update every enemy
    for (Enemy enemy : enemies){
      if (!enemy.isDead){
        enemy.update();
      }
    }
    enemyManager.updateSpawner();
    repaint();
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
    // update particles (queens effect)
    particles.removeIf(particle -> {
      particle.moveParticle(particle.speed);
      particle.decay++;
      return particle.decay > 20;
    });
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
      int x = player.playerX;
      int y = player.playerY;
      g2d.drawImage(selectedPiece, x, y, pieceWidth, pieceHeight, this);
      // Draw hitbox
      if (DEBUG_MODE) {
        g2d.setColor(Color.red);
        g2d.drawRect(player.playerX, player.playerY, pieceWidth, pieceHeight);
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
    for (Particle p : particles) {
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
    // Player health-bar always on top
    if (!player.isDead && player.health != 100) {
      createHealthBar(g2d, player.playerX, player.playerY, pieceWidth, 20, player.health);
    }
  }






}
