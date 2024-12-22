package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    private final int TILE_SIZE = 20;  // Размер клетки
    private final int WIDTH = 600;     // Ширина поля
    private final int HEIGHT = 600;    // Высота поля
    private final int BOARD_WIDTH = WIDTH / TILE_SIZE; // Количество клеток по ширине
    private final int BOARD_HEIGHT = HEIGHT / TILE_SIZE; // Количество клеток по высоте

    private LinkedList<Point> snake;  // Змейка
    private Point food;                    // Еда
    private int direction;                 // Направление движения змейки
    private boolean gameOver;              // Статус игры

    private Timer timer;  // Таймер для обновления игры

    public SnakeGame() {
        snake = new LinkedList<>();
        snake.add(new Point(BOARD_WIDTH / 2, BOARD_HEIGHT / 2));  // Начальная позиция змейки
        direction = KeyEvent.VK_RIGHT;  // Направление по умолчанию - вправо
        generateFood();  // Создаем еду
        gameOver = false;

        // Инициализация панели
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        // Таймер для обновления состояния игры
        timer = new Timer(100, this);  // Таймер с задержкой 100 мс
        timer.start();
    }

    // Метод для отрисовки игрового поля
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Game Over, enter ESC to restart.", WIDTH / 4, HEIGHT / 2);
        } else {
            // Рисуем еду
            g.setColor(Color.RED);
            g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

            // Рисуем змейку
            g.setColor(Color.GREEN);
            for (Point p : snake) {
                g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    // Метод для обновления состояния игры
    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            return;
        }

        // Двигаем змейку
        Point head = snake.get(0);
        Point newHead = null;

        switch (direction) {
            case KeyEvent.VK_UP:
                newHead = new Point(head.x, head.y - 1);
                break;
            case KeyEvent.VK_DOWN:
                newHead = new Point(head.x, head.y + 1);
                break;
            case KeyEvent.VK_LEFT:
                newHead = new Point(head.x - 1, head.y);
                break;
            case KeyEvent.VK_RIGHT:
                newHead = new Point(head.x + 1, head.y);
                break;
        }

        // Проверка на столкновение с границами поля или с телом змейки
        if (newHead.x < 0 || newHead.y < 0 || newHead.x >= BOARD_WIDTH || newHead.y >= BOARD_HEIGHT || snake.contains(newHead)) {
            gameOver = true;
            timer.stop();
            repaint();
            return;
        }

        // Добавляем новый сегмент змейки в начало
        snake.add(0, newHead);

        // Проверка на поедание еды
        if (newHead.equals(food)) {
            generateFood();  // Генерация новой еды
        } else {
            snake.remove(snake.size() - 1);  // Удаляем последний сегмент, если не съели еду
        }

        repaint();  // Перерисовываем поле
    }

    // Генерация новой еды в случайной позиции
    private void generateFood() {
        Random rand = new Random();
        int x = rand.nextInt(BOARD_WIDTH);
        int y = rand.nextInt(BOARD_HEIGHT);
        food = new Point(x, y);
    }

    // Обработка нажатий клавиш для управления змейкой
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (gameOver && key == KeyEvent.VK_ESCAPE) {
            this.gameOver = false;
            snake = new LinkedList<Point>();
            snake.add(new Point(BOARD_WIDTH / 2, BOARD_HEIGHT / 2));  // Начальная позиция змейки
            timer.start();
            direction = KeyEvent.VK_RIGHT;  // Направление по умолчанию - вправо
            generateFood();
            repaint();
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

