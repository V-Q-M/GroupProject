package Allies;

import enemies.Enemy;
import main.*;

import java.util.Random;

public class Player extends AnimateObject{
    GamePanel gamePanel;
    KeyHandler keyHandler;
    SoundManager soundManager;
    public boolean isDead = false;
    CollisionHandler collisionHandler;
    public boolean isInvulnerable;
    private int invulnerableCounter;
    final int BASE_MOVE_SPEED = 8;
    public final int DASH_SPEED = 16;
    public final int LEAP_SPEED = 8;
    public boolean queenDashing = false;
    private int queenDashingCounter = 0;
    public int health = 100;

    public int swapCounter = 0;

    public String facingDirection = "down";

    private boolean hasAttacked = false;
    private int attackCoolDownCounter = 0;
    private int targetX;
    private int targetY;
    private boolean isMoving = false;


    public Player(GamePanel gamePanel, KeyHandler keyHandler, SoundManager soundManager,  CollisionHandler collisionHandler, int startPositionX, int startPositionY){
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        this.soundManager = soundManager;
        this.collisionHandler = collisionHandler;
        this.x = startPositionX;
        this.y = startPositionY;
        this.targetX = startPositionX;
        this.targetY = startPositionY;
        this.height = gamePanel.pieceHeight;
        this.width  = gamePanel.pieceWidth;
    }

    public void playerUpdate(){
        movement();
        checkCollision();
        coolDowns();
        checkAlive();
        forceSwap();
    }
    PieceType[] values = PieceType.values(); // Gets an array of enum constants
    Random random = new Random();
    private void forceSwap(){
        if (swapCounter >= 600) {
            int index = random.nextInt(values.length); // Pick a random index
            PieceType randomValue = values[index]; // Access by index
            System.out.println("Random Enum: " + randomValue);
            if (randomValue != PieceType.PAWN){
                gamePanel.selectPiece(randomValue);
            } else {
                gamePanel.selectPiece(PieceType.ROOK);
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



    private void movement() {
        System.out.println(targetX + " " + targetY);
        if (!isMoving) {
            analyzeInput();
        }

        if (x == targetX && y == targetY) {
            isMoving = false;
        } else {
            isMoving = true;
            if (facingDirection == "up") {
                y -= speed;
            }
            if (facingDirection == "down") {
                y += speed;
            }
            if (facingDirection == "left") {
                x -= speed;
            }
            if (facingDirection == "right") {
                x += speed;
            }
        }
    }
    private void analyzeInput(){
        if (keyHandler.goingUp) {
            facingDirection = "up";
            if (notReachedBorder()) {
                targetY-= gamePanel.PIECE_HEIGHT;
            }
        }
        if (keyHandler.goingDown) {
            facingDirection = "down";
            if (notReachedBorder()) {
                targetY+= gamePanel.PIECE_HEIGHT;
            }
        }
        if (keyHandler.goingLeft) {
            facingDirection = "left";
            if (notReachedBorder()) {
                x -= speed;
                targetX -= gamePanel.PIECE_HEIGHT;
            }
        }
        if (keyHandler.goingRight) {
            facingDirection = "right";
            if (notReachedBorder()) {
                targetX += gamePanel.PIECE_HEIGHT;
            }
        }
        if (keyHandler.spacePressed) {
            keyHandler.spacePressed = false;
            if (!hasAttacked) {
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
        x = ((x + 127) / 128) * 128;
        y = ((y + 127) / 128) * 128;
        targetX=x;
        targetY=y;
        switch (gamePanel.selectedPieceType) {
            // Add new characters here
            case ROOK   -> gamePanel.entityManager.spawnCannonBall();
            //case KNIGHT -> knightAttack();
            case KNIGHT   -> gamePanel.entityManager.spawnCannonBall();
            case BISHOP -> gamePanel.entityManager.spawnCannonBall();
            case KING   -> gamePanel.entityManager.spawnCannonBall();
            case QUEEN  -> gamePanel.entityManager.spawnQueenParticles();
        }
    }

    void knightAttack(){
        switch(facingDirection){
            case "up" -> {
                y -= gamePanel.pieceHeight * 2;
            }
            case "down" -> {
                y += gamePanel.pieceHeight * 2;
            }
            case "left" -> {
                x -= gamePanel.pieceWidth * 2;
            }
            case "right" -> {
                x += gamePanel.pieceWidth * 2;
            }
        }
        gamePanel.entityManager.spawnKnightParticles();
    }
}
