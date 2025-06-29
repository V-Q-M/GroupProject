public class CollisionHandler {
    GamePanel gamePanel;
    Player player;
    public CollisionHandler(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    boolean playerCollision(int playerX, int playerY, int playerWidth, int playerHeight, int speed, String direction){
        switch (direction){
            case "up" -> {
                return (playerY - speed <= 0);
            }
            case "down" -> {
                return (playerY + playerHeight + speed >= Main.HEIGHT);
            }
            case "left" -> {
                return (playerX - speed <= 0);
            }
            case "right" -> {
                return (playerX + playerWidth + speed >= Main.WIDTH);
            }
        }

        return false;
    }
}
