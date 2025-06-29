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

    boolean projectileCollision(Enemy enemy, Projectile projectile) {
        boolean returnValue = false;
        if (gamePanel.selectedPieceType == PieceType.ROOK) {
            int speed = projectile.speed;
            if (projectile.x + projectile.width + speed > enemy.x &&
                    projectile.x - speed < enemy.x + enemy.width &&
                    projectile.y + projectile.height + speed > enemy.y &&
                    projectile.y - speed < enemy.y + enemy.height) {
                returnValue = true;
            }
        } else {
            int speed = projectile.speed;
            if (projectile.x + projectile.width + speed > enemy.x &&
                    projectile.x - speed < enemy.x + enemy.width &&
                    projectile.y + projectile.height + speed > enemy.y &&
                    projectile.y - speed < enemy.y + enemy.height) {
                returnValue = true;
            }
        }
        return returnValue;
    }
}
