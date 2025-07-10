package main;

import Allies.AllyPawn;
import entities.*;
import Allies.Player;

public class EntityManager {
    GamePanel gamePanel;
    SoundManager soundManager;
    KeyHandler keyHandler;
    Player player;

    int DEFAULT_CANNONBALL_SPEED = 10;
    int DEFAULT_QUEEN_PARTICLE_SPEED = 20;
    public EntityManager(GamePanel gamePanel, KeyHandler keyHandler, SoundManager soundManager, Player player) {
       this.gamePanel = gamePanel;
       this.soundManager = soundManager;
       this.keyHandler = keyHandler;
       this.player = player;
    }

    int CANNON_BALL_SIZE = 64;
    public void spawnCannonBall(int x, int y, String direction) {
        if (gamePanel.rookImage != null) {
            int size = CANNON_BALL_SIZE; // size of the cannonball
            // spawn at top‐center of the rook
            int bx = x + (gamePanel.pieceWidth - size) / 2;
            int by = y + (gamePanel.pieceHeight - size) / 2;
            // Append balls to the list of balls
            gamePanel.projectiles.add(new CannonBall(gamePanel, bx, by, size, DEFAULT_CANNONBALL_SPEED, direction));
            soundManager.playClip(soundManager.shootClip);
        }
    }

    public void spawnEnemyCannonBall(int x, int y){
       if (gamePanel.enemyRookImage != null) {
           int size = CANNON_BALL_SIZE;
           int bx = x + (gamePanel.pieceWidth - size) / 2;
           int by = y + (gamePanel.pieceHeight - size) / 2;
           // Append balls to the list of balls
           gamePanel.enemyBalls.add(new EnemyCannonBall(gamePanel, bx, by, size, DEFAULT_CANNONBALL_SPEED));
           soundManager.playClip(soundManager.shootClip);
       }
    }
    public void spawnLance(){
        if (gamePanel.bishopImage != null) {
            int size = CANNON_BALL_SIZE; // size of the cannonball
            // spawn at top‐center of the rook
            int bx = player.x + (gamePanel.pieceWidth - size) / 2;
            int by = player.y + (gamePanel.pieceHeight - size) / 2;
            // Append balls to the list of balls
            gamePanel.projectiles.add(new BishopLance(gamePanel, bx, by, size, DEFAULT_CANNONBALL_SPEED, player.facingDirection));
            soundManager.playClip(soundManager.shootClip);
        }
    }
    public void spawnQueenParticles() {
        if (gamePanel.queenImage != null) {
            int size = 132; // size of the cannonball
            player.speed = player.DASH_SPEED;
            player.queenDashing = true;
            player.isInvulnerable = true;
            switch(player.facingDirection){
                case "up" -> player.targetY -= gamePanel.PIECE_HEIGHT * 3;
                case "down" -> player.targetY += gamePanel.PIECE_HEIGHT * 3;
                case "left" -> player.targetX -= gamePanel.PIECE_HEIGHT * 3;
                case "right" -> player.targetX += gamePanel.PIECE_HEIGHT * 3;
            }
            int bx = player.x + (gamePanel.pieceWidth  - size) / 2;
            int by = player.y + (gamePanel.pieceHeight - size) / 2;
            gamePanel.projectiles.add(new QueenSlice(gamePanel, bx, by, size, DEFAULT_QUEEN_PARTICLE_SPEED, 20, player.facingDirection));
            soundManager.playClip(soundManager.sliceClip);
        }
    }

    public void spawnKnightParticles() {
       if (gamePanel.knightImage != null) {
           int size = 132; // size of the cannonball
           // spawn at top‐center of the rook
           int bx = player.x + (gamePanel.pieceWidth  - size) / 2;
           int by = player.y + (gamePanel.pieceHeight - size) / 2;

           gamePanel.projectiles.add(new KnightSmash(gamePanel, bx, by, size, DEFAULT_QUEEN_PARTICLE_SPEED,50, player.facingDirection));
           soundManager.playClip(soundManager.smashClip);
       }
    }

    public void spawnPawns(int x, int y){
        if (gamePanel.pawnImage != null){
            int size = gamePanel.pieceHeight;
            gamePanel.allies.add(new AllyPawn(gamePanel, soundManager, gamePanel.collisionHandler, x, y, size, size, true));
        }
    }
}
