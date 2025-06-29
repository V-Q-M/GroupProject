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

    public Player(GamePanel gamePanel, KeyHandler keyHandler, int startPositionX, int startPositionY){
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        this.playerX = startPositionX;
        this.playerY = startPositionY;
    }

    void playerUpdate(){
        int speed = moveSpeed;

        if (keyHandler.goingUp)
            playerY -= speed;
        if (keyHandler.goingDown)
            playerY += speed;
        if (keyHandler.goingLeft)
            playerX -= speed;
        if (keyHandler.goingRight)
            playerX += speed;

        if (queenDashing && queenDashingCounter <= 20){
            queenDashingCounter ++;
        } else {
            queenDashing = false;
            queenDashingCounter = 0;
            moveSpeed = BASE_MOVE_SPEED;
        }
    }
}
