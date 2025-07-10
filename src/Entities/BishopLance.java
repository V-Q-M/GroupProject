package entities;

import main.GamePanel;

public class BishopLance extends Projectile{

    // Specialized constructor
    public BishopLance(GamePanel gamePanel, int x, int y, int size, int speed, String direction) {
        this.gamePanel = gamePanel;
        this.x = x;
        this.y = y;
        this.width = size;
        this.height = size;
        this.direction = direction;
        this.speed = speed;
        this.skin = gamePanel.cannonBallImage;
    }

    // Moves the ball
    @Override
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
                x += speed;
            }
        }
    }
}
