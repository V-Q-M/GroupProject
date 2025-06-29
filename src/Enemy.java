import java.awt.image.BufferedImage;

public class Enemy {
    GamePanel gamePanel;
    CollisionHandler collisionHandler;
    public int x, y;
    public int width;
    public int height;
    boolean dead = false;
    boolean isInvulnerable = false;
    int invulnerableCounter = 0;

    int health = 100;
    int speed;
    int damage;

    BufferedImage skin;

    public Enemy(GamePanel gamePanel, CollisionHandler collisionHandler, int x, int y, int width, int height, BufferedImage skin) {
        this.gamePanel = gamePanel;
        this.collisionHandler = collisionHandler;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.skin = skin;
    }

    public void update(){
        checkAlive();
        checkCollision();
        updateCooldowns();
    }

    private void checkAlive(){
        if (health <= 0){
            this.dead = true;
        }
    }

    private void updateCooldowns(){
        if (isInvulnerable && invulnerableCounter<30){
            invulnerableCounter++;
        } else {
           isInvulnerable = false;
           invulnerableCounter = 0;
        }
    }

    private void checkCollision(){
        if (!isInvulnerable) {
            for (CannonBall ball : gamePanel.balls) {
                if (collisionHandler.projectileCollision(this, ball)) {
                    health -= 50;
                    isInvulnerable = true;
                }
            }
        }
    }
}
