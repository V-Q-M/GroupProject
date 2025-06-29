import java.awt.image.BufferedImage;

public class Enemy {
    public int x, y;
    public final int size;

    int health;
    int speed;
    int damage;

    BufferedImage skin;

    public Enemy(int x, int y, int size, BufferedImage skin) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.skin = skin;
    }

    public void update(){
    //System.out.println("Updated Enemy");
    }


}
