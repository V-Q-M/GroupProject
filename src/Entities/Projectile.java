package entities;

import main.GamePanel;

import java.awt.image.BufferedImage;

public abstract class Projectile {
    public GamePanel gamePanel;
    public int x;
    public int y;
    public int width;
    public int height;
    public int speed;
    public int health = 100;
    public String direction;
    public boolean isDead = false;
    public int damage;
    public boolean diesOnHit = false;
    public BufferedImage skin;

    public void moveProjectile(int speed) {
        switch (direction) {
            case "up" -> y -= speed;
            case "down" -> y += speed;
            case "left" -> x -= speed;
            default -> x += speed;
        }
    }

    public void update(){
        checkAlive();
        moveProjectile(speed);
    }

    public void checkAlive(){
        if (health <= 0){
            isDead = true;
        } else {
            health--;
        }
    }
}
