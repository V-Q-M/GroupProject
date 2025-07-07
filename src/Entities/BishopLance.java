package entities;

public class BishopLance extends Projectile{

    // Specialized constructor
    public BishopLance(int x, int y, int size, int speed, String direction) {
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
            case "up-left" -> {
                y -= speed;
                x -= speed;
            }
            case "up-right" -> {
                y -= speed;
                x += speed;
            }
            case "down-left" -> {
                y += speed;
                x -= speed;
            }
            case "down-right" -> {
                y += speed;
                x += speed;
            }
            default -> {
                y += speed;
                x -= speed;
            }
        }
    }
}
