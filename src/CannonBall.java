public class CannonBall {
  public int x, y;
  public final int size;
  public final String direction;
  private int ballSpeed = 10;
  public boolean hasHit = false;

  // Specialized constructor
  public CannonBall(int x, int y, int size, String direction) {
    this.x = x;
    this.y = y;
    this.size = size;
    this.direction = direction;
  }
  // Say no to null values
  public int getBallSpeed() {
    if (ballSpeed != 0) {
      return ballSpeed;
    }
    return 1;
  }

  // yeah, we ain't doin negative speed...
  public void setBallSpeed(int newBallSpeed) {
    if (newBallSpeed >= 0) {
      ballSpeed = newBallSpeed;
    }
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
