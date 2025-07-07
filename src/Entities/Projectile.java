package entities;

public abstract class Projectile {
    public int x;
    public int y;
    public int width;
    public int height;
    public int speed;
    public int decay;
    public String direction;
    public boolean hasHit = false;

    public void moveProjectile(int speed) {}

}
