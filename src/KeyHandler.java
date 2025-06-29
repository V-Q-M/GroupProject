import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    GamePanel gamePanel;
    boolean goingRight = false;
    boolean goingLeft = false;
    boolean goingUp = false;
    boolean goingDown = false;

    public KeyHandler(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        // rook has his own pattern, because i dont want him to move diagonally
        if (gamePanel.selectedPieceType == PieceType.ROOK) {
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
                case KeyEvent.VK_SPACE -> gamePanel.performAttack();
                case KeyEvent.VK_1 -> gamePanel.selectPiece(PieceType.ROOK);
                case KeyEvent.VK_2 -> gamePanel.selectPiece(PieceType.QUEEN);
            }
        } else {
            switch (key) {
                case KeyEvent.VK_W -> {
                    goingUp = true;
                }
                case KeyEvent.VK_S -> {
                    goingDown = true;
                }
                case KeyEvent.VK_A -> {
                    goingLeft = true;
                }
                case KeyEvent.VK_D -> {
                    goingRight = true;
                }
                case KeyEvent.VK_SPACE -> gamePanel.performAttack();
                case KeyEvent.VK_1 -> gamePanel.selectPiece(PieceType.ROOK);
                case KeyEvent.VK_2 -> gamePanel.selectPiece(PieceType.QUEEN);
            }
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
