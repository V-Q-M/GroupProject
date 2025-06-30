package enemies;

import main.CollisionHandler;
import main.GamePanel;
import main.SoundManager;

public class EnemyRook extends Enemy{
    public EnemyRook(GamePanel gamePanel, SoundManager soundManager, CollisionHandler collisionHandler, int x, int y, int width, int height) {
        super(gamePanel, soundManager, collisionHandler, x, y, width, height);
        this.damage = 10;
        this.speed = 4;
        this.health = 100;
        this.skin = gamePanel.enemyRookImage;
        this.attackCoolDown = 80;
    }
}
