//imported libraries 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JPanel;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{

  static final int SCREEN_WIDTH = 600; //screen width
  static final int SCREEN_HEIGHT = 600; //screen height
  static final int UNIT_SIZE = 25; //size of each block
  static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE; //breaks up whole screen into a grid 
  static final int DELAY = 75; //speed of the snake
  final int x[] = new int[GAME_UNITS]; //x coordinates
  final int y[] = new int[GAME_UNITS]; //y coordinates
  int bodyParts = 6; //starting size of snake 
  int applesEaten; //score counter 
  int appleX; //x coordinate of the apple 
  int appleY; //y coordinate of the apple 
  char direction = 'R'; // starting direction of the snake is right 
  boolean running = false; //game is not running at the start
  Timer timer; 
  Random random;
  
  GamePanel(){ 
    //sets the GUI and starts the game 
    random = new Random();
    this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
    this.setBackground(Color.black);
    this.setFocusable(true);
    this.addKeyListener(new MyKeyAdapter());
    startGame(); //starts the game 
  }
  //game starts with this method
  public void startGame(){
    newApple();
    running = true;
    timer = new Timer(DELAY, this);
    timer.start();
  }
  public void paintComponent(Graphics g){
    super.paintComponent(g);
  }
  //method draws all of the grpahics on the screen
  public void draw(Graphics g){
    if(running){
      //shows grid lines on screen
      /*
      for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++){
        g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
        g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
      }
      */
      //draw apple
      g.setColor(Color.red);
      g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

      //draw body
      for(int i = 0; i < bodyParts; i++){
        if(i == 0){
          g.setColor(Color.green);
          g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
        }
        else{
          g.setColor(new Color(45, 180, 0));
          g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
        }
      }
      //shows score 
      g.setColor(Color.red);
      g.setFont(new Font("Ink Free", Font.BOLD, 40));
      FontMetrics metrics = getFontMetrics(g.getFont());
      g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
    }
    else{
      //displays game over screen
      gameOver(g);
    }
  }
  //spawns a new apple when one is eaten 
  public void newApple(){
    appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
    appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
  }
  //method makes the snake move
  public void move(){
    for(int i = bodyParts; i>0; i--){
      x[i] = x[i-1];
      y[i] = y[i-1];
    }
    //switch statement dictates the direction based on the corresponding character 
    switch(direction){
      case 'U':
        y[0] = y[0] - UNIT_SIZE;
        break;
      case 'D':
        y[0] = y[0] + UNIT_SIZE;
        break;
      case 'L':
        x[0] = x[0] - UNIT_SIZE;
        break;
      case 'R':
        x[0] = x[0] + UNIT_SIZE;
        break;
    }
  }
  //method checks to see if apple was eaten 
  public void checkApple(){
    if((x[0] == appleX) && (y[0] == appleY)){
      bodyParts++; //grows snake if apple was eaten 
      applesEaten++; //score goes up by 1
      newApple(); //spawns a new apple 
    }
  }
  //game ends if the snake collides with anything
  //method checks to see if snake has collided with anything 
  public void checkCollisions(){
    //checks if head collides with body
    for(int i = bodyParts; i > 0; i--){
      if((x[0] == x[i]) && (y[0] == y[i])){
        running = false;
      }
    }
    //check if head touches left border
    if(x[0] < 0){
      running = false;
    }
    //check if head touches right border
    if(x[0] > SCREEN_WIDTH){
      running = false;
    }
    //check if head touches top border
    if(y[0] < 0){
      running = false;
    }
    //check if head touches bottom border
    if(y[0] > SCREEN_HEIGHT){
      running = false;
    }
    //stops timer
    if(!running){
      timer.stop();
    }
  }
  //game over 
  public void gameOver(Graphics g){
    //Score 
    g.setColor(Color.red);
    g.setFont(new Font("Ink Free", Font.BOLD, 40));
    FontMetrics metrics1 = getFontMetrics(g.getFont());
    g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
    //Game Over Text
    g.setColor(Color.red);
    g.setFont(new Font("Ink Free", Font.BOLD, 75));
    FontMetrics metrics2 = getFontMetrics(g.getFont());
    g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
  }
  @Override
  public void actionPerformed(ActionEvent e){
    if(running){
      move();
      checkApple();
      checkCollisions();
    }
    repaint();
  }

  public class MyKeyAdapter extends KeyAdapter{
    @Override
    //arrow keys movement 
    public void keyPressed(KeyEvent e){
      switch(e.getKeyCode()){
        case KeyEvent.VK_LEFT: //left arrow key is pressed
          if(direction != 'R'){
            direction = 'L';
          }
          break;
        case KeyEvent.VK_RIGHT: //right arrow key is pressed
          if(direction != 'L'){
            direction = 'R';
          }
          break;  
        case KeyEvent.VK_UP: //up arrow key is pressed
          if(direction != 'D'){
            direction = 'U';
          }
          break;
        case KeyEvent.VK_DOWN: //down arrow key is pressed
          if(direction != 'U'){
            direction = 'D';
          }
          break;
      }
    }
  }
  
}
