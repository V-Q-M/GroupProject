package entities;

import main.GamePanel;

public class EnemyCannonBall extends Projectile {

    public EnemyCannonBall(GamePanel gamePanel, int x, int y, int size, int speed, int damage) {
        this.gamePanel = gamePanel;
        this.x = x;
        this.y = y;
        this.width = size;
        this.height = size;
        this.gamePanel = gamePanel;
        this.health = 200;
        this.speed = speed;
        this.damage = damage;
        this.skin = gamePanel.cannonBallImage;
    }

    @Override
    public void update(){
        checkAlive();
        animateBall();
        moveProjectile(speed);
    }
    int animateCounter = 0;

    public void animateBall(){
        if (animateCounter > 30) {
            animateCounter = 0;
            this.y += 12;
        } else {
            animateCounter++;
        }
    }
    @Override
    public void moveProjectile(int speed) {
        x -= speed;
    }

    @Override
    public void checkAlive(){
        if (health <= 0){
            isDead = true;
        } else {
            health--;
        }
        if (x < 0){ // Has hit the castle
            isDead = true;
            gamePanel.castleHealth -= 10;
        }
    }
}
