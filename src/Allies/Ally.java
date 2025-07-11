package Allies;

import entities.Projectile;
import main.*;

public abstract class Ally extends livingBeing {
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
        checkProjectileCollision();
    }

    void checkAlive(){
        if (health <= 0){
            this.isDead = true;
            soundManager.playClip(soundManager.deathClip);
        }
    }

    void checkProjectileCollision(){
        for (Projectile projectile : gamePanel.enemyBalls){
            if (collisionHandler.projectileCollision(this, projectile)){
                health -= 50;
                projectile.isDead = true;
                gamePanel.entityManager.spawnExplosion(projectile.x, projectile.y);
                soundManager.playClip(soundManager.hitClip);
            }
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

    void updateCooldowns(){
        if (isInvulnerable && invulnerableCounter<30){
            invulnerableCounter++;
        } else {
            isInvulnerable = false;
            invulnerableCounter = 0;
            this.skin = baseSkin;
        }

        if (hasAttacked && attackCoolDownCounter < attackCoolDown){
            attackCoolDownCounter++;
        } else {
            hasAttacked = false;
            attackCoolDownCounter = 0;
        }
    }
}
