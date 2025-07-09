package enemies;

import main.CollisionHandler;
import main.GamePanel;
import main.Main;
import main.SoundManager;

public class EnemyRook extends Enemy{
    public EnemyRook(GamePanel gamePanel, SoundManager soundManager, CollisionHandler collisionHandler, int x, int y, int width, int height) {
        super(gamePanel, soundManager, collisionHandler, x, y, width, height);
        this.damage = 10;
        this.speed = 2;
        this.health = 150;
        this.maxHealth = 150;
        this.skin = gamePanel.enemyRookImage;
        this.attackCoolDown = 120;
    }

    boolean allowAttack = false;
    @Override
    public void move(){
        if (x < Main.WIDTH - 507){
            allowAttack = true;
        } else {
            x -= speed;
        }

        if (y < 0){
            y += speed;
        } else if (y > 8 * gamePanel.PIECE_HEIGHT){
            y = 8 * gamePanel.PIECE_HEIGHT;
        }
    }

    @Override
    public void update(){
        checkAlive();
        move();
        checkPlayerCollision();
        checkPawnWallCollision();
        updateCooldowns();
        performAttack();
    }

    int attackCoolDownCounter = 0;
    public void performAttack(){
       if (allowAttack) {
           if (attackCoolDownCounter > attackCoolDown){
               attackCoolDownCounter = 0;
               System.out.println("ROOK ATTACKS");
           } else {
               attackCoolDownCounter++;
           }
       }
    }
}
