package main;
import Allies.Ally;
import enemies.Enemy;
import entities.Projectile;
import Allies.Player;

public class CollisionHandler {
    GamePanel gamePanel;
    public CollisionHandler(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    public boolean borderCollision(int playerX, int playerY, int playerWidth, int playerHeight, int speed, String direction){
        switch (direction){
            case "up" -> {
                return (playerY - speed <= 0);
            }
            case "down" -> {
                return (playerY + playerHeight + speed >= Main.HEIGHT - 56);
            }
            case "left" -> {
                return (playerX - speed <= 0);
            }
            case "right" -> {
                return (playerX + playerWidth + speed >= Main.WIDTH);
            }
            case "up-right" -> {
                return (playerY - speed <= 0) || (playerX + playerWidth + speed >= Main.WIDTH);
            }
            case "up-left" -> {
                return (playerY - speed <= 0) || (playerX - speed <= 0);
            }
            case "down-right" -> {
                return (playerY + playerHeight + speed >= Main.HEIGHT - 56) ||
                        (playerX + playerWidth + speed >= Main.WIDTH);
            }
            case "down-left" -> {
                return (playerY + playerHeight + speed >= Main.HEIGHT - 56) ||
                        (playerX - speed <= 0);
            }
        }
        return false;
    }

    public boolean projectileCollision(livingBeing enemy, Projectile projectile) {
        return projectile.x + projectile.width > enemy.x &&
            projectile.x < enemy.x + enemy.width &&
            projectile.y + projectile.height > enemy.y + 10 &&
            projectile.y < enemy.y + enemy.height - 10;
    }


    public boolean projectileEnemyCollision(Projectile projectile, Player player){
        return player.x + gamePanel.pieceWidth > projectile.x &&
                player.x < projectile.x + projectile.width &&
                player.y + gamePanel.pieceHeight > projectile.y &&
                player.y < projectile.y + projectile.height;
    }

    public boolean enemyCollision(Enemy enemy, Player player){
        return player.x + gamePanel.pieceWidth > enemy.x &&
            player.x < enemy.x + enemy.width &&
            player.y + gamePanel.pieceHeight > enemy.y &&
            player.y < enemy.y + enemy.height;
    }
    public boolean allyCollision(Enemy enemy, Ally pawn){
        return pawn.x + pawn.width > enemy.x &&
            pawn.x < enemy.x + enemy.width &&
            pawn.y + pawn.height > enemy.y + 10 &&
            pawn.y < enemy.y + enemy.height - 10;
    }
}
