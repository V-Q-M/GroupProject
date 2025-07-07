package enemies;

import entities.CannonBall;
import entities.Projectile;
import main.*;

public class Enemy extends AnimateObject {
    final int DEFAULT_CANNON_BALL_DMG = 35;
    final int DEFAULT_SLICE_DMG= 50;
    final int DEFAULT_SLAM_DMG= 100;
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
        checkCollision();
        updateCooldowns();
    }

    private void checkAlive(){
        if (health <= 0){
            this.isDead = true;
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

    private void checkCollision(){
        if (!isInvulnerable) {
            switch(gamePanel.selectedPieceType){
                case ROOK   -> {
                    for (CannonBall ball : gamePanel.balls) {
                        if (collisionHandler.projectileCollision(this, ball)) {
                            health -= DEFAULT_CANNON_BALL_DMG;
                            isInvulnerable = true;
                            ball.hasHit = true;
                            soundManager.playClip(soundManager.hitClip);
                        }
                    }
                }
                case KNIGHT -> {
                    for (Projectile projectile: gamePanel.projectiles) {
                        if ( collisionHandler.projectileCollision(this, projectile)){
                            health -= DEFAULT_SLAM_DMG;
                            isInvulnerable = true;
                            soundManager.playClip(soundManager.hitClip);
                        }
                    }
                }
                case BISHOP -> {
                    for (CannonBall ball : gamePanel.balls) {
                        if (collisionHandler.projectileCollision(this, ball)) {
                            health -= DEFAULT_CANNON_BALL_DMG;
                            isInvulnerable = true;
                            ball.hasHit = true;
                            soundManager.playClip(soundManager.hitClip);
                        }
                    }
                }
                case QUEEN  -> {
                    for (Projectile projectile: gamePanel.projectiles) {
                        if ( collisionHandler.projectileCollision(this, projectile)){
                            health -= DEFAULT_SLICE_DMG;
                            isInvulnerable = true;
                            soundManager.playClip(soundManager.hitClip);
                        }
                    }
                }
                case KING   -> {
                    for (CannonBall ball : gamePanel.balls) {
                        if (collisionHandler.projectileCollision(this, ball)) {
                            health -= DEFAULT_CANNON_BALL_DMG;
                            isInvulnerable = true;
                            ball.hasHit = true;
                            soundManager.playClip(soundManager.hitClip);
                        }
                    }
                }
            }
        }
    }
}
