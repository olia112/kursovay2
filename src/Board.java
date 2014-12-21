import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Created by Оля on 16.12.2014.
 */
public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 500;
    private final int B_HEIGHT = 500;
    private final int DOT_SIZE = 20;
    private final int ALL_DOTS = 2500;
    private final int RAND_POS_X = B_WIDTH / DOT_SIZE;
    private final int RAND_POS_Y = B_HEIGHT / DOT_SIZE;
    private final int DELAY = 100;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image dot;
    private Image apple;
    private Image head;

    public Board() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon("src\\dot.png");
        dot = iid.getImage();

        ImageIcon iia = new ImageIcon("src\\apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("src\\head.png");
        head = iih.getImage();
    }

    private void initGame() {

        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = DOT_SIZE * 4 - z * DOT_SIZE;
            y[z] = DOT_SIZE * 4;
        }

        locateApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        if (inGame) {

            g.drawImage(apple, apple_x, apple_y, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(dot, x[z], y[z], this);
                }
            }
            g.setColor(Color.WHITE);
            g.drawString("Собрано яблочек = "+(dots-3),0,15);
            Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {

        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    private void checkApple() {

        Rectangle head = new Rectangle(x[0], y[0], 20, 20);
        Rectangle apple = new Rectangle(apple_x, apple_y, 22, 24);
        if (head.intersects(apple)) {

            dots++;
            locateApple();
        }
    }

    private void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {

        for (int z = dots; z > 0; z--) {

            if ((z > 3) && (x[0] == x[z]) && (y[0] == y[z])) { //(z>3)- условие того, что змейка не врежется в себя в самом начале игры, когда состоит из трёх соединений
                inGame = false;
            }
        }

        if (y[0] >= B_HEIGHT-DOT_SIZE * 2.5) { //Проверка столкновения змейки с нижней границей
            inGame = false;
        }

        if (y[0] <= 0) { //Проверка столкновения змейки с верхней границей
            inGame = false;
        }

        if (x[0] >= B_WIDTH-DOT_SIZE * 2.5) { //Проверка столкновения змейки с правой границей
            inGame = false;
        }

        if (x[0] <= 0) { //Проверка столкновения змейки с левой границей
            inGame = false;
        }

        if(!inGame) {
            timer.stop();
        }
    }

    private void locateApple() {

        Random rnd = new Random();

        int r = 1 + rnd.nextInt (RAND_POS_X-4); // Исключаем появление яблочка на краю по х

        apple_x = ((r * DOT_SIZE));

        r = 1 + rnd.nextInt (RAND_POS_Y-4); // Исключаем появление яблочка на краю по y

        apple_y = ((r * DOT_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            checkApple();
            checkCollision();
            move();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }

}

