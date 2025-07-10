package entities;

import main.GamePanel;
import main.Main;

public class KnightSmash extends Projectile {
    // Specialized constructor
    public KnightSmash(GamePanel gamePanel, int x, int y, int size, int speed, int decay, int damage, String direction) {
        this.gamePanel = gamePanel;
        this.x = x;
        this.y = y;
        this.height = size;
        this.width = size;
        this.direction = direction;
        this.speed = speed;
        this.health = decay;
        this.damage = damage;
        this.skin = gamePanel.knightParticleImage;
    }

    @Override
    public void moveProjectile(int speed) {
        int spreadSpeed = speed/4;
        x -= spreadSpeed/2;
        y -= spreadSpeed/2;

        width += spreadSpeed;
        height += spreadSpeed;
    }

}
