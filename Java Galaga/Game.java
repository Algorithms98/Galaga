import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Game extends JPanel implements KeyListener, ActionListener, MouseMotionListener
{
    private static final long serialVersionUID = -4999101245149671618L;
    private static final SoundManager SOUND_MANAGER = new SoundManager();
    private final Background background;
    private Player player;
    private final ArrayList<Enemy> enemies;
    private final ArrayList<enProject> enemyBullets;
    private final ArrayList<Projectile> playerBullets;
    private final ArrayList<Explosion> enemyExplosions;

    private int score;
    private int lives;
    private int roundNum;
    private int sleep;
    private int maxWidth;
    private int maxHeight;
<<<<<<< Upstream, based on origin/master
=======
    private int menuChoice;
    private int tempChoice;
    private boolean leftArrowDown = false;
    private boolean rightArrowDown = false;
    private boolean EnterDown = false;
    private boolean upArrowDown = false;
    private boolean downArrowDown = false;
    private boolean pressCounted = false;
    private boolean over = true;
    private boolean onMenu = true;
>>>>>>> 46b35d8 menu added, work on flying and circular path taking

<<<<<<< Upstream, based on origin/master
    private boolean leftArrowDown = false;
    private boolean rightArrowDown = false;
    private boolean over = true;
=======
    private final int MAX_ENEMY_BULLETS = 1;
    private final int MAX_PLAYER_BULLETS = 2;
    private final int numOfMenus = 4;
    
    JLabel title = new JLabel("Inspire AI ");
    JLabel game1 = new JLabel("Player Game");
    JLabel game2 = new JLabel("Player vs Ai ");
    JLabel options = new JLabel("Options ");
    JLabel quit = new JLabel("Quit ");
    
>>>>>>> 46b35d8 menu added, work on flying and circular path taking

<<<<<<< Upstream, based on origin/master
    private final int MAX_ENEMY_BULLETS = 1;
    private final int MAX_PLAYER_BULLETS = 2;
=======
    JLabel livesText = new JLabel("Lives: " + this.lives);
    JLabel overText = new JLabel("GAME OVER");
    JLabel scoreText = new JLabel("Score: " + this.score);
    JLabel roundsText = new JLabel("Round: " + roundNum);
    JLabel levelText = new JLabel("Round " + roundNum + " passed.");
>>>>>>> 46b35d8 menu added, work on flying and circular path taking

<<<<<<< Upstream, based on origin/master
    JLabel livesText = new JLabel("Lives: " + this.lives);
    JLabel overText = new JLabel("GAME OVER");
    JLabel scoreText = new JLabel("Score: " + this.score);
    JLabel roundsText = new JLabel("Round: " + roundNum);
    JLabel levelText = new JLabel("Round " + roundNum + " passed.");
=======
    // constructor - sets the initial conditions for this Game object
    public Game(final int width, final int height) {
        this.setLayout(null);// Don't change
        this.setBackground(Color.BLACK);
        this.setPreferredSize(new Dimension(width, height));
>>>>>>> 46b35d8 menu added, work on flying and circular path taking

<<<<<<< Upstream, based on origin/master
    // constructor - sets the initial conditions for this Game object
    public Game(final int width, final int height) {
        this.setLayout(null);// Don't change
        this.setBackground(Color.BLACK);
        this.setPreferredSize(new Dimension(width, height));
=======
        enemies = new ArrayList<Enemy>();
        playerBullets = new ArrayList<Projectile>();
        enemyBullets = new ArrayList<enProject>();
        enemyExplosions = new ArrayList<Explosion>();
        background = new Background(this);
        
        score = 0;
        menuChoice = 0;
        tempChoice = 1;
        roundNum = 1;
        
        lives = 3;
        
        sleep = 20;
        maxWidth = width;
        maxHeight = height;
        
>>>>>>> 46b35d8 menu added, work on flying and circular path taking

<<<<<<< Upstream, based on origin/master
        enemies = new ArrayList<Enemy>();
        playerBullets = new ArrayList<Projectile>();
        enemyBullets = new ArrayList<enProject>();
        enemyExplosions = new ArrayList<Explosion>();
        background = new Background(this);
=======
      this.addMouseMotionListener(this);
      this.addKeyListener(this);//allows the program to respond to key presses - Don't change
      this.setFocusable(true);//I'll tell you later - Don't change
    }
>>>>>>> 46b35d8 menu added, work on flying and circular path taking

<<<<<<< Upstream, based on origin/master
        score = 0;
        lives = 3;
        roundNum = 1;
        sleep = 20;
        maxWidth = width;
        maxHeight = height;
=======
void menu() {
	
	addMenuText();
>>>>>>> 46b35d8 menu added, work on flying and circular path taking

<<<<<<< Upstream, based on origin/master
        initialize();
    }

    public void initialize()
    {
        player = new Player("Images/pShip.gif", "Images/pShip2.gif", "Images/pShip3.gif", 150, 690 );//450, 750
        
        scoreText.setBounds(maxWidth - 300, 5, maxHeight - 50, 20);//Score:
        scoreText.setForeground(Color.WHITE);
        scoreText.setFont(new Font("Lava", Font.BOLD, 20));
        scoreText.setVisible(true);
        this.add(scoreText);

        levelText.setForeground(Color.WHITE);//Round Passed
        levelText.setVisible(false);
        levelText.setFont(new Font("Lava", Font.BOLD, 49));
        levelText.setBounds(340, 50, 400, 240);
        this.add(levelText);

        overText.setForeground(Color.WHITE);//Game Over
        overText.setVisible(false);
        overText.setBounds(maxWidth - 400, 50,maxHeight - 300, 240);
        overText.setForeground(Color.RED);

        livesText.setBounds(10, 5, 850, 20);//Lives:
        livesText.setForeground(Color.WHITE);
        livesText.setFont(new Font("Lava", Font.BOLD, 20));
        livesText.setVisible(true);
        this.add(livesText);

        roundsText.setBounds(maxWidth - 100, 5, maxHeight - 50, 20);//Rounds:
        roundsText.setForeground(Color.WHITE);
        roundsText.setFont(new Font("Lava", Font.BOLD, 20));
        roundsText.setVisible(true);
        this.add(roundsText);

        reset();
        this.addMouseMotionListener(this);
        this.addKeyListener(this);//allows the program to respond to key presses - Don't change
        this.setFocusable(true);//I'll tell you later - Don't change
    }

    //This is the method that runs the game
    public void playGame()
    {
        over = false;
        while( !over )
        {
            // Player
            if (leftArrowDown) {
                player.moveLeft();
            }
            if (rightArrowDown) {
                player.moveRight();
            }

            // Enemies
            int turnToShoot = (int) (Math.random() * enemies.size());
            ArrayList<Enemy> enemiesToRemove = new ArrayList<Enemy>();
            for (final Enemy enemy: enemies) {
                // Returns colliding bullet if enemy gets blown up
                final Projectile collidingBullet = enemy.update(turnToShoot == 0, enemyBullets.size() < MAX_ENEMY_BULLETS, playerBullets, this);
                if (collidingBullet != null) {
                    enemiesToRemove.add(enemy);
                    enemyExplosions.add(new Explosion("Images//eExplosion.gif", enemy.getX(), enemy.getY()));
                    playerBullets.remove(collidingBullet);
                    score += 10;
                    scoreText.setText("Score: " + this.score);
                    SOUND_MANAGER.enemyExplosion.play();
                }
                turnToShoot--;
            }

            for (final Enemy enemy: enemiesToRemove)
            {
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
                    livesText.setText("Lives: " + this.lives);                    
                    roundNum++;
                    roundsText.setText("Round " + roundNum + "/3");
                    player.updateSprite(lives);
                }
                else
                    over = true;
            }

            try
            {
                Thread.sleep( sleep );
            }
            catch( final InterruptedException ex ){}
=======
		while(menuChoice !=4)
		{
			try
			{
				Thread.sleep( sleep);
			}
			catch( final InterruptedException ex ){}
>>>>>>> 46b35d8 menu added, work on flying and circular path taking

<<<<<<< Upstream, based on origin/master
            this.repaint();//redraw the screen with the updated locations; calls paintComponent below
        }

        enemies.clear();
        playerBullets.clear();
        enemyBullets.clear();
        enemyExplosions.clear();
        overText.setVisible(true);
        overText.setFont(new Font("Lava", Font.BOLD, 50));
        this.add(overText);
    }
=======
			
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
					lives = 3;
					score = 0;menuChoice=0;
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
>>>>>>> 46b35d8 menu added, work on flying and circular path taking

<<<<<<< Upstream, based on origin/master
    public void hitPlayer()
    {
        if (lives == 1)
            gameOver();
        lives--;
        livesText.setText("Lives: " + this.lives);
        player.updateSprite(lives);
        if (lives != 0)
            SOUND_MANAGER.loseLife.play();
        else
            SOUND_MANAGER.playerExplosion.play();
    }
=======
private void addMenuText() {
	
		title.setBounds(maxWidth/2 -161, 100, 400, 300);//title
		title.setForeground(Color.WHITE);
		title.setFont(new Font("Lava", Font.BOLD, 80));
		title.setVisible(true);
	    this.add(title);
    
    	game1.setBounds(maxWidth/2 -35, maxHeight/2-52, 200, 50);//Menu1
    	game1.setForeground(Color.WHITE);
    	game1.setFont(new Font("Lava", Font.BOLD, 20));
    	game1.setVisible(true);
        this.add(game1);
        
        game2.setBounds(maxWidth/2 - 35 +3, maxHeight/2-52+100, 200, 50);//Menu2
        game2.setForeground(Color.WHITE);
        game2.setFont(new Font("Lava", Font.BOLD, 20));
        game2.setVisible(true);
        this.add(game2);
        
        options.setBounds(maxWidth/2 -35 +22, maxHeight/2-52+200, 200, 50);//Menu3
        options.setForeground(Color.WHITE);
        options.setFont(new Font("Lava", Font.BOLD, 20));
        options.setVisible(true);
        this.add(options);
        
        quit.setBounds(maxWidth/2 -35 + 38, maxHeight/2-52+300, 200, 50);//Menu4
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
	//this.revalidate();
}
>>>>>>> 46b35d8 menu added, work on flying and circular path taking

<<<<<<< Upstream, based on origin/master
    public void enemyShoot(final int x, final int y) {
        enemyBullets.add(new enProject("images//alienRocket.gif", x, y));
        SOUND_MANAGER.enemyShot.play();
    }
=======
	public void initialize()
    {
        player = new Player("Images/pShip.gif", "Images/pShip2.gif", "Images/pShip3.gif", 150, 690 );//450, 750
        
        scoreText.setBounds(maxWidth - 300, 5, maxHeight - 50, 20);//Score:
        scoreText.setForeground(Color.WHITE);
        scoreText.setFont(new Font("Lava", Font.BOLD, 20));
        scoreText.setVisible(true);
        this.add(scoreText);
>>>>>>> 46b35d8 menu added, work on flying and circular path taking

<<<<<<< Upstream, based on origin/master
    public void gameOver() {
        over = true;
    }
=======
        levelText.setForeground(Color.WHITE);//Round Passed
        levelText.setVisible(false);
        levelText.setFont(new Font("Lava", Font.BOLD, 49));
        levelText.setBounds(340, 50, 400, 240);
        this.add(levelText);
>>>>>>> 46b35d8 menu added, work on flying and circular path taking

<<<<<<< Upstream, based on origin/master
    //Precondition: executed when repaint() or paintImmediately is called
    //Postcondition: the screen has been updated with current player location
    @Override
    public void paintComponent( final Graphics page )
    {
        super.paintComponent(page);
        background.draw(page);
        for (final Projectile playerBullet: playerBullets)
        {
            playerBullet.draw(page);
        }
        for (final enProject enemyBullet: enemyBullets)
        {
            enemyBullet.draw(page);
        }
        if (!over)
            player.draw(page);
        for (final Enemy enemy: enemies) {
            enemy.draw(page);
        }
        for (Explosion enemyExplosion: enemyExplosions)
        {
            enemyExplosion.draw(page);
        }
=======
        overText.setForeground(Color.WHITE);//Game Over
        overText.setVisible(false);
        overText.setBounds(maxWidth/2-150, maxHeight/2-120,maxHeight - 300, 240);
        overText.setForeground(Color.RED);

        livesText.setBounds(10, 5, 850, 20);//Lives:
        livesText.setForeground(Color.WHITE);
        livesText.setFont(new Font("Lava", Font.BOLD, 20));
        livesText.setVisible(true);
        this.add(livesText);

        roundsText.setBounds(maxWidth - 100, 5, maxHeight - 50, 20);//Rounds:
        roundsText.setForeground(Color.WHITE);
        roundsText.setFont(new Font("Lava", Font.BOLD, 20));
        roundsText.setVisible(true);
        this.add(roundsText);
        
        reset();
        playGame();
        
    }

    //This is the method that runs the game
    public void playGame()
    {
        over = false;
        while( !over )
        {
            // Player
            if (leftArrowDown) {
                player.moveLeft();
            }
            if (rightArrowDown) {
                player.moveRight();
            }

            // Enemies
            int turnToShoot = (int) (Math.random() * enemies.size());
            ArrayList<Enemy> enemiesToRemove = new ArrayList<Enemy>();
            for (final Enemy enemy: enemies) {
                // Returns colliding bullet if enemy gets blown up
                final Projectile collidingBullet = enemy.update(turnToShoot == 0, enemyBullets.size() < MAX_ENEMY_BULLETS, playerBullets, this);
                if (collidingBullet != null) {
                    enemiesToRemove.add(enemy);
                    enemyExplosions.add(new Explosion("Images//eExplosion.gif", enemy.getX(), enemy.getY()));
                    playerBullets.remove(collidingBullet);
                    score += 10;
                    scoreText.setText("Score: " + this.score);
                    SOUND_MANAGER.enemyExplosion.play();
                }
                turnToShoot--;
            }

            for (final Enemy enemy: enemiesToRemove)
            {
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
                    livesText.setText("Lives: " + this.lives);                    
                    roundNum++;
                    roundsText.setText("Round " + roundNum + "/3");
                    player.updateSprite(lives);
                }
                else
                    over = true;
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
        this.remove(overText);
        this.remove(scoreText);
        this.remove(roundsText);
        this.remove(livesText);
        //this.revalidate();
        
        onMenu =true;
        addMenuText();
>>>>>>> 46b35d8 menu added, work on flying and circular path taking
    }

<<<<<<< Upstream, based on origin/master
    //tells the program what to do when keys are pressed
    public void keyPressed( final KeyEvent event )
    {
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
            playerBullets.add(new Projectile("Images//rocket.gif", player.getX() - 20, player.getY() - 30));
            SOUND_MANAGER.playerShot.play();
        }
    }
=======
    public void hitPlayer()
    {
        if (lives == 1)
            gameOver();
        lives--;
        livesText.setText("Lives: " + this.lives);
        player.updateSprite(lives);
        if (lives != 0)
            SOUND_MANAGER.loseLife.play();
        else
            SOUND_MANAGER.playerExplosion.play();
    }

    public void enemyShoot(final int x, final int y) {
        enemyBullets.add(new enProject("images//alienRocket.gif", x, y));
        SOUND_MANAGER.enemyShot.play();
    }

    public void gameOver() {
        over = true;
    }

    //Precondition: executed when repaint() or paintImmediately is called
    //Postcondition: the screen has been updated with current player location
    @Override
    public void paintComponent( final Graphics page )
    {
        super.paintComponent(page);
        background.draw(page);
        
        // draws main menu
        if(onMenu)
        {
        	page.setColor(Color.DARK_GRAY);
        	
        	for(int i =1; i <=numOfMenus; i++)
        	{
        		if(tempChoice ==i)
            	{
    		        page.setColor(Color.red);
    		        page.fillRect(450, 350+i*100, 150, 50);
    		        page.setColor(Color.DARK_GRAY);
            	}
        		else page.fillRect(450, 350+i*100, 150, 50);
        	}
        	
	        
        }
        
        
        for (final Projectile playerBullet: playerBullets)
        {
            playerBullet.draw(page);
        }
        for (final enProject enemyBullet: enemyBullets)
        {
            enemyBullet.draw(page);
        }
        if (!over)
            player.draw(page);
        for (final Enemy enemy: enemies) {
            enemy.draw(page);
        }
        for (Explosion enemyExplosion: enemyExplosions)
        {
            enemyExplosion.draw(page);
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
>>>>>>> 46b35d8 menu added, work on flying and circular path taking

<<<<<<< Upstream, based on origin/master
    //not used but must be present
    public void keyReleased( final KeyEvent event )
    {
        if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightArrowDown = false;
        } else if (event.getKeyCode() == KeyEvent.VK_LEFT) {
            leftArrowDown = false;
        }
    }
=======
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
	}
>>>>>>> 46b35d8 menu added, work on flying and circular path taking

<<<<<<< Upstream, based on origin/master
    public void reset()
    {
        for (int row=0; row<5; row++)
        {
=======
	public void reset()
	{
		
				
		for (int row=0; row<5; row++)
        {

            for (int col=0; col<5; col++)
            {
                final int posX = (col+1) * 75 + 70;
                final int posY = (row+1) * 75 - 70;
                //enemies.add(new FlyingEnemy("Images//eShip.gif", posX, posY,player));
                if (row == 0)
                    enemies.add(new Enemy("Images//eShip3.gif", posX, posY));
                else if (row < 3)
                    enemies.add(new Enemy("Images//eShip2.gif", posX, posY));
                else
                    enemies.add(new Enemy("Images//eShip.gif", posX, posY));
            }
        }
				
		
		
	}
>>>>>>> 46b35d8 menu added, work on flying and circular path taking

<<<<<<< Upstream, based on origin/master
            for (int col=0; col<5; col++)
            {
                final int posX = (col+1) * 75 + 70;
                final int posY = (row+1) * 75 - 70;
                //enemies.add(new FlyingEnemy("Images//eShip.gif", posX, posY,player));
                if (row == 0)
                    enemies.add(new Enemy("Images//eShip3.gif", posX, posY));
                else if (row < 3)
                    enemies.add(new Enemy("Images//eShip2.gif", posX, posY));
                else
                    enemies.add(new Enemy("Images//eShip.gif", posX, posY));
            }
        }
        sleep -= 5;
    }

=======
>>>>>>> 46b35d8 menu added, work on flying and circular path taking
    public void actionPerformed(final ActionEvent event) {}

    public void keyTyped( final KeyEvent event ) {}

    public void mouseMoved(final MouseEvent event) {}

public void mouseDragged(final MouseEvent event) {}
}