package enemies;

import Allies.Ally;
import entities.Projectile;
import main.*;

public abstract class Enemy extends livingBeing {
    public int maxHealth = 100; // need to pass it in constructor soon
    public boolean isKing = false;


    public Enemy(GamePanel gamePanel, SoundManager soundManager, CollisionHandler collisionHandler, int x, int y, int width, int height) {
        this.gamePanel = gamePanel;
        this.soundManager = soundManager;
        this.collisionHandler = collisionHandler;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.recoveryTime = 45;
    }

    public void update(){
        checkAlive();
        move();
        checkPlayerCollision();
        checkPawnWallCollision();
        updateCooldowns();
    }

    void checkAlive(){
        if (health <= 0){
            this.isDead = true;
            gamePanel.score+=maxHealth;
            soundManager.playClip(soundManager.deathClip);
        }
    }


    public void move(){
        if (x < 0){
            health = 0;
            gamePanel.castleHealth -= damage / 3;
            gamePanel.castleGotHit = true;
        } else {
            x -= speed;
        }

        if (y < 0){
            y += speed;
        } else if (y > 8 * gamePanel.PIECE_HEIGHT){
            y = 8 * gamePanel.PIECE_HEIGHT;
        }
    }

    void updateCooldowns(){
        if (isInvulnerable){
            if (invulnerableCounter >= recoveryTime){
                isInvulnerable = false;
                invulnerableCounter = 0;
            }  else if (invulnerableCounter > recoveryMarkerTime) {
                this.skin = baseSkin;
            }
            invulnerableCounter++;
        }


        if (hasAttacked && attackCoolDownCounter < attackCoolDown){
            attackCoolDownCounter++;
        } else {
            hasAttacked = false;
            attackCoolDownCounter = 0;
        }
    }

    void checkPlayerCollision(){
        if (!isInvulnerable) {
            for (Projectile projectile : gamePanel.projectiles) {
                if (collisionHandler.projectileCollision(this, projectile)) {
                    isInvulnerable = true;
                    this.skin = hurtSkin;
                    soundManager.playClip(soundManager.hitClip);
                    health -= projectile.damage;

                    if (projectile.diesOnHit) {
                            gamePanel.entityManager.spawnExplosion(projectile.x, projectile.y);
                            projectile.isDead = true;
                    }
                }
            }
        }
    }
     void checkPawnWallCollision(){
        for (Ally pawn : gamePanel.allies){
            if (!pawn.isDead && collisionHandler.allyCollision(this, pawn)) {
                health -= 100;
                pawn.health = 0;
            }
        }
    }
}
