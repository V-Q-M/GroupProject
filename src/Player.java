public class Player {
    GamePanel gamePanel;
    KeyHandler keyHandler;
    int playerX;
    int playerY;
    final int BASE_MOVE_SPEED = 5;
    final int DASH_SPEED = 18;
    int moveSpeed;
    boolean queenDashing = false;
    private int queenDashingCounter = 0;

    String facingDirection = "right";

    private boolean onCoolDown = false;
    private int coolDownCounter = 0;

    public Player(GamePanel gamePanel, KeyHandler keyHandler, int startPositionX, int startPositionY){
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        this.playerX = startPositionX;
        this.playerY = startPositionY;
    }

    void playerUpdate(){
        movement();
        coolDowns();
    }

    private void movement(){
        int speed = moveSpeed;

        if (keyHandler.goingUp) {
            playerY -= speed;
            facingDirection = "up";
        }
        if (keyHandler.goingDown) {
            playerY += speed;
            facingDirection = "down";
        }
        if (keyHandler.goingLeft) {
            playerX -= speed;
            facingDirection = "left";
        }
        if (keyHandler.goingRight) {
            playerX += speed;
            facingDirection = "right";
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
