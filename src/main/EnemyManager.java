package main;

import enemies.*;

public class EnemyManager {
    GamePanel gamePanel;
    private int lastSpawnCounter;
    private int spawnCoolDown = 240;
    private int difficultyScalar = 0;
    private int size = 4 * 32;


    // Special logic for enemy king
    private int kingsX;
    private int kingsY;
    private int kingsSize;
    private boolean shouldSpawnGuard = false;


    public EnemyManager(GamePanel gamePanel){

        this.gamePanel = gamePanel;
    }

    void spawnEnemy(int x, int y, int width, int height, PieceType type){
        switch (type){
            case PieceType.PAWN   -> gamePanel.enemies.add(new EnemyPawn(gamePanel, gamePanel.soundManager, gamePanel.collisionHandler, x, y, width, height));
            case PieceType.ROOK   -> gamePanel.enemies.add(new EnemyRook(gamePanel, gamePanel.soundManager, gamePanel.collisionHandler, x, y, width, height));
            case PieceType.KING   -> gamePanel.enemies.add(new EnemyKing(gamePanel, gamePanel.soundManager, gamePanel.collisionHandler, x, y, width, height));
        }

        if (shouldSpawnGuard){
            shouldSpawnGuard = false;
            gamePanel.enemies.add(new EnemyPawn(gamePanel, gamePanel.soundManager, gamePanel.collisionHandler, kingsX, kingsY, kingsSize, kingsSize));
            gamePanel.soundManager.playClip(gamePanel.soundManager.summonClip);
        }
    }
    public void spawnKingsGuard(int x, int y, int size){

        kingsX = x;
        kingsY = y;
        kingsSize = size;
        shouldSpawnGuard = true;
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
            if (difficultyScalar % 8 == 0){
                spawnEnemy(X, randomY, size, size, PieceType.ROOK);
            }
        }
        adjustDifficulty();
    }

    public void spawnKing(){
        int X = Main.WIDTH;
        int randomY = (int)(Math.random() * (Main.HEIGHT / size)) * size;
        spawnEnemy(X, randomY, size, size, PieceType.KING);
    }

    private int difficultyThreshold = 3;

    private void adjustDifficulty() {
        if (difficultyScalar > difficultyThreshold) {
            difficultyThreshold += 2;

            // Reduce cooldown by 5%, with a minimum floor
            spawnCoolDown *= 0.95;

            if (spawnCoolDown < 100) {
                spawnCoolDown = 100; // Set a lower bound
            }
        }
    }
}
