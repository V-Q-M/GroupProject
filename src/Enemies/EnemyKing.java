package enemies;

import main.*;

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
        this.attackCoolDown = 1000;
        this.attackCoolDownCounter = 0;
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

    boolean allowAttack = false;
    @Override
    public void move(){
        if (x < Main.WIDTH - 382){
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

        if (isInvulnerable){
            if (invulnerableCounter >= recoveryTime){
                isInvulnerable = false;
                invulnerableCounter = 0;
            } else if (invulnerableCounter > recoveryMarkerTime) {
                this.skin = baseSkin;
            }
            invulnerableCounter ++;
        }

        if (this.attackCoolDownCounter > this.attackCoolDown){
            performAttack();
            hasAttacked = false;
            this.attackCoolDownCounter = 0;
        } else {
            this.attackCoolDownCounter++;
        }
    }

    private void performAttack() {
        gamePanel.enemyManager.spawnKingsGuard(x-128,y,width);
    }

}
