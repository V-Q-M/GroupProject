import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GamePanel extends JPanel{

  // Textures of the pieces
  BufferedImage rookImage;
  BufferedImage knightImage;
  BufferedImage bishopImage;
  BufferedImage kingImage;
  BufferedImage queenImage;
  BufferedImage pawnImage;
  // uses the enum
  PieceType selectedPieceType;

  // Part of the background
  private BufferedImage tileImage;

  // This will hold the actual Player piece
  private BufferedImage selectedPiece;

  // Im scaling 32x32 Textures so that they look nicer
  final int SCALE = 10;

  // List to track cannon balls
  final List<CannonBall> balls = new ArrayList<>();
  // carries particle effects
  final List<Particle> particles = new ArrayList<>();
  // carries enemies
  final List<Enemy>  enemies = new ArrayList<>();

  KeyHandler keyHandler = new KeyHandler(this);
  SoundManager soundManager = new SoundManager(this);

  int startX = 100;
  int startY = 100;
  Player player = new Player(this, keyHandler, startX, startY);
  EnemyManager enemyManager = new EnemyManager(this);
  EntityManager entityManager = new EntityManager(this, keyHandler, soundManager, player);




  public GamePanel() {
    // Window size
    setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));
    setFocusable(true);
    requestFocusInWindow();
    addKeyListener((KeyListener) keyHandler);

    this.loadImages();
    soundManager.loadSounds();
    // Default piece
    selectPiece(PieceType.ROOK);
    // Test enemy
    enemyManager.spawnEnemy(200,200,80,rookImage);
    enemyManager.spawnEnemy(400,200,80,rookImage);
    enemyManager.spawnEnemy(600,200,80,rookImage);
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

    } catch (IOException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, "Could not load images");
    }
  }

  // SelectPiece. Should prompt the user to pick one eventually
  void selectPiece(PieceType changePiece) {
    selectedPieceType = changePiece;
    switch (changePiece) {
    case PieceType.ROOK -> selectedPiece = rookImage;
    case PieceType.QUEEN -> selectedPiece = queenImage;
    case PieceType.KING -> selectedPiece = kingImage;
    case PieceType.KNIGHT -> selectedPiece = knightImage;
    case PieceType.BISHOP -> selectedPiece = bishopImage;
    }
  }

  public void update() {
    // Self-explanatory
    player.playerUpdate();
    entityUpdate();

    // Update every enemy
    for (Enemy enemy : enemies){
      enemy.update();
    }
    repaint();
  }

  private void entityUpdate(){
    // Update cannon balls
    balls.removeIf(ball -> {
      ball.moveBall(ball.getBallSpeed());
      return ball.y + ball.size < 0;
    });
    // update particles (queens effect)
    particles.removeIf(particle -> {
      particle.moveParticle(particle.getParticleSpeed());
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

  private void drawPlayer(Graphics2D g2d){
    // Draw selectedPiece at current position
    if (selectedPiece != null) {
      int pieceWidth = selectedPiece.getWidth() * SCALE;
      int pieceHeight = selectedPiece.getHeight() * SCALE;
      g2d.drawImage(selectedPiece, player.playerX, player.playerY, pieceWidth, pieceHeight, this);
    }
  }

  private void drawEntities(Graphics2D g2d){
    // Draw all cannon balls
    g2d.setColor(Color.WHITE);
    for (CannonBall b : balls) {
      g2d.fillRect(b.x, b.y, b.size, b.size);
    }
    for (Particle p : particles) {
      g2d.fillRect(p.x, p.y, p.size, p.size);
    }
  }

  private void drawEnemies(Graphics2D g2d){
    for (Enemy enemy : enemies) {
      g2d.drawImage(enemy.skin, enemy.x, enemy.y, enemy.size, enemy.size, this);
    }
  }


  // gets called when space is pressed
  void performAttack() {
    switch (selectedPieceType) {
      case ROOK -> entityManager.spawnCannonBall();
      case QUEEN -> entityManager.spawnQueenParticles();
    }
  }

}
