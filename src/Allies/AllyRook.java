package Allies;

import main.CollisionHandler;
import main.GamePanel;
import main.SoundManager;

public class AllyRook extends Ally {

    public AllyRook(GamePanel gamePanel, SoundManager soundManager, CollisionHandler collisionHandler, int x, int y, int width, int height) {
        super(gamePanel, soundManager, collisionHandler, x, y, width, height);
        this.damage = 10;
        this.speed = 3;
        this.health = 150;
        this.maxHealth = 150;
        this.baseSkin = gamePanel.rookImage;
        this.hurtSkin = gamePanel.rookHurtImage;
        this.skin = baseSkin;
        this.attackCoolDown = 200;
        this.attackCoolDownCounter = (int) (Math.random() * 100); // Makes it so that the rooks arent shooting in sync (looks goofy)
    }

    @Override
    void updateCooldowns(){
        if (isInvulnerable){
            if (invulnerableCounter >= recoveryTime){
                isInvulnerable = false;
                invulnerableCounter = 0;
            } else if (invulnerableCounter > recoveryMarkerTime){
                this.skin = baseSkin;
            }
            invulnerableCounter++;
        }

        if (attackCoolDownCounter > attackCoolDown){
            performAttack();
            hasAttacked = false;
            attackCoolDownCounter = 0;
        } else {
            attackCoolDownCounter++;
        }
    }

    private void performAttack(){
        gamePanel.entityManager.spawnCannonBall(x, y, "right");
    }
}
