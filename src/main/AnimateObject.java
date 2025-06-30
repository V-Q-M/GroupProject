package main;

import java.awt.image.BufferedImage;

public class AnimateObject {
    public GamePanel gamePanel;
    public SoundManager soundManager;
    public CollisionHandler collisionHandler;
    public int x, y;
    public int width;
    public int height;
    public boolean isDead = false;
    public boolean isInvulnerable = false;
    public int invulnerableCounter = 0;
    public boolean hasAttacked = false;
    public int attackCoolDownCounter;
    public int attackCoolDown;

    public BufferedImage skin;

    public int speed;
    public int damage;
    public int health;
}
