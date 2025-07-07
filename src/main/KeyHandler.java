package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    GamePanel gamePanel;
    public boolean goingRight = false;
    public boolean goingLeft = false;
    public boolean goingUp = false;
    public boolean goingDown = false;
    public boolean spacePressed = false;
    public boolean escapePressed = false;
    public boolean enterPressed = false;

    public KeyHandler(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        // rook has his own pattern, because i dont want him to move diagonally
        if (gamePanel.selectedPieceType == PieceType.ROOK) {
            switch (key) {
                case KeyEvent.VK_W, KeyEvent.VK_UP, KeyEvent.VK_K -> {
                    goingUp = true;
                    goingRight = false;
                    goingLeft = false;
                    goingDown = false;
                }
                case KeyEvent.VK_S, KeyEvent.VK_DOWN, KeyEvent.VK_J -> {
                    goingDown = true;
                    goingRight = false;
                    goingLeft = false;
                    goingUp = false;
                }
                case KeyEvent.VK_A, KeyEvent.VK_LEFT, KeyEvent.VK_H -> {
                    goingLeft = true;
                    goingDown = false;
                    goingRight = false;
                    goingUp = false;
                }
                case KeyEvent.VK_D, KeyEvent.VK_RIGHT, KeyEvent.VK_L -> {
                    goingRight = true;
                    goingDown = false;
                    goingLeft = false;
                    goingUp = false;
                }
                //case KeyEvent.VK_SPACE -> gamePanel.performAttack();
                case KeyEvent.VK_SPACE -> spacePressed = true;
                case KeyEvent.VK_1 -> gamePanel.selectPiece(PieceType.ROOK);
                case KeyEvent.VK_2 -> gamePanel.selectPiece(PieceType.QUEEN);
            }
        } else {
            switch (key) {
                case KeyEvent.VK_W, KeyEvent.VK_UP, KeyEvent.VK_K -> {
                    goingUp = true;
                }
                case KeyEvent.VK_S, KeyEvent.VK_DOWN, KeyEvent.VK_J -> {
                    goingDown = true;
                }
                case KeyEvent.VK_A, KeyEvent.VK_LEFT, KeyEvent.VK_H -> {
                    goingLeft = true;
                }
                case KeyEvent.VK_D, KeyEvent.VK_RIGHT, KeyEvent.VK_L -> {
                    goingRight = true;
                }
                //case KeyEvent.VK_SPACE -> gamePanel.performAttack();
                case KeyEvent.VK_SPACE -> spacePressed = true;
                case KeyEvent.VK_ENTER -> enterPressed = true;
                case KeyEvent.VK_ESCAPE -> escapePressed = true;
                case KeyEvent.VK_1 -> gamePanel.selectPiece(PieceType.ROOK);
                case KeyEvent.VK_2 -> gamePanel.selectPiece(PieceType.QUEEN);
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_W, KeyEvent.VK_UP, KeyEvent.VK_K -> {
                goingUp = false;
            }
            case KeyEvent.VK_S, KeyEvent.VK_DOWN, KeyEvent.VK_J -> {
                goingDown = false;
            }
            case KeyEvent.VK_A, KeyEvent.VK_LEFT, KeyEvent.VK_H -> {
                goingLeft = false;
            }
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT, KeyEvent.VK_L -> {
                goingRight = false;
            }
        }
    }

    // Unused but required
    @Override
    public void keyTyped(KeyEvent e) {}
}
