import java.awt.image.BufferedImage;

public class EnemyManager {
    GamePanel gamePanel;
    public EnemyManager(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    void spawnEnemy(int x, int y, int width, int height, BufferedImage skin){
        gamePanel.enemies.add(new Enemy(x, y, width, height, skin));
    }
}
