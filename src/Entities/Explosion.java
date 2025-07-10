package entities;

import main.GamePanel;

public class Explosion extends Projectile{

    public Explosion(GamePanel gamePanel, int x, int y, int size) {
        this.gamePanel = gamePanel;
        this.x = x;
        this.y = y;
        this.height = size;
        this.width = size;
        this.health = 30;
        this.skin = gamePanel.explosionImage;
        this.speed = 4;
    }

    @Override
    public void moveProjectile(int speed) {
        int spreadSpeed = speed;
        x -= spreadSpeed/2;
        y -= spreadSpeed/2;

        width += spreadSpeed;
        height += spreadSpeed;
    }
}
