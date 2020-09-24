import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;
import java.util.*;

public class Game extends JPanel implements KeyListener, ActionListener, MouseMotionListener
{
    private Player player;
    private Enemy[][] enemy;
    private Projectile bullet;
    private enProject enBullet;
    private Sound pShot;
    private Sound eShot;
    private Sound loseLife;
    private int score;
    private int lives;
    private int roundNum;
    private int sleep;
    private boolean hasStarted;
    private boolean canShoot;
    private Sound pExplosion;
    private Sound eExplosion;
    private Background background;
    private Explosion eExplosionImg;
    private Explosion pExplosionImg;

    //constructor - sets the initial conditions for this Game object
    public Game(int width, int height)
    {
        //make a panel with dimensions width by height with a black background
        this.setLayout( null );//Don't change
        this.setBackground( Color.BLACK );
        this.setPreferredSize( new Dimension( width, height ) );//Don't change

        enemy = new Enemy[5][7];
        bullet = new Projectile("Images//rocket.gif", 1000, 0);
        enBullet = new enProject("Images//alienRocket.gif", 490, 50);
        pShot = new Sound("Sounds//pShot.wav");
        eShot = new Sound("Sounds//eShot.wav");
        loseLife = new Sound("Sounds//loseLife.wav");
        pExplosion = new Sound("Sounds//pExplosion.wav");
        eExplosion = new Sound("Sounds//eExplosion.wav");
        eExplosionImg = new Explosion("Images//eExplosion.gif", 100, 20);
        eExplosionImg = new Explosion("Images//pExplosion.gif", 10000, 20);
        score = 0;
        lives = 3;
        roundNum = 1;
        sleep = 20;
        hasStarted = false;
        canShoot = true;

        background = new Background();

        //initialize the instance variables
        player = new Player("images//pShip.gif", 450, 650 );//450, 750
        for(int r = 0; r < enemy.length; r++)
        {
            for(int c = 0; c < enemy[0].length; c++)
            {
                if(r == 0)
                    enemy[r][c] = new Enemy("Images//eShip3.gif", (c + 1) * 100 + 70, (r + 1) * 100 - 70);
                else if(r > 0 && r < 3)
                    enemy[r][c] = new Enemy("Images//eShip2.gif", (c + 1) * 100 + 70, (r + 1) * 100 - 70);
                else
                     enemy[r][c] = new Enemy("Images//eShip.gif", (c + 1) * 100 + 70, (r + 1) * 100 - 70);
            }
        }

        this.addKeyListener(this);//allows the program to respond to key presses - Don't change

        this.setFocusable(true);//I'll tell you later - Don't change

    }
    
    //This is the method that runs the game
    public void playGame()
    {
        JLabel overText = new JLabel("GAME OVER");
        JLabel scoreText = new JLabel("Score: " + this.score);
        JLabel livesText = new JLabel("Lives: " + this.lives);
        JLabel roundsText = new JLabel("Round " + roundNum + "/3");
        JLabel levelText = new JLabel("Round " + roundNum + " passed.");

        scoreText.setBounds(885, 5, 850, 20);
        scoreText.setForeground(Color.WHITE);
        scoreText.setFont(new Font("Lava", Font.BOLD, 20));
        scoreText.setVisible(true);
        this.add(scoreText);
        
        levelText.setForeground(Color.WHITE);
        levelText.setVisible(false);
        levelText.setFont(new Font("Lava", Font.BOLD, 49));
        levelText.setBounds(340, 50, 400, 240);
        this.add(levelText);

        overText.setForeground(Color.WHITE);
        overText.setVisible(false);
        overText.setBounds(350, 50, 400, 240);
        overText.setForeground(Color.RED);

        livesText.setBounds(10, 5, 850, 20);
        livesText.setForeground(Color.WHITE);
        livesText.setFont(new Font("Lava", Font.BOLD, 20));
        livesText.setVisible(true);
        this.add(livesText);

        roundsText.setBounds(450, 5, 850, 20);
        roundsText.setForeground(Color.WHITE);
        roundsText.setFont(new Font("Lava", Font.BOLD, 20));
        roundsText.setVisible(true);
        this.add(roundsText);

        this.addMouseMotionListener(this);

        boolean allDead = true;;

        boolean enCanShoot = true;;

        boolean over = false;
        while( !over )
        {
            for(int r = 0; r < enemy.length; r++)
            {
                for(int c = 0; c < enemy[0].length; c++)
                {
                    int randRow = (int)(Math.random() * 5);
                    int randCol = (int)(Math.random() * 7);

                    if(enCanShoot && !enemy[randRow][randCol].isDead())
                    {
                        enBullet = new enProject("images//alienRocket.gif", enemy[randRow][randCol].getX() + 10, enemy[randRow][randCol].getY());
                        eShot.play();
                        enCanShoot = false;
                    }
                    if(!enemy[r][c].isDead())
                    {
                        enemy[r][c].move();
                        allDead = false;
                    }
                    if(!enemy[r][c].isDead() && enemy[r][c].getY() > 680)
                        over = true;
                    if(!enemy[r][c].isDead() && bullet.isInside(enemy[r][c]))
                    {
                        enemy[r][c].setIsDead(true);
                        eExplosionImg = new Explosion("Images//eExplosion.gif", enemy[r][c].getX(), enemy[r][c].getY());
                        enemy[r][c].setX(10000);
                        enemy[r][c].setY(0);
                        bullet.setX(10000);
                        score += 10;
                        scoreText.setText("Score: " + this.score);
                        canShoot = true;
                        eExplosion.play();
                    }
                    if(enBullet.isInsideP(this.player))
                    {
                        if(lives == 1)
                            over = true;
                        enBullet.setX(10000);
                        lives--;
                        livesText.setText("Lives: " + this.lives);
                        if(lives != 0)
                            loseLife.play();
                        else
                            pExplosion.play();
                    }
                    if(enBullet.getY() > 780)
                    {
                        enBullet.setX(10000);
                        enCanShoot = true;
                        eExplosionImg = new Explosion("Images//eExplosion.gif", 10000, 0);
                    }
                    if(this.bullet.getY() <= 2)
                    {
                        bullet.setX(10000);
                        canShoot = true;
                    }
                }
            }
            if(allDead)
            {
                if(roundNum == 3)
                {
                    overText = new JLabel("You won!");
                    overText.setBounds(400, 50, 400, 240);
                    overText.setForeground(Color.GREEN);
                }
                if(roundNum < 3)
                {
                    eExplosionImg = new Explosion("Images//eExplosion.gif", 10000, 0);
                    levelText.setText("Round " + roundNum + " passed.");
                    levelText.setVisible(true);
                    try
                    {Thread.sleep(1000);}
                    catch(InterruptedException ex){}
                    levelText.setVisible(false);
                    reset();
                    lives++;
                    livesText.setText("Lives: " + this.lives);                    
                    roundNum++;
                    roundsText.setText("Round " + roundNum + "/3");
                }
                else
                    over = true;
            }
            try
            {
                Thread.sleep( sleep );
            }
            catch( InterruptedException ex ){}
            if(lives > 2)
            {
                player = new Player("images//pShip.gif", player.getX(), player.getY());
            }
            else if(lives == 2)
            {
                player = new Player("images//pShip2.gif", player.getX(), player.getY());
            }
            else if(lives == 1)
            {
                player = new Player("images//pShip3.gif", player.getX(), player.getY());
            }
            enBullet.move();
            this.repaint();//redraw the screen with the updated locations; calls paintComponent below
            //this.add(background);
            bullet.move();
            allDead = true;
        }

        for(int r = 0; r < enemy.length; r++)
            for(int c = 0; c < enemy[0].length; c++)
                enemy[r][c].setX(10000);
                
        player.setX(10000);
        enBullet.setX(10000);
        eExplosionImg.setX(10000);
        bullet.setX(10000);
        this.add(overText);
        overText.setFont(new Font("Lava", Font.BOLD, 50));
        overText.setVisible(true);
    }

    //Precondition: executed when repaint() or paintImmediately is called
    //Postcondition: the screen has been updated with current player location
    public void paintComponent( Graphics page )
    {
        super.paintComponent( page );//I'll tell you later.
        player.draw( page );
        page.drawImage(bullet.getImage(), bullet.getX() + 43, bullet.getY() - 10, null);
        page.drawImage(enBullet.getImage(), enBullet.getX(), enBullet.getY() + 10, null);
        page.drawImage(player.getImage(), player.getX(), player.getY() - 60, null);
        page.drawImage(eExplosionImg.getImage(), eExplosionImg.getX() - 15, eExplosionImg.getY() - 10, null);
        for(int r = 0; r < enemy.length; r++)
        {
            for(int c = 0; c < enemy[0].length; c++)
            {
                page.drawImage(enemy[r][c].getImage(), enemy[r][c].getX() - 5, enemy[r][c].getY(), null);
            }
        }
        bullet.draw(page);
        enBullet.draw(page);
        for(Enemy[] e: enemy)
            for(Enemy en: e)
                en.draw( page );//calls the draw method in the Square class
    }

    //not used but must be present
    public void keyReleased( KeyEvent event )
    {  
    }

    //tells the program what to do when keys are pressed
    public void keyPressed( KeyEvent event )
    {
        if( event.getKeyCode() == KeyEvent.VK_RIGHT )
        {
            player.moveRight();
        }
        else if( event.getKeyCode() == KeyEvent.VK_LEFT )
        {
            player.moveLeft();
        }
        else if(event.getKeyCode() == KeyEvent.VK_SPACE && canShoot)
        {
            bullet = new Projectile("Images//rocket.gif", player.getX(), player.getY());
            pShot.play();
            canShoot = false;
        }
    }

    public void mouseMoved(MouseEvent event)
    {
        int mouseX = event.getX();
        if(mouseX <= Driver.WIDTH - 90) // mouse movement
        {
            player.movePlayer(mouseX);
        }
    }

    public void reset()
    {
        for(int r = 0; r < enemy.length; r++)
        {
            for(int c = 0; c < enemy[0].length; c++)
            {
                if(r == 0)
                    enemy[r][c] = new Enemy("Images//eShip3.gif", (c + 1) * 100 + 70, (r + 1) * 100 - 70);
                else if(r > 0 && r < 3)
                    enemy[r][c] = new Enemy("Images//eShip2.gif", (c + 1) * 100 + 70, (r + 1) * 100 - 70);
                else
                     enemy[r][c] = new Enemy("Images//eShip.gif", (c + 1) * 100 + 70, (r + 1) * 100 - 70);
            }
        }
        sleep -= 5;
    }

    public void actionPerformed(ActionEvent event)
    {

    }

    //not used but must be present
    public void keyTyped( KeyEvent event )
    {

    }

    public void mouseDragged(MouseEvent event)
    {

    }
}