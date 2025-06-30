package entities;

public class KnightSmash extends Projectile {
    // Specialized constructor
    public KnightSmash(int x, int y, int size, int speed, int decay, String direction) {
        this.x = x;
        this.y = y;
        this.height = size;
        this.width = size;
        this.direction = direction;
        this.speed = speed;
        this.decay = decay;
    }

    // Moves the ball
    public void moveProjectile(int speed) {
        int spreadSpeed = speed/4;
        /*
        switch (direction) {
            case "up" -> y -= speed;
            case "down" -> y += speed;
            case "right" -> x += speed;
            case "left" -> x -= speed;
        }
        */
        x -= spreadSpeed/2;
        y -= spreadSpeed/2;

        width += spreadSpeed;
        height += spreadSpeed;
    }
}
