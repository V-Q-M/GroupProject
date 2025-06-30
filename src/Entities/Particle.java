package entities;

public class Particle extends Projectile{
  public final String direction;
  public int decay = 0;

  // Specialized constructor
  public Particle(int x, int y, int size, int speed, String direction) {
    this.x = x;
    this.y = y;
    this.height = size;
    this.width = size;
    this.direction = direction;
    this.speed = speed;
  }

  // Moves the ball
  public void moveParticle(int speed) {
    switch (direction) {
    case "up" -> y -= speed;
    case "down" -> y += speed;
    case "right" -> x += speed;
    case "left" -> x -= speed;
    }
  }
}
