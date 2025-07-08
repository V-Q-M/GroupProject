package main;

import enemies.*;

public class EnemyManager {
    GamePanel gamePanel;
    private int lastSpawnCounter;
    private int spawnCoolDown = 240;
    private int difficultyScalar = 0;
    private int size = 4 * 32;
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
            difficultyScalar ++;
            int X = Main.WIDTH;
            //int randomY = (int) (Math.random() * Main.HEIGHT);
            int randomY = (int)(Math.random() * (Main.HEIGHT / size)) * size;

            spawnEnemy(X,randomY, size, size, PieceType.PAWN);
        }
        adjustDifficulty();
    }

    private int difficultyThreshold = 3;
    private void adjustDifficulty(){
        if (difficultyScalar > difficultyThreshold){
           difficultyThreshold += 2;
           spawnCoolDown-=10;
        }
    }
}
