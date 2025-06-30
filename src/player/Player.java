package player;

import enemies.Enemy;
import main.*;

public class Player extends AnimateObject{
    GamePanel gamePanel;
    KeyHandler keyHandler;
    SoundManager soundManager;
    public boolean isDead = false;
    CollisionHandler collisionHandler;
    public boolean isInvulnerable;
    private int invulnerableCounter;
    final int BASE_MOVE_SPEED = 5;
    public final int DASH_SPEED = 18;
    public boolean queenDashing = false;
    private int queenDashingCounter = 0;
    public int health = 100;

    public int swapCounter = 0;

    public String facingDirection = "right";

    private boolean hasAttacked = false;
    private int attackCoolDownCounter = 0;


    public Player(GamePanel gamePanel, KeyHandler keyHandler, SoundManager soundManager,  CollisionHandler collisionHandler, int startPositionX, int startPositionY){
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        this.soundManager = soundManager;
        this.collisionHandler = collisionHandler;
        this.x = startPositionX;
        this.y = startPositionY;
    }

    public void playerUpdate(){
        movement();
        checkCollision();
        coolDowns();
        checkAlive();
        forceSwap();
    }
    private void forceSwap(){
        if (swapCounter >= 600) {
            switch (gamePanel.selectedPieceType) {
                case QUEEN -> gamePanel.selectPiece(PieceType.ROOK);
                case ROOK -> gamePanel.selectPiece(PieceType.QUEEN);
            }
        } else if (swapCounter >= 570){
            gamePanel.swapSoon = false;
            swapCounter++;
        } else if (swapCounter >= 540){
            gamePanel.swapSoon = true;
            swapCounter++;
        } else if (swapCounter >= 510){
            gamePanel.swapSoon = false;
            swapCounter++;
        } else if (swapCounter >= 480){
            gamePanel.swapSoon = true;
            swapCounter++;
        } else {
            swapCounter++;
        }
    }

    private boolean notReachedBorder(){
        return !collisionHandler.borderCollision(x, y, gamePanel.pieceWidth, gamePanel.pieceHeight, speed, facingDirection);
    }

    private void movement(){

        if (keyHandler.goingUp) {
            facingDirection = "up";
            if (notReachedBorder()) {
                y -= speed;
            }
        }
        if (keyHandler.goingDown) {
            facingDirection = "down";
            if (notReachedBorder()) {
                y += speed;
            }
        }
        if (keyHandler.goingLeft) {
            facingDirection = "left";
            if (notReachedBorder()) {
                x -= speed;
            }
        }
        if (keyHandler.goingRight) {
            facingDirection = "right";
            if (notReachedBorder()) {
                x += speed;
            }
        }
        if (keyHandler.spacePressed) {
            keyHandler.spacePressed = false;
            if (!hasAttacked){
                performAttack();
                hasAttacked = true;
            }
        }
    }

    private void coolDowns(){
        if (queenDashing && queenDashingCounter <= 20){
            queenDashingCounter ++;
        } else {
            queenDashing = false;
            queenDashingCounter = 0;
            isInvulnerable = false;
            speed = BASE_MOVE_SPEED;
        }

        if (hasAttacked && attackCoolDownCounter < gamePanel.abilityCoolDown){
            attackCoolDownCounter++;
        } else {
            hasAttacked = false;
            attackCoolDownCounter = 0;
        }
        if (isInvulnerable && invulnerableCounter<30){
            invulnerableCounter++;
        } else {
            isInvulnerable = false;
            invulnerableCounter = 0;
        }
    }

    private void checkCollision(){
        if (!isInvulnerable) {
            for (Enemy enemy : gamePanel.enemies) {
                if (collisionHandler.enemyCollision(enemy, this) && !enemy.hasAttacked && !enemy.isDead) {
                    enemy.hasAttacked = true;
                    health -= enemy.damage;
                    isInvulnerable = true;
                    soundManager.playClip(soundManager.hitClip);
                }
            }
        }
    }

    private void checkAlive(){
        if (health <= 0){
            this.isDead = true;
            soundManager.playClip(soundManager.deathClip);
            gamePanel.gameOver = true;
        }
    }

    void performAttack() {
        switch (gamePanel.selectedPieceType) {
            // Add new characters here
            case ROOK -> gamePanel.entityManager.spawnCannonBall();
            case QUEEN -> gamePanel.entityManager.spawnQueenParticles();
        }
    }
}
