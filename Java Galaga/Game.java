import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
    
   
    
    private boolean initialized = false;
    private boolean leftArrowDown = false;
    private boolean rightArrowDown = false;
    private boolean EnterDown = false;
    private boolean upArrowDown = false;
    private boolean downArrowDown = false;
    private boolean pressCounted = false;
    private boolean over = true;
    private boolean gameWillEnd;
    private boolean onMenu = true;
    private boolean roundOver;
    private boolean respawning = false;
  
    
    private long roundOverTime;
    private long deathTime;
    private long gameOverTime;
    private long elapsedRoundTime = 0L;
    private long elapsedDeathTime = 0L;
    private long elapsedGameOverTime = 0L;
    
    private Random random = new Random();
	private enemyGrid grid = new enemyGrid(60,gridLeftBound,gridRightBound, SOUND_MANAGER);
	
	//Menu labels
    JLabel title = new JLabel("Inspire AI ");
    JLabel game1 = new JLabel("Player Game");
    JLabel game2 = new JLabel("Player vs Ai ");
    JLabel options = new JLabel("Options ");
    JLabel quit = new JLabel("Quit ");
    
    // game labels
    JLabel respawnText = new JLabel("REDEPLOYING...");
    JLabel livesText = new JLabel("Lives: " + this.lives);
    JLabel overText = new JLabel("GAME OVER");
    JLabel scoreText = new JLabel("Score: " + this.score);
    JLabel roundsText = new JLabel("Round: " + roundNum);
    JLabel levelText = new JLabel("Round " + roundNum + " passed.");
    JLabel noLivesText = new JLabel("no lives left, Score animations and what not here");

    // constructor - sets the initial conditions for this Game object
    public Game(final int width, final int height) {
        this.setLayout(null);	// Don't change
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
        SOUND_MANAGER.menuSound.play();
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
        	
        	
        	respawnText.setBounds(345, 425, 300, 100);//respawn:
        	respawnText.setForeground(Color.RED);
        	respawnText.setFont(new Font("Lava", Font.BOLD, 30));
            this.add(respawnText);
            
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
            this.add(overText);
    
            livesText.setBounds(10, 5, 850, 20);//Lives:
            livesText.setForeground(Color.WHITE);
            livesText.setFont(new Font("Lava", Font.BOLD, 20));
            this.add(livesText);
    
            roundsText.setBounds(maxWidth - 100, 5, maxHeight - 50, 20);//Rounds:
            roundsText.setForeground(Color.WHITE);
            roundsText.setFont(new Font("Lava", Font.BOLD, 20)); 
            this.add(roundsText);
            
            noLivesText.setBounds(345, 425, 500, 100);//final score:
            noLivesText.setForeground(Color.WHITE);
            noLivesText.setFont(new Font("Lava", Font.BOLD, 20)); 
            this.add(noLivesText);
            
            initialized = true;
        }
        
              
        
        over = false;
        score = 0;
        roundNum = 1;  
        lives = 5;
        
        livesText.setText("Lives: " + lives);
        scoreText.setText("Score: " + score);
        roundsText.setText("Round " + roundNum + "/3");
        
        overText.setVisible(false);
        levelText.setVisible(false);
        respawnText.setVisible(false);
        livesText.setVisible(true);
        scoreText.setVisible(true);
        roundsText.setVisible(true);
        noLivesText.setVisible(false);
        
        elapsedGameOverTime = 0;
        elapsedDeathTime =0;
        elapsedRoundTime=0;
        gameWillEnd = false;
        respawning = false;
        
        reset();
        playGame();
    }

    //This is the method that runs the game
    public void playGame()
    {
        over = false;
        // game loop
        while( !over )
        {
        	 
            // moves the grid
        	 grid.update();
        	 
            // Player movement handling
            if (leftArrowDown && !respawning) {
                player.moveLeft();
            }
            if (rightArrowDown && !respawning) {
                player.moveRight();
            }

            // Enemies
            int turnToShoot = (int) (Math.random() * enemies.size());
            ArrayList<FlyingEnemy> enemiesToRemove = new ArrayList<FlyingEnemy>();
            
            // checks if all enemies have gone on the grid at least once so the grid can start "breathing"
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
            
            // handles checking when enemies should fly down the screen
            // enemies should only fly down when all enemies have spawned and landed on the grid
            if(grid.isBreathing() ||grid.isSetToBreathe())      
            if(enemiesInFlight < enemiesInFlightMax && !respawning)
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

            // handles checking when enemies should fly down the screen
            // enemies should only fly down when all enemies have spawned and landed on the grid
            // TODO NOT SURE IF THIS IS NEEDED
            if(grid.isSetToBreathe())  	
            if(enemiesInFlight < enemiesInFlightMax && !respawning)
            	{
            		// find all enemies that are able to fly down the screen(those who are not already in flight)
	            	 ArrayList<Integer> enemiesEligible = new ArrayList<Integer>();
	            	 for (final FlyingEnemy enemy: enemies) 
	            	 {
	            		 if(enemy.isOnGrid())	
	            		 {
	            			 enemiesEligible.add(enemies.indexOf(enemy));
	            		 }
	            	 }
	            	 
	            	 //randomize the positions of the eligible enemies
	            	 Collections.shuffle(enemiesEligible);
	            	 
	            	 //send X amount of enemies to fly to reach the maximum # of enemiesInFlight
	            	 if(enemiesEligible.size()<enemiesInFlightMax-enemiesInFlight)
	            	 {
	            		 // if there isn't enough enemies left, just send all
	            		 for (final Integer enemyFly: enemiesEligible) 
		            	 {
	            			 enemiesInFlight++;
	            			 enemies.get(enemyFly).advanceAction();
	            			 SOUND_MANAGER.enemyFlyDown.play(); 
		            	 }
	            	 }
	            	 
	            	 else
	            		 // there is enough enemies left
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
                
                // player bullet has hit an enemy
                if (collidingBullet != null) {
                	if(!enemy.isBoss())
                	{
	                    enemiesToRemove.add(enemy);
	                    enemyExplosions.add(new Explosion("Images//eExplosion.gif", enemy.getX(), enemy.getY()));
	                    playerBullets.remove(collidingBullet);
	                    score += 10;
	                    
	                    // updates score label
	                    // keeps label from flickering
	                    SwingUtilities.invokeLater(() -> {scoreText.setText("Score: " + this.score);});
	                    
	                    
	                    // plays random sound on enemy death
	                    int soundChoice = random.nextInt(1);
	                    if(soundChoice == 0)
	                    	SOUND_MANAGER.enemyExplosion.play();
	                    else 	
	                    		SOUND_MANAGER.enemyExplosion2.play();
	                    	
                	}
                	else
                	{
                		if(!enemy.isBossHit())
                		{
                			enemy.hitBoss();
                			SOUND_MANAGER.bossHit.play();
                			playerBullets.remove(collidingBullet);
                			enemy.changeImage("Images//eShip2D.gif");
                		}
                		else
                		{
                			enemiesToRemove.add(enemy);
		                    enemyExplosions.add(new Explosion("Images//eExplosion.gif", enemy.getX(), enemy.getY()));
		                    playerBullets.remove(collidingBullet);
		                    score += 10;
		                    
		                    // updates score label
		                    // keeps label from flickering
		                    SwingUtilities.invokeLater(() -> {scoreText.setText("Score: " + this.score);});
		                    
		                    
		                    
		                    SOUND_MANAGER.enemyExplosion3.play();
                		}
                	}
                }
                
                turnToShoot--;
                
                //Collision check between enemy ships and player ship
                Rectangle r1 = new Rectangle(enemy.getX()+2,enemy.getY()+2,enemy.getWidth()-2,enemy.getHeight()-2); // enemy ship hitbox definition
        		Rectangle r2 = new Rectangle(player.getX()+4,player.getY()+4,56,30); // player ship hitbox definition
        		
        		if(r1.intersects(r2))
        		{
        			//explode the player
        			hitPlayer();
        			
        			//set the enemy who collided with player to be removed
        			enemiesToRemove.add(enemy);
        			
        			// add explosion at enemy ship location
        			enemyExplosions.add(new Explosion("Images//eExplosion.gif", enemy.getX(), enemy.getY()));
        		}
            }

            //removes enemies
            for (final FlyingEnemy enemy: enemiesToRemove)
            { 
            	// keep enemiesInFlight accurate by checking if the removed enemy was in flight;
            	if(grid.isBreathing()&&!enemy.isOnGrid())
                    enemiesInFlight--;
            	
                enemies.remove(enemy);
            }

            // Enemy Explosions
            ArrayList<Explosion> enemyExplosionsToRemove = new ArrayList<Explosion>();
            for (Explosion enemyExplosion: enemyExplosions)
            {
                if (enemyExplosion.update()) {
                    enemyExplosionsToRemove.add(enemyExplosion);
                }
            }
            
            //Explosions being removed
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

            // removing enemy bullets set to be removed
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
            // removing player bullets set to be removed
            for (final Projectile playerBullet: playerBulletsToRemove)
            {
                playerBullets.remove(playerBullet);
            } 

            // Next Level
            if(enemies.size() == 0 && !roundOver && !respawning)
            {
            	roundOver = true;
            	
                if(roundNum == 3)
                {
                    overText = new JLabel("You won!");
                    overText.setBounds(maxWidth - 400, 50,maxHeight - 300, 240);
                    overText.setForeground(Color.GREEN);
                }
                if(roundNum < 3)
                {
                	grid.reset();
                    levelText.setText("Round " + roundNum + " passed.");
                    levelText.setVisible(true);
                    
                    	 roundOverTime = System.currentTimeMillis();
                    	 if(respawning)
                    	 {
                    		 roundOverTime += 3*1000;
                    	 }
                    	 elapsedRoundTime = 0L;

                 
                    lives++;
                    
                    // keeps label from flickering
                    // Updates lives label
                    SwingUtilities.invokeLater(() -> {livesText.setText("Lives: " + this.lives);});
                    
                    
                    roundNum++;
                    roundsText.setText("Round " + roundNum + "/3");
                    
                }
                else
                    over = true;
                
                SOUND_MANAGER.breathingDown.stop();
            }
            
            // how often the game refreshes itself (FPS) 
            try
            {
                Thread.sleep( sleep );
            }
            catch( final InterruptedException ex ){}

            // spawn 7 seconds after dying
            if(respawning && elapsedDeathTime > 8*1000 && !gameWillEnd)
            {
            	respawning = false;
            	player.setX(437);
            	respawnText.setVisible(false);
            	
            }
            // wait 7 seconds after last enemy is killed to start next round
            if(roundOver && elapsedRoundTime > 7*1000)
            {
	
            	reset();
            	roundOver = false;
            	 levelText.setVisible(false);
            }
            
            //wait 7 seconds before returning to main menu after last life is lost
            if(gameWillEnd && elapsedGameOverTime > 7*1000)
            {
            	over = true;
            }
            
            // keeps track of time for various timers if relevant booleans true
            if(gameWillEnd)
            	elapsedGameOverTime = (new Date()).getTime() - gameOverTime;
            
            if(respawning)
            	elapsedDeathTime = (new Date()).getTime() - deathTime;
            
            if(roundOver)
            	elapsedRoundTime = (new Date()).getTime() - roundOverTime;
            
            
            this.repaint();//redraw the screen with the updated locations; calls paintComponent below
        }
        
        // current game has ended
        noLivesText.setVisible(false);
        
        enemies.clear();
        playerBullets.clear();
        playerBullets.clear();
        enemyBullets.clear();
        enemyExplosions.clear();
        
        overText.setVisible(true);
        overText.setFont(new Font("Lava", Font.BOLD, 50));
        
        SOUND_MANAGER.stopAll();
        
        try
        {
            Thread.sleep(1500);
        }
        catch( final InterruptedException ex ){}
        
        scoreText.setVisible(false);
        overText.setVisible(false);
        roundsText.setVisible(false);
        livesText.setVisible(false);
        overText.setVisible(false);
        
        this.revalidate();
        
        onMenu =true;
        addMenuText();
        
    }
    
    // Used when an enemy ship or enemy bullet connects with the player ship
    public void hitPlayer()
    {
	    if(!respawning)
	    {
	        
	        
		        // add explosion at player location and move player off-screen
		        enemyExplosions.add(new Explosion("Images//eExplosion.gif", player.getX(), player.getY()));
		        player.setX(400000);
		        SOUND_MANAGER.playerExplosion.play();
		        
		        // update life counter
		        lives--;
			    SwingUtilities.invokeLater(() -> { livesText.setText("Lives: " + this.lives);});
			    respawning = true;
			        
			    // starts game over procedures
		        if (! (lives == 0))
		        {
			        respawnText.setVisible(true);
			        
			       
			        
			        // keep track of time of death
			        
			        deathTime = System.currentTimeMillis();
			        elapsedDeathTime = 0;
		        }
		        else 
		        	gameOver();
	        
	            
	    }
    }

    public void enemyShoot(final int x, final int y) {
        enemyBullets.add(new enProject("images//alienRocket.gif", x, y, player.getX()));

    }

    public void gameOver() {
    	
    	// Wait some time before going to main menu
    	gameOverTime = System.currentTimeMillis();
    	elapsedGameOverTime = 0;
    	gameWillEnd = true;
    	
    	noLivesText.setVisible(true);
        
    }
   
    //Precondition: executed when repaint() or paintImmediately is called
    //Postcondition: the screen has been updated with current player location
    @Override
    public void paintComponent( final Graphics page )
    {
        super.paintComponent(page);
        
        //draws background stars
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
                else 
                	page.fillRect(390, (maxHeight/2)-200+i*100, 150, 50);
            }
            
            
        }
        
        // Draws all game assets
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
	        
	        ArrayList<Explosion> enemyExplosionDraw = new ArrayList<Explosion>(enemyExplosions);
	        for (Explosion enemyExplosion: enemyExplosionDraw)
	        {
	        	if(enemyExplosion != null)
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
        	if(!respawning)
        	{
	            playerBullets.add(new Projectile("Images//rocket.gif", player.getX(), player.getY()));
	            SOUND_MANAGER.playerShot.play();
        	}
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
                        0,3+i,true)); // row and column numb
            
            for(int i = 0; i <4; i++)
                enemies.add(new FlyingEnemy("Images//eShip2.gif", 387, -200-(60*i), 4, player, //spawn location
                        3,3+i,true)); // row and column numb
            
            for(int i = 0; i <10; i++)
                enemies.add(new FlyingEnemy("Images//eShip.gif", -1000-(90*i), 700 , 1, player, //spawn location
                        1,i,false)); // row and column numb

            for(int i = 0; i <10; i++)
                enemies.add(new FlyingEnemy("Images//eShip3.gif", 3876+(90*i), 700  , 2, player, //spawn location
                        2,i,false)); // row and column numb
	}
    
	public void minusOneFlying()
	{
		enemiesInFlight--;
	}
	
	 public boolean gridIsBreathing()
	{
	    return grid.isSetToBreathe();
	}
    public void actionPerformed(final ActionEvent event) {}

    public void keyTyped( final KeyEvent event ) {}

    public void mouseMoved(final MouseEvent event) {}

    public void mouseDragged(final MouseEvent event) {}

}