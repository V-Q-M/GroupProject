package entities;

import main.GamePanel;

public class QueenSlice extends Projectile{

  // Specialized constructor
  public QueenSlice(GamePanel gamePanel, int x, int y, int size, int speed, int decay , int damage, String direction) {
    this.gamePanel = gamePanel;
    this.x = x;
    this.y = y;
    this.height = size;
    this.width = size;
    this.direction = direction;
    this.speed = speed;
    this.health = decay;
    this.damage = damage;
    this.skin = gamePanel.queenParticleImage;
  }
}
