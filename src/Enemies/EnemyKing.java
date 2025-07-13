package enemies;

import main.CollisionHandler;
import main.GamePanel;
import main.Main;
import main.SoundManager;

public class EnemyKing extends Enemy{
    public EnemyKing(GamePanel gamePanel, SoundManager soundManager, CollisionHandler collisionHandler, int x, int y, int width, int height) {
        super(gamePanel, soundManager, collisionHandler, x, y, width, height);
        this.damage = 10;
        this.speed = 2;
        this.maxHealth = 300;
        this.health = 300;
        this.baseSkin = gamePanel.enemyKingImage;
        this.hurtSkin = gamePanel.enemyKingHurtImage;
        this.skin = baseSkin;
        this.attackCoolDown = 80;
        this.isKing = true;
    }


    @Override
    void checkAlive(){
        if (health <= 0){
            this.isDead = true;
            gamePanel.score+=maxHealth;
            gamePanel.enemyKingSlain = true;
            soundManager.playClip(soundManager.deathClip);
        }
    }

    @Override
    public void move(){
        if (x < Main.WIDTH - 382){
            //allowAttack = true;
        } else {
            x -= speed;
        }

        if (y < 0){
            y += speed;
        } else if (y > 8 * gamePanel.PIECE_HEIGHT){
            y = 8 * gamePanel.PIECE_HEIGHT;
        }
    }
}
