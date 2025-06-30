package main;

import enemies.EnemyPawn;

import java.awt.image.BufferedImage;

public class EnemyManager {
    GamePanel gamePanel;
    private int lastSpawnCounter;
    private int spawnCoolDown = 180;
    public EnemyManager(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    void spawnEnemy(int x, int y, int width, int height, BufferedImage skin, PieceType type){
        switch (type){
            case PieceType.PAWN -> gamePanel.enemies.add(new EnemyPawn(gamePanel, gamePanel.soundManager, gamePanel.collisionHandler, x, y, width, height, skin));
            //case ROOK -> gamePanel.enemies.add(new EnemyRook(gamePanel, gamePanel.soundManager, gamePanel.collisionHandler, x, y, width, height, skin));
        }
    }

    void updateSpawner(){
        if (lastSpawnCounter < spawnCoolDown) {
            lastSpawnCounter++;
        } else {
            lastSpawnCounter = 0;
            int X = Main.WIDTH;
            int randomY = (int) (Math.random() * Main.HEIGHT);
            spawnEnemy(X,randomY,80, 80, gamePanel.enemyPawnImage, PieceType.PAWN);
        }
    }
}
