package entities;

public class CannonBall extends Projectile {

  // Specialized constructor
  public CannonBall(int x, int y, int size, int speed, String direction) {
    this.x = x;
    this.y = y;
    this.width = size;
    this.height = size;
    this.direction = direction;
    this.speed = speed;
  }

  // Moves the ball
  public void moveProjectile(int speed) {
    switch (direction) {
      case "up" -> y -= speed;
      case "down" -> y += speed;
      case "right" -> x += speed;
      case "left" -> x -= speed;
      default -> x += speed;
    }
  }
}
