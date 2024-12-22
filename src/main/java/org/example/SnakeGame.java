package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    private final int TILE_SIZE = 20;
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private final int BOARD_WIDTH = WIDTH / TILE_SIZE;
    private final int BOARD_HEIGHT = HEIGHT / TILE_SIZE;
    private final int GAP = 2;

    private Snake snake;
    private Point food;
    private int direction;
    private boolean gameOver;

    private final Timer timer = new Timer(100, this);

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        restart();
    }

    private void restart() {
        snake = new Snake();
        snake.initHead(BOARD_WIDTH / 2, BOARD_HEIGHT / 2);
        direction = KeyEvent.VK_RIGHT;
        generateFood();
        gameOver = false;
        timer.start();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Game Over, enter ESC to restart.", WIDTH / 4, HEIGHT / 2);
        } else {
            // Рисуем поле
            for (int i = 0; i < BOARD_WIDTH; i++) {
                for (int j = 0; j < BOARD_HEIGHT; j++) {
                    g.setColor((i + j) % 2 == 0 ? Color.GREEN.darker() : Color.GREEN);
                    g.fillRect(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }

            // Рисуем еду
            g.setColor(Color.RED);
            g.fillRoundRect(food.x * TILE_SIZE + GAP, food.y * TILE_SIZE + GAP, TILE_SIZE - GAP * 2, TILE_SIZE - GAP * 2, TILE_SIZE / 2 - GAP, TILE_SIZE / 2 - GAP);

            // Рисуем змейку
            g.setColor(Color.BLUE.brighter());
            Point head = snake.getHead();
            g.fillRect(head.x * TILE_SIZE + (GAP / 2), head.y * TILE_SIZE + (GAP / 2), TILE_SIZE - (GAP / 2) * 2, TILE_SIZE - (GAP / 2) * 2);
            for (Point p : snake.getBody()) {
                g.fillRect(p.x * TILE_SIZE + GAP, p.y * TILE_SIZE + GAP, TILE_SIZE - GAP * 2, TILE_SIZE - GAP * 2);
            }
        }
    }

    // Метод для обновления состояния игры
    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            return;
        }

        Point newHead = getNewPoint();
        if (newHead.x < 0 || newHead.y < 0 || newHead.x >= BOARD_WIDTH || newHead.y >= BOARD_HEIGHT || snake.getBody().contains(newHead)) {
            gameOver = true;
            timer.stop();
            repaint();
            return;
        }

        snake.addHead(newHead);

        if (newHead.equals(food)) {
            generateFood();
        } else {
            snake.removeTail();
        }

        repaint();
    }

    private Point getNewPoint() {
        Point head = snake.getHead();

        return switch (direction) {
            case KeyEvent.VK_UP -> new Point(head.x, head.y - 1);
            case KeyEvent.VK_DOWN -> new Point(head.x, head.y + 1);
            case KeyEvent.VK_LEFT -> new Point(head.x - 1, head.y);
            case KeyEvent.VK_RIGHT -> new Point(head.x + 1, head.y);
            default -> null;
        };
    }

    private void generateFood() {
        Random rand = new Random();
        int x = rand.nextInt(BOARD_WIDTH);
        int y = rand.nextInt(BOARD_HEIGHT);
        food = new Point(x, y);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (gameOver && key == KeyEvent.VK_ESCAPE) {
            restart();
        }

        if (key == KeyEvent.VK_UP && direction != KeyEvent.VK_DOWN) {
            direction = KeyEvent.VK_UP;
        } else if (key == KeyEvent.VK_DOWN && direction != KeyEvent.VK_UP) {
            direction = KeyEvent.VK_DOWN;
        } else if (key == KeyEvent.VK_LEFT && direction != KeyEvent.VK_RIGHT) {
            direction = KeyEvent.VK_LEFT;
        } else if (key == KeyEvent.VK_RIGHT && direction != KeyEvent.VK_LEFT) {
            direction = KeyEvent.VK_RIGHT;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}

