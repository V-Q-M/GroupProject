package Allies;

import entities.Projectile;
import main.*;

public class Ally extends AnimateObject {
    final int DEFAULT_CANNON_BALL_DMG = 35;
    final int DEFAULT_SLICE_DMG= 50;
    final int DEFAULT_SLAM_DMG= 100;
    public int maxHealth = 100; // need to pass it in constructor soon
    public boolean canMove = false;


    public Ally(GamePanel gamePanel, SoundManager soundManager, CollisionHandler collisionHandler, int x, int y, int width, int height) {
        this.gamePanel = gamePanel;
        this.soundManager = soundManager;
        this.collisionHandler = collisionHandler;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void update(){
        checkAlive();
        move();
        updateCooldowns();
    }

    private void checkAlive(){
        if (health <= 0){
            this.isDead = true;
            soundManager.playClip(soundManager.deathClip);
        }
    }


    public void move(){
        if (canMove){
            if (x > Main.WIDTH){
                health = 0;
            } else {
                x += speed;
            }
        }
    }

    private void updateCooldowns(){
        if (isInvulnerable && invulnerableCounter<30){
            invulnerableCounter++;
        } else {
            isInvulnerable = false;
            invulnerableCounter = 0;
        }

        if (hasAttacked && attackCoolDownCounter < attackCoolDown){
            attackCoolDownCounter++;
        } else {
            hasAttacked = false;
            attackCoolDownCounter = 0;
        }
    }
}
