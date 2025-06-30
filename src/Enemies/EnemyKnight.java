package enemies;

import main.CollisionHandler;
import main.GamePanel;
import main.SoundManager;

public class EnemyKnight extends Enemy{
    public EnemyKnight(GamePanel gamePanel, SoundManager soundManager, CollisionHandler collisionHandler, int x, int y, int width, int height) {
        super(gamePanel, soundManager, collisionHandler, x, y, width, height);
        this.damage = 10;
        this.speed = 4;
        this.health = 100;
        this.skin = gamePanel.enemyKnightImage;
        this.attackCoolDown = 80;
    }
}
