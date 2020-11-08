import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Game extends JPanel implements KeyListener, ActionListener, MouseMotionListener
{
    private static final long serialVersionUID = -4999101245149671618L;
    private static final SoundManager SOUND_MANAGER = new SoundManager();
    private final Background background;
    private Player player;
    private final ArrayList<FlyingEnemy> enemies;
    private final ArrayList<enProject> enemyBullets;
    private final ArrayList<Projectile> playerBullets;
    private final ArrayList<Explosion> enemyExplosions;
    
    private int score;
    private int lives;
    private int roundNum;
    private int sleep;
    private int maxWidth;
    private int maxHeight;
    private int menuChoice;
    private int tempChoice;
    private int gridLeftBound = 100;
    private int gridRightBound = 876-100;
    private int enemiesInFlight = 0;
    private int enemiesInFlightMax = 3;
    
    private final int MAX_ENEMY_BULLETS = 3;
    private final int MAX_PLAYER_BULLETS = 2;
    private final int numOfMenus = 4;
    
    private enemyGrid grid = new enemyGrid(60,gridLeftBound,gridRightBound, SOUND_MANAGER);
    
    private boolean initialized = false;
    private boolean leftArrowDown = false;
    private boolean rightArrowDown = false;
    private boolean EnterDown = false;
    private boolean upArrowDown = false;
    private boolean downArrowDown = false;
    private boolean pressCounted = false;
    private boolean over = true;
    private boolean onMenu = true;
    
    private Random random = new Random();
    JLabel title = new JLabel("Inspire AI ");
    JLabel game1 = new JLabel("Player Game");
    JLabel game2 = new JLabel("Player vs Ai ");
    JLabel options = new JLabel("Options ");
    JLabel quit = new JLabel("Quit ");
    

    JLabel livesText = new JLabel("Lives: " + this.lives);
    JLabel overText = new JLabel("GAME OVER");
    JLabel scoreText = new JLabel("Score: " + this.score);
    JLabel roundsText = new JLabel("Round: " + roundNum);
    JLabel levelText = new JLabel("Round " + roundNum + " passed.");

    // constructor - sets the initial conditions for this Game object
    public Game(final int width, final int height) {
        this.setLayout(null);// Don't change
        this.setBackground(Color.BLACK);
        this.setPreferredSize(new Dimension(width, height));

        enemies = new ArrayList<FlyingEnemy>();
        playerBullets = new ArrayList<Projectile>();
        enemyBullets = new ArrayList<enProject>();
        enemyExplosions = new ArrayList<Explosion>();
        background = new Background(this);
        
        score = 0;
        menuChoice = 0;
        tempChoice = 1;
        roundNum = 1;

        sleep = 20;
        maxWidth = width;
        maxHeight = height;
        

      this.addMouseMotionListener(this);
      this.addKeyListener(this);//allows the program to respond to key presses - Don't change
      this.setFocusable(true);//I'll tell you later - Don't change
    }

void menu() {
    
    addMenuText();

        while(menuChoice !=4)
        {
            try
            {
                Thread.sleep( sleep);
            }
            catch( final InterruptedException ex ){}

            
            if(upArrowDown && !pressCounted)
            {
                tempChoice--;
                pressCounted = true;
                if(tempChoice < 1 ) tempChoice = 1;
            }
            if(downArrowDown && !pressCounted)
            {
                tempChoice++;
                pressCounted = true;
                if(tempChoice > numOfMenus ) tempChoice = 4;
            }
            
            
            
            
            if(EnterDown)
            {
                menuChoice = tempChoice;
                
                switch(menuChoice)
                {
                case 1:
                    onMenu = false;
                    menuChoice=0;
                    removeMenuText();
                    
                    lives = 500000;
                    score = 0;
                    
                    
                    initialize();
                    
                    
                    break;
                    
                case 2:
                    //startPlayerGame();
                    menuChoice=0;
                    break;
                case 3:
                    //openOptions();
                    menuChoice=0;
                    break;
                }
            }this.repaint();//redraw the screen with the updated locations; calls paintComponent below
        }
        System.exit(0);
    }

private void addMenuText() {
    
        title.setBounds(maxWidth/2 -167, 50, 500, 300);//title
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Lava", Font.BOLD, 80));
        title.setVisible(true);
        this.add(title);
    
        game1.setBounds(maxWidth/2 -35, maxHeight/2-102, 200, 50);//Menu1
        game1.setForeground(Color.WHITE);
        game1.setFont(new Font("Lava", Font.BOLD, 20));
        game1.setVisible(true);
        this.add(game1);
        
        game2.setBounds(maxWidth/2 - 35 +3, maxHeight/2-52+50, 200, 50);//Menu2
        game2.setForeground(Color.WHITE);
        game2.setFont(new Font("Lava", Font.BOLD, 20));
        game2.setVisible(true);
        this.add(game2);
        
        options.setBounds(maxWidth/2 -35 +22, maxHeight/2-52+150, 200, 50);//Menu3
        options.setForeground(Color.WHITE);
        options.setFont(new Font("Lava", Font.BOLD, 20));
        options.setVisible(true);
        this.add(options);
        
        quit.setBounds(maxWidth/2 -35 + 38, maxHeight/2-52+250, 200, 50);//Menu4
        quit.setForeground(Color.WHITE);
        quit.setFont(new Font("Lava", Font.BOLD, 20));
        quit.setVisible(true);
        this.add(quit);
    
}
private void removeMenuText()
{
    this.remove(game1);
    this.remove(game2);
    this.remove(options);
    this.remove(quit);
    this.remove(title);
    this.revalidate();
}

    public void initialize()
    {
        player = new Player("Images/pShip.gif", "Images/pShip2.gif", "Images/pShip3.gif", 150, 760 );//450, 750

        if(!initialized)
        {
            scoreText.setBounds(maxWidth - 300, 5, maxHeight - 50, 20);//Score:
            scoreText.setForeground(Color.WHITE);
            scoreText.setFont(new Font("Lava", Font.BOLD, 20));
            this.add(scoreText);
    
            levelText.setForeground(Color.WHITE);//Round Passed
            levelText.setFont(new Font("Lava", Font.BOLD, 49));
            levelText.setBounds(340, 50, 400, 240);
            this.add(levelText);
    
            overText.setForeground(Color.WHITE);//Game Over
            overText.setBounds(maxWidth/2-150,maxHeight/2-175,maxHeight - 300, 240);
            overText.setForeground(Color.RED);
    
            livesText.setBounds(10, 5, 850, 20);//Lives:
            livesText.setForeground(Color.WHITE);
            livesText.setFont(new Font("Lava", Font.BOLD, 20));
            this.add(livesText);
    
            roundsText.setBounds(maxWidth - 100, 5, maxHeight - 50, 20);//Rounds:
            roundsText.setForeground(Color.WHITE);
            roundsText.setFont(new Font("Lava", Font.BOLD, 20)); 
            this.add(roundsText);
            
            initialized = true;
        }
        
        livesText.setText("Lives: " + lives);
        scoreText.setText("Score: " + score);
        livesText.setVisible(true);
        overText.setVisible(false);
        levelText.setVisible(false);
        scoreText.setVisible(true);
        roundsText.setVisible(true);
        reset();
        playGame();
    }

    //This is the method that runs the game
    public void playGame()
    {
        over = false;
        while( !over )
        {
        	 
            
        	 grid.update();
            // Player
            if (leftArrowDown) {
                player.moveLeft();
            }
            if (rightArrowDown) {
                player.moveRight();
            }

            // Enemies
            int turnToShoot = (int) (Math.random() * enemies.size());
            ArrayList<FlyingEnemy> enemiesToRemove = new ArrayList<FlyingEnemy>();
            
            // checks if all enemies have gone on the grid atleast once so the grid can start "breathing"
            if(!grid.isBreathing())
            {

                boolean tempCheck = true;
                for (final FlyingEnemy enemy: enemies)
                {
                    if(!enemy.isOnGrid())
                    {
                        tempCheck = false;
                    }
                }
                if(tempCheck)
                        grid.setToBreathe();

	            boolean allEnemiesOnGrid = true;
	            for (final FlyingEnemy enemy: enemies)
	            {
	            	if(!enemy.isOnGrid())
	            	{
	            		allEnemiesOnGrid = false;
	            	}
	            }
	            if(allEnemiesOnGrid)
	            		grid.setToBreathe();
            }
            if(grid.isBreathing())      
            if(enemiesInFlight <enemiesInFlightMax)
                {
                     ArrayList<Integer> enemiesEligible = new ArrayList<Integer>();
                     for (final FlyingEnemy enemy: enemies) 
                     {
                         if(enemy.isOnGrid())   
                         {
                             enemiesEligible.add(enemies.indexOf(enemy));
                         }
                     }
                     
                     Collections.shuffle(enemiesEligible);
                     
                     if(enemiesEligible.size()<enemiesInFlightMax-enemiesInFlight)
                     {
                         for (final Integer enemyFly: enemiesEligible) 
                         {
                             enemiesInFlight++;
                             SOUND_MANAGER.enemyFlyDown.play();
                             enemies.get(enemyFly).setOnGrid(false);
                             
                             enemies.get(enemyFly).advanceAction();
                             enemies.get(enemyFly).setCircle(enemies.get(enemyFly).getX()+150,enemies.get(enemyFly).getY(),false,6);
                             
                         }
                     }
                     
                     else
                        for(int i =0;i< enemiesInFlightMax-enemiesInFlight; i++)
                        {
                            enemiesInFlight++;
                            SOUND_MANAGER.enemyFlyDown.play();
                            enemies.get(enemiesEligible.get(i)).setOnGrid(false);
                            enemies.get(enemiesEligible.get(i)).advanceAction();
                            enemies.get(enemiesEligible.get(i)).setCircle(enemies.get(enemiesEligible.get(i)).getX()+150,
                                                                enemies.get(enemiesEligible.get(i)).getY(),false,6);
                        }
                }

            if(grid.isSetToBreathe())  	
            if(enemiesInFlight < enemiesInFlightMax)
            	{
	            	 ArrayList<Integer> enemiesEligible = new ArrayList<Integer>();
	            	 for (final FlyingEnemy enemy: enemies) 
	            	 {
	            		 if(enemy.isOnGrid())	
	            		 {
	            			 enemiesEligible.add(enemies.indexOf(enemy));
	            		 }
	            	 }
	            	 
	            	 Collections.shuffle(enemiesEligible);
	            	 
	            	 if(enemiesEligible.size()<enemiesInFlightMax-enemiesInFlight)
	            	 {
	            		 for (final Integer enemyFly: enemiesEligible) 
		            	 {
	            			 enemiesInFlight++;
	            			 enemies.get(enemyFly).advanceAction();
	            			 SOUND_MANAGER.enemyFlyDown.play();
	            			 
	            			 
		            	 }
	            	 }
	            	 
	            	 else
	            		for(int i =0;i< enemiesInFlightMax-enemiesInFlight; i++)
	            		{
	            			enemiesInFlight++;
	            			enemies.get(enemiesEligible.get(i)).advanceAction();
	            			SOUND_MANAGER.enemyFlyDown.play();
	            			
	            		}
            	}

            
            for (final FlyingEnemy enemy: enemies) 
            {
                
                
                // Returns colliding bullet if enemy gets blown up
                final Projectile collidingBullet = enemy.update(turnToShoot == 0, enemyBullets.size() < MAX_ENEMY_BULLETS, grid.getXCord(enemy.getGridRow(), enemy.getGridCol())
                        , grid.getYCord(enemy.getGridRow(), enemy.getGridCol()) ,playerBullets, this, grid);
                
                if (collidingBullet != null) {
                    enemiesToRemove.add(enemy);
                    enemyExplosions.add(new Explosion("Images//eExplosion.gif", enemy.getX(), enemy.getY()));
                    playerBullets.remove(collidingBullet);
                    score += 10;
                    
                    // keeps label from flickering
                    SwingUtilities.invokeLater(() -> {
                        scoreText.setText("Score: " + this.score);});
                    
                    int soundChoice = random.nextInt(2);
                    if(soundChoice == 0)
                    	SOUND_MANAGER.enemyExplosion.play();
                    else
                    	if(soundChoice == 1)
                    		SOUND_MANAGER.enemyExplosion2.play();
                    	else
                    		SOUND_MANAGER.enemyExplosion3.play();
                }
                turnToShoot--;
            }

            for (final FlyingEnemy enemy: enemiesToRemove)
            {
                enemies.remove(enemy);
                

                if(grid.isBreathing()&&!enemy.isOnGrid())
                    enemiesInFlight--;

                

            }

            // Enemy Explosions
            ArrayList<Explosion> enemyExplosionsToRemove = new ArrayList<Explosion>();
            for (Explosion enemyExplosion: enemyExplosions)
            {
                if (enemyExplosion.update()) {
                    enemyExplosionsToRemove.add(enemyExplosion);
                }
            }

            for (Explosion enemyExplosion: enemyExplosionsToRemove)
            {
                enemyExplosions.remove(enemyExplosion);
            }

            // Enemy Bullets
            final ArrayList<enProject> enemyBulletsToRemove = new ArrayList<enProject>();
            for (enProject enBullet: enemyBullets)
            {
                if (enBullet.update(player, this))
                {
                    enemyBulletsToRemove.add(enBullet);
                }
            }

            for (final enProject enBullet: enemyBulletsToRemove)
            {
                enemyBullets.remove(enBullet);
            }

            // Player Bullets
            final ArrayList<Projectile> playerBulletsToRemove = new ArrayList<Projectile>();
            for (final Projectile playerBullet: playerBullets)
            {
                if (playerBullet.update()) {
                    playerBulletsToRemove.add(playerBullet);
                }
            }
            for (final Projectile playerBullet: playerBulletsToRemove)
            {
                playerBullets.remove(playerBullet);
            } 

            // Next Level
            if(enemies.size() == 0)
            {
                if(roundNum == 3)
                {
                    overText = new JLabel("You won!");
                    overText.setBounds(maxWidth - 400, 50,maxHeight - 300, 240);
                    overText.setForeground(Color.GREEN);
                }
                if(roundNum < 3)
                {
                    levelText.setText("Round " + roundNum + " passed.");
                    levelText.setVisible(true);
                    try
                    {Thread.sleep(1000);}
                    catch(final InterruptedException ex){}

                    levelText.setVisible(false);
                    reset();

                    lives++;
                    
                    // keeps label from flickering
                    SwingUtilities.invokeLater(() -> {
                        livesText.setText("Lives: " + this.lives);});
                    
                    roundNum++;
                    roundsText.setText("Round " + roundNum + "/3");
                    
                }
                else
                    over = true;
                
                SOUND_MANAGER.breathingDown.stop();
            }

            try
            {
                Thread.sleep( sleep );
            }
            catch( final InterruptedException ex ){}

            this.repaint();//redraw the screen with the updated locations; calls paintComponent below
        }
        
        enemies.clear();
        playerBullets.clear();
        playerBullets.clear();
        enemyBullets.clear();
        enemyExplosions.clear();
        overText.setVisible(true);
        overText.setFont(new Font("Lava", Font.BOLD, 50));
        
        this.add(overText);
        
        try
        {
            Thread.sleep(1500);
        }
        catch( final InterruptedException ex ){}
        
        scoreText.setVisible(false);
        overText.setVisible(false);
        roundsText.setVisible(false);
        livesText.setVisible(false);
        this.revalidate();
        
        onMenu =true;
        addMenuText();
        
    }

    public void hitPlayer()
    {
        if (lives == 1)
            gameOver();
        lives--;

        SwingUtilities.invokeLater(() -> {
        	livesText.setText("Lives: " + this.lives);});
        
       
        
        
        if (lives != 0)
            SOUND_MANAGER.loseLife.play();
        else
            SOUND_MANAGER.playerExplosion.play();
    }

    public void enemyShoot(final int x, final int y) {
        enemyBullets.add(new enProject("images//alienRocket.gif", x, y, player.getX()));

    }

    public void gameOver() {
        over = true;
    }
    public boolean gridIsBreathing()
    {
    	return grid.isSetToBreathe();
    }
    //Precondition: executed when repaint() or paintImmediately is called
    //Postcondition: the screen has been updated with current player location
    @Override
    public void paintComponent( final Graphics page )
    {
        this.setDoubleBuffered(true);
        super.paintComponent(page);
        background.draw(page);
        
        // draws main menu
        if(onMenu)
        {
            page.setColor(Color.DARK_GRAY);
            
            for(int i =1; i <=numOfMenus; i++)
            {
                if(tempChoice == i )
                {
                    page.setColor(Color.red);
                    page.fillRect(390, (maxHeight/2)-200+i*100, 150, 50);
                    page.setColor(Color.DARK_GRAY);
                }
                else page.fillRect(390, (maxHeight/2)-200+i*100, 150, 50);
            }
            
            
        }
        else
        {
        
	        for (final Projectile playerBullet: playerBullets)
	        {
	            playerBullet.draw(page);
	        }
	        
	        ArrayList<enProject> enemyBulletsDraw = new ArrayList<enProject>(enemyBullets); 
	        for (final enProject enemyBullet: enemyBulletsDraw)
	        {
	        	if(enemyBullet != null)
	            enemyBullet.draw(page);
	        }
	        if (!over)
	            player.draw(page);
	        
	        ArrayList<FlyingEnemy> enemyDraw = new ArrayList<FlyingEnemy>(enemies);
	        for (final Enemy enemy: enemyDraw) 
	        {
	        	if(enemy != null)
	            enemy.draw(page);
	        }
	        
	        for (Explosion enemyExplosion: enemyExplosions)
	        {
	            enemyExplosion.draw(page);
	        }
        }
    }

    //tells the program what to do when keys are pressed
    public void keyPressed( final KeyEvent event )
    {
        if( event.getKeyCode() == KeyEvent.VK_ENTER )
        {
            EnterDown = true;
            
        }
        if( event.getKeyCode() == KeyEvent.VK_UP )
        {
            upArrowDown = true;
            pressCounted = false;
        }
        if( event.getKeyCode() == KeyEvent.VK_DOWN )
        {
            downArrowDown = true;
            pressCounted = false;
        }
        if( event.getKeyCode() == KeyEvent.VK_RIGHT )
        {
            rightArrowDown = true;
        }
        else if( event.getKeyCode() == KeyEvent.VK_LEFT )
        {
            leftArrowDown = true;
        }
        
        else if(event.getKeyCode() == KeyEvent.VK_SPACE && playerBullets.size() < MAX_PLAYER_BULLETS)
        {
            playerBullets.add(new Projectile("Images//rocket.gif", player.getX(), player.getY()));
            SOUND_MANAGER.playerShot.play();
        }
    }

    //not used but must be present
    public void keyReleased( final KeyEvent event )
    {
        if( event.getKeyCode() == KeyEvent.VK_ENTER )
        {
            EnterDown = false;
        }
        if( event.getKeyCode() == KeyEvent.VK_UP )
        {
            upArrowDown = false;
        }
        if( event.getKeyCode() == KeyEvent.VK_DOWN )
        {
            downArrowDown = false;
        }
        if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightArrowDown = false;
        } else if (event.getKeyCode() == KeyEvent.VK_LEFT) {
            leftArrowDown = false;
        }
        if(event.getKeyCode() == KeyEvent.VK_C)
        {
        	reset();
        }
    }

    public void reset()
    {
    
            
            grid.reset();
            enemiesInFlight = 0;
            
            
            for(int i = 0; i <4; i++)
                enemies.add(new FlyingEnemy("Images//eShip2.gif", 487, -200-(60*i), 3, player, //spawn location
                        0,3+i)); // row and column numb
            
            for(int i = 0; i <4; i++)
                enemies.add(new FlyingEnemy("Images//eShip2.gif", 387, -200-(60*i), 4, player, //spawn location
                        3,3+i)); // row and column numb
            
            for(int i = 0; i <10; i++)
                enemies.add(new FlyingEnemy("Images//eShip3.gif", -1000-(90*i), 725 , 1, player, //spawn location
                        1,i)); // row and column numb

            for(int i = 0; i <10; i++)
                enemies.add(new FlyingEnemy("Images//eShip.gif", 1876+(90*i), 725  , 2, player, //spawn location
                        2,i)); // row and column numb
        
        
 
		
	}
    
	public void minusOneFlying()
	{
		enemiesInFlight--;
	}

    public void actionPerformed(final ActionEvent event) {}

    public void keyTyped( final KeyEvent event ) {}

    public void mouseMoved(final MouseEvent event) {}

    public void mouseDragged(final MouseEvent event) {}

}