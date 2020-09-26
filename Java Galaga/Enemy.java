import java.awt.*;//must be imported to use Graphics and Color
import javax.swing.*;

public class Enemy
{
    private int x;                  // x position
    private int y;                  // y position
    private int vx;                 // x velocity
    private int vy;    
    private int numMoves;   
    private int maxMoves;
    private boolean isDead;
    private Image image;

    public Enemy(Image img, int xLoc, int yLoc )
    {
        x = xLoc;
        y = yLoc;
        vx = 1;
        vy = 20;
        numMoves = 0;
        maxMoves = 150;
        isDead = false;
        image = img;
    }
    
    public Enemy(String path, int xLoc, int yLoc)
    {
        this(new ImageIcon(path).getImage(), xLoc, yLoc);
        vx = 1;
        vy = 20;
        numMoves = 0;
        maxMoves = 150;
        isDead = false;
    }
	
	public boolean update(boolean turnToShoot, boolean enCanShoot, Projectile bullet, Game game)
	{
		if (turnToShoot && enCanShoot) {
			game.enemyShoot(x + 10, y);
		}
		move();
		if (y > 680)
			game.gameOver();
		if (bullet.isInside(this)) {
			return true;
		}
		return false;
	}

    public void move()
    {
        x += vx;
        if(numMoves > maxMoves)
        {
            vx = -vx;
            y += vy;
            numMoves = 0;
            maxMoves = 300;
        } 
        numMoves++;
    }

    public void draw( Graphics page )
    {
        page.setColor( new Color( 255, 255, 255 ) ); //color defined using rgb values (0-255 each)
        //page.fillRect( x, y, 55, 55 ); //change the last two numbers and see what happens
    }
    
    public void setIsDead(boolean set)
    {
        isDead = set;
    }
    
    public boolean isDead()
    {
        return isDead;
    }
    
    public int getY()
    {
        return this.y;
    }
    
    public int getX()
    {
        return this.x;
    }
    
    public void setX(int val)
    {
        x = val;
    }
    
    public void setY(int val)
    {
        y = val;
    }
    
    public static boolean isAllDead(Enemy[][] enemy)
    {
        return false;
    }
    
    public Image getImage()
    {
        return image;
    }
}