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

	private boolean leftArrowDown = false;
	private boolean rightArrowDown = false;
	private boolean over = true;

	private final int MAX_ENEMY_BULLETS = 1;
	private final int MAX_PLAYER_BULLETS = 2;

	JLabel livesText = new JLabel("Lives: " + this.lives);
	JLabel overText = new JLabel("GAME OVER");
	JLabel scoreText = new JLabel("Score: " + this.score);
	JLabel roundsText = new JLabel("Round " + roundNum + "/3");
	JLabel levelText = new JLabel("Round " + roundNum + " passed.");

	// constructor - sets the initial conditions for this Game object
	public Game(final int width, final int height) {
		this.setLayout(null);// Don't change
		this.setBackground(Color.BLACK);
		this.setPreferredSize(new Dimension(width, height));

		enemies = new ArrayList<Enemy>();
		playerBullets = new ArrayList<Projectile>();
		enemyBullets = new ArrayList<enProject>();
		enemyExplosions = new ArrayList<Explosion>();
		background = new Background();

		score = 0;
		lives = 3;
		roundNum = 1;
		sleep = 20;

		initialize();
	}

	public void initialize()
	{
		player = new Player("Images/pShip.gif", "Images/pShip2.gif", "Images/pShip3.gif", 450, 650 );//450, 750
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
					overText.setBounds(400, 50, 400, 240);
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
	}

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
		if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
			rightArrowDown = false;
		} else if (event.getKeyCode() == KeyEvent.VK_LEFT) {
			leftArrowDown = false;
		}
	}

	public void reset()
	{
		for (int row=0; row<5; row++)
		{
			for (int col=0; col<7; col++)
			{
				final int posX = (col+1) * 100 + 70;
				final int posY = (row+1) * 100 - 70;
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

	public void actionPerformed(final ActionEvent event) {}
	public void keyTyped( final KeyEvent event ) {}
	public void mouseMoved(final MouseEvent event) {}
	public void mouseDragged(final MouseEvent event) {}
}