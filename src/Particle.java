public class Particle {
  public int x, y;
  public final int size;
  public final String direction;
  private int particleSpeed = 20;
  public int decay = 0;

  // Specialized constructor
  public Particle(int x, int y, int size, String direction) {
    this.x = x;
    this.y = y;
    this.size = size;
    this.direction = direction;
  }
  // Say no to null values
  public int getParticleSpeed() {
    if (particleSpeed != 0) {
      return particleSpeed;
    }
    return 1;
  }

  // yeah, we ain't doin negative speed...
  public void setParticleSpeed(int newParticleSpeed) {
    if (newParticleSpeed >= 0) {
      particleSpeed = newParticleSpeed;
    }
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
