public class EntityManager {
    GamePanel gamePanel;
    public EntityManager(GamePanel gamePanel) {
       this.gamePanel = gamePanel;
    }

    int CANNON_BALL_SIZE = 80;
    // Yeah this could get its own class later
    protected void spawnCannonBall() {
        if (gamePanel.rookImage != null) {
            int size = CANNON_BALL_SIZE; // size of the cannonball
            int rookWidth = gamePanel.rookImage.getWidth() * gamePanel.SCALE;
            // spawn at top‐center of the rook
            int bx = gamePanel.playerX + (rookWidth - size) / 2;
            int by = gamePanel.playerY;
            String direction = "right";
            if (gamePanel.goingDown) {
                direction = "down";
            }
            if (gamePanel.goingUp) {
                direction = "up";
            }
            if (gamePanel.goingLeft) {
                direction = "left";
            }
            if (gamePanel.goingRight) {
                direction = "right";
            }
            // Append balls to the list of balls
            gamePanel.balls.add(new CannonBall(bx, by, size, direction));
            // this should move to a variable
            gamePanel.playClip(gamePanel.shootClip);
        }
    }
    protected void spawnQueenParticles() {
        if (gamePanel.queenImage != null) {
            int size = 140; // size of the cannonball
            int queenWidth= gamePanel.queenImage.getWidth() * gamePanel.SCALE;
            int queenHeight = gamePanel.queenImage.getHeight() * gamePanel.SCALE;
            gamePanel.moveSpeed = gamePanel.DASH_SPEED;
            gamePanel.queenDashing = true;

            // spawn at top‐center of the rook
            int bx = gamePanel.playerX + (queenWidth - size) / 2;
            int by = gamePanel.playerY + (queenHeight - size) / 2;
            String direction = "right";
            if (gamePanel.goingDown) {
                direction = "down";
            }
            if (gamePanel.goingUp) {
                direction = "up";
            }
            if (gamePanel.goingLeft) {
                direction = "left";
            }
            if (gamePanel.goingRight) {
                direction = "right";
            }
            // Append balls to the list of balls
            gamePanel.particles.add(new Particle(bx, by, size, direction));
            // this should move to a variable
            gamePanel.playClip(gamePanel.sliceClip);
        }
    }
}
