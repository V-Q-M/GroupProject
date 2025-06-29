public class EntityManager {
    GamePanel gamePanel;
    SoundManager soundManager;
    KeyHandler keyHandler;
    Player player;
    String direction = "right";

    public EntityManager(GamePanel gamePanel, KeyHandler keyHandler, SoundManager soundManager, Player player) {
       this.gamePanel = gamePanel;
       this.soundManager = soundManager;
       this.keyHandler = keyHandler;
       this.player = player;
    }

    int CANNON_BALL_SIZE = 80;
    // Yeah this could get its own class later
    protected void spawnCannonBall() {
        if (gamePanel.rookImage != null) {
            int size = CANNON_BALL_SIZE; // size of the cannonball
            int rookWidth = gamePanel.rookImage.getWidth() * gamePanel.SCALE;
            // spawn at top‐center of the rook
            int bx = player.playerX + (rookWidth - size) / 2;
            int by = player.playerY;
            // Append balls to the list of balls
            gamePanel.balls.add(new CannonBall(bx, by, size, player.facingDirection));
            // this should move to a variable
            soundManager.playClip(soundManager.shootClip);
        }
    }
    protected void spawnQueenParticles() {
        if (gamePanel.queenImage != null) {
            int size = 140; // size of the cannonball
            int queenWidth= gamePanel.queenImage.getWidth() * gamePanel.SCALE;
            int queenHeight = gamePanel.queenImage.getHeight() * gamePanel.SCALE;
            player.moveSpeed = player.DASH_SPEED;
            player.queenDashing = true;

            // spawn at top‐center of the rook
            int bx = player.playerX + (queenWidth - size) / 2;
            int by = player.playerY + (queenHeight - size) / 2;
            String direction = "right";
            if (keyHandler.goingDown) {
                direction = "down";
            }
            if (keyHandler.goingUp) {
                direction = "up";
            }
            if (keyHandler.goingLeft) {
                direction = "left";
            }
            if (keyHandler.goingRight) {
                direction = "right";
            }
            // Append balls to the list of balls
            gamePanel.particles.add(new Particle(bx, by, size, direction));
            // this should move to a variable
            soundManager.playClip(soundManager.sliceClip);
        }
    }
}
