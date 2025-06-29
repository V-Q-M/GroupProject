import java.awt.image.BufferedImage;

public class EnemyManager {
    GamePanel gamePanel;
    public EnemyManager(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    void spawnEnemy(int x, int y, int size, BufferedImage skin){
        gamePanel.enemies.add(new Enemy(x, y, size, skin));
    }
}
