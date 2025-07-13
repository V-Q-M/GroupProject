package Allies;

import enemies.Enemy;
import entities.Projectile;
import main.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player extends livingBeing {
    GamePanel gamePanel;
    KeyHandler keyHandler;
    SoundManager soundManager;
    CollisionHandler collisionHandler;

    public boolean isDead = false;
    // Is used to avoid being attacked a billion times at once
    public boolean isInvulnerable;
    private int invulnerableCounter;

    final int BASE_MOVE_SPEED = 6;
    public final int DASH_SPEED = 16;
    public final int LEAP_SPEED = 8;

    // Ability Cooldowns
    private final int ROOK_ABILITY_COOLDOWN = 50;
    private final int KNIGHT_ABILITY_COOLDOWN = 150;
    private final int QUEEN_ABILITY_COOLDOWN = 35;
    private final int KING_ABILITY_COOLDOWN = 250;

    // Initializes it
    public int abilityCoolDown = ROOK_ABILITY_COOLDOWN;

    // Used for the queen ability
    public boolean queenDashing = false;
    private int queenDashingCounter = 0;

    // Castlehealth
    public int health = 100;

    // Are the pieces alive?
    public boolean rookAlive = true;
    public boolean bishopAlive = true;
    public boolean knightAlive = true;
    public boolean queenAlive = true;
    public boolean kingAlive = true;

    // Base health constants
    public final int ROOK_BASE_HEALTH = 25;
    public final int KNIGHT_BASE_HEALTH = 50;
    public final int BISHOP_BASE_HEALTH = 20;
    public final int QUEEN_BASE_HEALTH = 50;
    public final int KING_BASE_HEALTH = 30;
    public final int PAWN_BASE_HEALTH = 10;

    // Mutable health values
    public int rookHealth = ROOK_BASE_HEALTH;
    public int knightHealth = KNIGHT_BASE_HEALTH;
    public int bishopHealth = BISHOP_BASE_HEALTH;
    public int queenHealth = QUEEN_BASE_HEALTH;
    public int kingHealth = KING_BASE_HEALTH;
    public int pawnHealth = PAWN_BASE_HEALTH;

    // Used in the swap feature
    public int swapCounter = 0;

    public String facingDirection = "down";
    public String facingDirectionX = "right";

    private boolean hasAttacked = false;
    private int attackCoolDownCounter = 0;

    // Very important. Manages the grid based walking
    public int targetX;
    public int targetY;
    private boolean isMoving = false;

    // Used in the swap feature
    Random random = new Random();
    ArrayList<PieceType> availablePieces = new ArrayList<>(List.of(
            PieceType.ROOK,
            PieceType.KNIGHT,
            PieceType.BISHOP,
            PieceType.QUEEN,
            PieceType.KING
    ));
    PieceType lastPiece;

    public Player(GamePanel gamePanel, KeyHandler keyHandler, SoundManager soundManager, CollisionHandler collisionHandler, int startPositionX, int startPositionY){
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
        this.speed = BASE_MOVE_SPEED;
    }

    public void playerUpdate(){
        movement();
        checkCollision();
        checkProjectileCollision();
        coolDowns();
        checkAlive();
        prepareForcedSwap();
    }

    private void forceSwap(){
        if (lastPiece != null && availablePieces.size() >= 1){
            availablePieces.add(lastPiece);
        }
        int index = random.nextInt(availablePieces.size()); // Pick a random index
        PieceType randomValue = availablePieces.get(index); // Access by index
        lastPiece = randomValue;
        System.out.println("Random Enum: " + randomValue);
        if (availablePieces.size() >= 2){
           availablePieces.remove(randomValue);
        }
        selectPiece(randomValue);
        swapCounter = 0;
        attackCoolDownCounter = 0;
        hasAttacked = false;
    }

    private void prepareForcedSwap(){
        if (swapCounter >= 600) {
            forceSwap();
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

    public void selectPiece(PieceType changePiece) {
        gamePanel.selectedPieceType = changePiece;
        swapCounter = 0;
        soundManager.playClip(soundManager.swapClip);
        gamePanel.swapSoon = false;
        switch (changePiece) {
            case PieceType.ROOK -> {

                //gamePanel.selectedPiece = gamePanel.rookImage;
                this.baseSkin = gamePanel.rookImage;
                this.hurtSkin = gamePanel.rookHurtImage;

                abilityCoolDown = ROOK_ABILITY_COOLDOWN;
            }
            case PieceType.QUEEN -> {
                //gamePanel.selectedPiece = gamePanel.queenImage;
                abilityCoolDown = QUEEN_ABILITY_COOLDOWN;
                this.baseSkin = gamePanel.queenImage;
                this.hurtSkin = gamePanel.queenHurtImage;
            }
            case PieceType.KING -> {
                //gamePanel.selectedPiece = gamePanel.kingImage;
                abilityCoolDown = KING_ABILITY_COOLDOWN;
                this.baseSkin = gamePanel.kingImage;
                this.hurtSkin = gamePanel.kingHurtImage;
            }
            case PieceType.KNIGHT -> {
                //gamePanel.selectedPiece = gamePanel.knightImage;
                abilityCoolDown = KNIGHT_ABILITY_COOLDOWN;
                this.baseSkin = gamePanel.knightImage;
                this.hurtSkin = gamePanel.knightHurtImage;
            }
            case PieceType.BISHOP -> {
                //gamePanel.selectedPiece = gamePanel.bishopImage;
                abilityCoolDown = ROOK_ABILITY_COOLDOWN;
                this.baseSkin = gamePanel.bishopImage;
                this.hurtSkin = gamePanel.bishopHurtImage;
            }
        }
        this.skin = baseSkin;
        //gamePanel.pieceHeight = gamePanel.selectedPiece.getHeight() * gamePanel.SCALE;
        gamePanel.pieceWidth = skin.getWidth() * gamePanel.SCALE;
        gamePanel.pieceHeight = skin.getHeight() * gamePanel.SCALE;
    }


    private boolean reachedBorder(){
        return collisionHandler.borderCollision(x, y, gamePanel.pieceWidth, gamePanel.pieceHeight, speed, facingDirection);
    }

    private void movement() {
        if (!isMoving) {
            if (gamePanel.selectedPieceType == PieceType.BISHOP){ // Is extra because he can only move diagonally
                analyzeInputBishop();
            } else {
                analyzeInput();
            }
        }

        // Grid based movement
        if (x == targetX && y == targetY) {
            isMoving = false;
        } else {
            isMoving = true;

            if (y < targetY) y = Math.min(y + speed, targetY);
            if (y > targetY) y = Math.max(y - speed, targetY);
            if (x < targetX) x = Math.min(x + speed, targetX);
            if (x > targetX) x = Math.max(x - speed, targetX);
        }
    }
    private void analyzeInput() {
        int deltaX = 0;
        int deltaY = 0;
        // Prioritize diagonal movement
        if (keyHandler.goingUp) deltaY -= gamePanel.PIECE_HEIGHT;
        if (keyHandler.goingDown) deltaY += gamePanel.PIECE_HEIGHT;
        if (keyHandler.goingLeft) deltaX -= gamePanel.PIECE_HEIGHT;
        if (keyHandler.goingRight) deltaX += gamePanel.PIECE_HEIGHT;

        if ((deltaX != 0 || deltaY != 0) && !reachedBorder()) {
            targetX += deltaX;
            targetY += deltaY;
            isMoving = true;
        }

        // Set facing direction based on input (last key pressed takes priority)
        if (deltaY < 0) {
            facingDirection = "up";
        }
        else if (deltaY > 0) {
            facingDirection = "down";
        }
        if (deltaX < 0) {
            facingDirection = "left";
            facingDirectionX = "left";
        }
        else if (deltaX > 0) {
            facingDirection = "right";
            facingDirectionX = "right";
        }

        // Handle attack input
        if (keyHandler.spacePressed) {
            keyHandler.spacePressed = false;
            if (!hasAttacked) {
                performAttack();
                hasAttacked = true;
            }
        }
    }

    // Bishop has diagonal only movement
    private void analyzeInputBishop() {
        int deltaX = 0;
        int deltaY = 0;

        boolean up = keyHandler.goingUp;
        boolean down = keyHandler.goingDown;
        boolean left = keyHandler.goingLeft;
        boolean right = keyHandler.goingRight;

        // Only allow diagonal movement (both one vertical and one horizontal key must be pressed)
        if (up && left) {
            deltaY -= gamePanel.PIECE_HEIGHT;
            deltaX -= gamePanel.PIECE_HEIGHT;
            facingDirection = "up-left";
        } else if (up && right) {
            deltaY -= gamePanel.PIECE_HEIGHT;
            deltaX += gamePanel.PIECE_HEIGHT;
            facingDirection = "up-right";
        } else if (down && left) {
            deltaY += gamePanel.PIECE_HEIGHT;
            deltaX -= gamePanel.PIECE_HEIGHT;
            facingDirection = "down-left";
        } else if (down && right) {
            deltaY += gamePanel.PIECE_HEIGHT;
            deltaX += gamePanel.PIECE_HEIGHT;
            facingDirection = "down-right";
        }

        // Only move if diagonal keys were pressed
        if ((deltaX != 0 && deltaY != 0) && !reachedBorder()) {
            targetX += deltaX;
            targetY += deltaY;
            isMoving = true;
        }

        // Handle attack input
        if (keyHandler.spacePressed) {
            keyHandler.spacePressed = false;
            if (!hasAttacked) {
                performAttack();
                hasAttacked = true;
            }
        }
    }

    private void coolDowns(){
        if (isInvulnerable){
            if (invulnerableCounter >= recoveryTime){
                isInvulnerable = false;
                invulnerableCounter = 0;
            } else if (invulnerableCounter > recoveryMarkerTime) {
                this.skin = baseSkin;
            }
            invulnerableCounter ++;
        }

        if (queenDashing){
           if (queenDashingCounter <= 30) {
               queenDashingCounter ++;
           } else {
               queenDashing = false;
               queenDashingCounter = 0;
               //isInvulnerable = false;
               speed = BASE_MOVE_SPEED;
           }
        }

        if (hasAttacked && attackCoolDownCounter < abilityCoolDown){
            attackCoolDownCounter++;
        } else {
            hasAttacked = false;
            attackCoolDownCounter = 0;
        }



    }

    private void checkCollision(){
        if (!isInvulnerable) {
            for (Enemy enemy : gamePanel.enemies) {
                if (collisionHandler.enemyCollision(enemy, this) && !enemy.hasAttacked) {
                    enemy.hasAttacked = true;
                    takeDamage(enemy.damage);
                    isInvulnerable = true;
                    this.skin = hurtSkin;
                    soundManager.playClip(soundManager.hitClip);
                }
            }
        }
    }

    private void checkProjectileCollision(){
        for (Projectile projectile : gamePanel.enemyBalls){
            if (collisionHandler.projectileEnemyCollision(projectile, this)){
                takeDamage(15);
                projectile.isDead = true;
                gamePanel.entityManager.spawnExplosion(projectile.x, projectile.y);
                soundManager.playClip(soundManager.hitClip);
            }
        }
    }

    private void takeDamage(int damageAmount){
        switch(gamePanel.selectedPieceType){
            case ROOK -> rookHealth -= damageAmount;
            case KNIGHT -> knightHealth -= damageAmount;
            case BISHOP -> bishopHealth -= damageAmount;
            case QUEEN -> queenHealth -= damageAmount;
            case KING -> kingHealth -= damageAmount;
            case PAWN -> pawnHealth -= damageAmount;
        }
    }

    private void checkAlive(){
        if (health <= 0){
            this.isDead = true;
            soundManager.playClip(soundManager.deathClip);
            gamePanel.gameOver = true;
        }

        if (rookHealth <= 0 && rookAlive){
            rookAlive = false;
            availablePieces.remove(PieceType.ROOK);
            lastPiece = null;
            forceSwap();
        }
        if (knightHealth <= 0 && knightAlive){
            knightAlive = false;
            availablePieces.remove(PieceType.KNIGHT);
            lastPiece = null;
            forceSwap();
        }
        if (bishopHealth <= 0 && bishopAlive){
            bishopAlive = false;
            availablePieces.remove(PieceType.BISHOP);
            lastPiece = null;
            forceSwap();
        }
        if (queenHealth <= 0 && queenAlive){
            queenAlive = false;
            availablePieces.remove(PieceType.QUEEN);
            lastPiece = null;
            forceSwap();
        }
        // specialcase - king dead
        if (kingHealth <= 0 && kingAlive){
            kingAlive = false;
            availablePieces.remove(PieceType.KING);
            lastPiece = null;
            health = 0;
        }
    }

    void performAttack() {
        // should be obsolete
        //x = ((x + 127) / 128) * 128;
        //y = ((y + 127) / 128) * 128;
        //targetX=x;
        //targetY=y;
        switch (gamePanel.selectedPieceType) {
            // Add new characters here
            case ROOK   -> gamePanel.entityManager.spawnCannonBall(x, y, facingDirection);
            case BISHOP -> performBishopAttack();
            case QUEEN  -> performQueenAttack();
            case KNIGHT -> performKnightAttack();
            case KING   -> performKingAttack();
        }
    }

    private void performBishopAttack(){
        BufferedImage skin;
        switch(facingDirection){
            case "up-left" -> {
                skin = gamePanel.bishopParticleImageUpLeft;
            }
            case "up-right" -> {
                skin = gamePanel.bishopParticleImageUpRight;
            }
            case "down-left" -> {
                skin = gamePanel.bishopParticleImageDownLeft;
            }
            default -> {
                skin = gamePanel.bishopParticleImageDownRight;
            }
        }


        gamePanel.entityManager.spawnLance(skin);
    }

    private void performQueenAttack(){
        BufferedImage skin;

        speed = DASH_SPEED;
        queenDashing = true;
        isInvulnerable = true;
        switch(facingDirection){
            case "up" -> {
                targetY -= gamePanel.PIECE_HEIGHT * 3;
                skin = gamePanel.queenParticleImageUp;
            }
            case "down" -> {
                targetY += gamePanel.PIECE_HEIGHT * 3;
                skin = gamePanel.queenParticleImageDown;
            }
            case "left" -> {
                targetX -= gamePanel.PIECE_HEIGHT * 3;
                skin = gamePanel.queenParticleImageLeft;
            }
            default -> {
                targetX += gamePanel.PIECE_HEIGHT * 3;
                skin = gamePanel.queenParticleImageRight;
            }
        }

        gamePanel.entityManager.spawnQueenParticles(skin);
    }

    private void performKnightAttack(){

        switch (facingDirection){
            case "up" -> {
                targetY -= 2 * gamePanel.pieceHeight;
                y = targetY;
            }
            case "down" -> {
                targetY += 2 * gamePanel.pieceHeight;
                y = targetY;
            }
            case "left" -> {
                targetX -= 2 * gamePanel.pieceHeight;
                x = targetX;
            }
            case "right" -> {
                targetX += 2 * gamePanel.pieceHeight;
                x = targetX;
            }
        }
        gamePanel.entityManager.spawnKnightParticles();
    }

    private void performKingAttack(){
        soundManager.playClip(soundManager.summonClip);
        gamePanel.entityManager.spawnPawns(x , y - gamePanel.pieceHeight);
        soundManager.playClip(soundManager.summonClip);
        gamePanel.entityManager.spawnPawns(x + gamePanel.pieceWidth, y);
        soundManager.playClip(soundManager.summonClip);
        gamePanel.entityManager.spawnPawns(x , y + gamePanel.pieceHeight);
    }
}
