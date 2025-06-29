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
    final int DEFAULT_CANNON_BALL_DMG = 35;
    final int DEFAULT_SLICE_DMG= 50;
    int speed = 3;
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
        move();
        checkCollision();
        updateCooldowns();
    }

    private void checkAlive(){
        if (health <= 0){
            this.dead = true;
        }
    }


    private void move(){
        if (x < 0){
            health = 0;
        } else {
            x -= speed;
        }

        if (y < 100){
            y += speed;
        } else if (y > Main.HEIGHT - height - 100){
            y -= speed;
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
            if(gamePanel.selectedPieceType == PieceType.ROOK) {
                for (CannonBall ball : gamePanel.balls) {
                    if (collisionHandler.projectileCollision(this, ball)) {
                        health -= DEFAULT_CANNON_BALL_DMG;
                        isInvulnerable = true;
                        ball.hasHit = true;
                    }
                }
            } else {
                for (Particle particle: gamePanel.particles) {
                    if ( collisionHandler.projectileCollision(this, particle)){
                        health -= DEFAULT_SLICE_DMG;
                        isInvulnerable = true;
                    }
                }
            }
        }
    }
}
