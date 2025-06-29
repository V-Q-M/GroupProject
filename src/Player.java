public class Player {
    GamePanel gamePanel;
    KeyHandler keyHandler;
    SoundManager soundManager;
    boolean isDead = false;
    CollisionHandler collisionHandler;
    boolean isInvulnerable;
    private int invulnerableCounter;
    int playerX;
    int playerY;
    final int BASE_MOVE_SPEED = 5;
    final int DASH_SPEED = 18;
    int moveSpeed;
    boolean queenDashing = false;
    private int queenDashingCounter = 0;
    public int health = 100;

    String facingDirection = "right";

    private boolean hasAttacked = false;
    private int attackCoolDownCounter = 0;


    public Player(GamePanel gamePanel, KeyHandler keyHandler, SoundManager soundManager,  CollisionHandler collisionHandler, int startPositionX, int startPositionY){
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        this.soundManager = soundManager;
        this.collisionHandler = collisionHandler;
        this.playerX = startPositionX;
        this.playerY = startPositionY;
    }

    void playerUpdate(){
        movement();
        checkCollision();
        coolDowns();
        checkAlive();
    }

    private boolean notReachedBorder(){
        return !collisionHandler.borderCollision(playerX, playerY, gamePanel.pieceWidth, gamePanel.pieceHeight, moveSpeed, facingDirection);
    }

    private void movement(){

        if (keyHandler.goingUp) {
            facingDirection = "up";
            if (notReachedBorder()) {
                playerY -= moveSpeed;
            }
        }
        if (keyHandler.goingDown) {
            facingDirection = "down";
            if (notReachedBorder()) {
                playerY += moveSpeed;
            }
        }
        if (keyHandler.goingLeft) {
            facingDirection = "left";
            if (notReachedBorder()) {
                playerX -= moveSpeed;
            }
        }
        if (keyHandler.goingRight) {
            facingDirection = "right";
            if (notReachedBorder()) {
                playerX += moveSpeed;
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
            moveSpeed = BASE_MOVE_SPEED;
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
                    health -= 10;
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
