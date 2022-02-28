import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.util.Random;
public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int top_rectangle =50;
    static final int GAME_UNITS = (SCREEN_HEIGHT*SCREEN_WIDTH)/UNIT_SIZE;
    int DELAY = 85; //75;
    int food_switch,X,Y ;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int snake_check = 1;
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    int Score = 0;
    boolean App_col = false;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        x [0] = 300;
        y [0] = 300;
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {
        if(running){
            g.fillRect(0,0,600,top_rectangle);
            for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            }
            for(int i=0;i<SCREEN_WIDTH/UNIT_SIZE;i++) {
                g.drawLine(0, i * UNIT_SIZE, SCREEN_HEIGHT, i * UNIT_SIZE);
            }
            if (food_switch == 0){
                g.setColor(Color.blue);
                g.fillOval(appleX,appleY,UNIT_SIZE,UNIT_SIZE);
                App_col = true;

            }
            else{
                g.setColor(Color.red);
                g.fillOval(appleX,appleY,UNIT_SIZE,UNIT_SIZE);
                App_col = false;
            }
            for(int i = 0; i<bodyParts;i++) {
                if(i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
                }
                else {
                    g.setColor(new Color(45,180,0));
                    g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont( new Font("Ink Free",Font.BOLD,50));
           // FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + Score, 0,50);
        }

        else {
            gameOver(g);
        }
    }

    public void newApple() {

        X = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        Y = (random.nextInt((int) ((SCREEN_HEIGHT - top_rectangle) / UNIT_SIZE)) * UNIT_SIZE) + top_rectangle;
        appleX = X;
        appleY = Y;
        for(int i = 0; i<bodyParts+1;i++) {
            if ((X == x[i]) && (Y == y[i])) {
                newApple();
            }
        }
    }

    public void move() {
        for(int i = bodyParts; i > 0;i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
        }
    }
    public void checkApple() {
        if((x[0] == appleX)&&(y[0] == appleY)) {
            food_switch = random.nextInt(5);
            newApple();
            if(App_col) {
                Score +=2;
                timer.setDelay(DELAY-25);
                timer.restart();
            }
            else {
                Score +=1;
                timer.setDelay(DELAY);
                timer.restart();
            }
            bodyParts++;
            applesEaten++;
        }
    }
    public void checkCollisions() {
        for(int i= bodyParts; i>0;i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }

        if(x[0] <0){
            running = false;
        }

        if(x[0] >SCREEN_WIDTH-1){
            running = false;
        }

        if(y[0] <top_rectangle){
            running = false;
        }

        if(y[0] >SCREEN_HEIGHT-1){
            running = false;
        }
        if (!running){
            timer.stop();
        }
    }
    public void gameOver(Graphics g) {
        //Game over text
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free",Font.BOLD,75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAME OVER!", (SCREEN_WIDTH - metrics.stringWidth("GAME OVER!"))/2,SCREEN_HEIGHT /2);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkApple();
            checkCollisions();
        }
            repaint();
    }
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}




