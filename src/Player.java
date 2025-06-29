public class Player {
    GamePanel gamePanel;
    KeyHandler keyHandler;
    CollisionHandler collisionHandler;
    int playerX;
    int playerY;
    final int BASE_MOVE_SPEED = 5;
    final int DASH_SPEED = 18;
    int moveSpeed;
    boolean queenDashing = false;
    private int queenDashingCounter = 0;
    public int health = 60;

    String facingDirection = "right";

    private boolean onCoolDown = false;
    private int coolDownCounter = 0;

    public Player(GamePanel gamePanel, KeyHandler keyHandler,  CollisionHandler collisionHandler, int startPositionX, int startPositionY){
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        this.collisionHandler = collisionHandler;
        this.playerX = startPositionX;
        this.playerY = startPositionY;
    }

    void playerUpdate(){
        movement();
        coolDowns();
    }

    private boolean playerNotColliding(){
        return !collisionHandler.playerCollision(playerX, playerY, gamePanel.pieceWidth, gamePanel.pieceHeight, moveSpeed, facingDirection);
    }

    private void movement(){

        if (keyHandler.goingUp) {
            facingDirection = "up";
            if (playerNotColliding()) {
                playerY -= moveSpeed;
            }
        }
        if (keyHandler.goingDown) {
            facingDirection = "down";
            if (playerNotColliding()) {
                playerY += moveSpeed;
            }
        }
        if (keyHandler.goingLeft) {
            facingDirection = "left";
            if (playerNotColliding()) {
                playerX -= moveSpeed;
            }
        }
        if (keyHandler.goingRight) {
            facingDirection = "right";
            if (playerNotColliding()) {
                playerX += moveSpeed;
            }
        }
        if (keyHandler.spacePressed) {
            keyHandler.spacePressed = false;
            if (!onCoolDown){
                performAttack();
                onCoolDown = true;
            }
        }
    }

    private void coolDowns(){
        if (queenDashing && queenDashingCounter <= 20){
            queenDashingCounter ++;
        } else {
            queenDashing = false;
            queenDashingCounter = 0;
            moveSpeed = BASE_MOVE_SPEED;
        }

        if (onCoolDown && coolDownCounter < gamePanel.abilityCoolDown){
            coolDownCounter++;
        } else {
            onCoolDown = false;
            coolDownCounter = 0;
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
