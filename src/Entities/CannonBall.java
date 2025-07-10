package entities;
import main.GamePanel;


public class CannonBall extends Projectile {

  // Specialized constructor
  public CannonBall(GamePanel gamePanel, int x, int y, int size, int speed, String direction) {
    this.gamePanel = gamePanel;
    this.x = x;
    this.y = y;
    this.width = size;
    this.height = size;
    this.direction = direction;
    this.speed = speed;
    this.health = 200;
    this.skin = gamePanel.cannonBallImage;
  }
}
