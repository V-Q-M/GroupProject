package Allies;

import enemies.Enemy;
import main.CollisionHandler;
import main.GamePanel;
import main.Main;
import main.SoundManager;

public class AllyPawn extends Ally {

    public AllyPawn(GamePanel gamePanel, SoundManager soundManager, CollisionHandler collisionHandler, int x, int y, int width, int height, boolean canMove) {
        super(gamePanel, soundManager, collisionHandler, x, y, width, height);
        this.damage = 10;
        this.speed = 3;
        this.health = 100;
        this.skin = gamePanel.pawnImage;
        this.attackCoolDown = 80;
        this.canMove = canMove;
    }
}
