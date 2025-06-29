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

    boolean projectileCollision(Enemy enemy, CannonBall ball) {
        int speed = ball.getBallSpeed();

        if (ball.x + ball.size + speed > enemy.x &&
                ball.x - speed < enemy.x + enemy.width &&
                ball.y + ball.size + speed > enemy.y &&
                ball.y - speed < enemy.y + enemy.height) {
            return true;
        } else {
            return false;
        }
    }
}
