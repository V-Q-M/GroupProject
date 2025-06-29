import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;

public class GamePanel extends JPanel implements KeyListener{

  // Textures of the pieces
  private BufferedImage rookImage;
  private BufferedImage knightImage;
  private BufferedImage bishopImage;
  private BufferedImage kingImage;
  private BufferedImage queenImage;
  private BufferedImage pawnImage;
  // uses the enum
  private PieceType selectedPieceType;

  // Part of the background
  private BufferedImage tileImage;

  // This will hold the actual Player piece
  private BufferedImage selectedPiece;

  private Clip shootClip;
  private Clip sliceClip;

  // Start Position
  private int rookX = 100;
  private int rookY = 100;

  private boolean queenDashing = false;
  private int queenDashingCounter = 0;

  // Im scaling 32x32 Textures so that they look nicer
  private final int SCALE = 10;

  // Rook movespeed. Might create a PieceClass and move it there
  private int BASE_MOVE_SPEED = 5;
  private int DASH_SPEED = 18;
  private int moveSpeed;


  // List to track cannon balls
  // Might expand that to carry other projectiles
  private final List<CannonBall> balls = new ArrayList<>();
  // carries particle effects
  private final List<Particle> particles = new ArrayList<>();

  public GamePanel() {
    // Window size
    setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));
    setFocusable(true);
    requestFocusInWindow();
    addKeyListener((KeyListener) this);

    loadImages();
    loadSounds();
    selectPiece(PieceType.ROOK);
    // Refreshrate. Might have to improve that
    new Timer(16, e -> update()).start(); // ~60 FPS
                                          //
  }

  // Might move to specialised class if Soundlogic gets more complicated
  private void playClip(Clip clip) {
    if (clip == null) return;
    if (clip.isRunning()) clip.stop();  // Stop current sound if needed
    clip.setFramePosition(0);           // Rewind to the beginning
    clip.start();
  }


  private void loadSounds() {
    shootClip = loadClip("res/shoot.wav");
    sliceClip = loadClip("res/slice1.wav");
  }

  private Clip loadClip(String path) {
    try {
      AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(path));
      Clip clip = AudioSystem.getClip();
      clip.open(audioIn);
      return clip;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
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
  private void selectPiece(PieceType changePiece) {
    //
    selectedPieceType = changePiece;
    switch (changePiece) {
    case PieceType.ROOK -> selectedPiece = rookImage;
    case PieceType.QUEEN -> selectedPiece = queenImage;
    case PieceType.KING -> selectedPiece = kingImage;
    case PieceType.KNIGHT -> selectedPiece = knightImage;
    case PieceType.BISHOP -> selectedPiece = bishopImage;
    }
  }

  // Movement logic is abit weird. Idk wether I should fix it
  public void update() {
    int speed = moveSpeed;

    if (goingUp)
      rookY -= speed;
    if (goingDown)
      rookY += speed;
    if (goingLeft)
      rookX -= speed;
    if (goingRight)
      rookX += speed;

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

    if (queenDashing && queenDashingCounter <= 20){
      queenDashingCounter ++;
    } else {
      queenDashing = false;
      queenDashingCounter = 0;
      moveSpeed = BASE_MOVE_SPEED;
    }

    repaint();
  }

  // Carefull. Render method
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D)g;
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

    // Draw selectedPiece at current position
    if (selectedPiece != null) {
      int rw = selectedPiece.getWidth() * SCALE;
      int rh = selectedPiece.getHeight() * SCALE;
      g2d.drawImage(selectedPiece, rookX, rookY, rw, rh, this);
    }

    // Draw all cannon balls
    g2d.setColor(Color.WHITE);
    for (CannonBall b : balls) {
      g2d.fillRect(b.x, b.y, b.size, b.size);
    }
    for (Particle p : particles) {
      g2d.fillRect(p.x, p.y, p.size, p.size);
    }
  }

  // Yes ik. Scuffed logic
  boolean goingRight = false;
  boolean goingLeft = false;
  boolean goingUp = false;
  boolean goingDown = false;

  @Override
  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    // rook has his own pattern, because i dont want him to move diagonally
    if (selectedPieceType == PieceType.ROOK) {
      switch (key) {
      case KeyEvent.VK_W -> {
        goingUp = true;
        goingRight = false;
        goingLeft = false;
        goingDown = false;
      }
      case KeyEvent.VK_S -> {
        goingDown = true;
        goingRight = false;
        goingLeft = false;
        goingUp = false;
      }
      case KeyEvent.VK_A -> {
        goingLeft = true;
        goingDown = false;
        goingRight = false;
        goingUp = false;
      }
      case KeyEvent.VK_D -> {
        goingRight = true;
        goingDown = false;
        goingLeft = false;
        goingUp = false;
      }
      case KeyEvent.VK_SPACE -> performAttack();
      case KeyEvent.VK_1 -> selectPiece(PieceType.ROOK);
      case KeyEvent.VK_2 -> selectPiece(PieceType.QUEEN);
      }
    } else {
      switch (key) {
      case KeyEvent.VK_W -> {
        goingUp = true;
      }
      case KeyEvent.VK_S -> {
        goingDown = true;
      }
      case KeyEvent.VK_A -> {
        goingLeft = true;
      }
      case KeyEvent.VK_D -> {
        goingRight = true;
      }
      case KeyEvent.VK_SPACE -> performAttack();
      case KeyEvent.VK_1 -> selectPiece(PieceType.ROOK);
      case KeyEvent.VK_2 -> selectPiece(PieceType.QUEEN);
      }
    }
  }
  @Override
  public void keyReleased(KeyEvent e) {
    int key = e.getKeyCode();
    switch (key) {
    case KeyEvent.VK_W -> goingUp = false;
    case KeyEvent.VK_S -> goingDown = false;
    case KeyEvent.VK_A -> goingLeft = false;
    case KeyEvent.VK_D -> goingRight = false;
    }
  }

  // Unused but required
  @Override
  public void keyTyped(KeyEvent e) {}
  // gets called when space is pressed
  private void performAttack() {
    switch (selectedPieceType) {
    case ROOK -> spawnCannonBall();
    case QUEEN -> spawnQueenParticles();
    }
  }
  // Yeah this could get its own class later
  private void spawnCannonBall() {
    if (rookImage != null) {
      int size = 80; // size of the cannonball
      int rw = rookImage.getWidth() * SCALE;
      int rh = rookImage.getHeight() * SCALE;
      // spawn at top‐center of the rook
      int bx = rookX + (rw - size) / 2;
      int by = rookY;
      String direction = "right";
      if (goingDown) {
        direction = "down";
      }
      if (goingUp) {
        direction = "up";
      }
      if (goingLeft) {
        direction = "left";
      }
      if (goingRight) {
        direction = "right";
      }
      // Append balls to the list of balls
      balls.add(new CannonBall(bx, by, size, direction));
      // this should move to a variable
      playClip(shootClip);
    }
  }
  //TODO make it so that movespeed return to normal after a second (dash stopped)
  private void spawnQueenParticles() {
    if (queenImage != null) {
      int size = 140; // size of the cannonball
      int rw = queenImage.getWidth() * SCALE;
      int rh = queenImage.getHeight() * SCALE;
      moveSpeed = DASH_SPEED;
      queenDashing = true;

      // spawn at top‐center of the rook
      int bx = rookX + (rw - size) / 2;
      int by = rookY + (rh - size) / 2;
      String direction = "right";
      if (goingDown) {
        direction = "down";
      }
      if (goingUp) {
        direction = "up";
      }
      if (goingLeft) {
        direction = "left";
      }
      if (goingRight) {
        direction = "right";
      }
      // Append balls to the list of balls
      particles.add(new Particle(bx, by, size, direction));
      // this should move to a variable
      playClip(sliceClip);
    }
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame("ChessBrawl");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new GamePanel());
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

}
