package entities;

public class CannonBall extends Projectile {
  public boolean hasHit = false;

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
  public void moveBall(int speed) {
    switch (direction) {
    case "up" -> y -= speed;
    case "down" -> y += speed;
    case "right" -> x += speed;
    case "left" -> x -= speed;
    }
  }
}
