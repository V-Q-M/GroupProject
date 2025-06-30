package main;

import enemies.*;

public class EnemyManager {
    GamePanel gamePanel;
    private int lastSpawnCounter;
    private int spawnCoolDown = 180;
    public EnemyManager(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    void spawnEnemy(int x, int y, int width, int height, PieceType type){
        switch (type){
            case PieceType.PAWN   -> gamePanel.enemies.add(new EnemyPawn(gamePanel, gamePanel.soundManager, gamePanel.collisionHandler, x, y, width, height));
            case PieceType.ROOK   -> gamePanel.enemies.add(new EnemyRook(gamePanel, gamePanel.soundManager, gamePanel.collisionHandler, x, y, width, height));
            case PieceType.BISHOP -> gamePanel.enemies.add(new EnemyBishop(gamePanel, gamePanel.soundManager, gamePanel.collisionHandler, x, y, width, height));
            case PieceType.KNIGHT -> gamePanel.enemies.add(new EnemyKnight(gamePanel, gamePanel.soundManager, gamePanel.collisionHandler, x, y, width, height));
            case PieceType.QUEEN  -> gamePanel.enemies.add(new EnemyQueen(gamePanel, gamePanel.soundManager, gamePanel.collisionHandler, x, y, width, height));
            case PieceType.KING   -> gamePanel.enemies.add(new EnemyKing(gamePanel, gamePanel.soundManager, gamePanel.collisionHandler, x, y, width, height));
        }
    }

    void updateSpawner(){
        if (lastSpawnCounter < spawnCoolDown) {
            lastSpawnCounter++;
        } else {
            lastSpawnCounter = 0;
            int X = Main.WIDTH;
            int randomY = (int) (Math.random() * Main.HEIGHT);
            spawnEnemy(X,randomY,80, 80, PieceType.PAWN);
        }
    }
}
