import java.awt.image.BufferedImage;

public class EnemyManager {
    GamePanel gamePanel;
    private int lastSpawnCounter;
    private int spawnCoolDown = 200;
    public EnemyManager(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    void spawnEnemy(int x, int y, int width, int height, BufferedImage skin){
        gamePanel.enemies.add(new Enemy(gamePanel, gamePanel.soundManager, gamePanel.collisionHandler, x, y, width, height, skin));
    }

    void updateSpawner(){
        if (lastSpawnCounter < spawnCoolDown) {
            lastSpawnCounter++;
        } else {
            lastSpawnCounter = 0;
            int randomX = (int) ((Math.random() * Main.WIDTH / 2) + Main.WIDTH / 2);
            int randomY = (int) (Math.random() * Main.HEIGHT);
            spawnEnemy(randomX,randomY,80, 80, gamePanel.enemyPawnImage);
        }
    }
}
