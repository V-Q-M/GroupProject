package main;

import java.awt.image.BufferedImage;

public abstract class livingBeing {
    public GamePanel gamePanel;
    public SoundManager soundManager;
    public CollisionHandler collisionHandler;

    public int x, y, width, height;
    public boolean isDead, isInvulnerable, hasAttacked = false;
    public int invulnerableCounter, attackCoolDownCounter = 0;
    public int attackCoolDown;

    public BufferedImage skin;
    public BufferedImage baseSkin;
    public BufferedImage hurtSkin;


    public int speed, damage, health;
}
