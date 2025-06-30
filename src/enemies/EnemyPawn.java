package enemies;

import main.CollisionHandler;
import main.GamePanel;
import main.SoundManager;

import java.awt.image.BufferedImage;

public class EnemyPawn extends Enemy {
    public EnemyPawn(GamePanel gamePanel, SoundManager soundManager, CollisionHandler collisionHandler, int x, int y, int width, int height, BufferedImage skin) {
        super(gamePanel, soundManager, collisionHandler, x, y, width, height, skin);
        this.damage = 10;
        this.speed = 4;
        this.health = 100;

        this.attackCoolDown = 80;
    }
}
