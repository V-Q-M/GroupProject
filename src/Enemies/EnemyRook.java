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
        this.baseSkin = gamePanel.enemyRookImage;
        this.hurtSkin = gamePanel.enemyRookHurtImage;
        this.skin = baseSkin;
        this.attackCoolDown = 300;
        this.attackCoolDownCounter = 0;
    }

    boolean allowAttack = false;
    @Override
    public void move(){
        if (x < Main.WIDTH - 508){
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
    }

    @Override
    void updateCooldowns(){
        if (isInvulnerable && invulnerableCounter<45){
            invulnerableCounter++;
        } else {
            isInvulnerable = false;
            invulnerableCounter = 0;
            this.skin = baseSkin;
        }

        if (attackCoolDownCounter > attackCoolDown){
            performAttack();
            hasAttacked = false;
            attackCoolDownCounter = 0;
        } else {
            attackCoolDownCounter++;
        }
    }

    private void performAttack() {
        gamePanel.entityManager.spawnEnemyCannonBall(x, y);
    }
}
