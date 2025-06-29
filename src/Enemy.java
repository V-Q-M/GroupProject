import java.awt.image.BufferedImage;

public class Enemy {
    public int x, y;
    public int width;
    public int height;

    int health;
    int speed;
    int damage;

    BufferedImage skin;

    public Enemy(int x, int y, int width, int height, BufferedImage skin) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.skin = skin;
    }

    public void update(){
    //System.out.println("Updated Enemy");
    }


}
