package main;
import Allies.AllyPawn;
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

    public boolean projectileCollision(Enemy enemy, Projectile projectile) {
        return projectile.x + projectile.width > enemy.x &&
            projectile.x < enemy.x + enemy.width &&
            projectile.y + projectile.height > enemy.y &&
            projectile.y < enemy.y + enemy.height;
    }

    public boolean enemyCollision(Enemy enemy, Player player){
        return player.x + gamePanel.pieceWidth > enemy.x &&
            player.x < enemy.x + enemy.width &&
            player.y + gamePanel.pieceHeight > enemy.y &&
            player.y < enemy.y + enemy.height;
    }
    public boolean allyCollision(Enemy enemy, Enemy pawn){
        return pawn.x + pawn.width > enemy.x &&
            pawn.x < enemy.x + enemy.width &&
            pawn.y + pawn.height > enemy.y &&
            pawn.y < enemy.y + enemy.height;
    }
}
