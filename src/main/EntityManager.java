package main;

import Allies.AllyPawn;
import entities.*;
import Allies.Player;

import java.awt.image.BufferedImage;

public class EntityManager {
    GamePanel gamePanel;
    SoundManager soundManager;
    KeyHandler keyHandler;
    Player player;

    final int DEFAULT_CANNONBALL_SPEED = 10;
    final int DEFAULT_LANCE_SPEED = 10;
    final int DEFAULT_QUEEN_PARTICLE_SPEED = 20;

    final int DEFAULT_TIME_TO_DECAY_QUEEN = 20;
    final int DEFAULT_TIME_TO_DECAY_KNIGHT = 44;

    final int DEFAULT_CANNON_BALL_DMG = 50;
    final int DEFAULT_SLICE_DMG= 50;
    final int DEFAULT_SLAM_DMG= 100;
    final int DEFAULT_LANCE_DMG= 75;


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
            // Append balls to the list of balls
            gamePanel.projectiles.add(new CannonBall(gamePanel, bx, y, size, DEFAULT_CANNONBALL_SPEED, DEFAULT_CANNON_BALL_DMG, direction));
            soundManager.playClip(soundManager.shootClip);
        }
    }

    public void spawnEnemyCannonBall(int x, int y){
       if (gamePanel.enemyRookImage != null) {
           int size = CANNON_BALL_SIZE;
           int bx = x + (gamePanel.pieceWidth - size) / 2;
           gamePanel.enemyBalls.add(new EnemyCannonBall(gamePanel, bx, y, size, DEFAULT_CANNONBALL_SPEED, DEFAULT_CANNON_BALL_DMG));
           soundManager.playClip(soundManager.shootClip);
       }
    }

    public void spawnExplosion(int x, int y){
        if (gamePanel.explosionImage != null){
            int size = CANNON_BALL_SIZE;
            int bx = x + (gamePanel.pieceWidth - size) / 2;
            gamePanel.effects.add(new Explosion(gamePanel, bx, y, size));
            soundManager.playClip(soundManager.shootClip);
        }

    }

    public void spawnLance(BufferedImage skin){
        if (gamePanel.bishopImage != null) {
            int size = 96; // size of the cannonball
            // spawn at top‐center of the rook
            int bx = player.x + (gamePanel.pieceWidth - size) / 2;
            int by = player.y + (gamePanel.pieceHeight - size) / 2;
            // Append balls to the list of balls
            gamePanel.projectiles.add(new BishopLance(gamePanel, bx, by, size, DEFAULT_LANCE_SPEED ,DEFAULT_LANCE_DMG, skin, player.facingDirection));
            soundManager.playClip(soundManager.holyClip);
        }
    }
    public void spawnQueenParticles(BufferedImage skin) {
        if (gamePanel.queenImage != null) {
            int size = 132; // size of the cannonball
            int bx = player.x + (gamePanel.pieceWidth  - size) / 2;
            int by = player.y + (gamePanel.pieceHeight - size) / 2;
            gamePanel.projectiles.add(new QueenSlice(gamePanel, bx, by, size, DEFAULT_QUEEN_PARTICLE_SPEED, DEFAULT_TIME_TO_DECAY_QUEEN, DEFAULT_SLICE_DMG, skin, player.facingDirection));
            soundManager.playClip(soundManager.sliceClip);
        }
    }

    public void spawnKnightParticles() {
       if (gamePanel.knightImage != null) {
           int size = 132; // size of the cannonball
           // spawn at top‐center of the rook
           int bx = player.x + (gamePanel.pieceWidth  - size) / 2;
           int by = player.y + (gamePanel.pieceHeight - size) / 2;

           gamePanel.projectiles.add(new KnightSmash(gamePanel, bx, by, size, DEFAULT_QUEEN_PARTICLE_SPEED, DEFAULT_TIME_TO_DECAY_KNIGHT, DEFAULT_SLAM_DMG, player.facingDirection));
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
