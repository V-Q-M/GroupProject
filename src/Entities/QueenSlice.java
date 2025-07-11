package entities;

import main.GamePanel;

import java.awt.image.BufferedImage;

public class QueenSlice extends Projectile{

  // Specialized constructor
  public QueenSlice(GamePanel gamePanel, int x, int y, int size, int speed, int decay , int damage, BufferedImage skin, String direction) {
    this.gamePanel = gamePanel;
    this.x = x;
    this.y = y;
    this.height = size;
    this.width = size;
    this.direction = direction;
    this.speed = speed;
    this.health = decay;
    this.damage = damage;
    this.skin = skin;
  }
}
