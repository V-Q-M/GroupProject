package enemies;

import entities.CannonBall;
import entities.Particle;
import main.*;

import java.awt.image.BufferedImage;

public class Enemy extends AnimateObject {
    final int DEFAULT_CANNON_BALL_DMG = 35;
    final int DEFAULT_SLICE_DMG= 50;


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


    private void move(){
        if (x < 0){
            health = 0;
            gamePanel.castleHealth -= damage / 3;
        } else {
            x -= speed;
        }

        if (y < 100){
            y += speed;
        } else if (y > Main.HEIGHT - height - 100){
            y -= speed;
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
            if(gamePanel.selectedPieceType == PieceType.ROOK) {
                for (CannonBall ball : gamePanel.balls) {
                    if (collisionHandler.projectileCollision(this, ball)) {
                        health -= DEFAULT_CANNON_BALL_DMG;
                        isInvulnerable = true;
                        ball.hasHit = true;
                        soundManager.playClip(soundManager.hitClip);
                    }
                }
            } else {
                for (Particle particle: gamePanel.particles) {
                    if ( collisionHandler.projectileCollision(this, particle)){
                        health -= DEFAULT_SLICE_DMG;
                        isInvulnerable = true;
                        soundManager.playClip(soundManager.hitClip);
                    }
                }
            }
        }
    }
}
