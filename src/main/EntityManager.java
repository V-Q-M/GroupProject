package main;

import entities.CannonBall;
import entities.KnightSmash;
import entities.QueenSlice;
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
    // Yeah this could get its own class later
    public void spawnCannonBall() {
        if (gamePanel.rookImage != null) {
            int size = CANNON_BALL_SIZE; // size of the cannonball
            // spawn at top‐center of the rook
            int bx = player.x + (gamePanel.pieceWidth - size) / 2;
            int by = player.y + (gamePanel.pieceHeight - size) / 2;
            // Append balls to the list of balls
            //gamePanel.balls.add(new CannonBall(bx, by, size, DEFAULT_CANNONBALL_SPEED, player.facingDirection));
            gamePanel.balls.add(new CannonBall(bx, by, size, DEFAULT_CANNONBALL_SPEED, "right"));
            // this should move to a variable
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

            // spawn at top‐center of the rook
            int bx = player.x + (gamePanel.pieceWidth  - size) / 2;
            int by = player.y + (gamePanel.pieceHeight - size) / 2;
            // Append balls to the list of balls
            gamePanel.projectiles.add(new QueenSlice(bx, by, size, DEFAULT_QUEEN_PARTICLE_SPEED, 20, player.facingDirection));
            soundManager.playClip(soundManager.sliceClip);
        }
    }

    public void spawnKnightParticles() {
       if (gamePanel.knightImage != null) {
           int size = 132; // size of the cannonball
           // spawn at top‐center of the rook
           int bx = player.x + (gamePanel.pieceWidth  - size) / 2;
           int by = player.y + (gamePanel.pieceHeight - size) / 2;

           // Append balls to the list of balls
           gamePanel.projectiles.add(new KnightSmash(bx, by, size, DEFAULT_QUEEN_PARTICLE_SPEED,50, player.facingDirection));
           soundManager.playClip(soundManager.smashClip);
       }
    }
}
