package enemies;

import Allies.Ally;
import entities.Projectile;
import main.*;

public class Enemy extends AnimateObject {
    final int DEFAULT_CANNON_BALL_DMG = 50;
    final int DEFAULT_SLICE_DMG= 50;
    final int DEFAULT_SLAM_DMG= 100;
    final int DEFAULT_LANCE_DMG= 75;
    public int maxHealth = 100; // need to pass it in constructor soon


    public Enemy(GamePanel gamePanel, SoundManager soundManager, CollisionHandler collisionHandler, int x, int y, int width, int height) {
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

    void checkPlayerCollision(){
        if (!isInvulnerable) {
            for (Projectile projectile : gamePanel.projectiles) {
                if (collisionHandler.projectileCollision(this, projectile)) {
                    isInvulnerable = true;
                    soundManager.playClip(soundManager.hitClip);

                    switch (gamePanel.selectedPieceType) {
                        case ROOK -> {
                            health -= DEFAULT_CANNON_BALL_DMG;
                            gamePanel.entityManager.spawnExplosion(projectile.x, projectile.y);
                            projectile.isDead = true;
                        }
                        case KNIGHT -> health -= DEFAULT_SLAM_DMG;
                        case BISHOP -> health -= DEFAULT_LANCE_DMG;
                        case QUEEN -> health -= DEFAULT_SLICE_DMG;
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
