package Menu;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MenuKeyHandler implements KeyListener {
    MenuPanel menuPanel;

    public boolean goingRight = false;
    public boolean goingLeft = false;
    public boolean goingUp = false;
    public boolean goingDown = false;
    public boolean spacePressed = false;
    public boolean enterPressed = false;

    public MenuKeyHandler(MenuPanel menuPanel){
        this.menuPanel = menuPanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_W -> {
                    goingUp = true;
                    goingRight = false;
                    goingLeft = false;
                    goingDown = false;
                }
                case KeyEvent.VK_S -> {
                    goingDown = true;
                    goingRight = false;
                    goingLeft = false;
                    goingUp = false;
                }
                case KeyEvent.VK_A -> {
                    goingLeft = true;
                    goingDown = false;
                    goingRight = false;
                    goingUp = false;
                }
                case KeyEvent.VK_D -> {
                    goingRight = true;
                    goingDown = false;
                    goingLeft = false;
                    goingUp = false;
                }
                //case KeyEvent.VK_SPACE -> gamePanel.performAttack();
                case KeyEvent.VK_SPACE -> spacePressed = true;
                case KeyEvent.VK_ENTER -> enterPressed = true;
            }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_W -> goingUp = false;
            case KeyEvent.VK_S -> goingDown = false;
            case KeyEvent.VK_A -> goingLeft = false;
            case KeyEvent.VK_D -> goingRight = false;
        }
    }

    // Unused but required
    @Override
    public void keyTyped(KeyEvent e) {}
}
