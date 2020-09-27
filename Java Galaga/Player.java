import java.awt.*;//must be imported to use Graphics
import javax.swing.*;

public class Player
{
    private int x; // x and y location
    private int y;
    private Image image;
    private Image healthy;
    private Image dmg1;
    private Image dmg2;
    
    public Player(Image healthy, Image dmg1, Image dmg2, int xLoc, int yLoc)
    {
        x = xLoc;
        y = yLoc;
        this.healthy = healthy;
        this.dmg1 = dmg1;
        this.dmg2 = dmg2;
        image = this.healthy;
    }
    
    public Player(String path1, String path2, String path3, int xLoc, int yLoc)
    {
        this(new ImageIcon(path1).getImage(), new ImageIcon(path2).getImage(), new ImageIcon(path3).getImage(), xLoc, yLoc);
    }
    
    public void updateSprite(int lives)
    {
		if (lives > 2)
		{
			image = healthy;
		}
		else if (lives == 2)
		{
			image = dmg1;
		}
		else
		{
			image = dmg2;
		}
    }

    public void moveRight() //moves player right
    {
        x += 5;
        if (x>Main.WIDTH - 90) {
          x = Main.WIDTH - 90;
        }
    }
    
    public void moveLeft() // moves player left
    {
        x -= 5;
        if (x<0) {
          x = 0;
        }
    }
    
    public void movePlayer(int mouseLocation) // moves player's position to hte mouse's position
    {
        x = mouseLocation;
    }
    
    public void draw( Graphics page ) // draws player paddle
    {
        page.setColor( Color.WHITE );
        //page.fillRect( x, y, 100, 10 );//change the last two numbers and see what happens
    }
    
    public int getX() // returns x value
    {
        return this.x;
    }
    
    public int getY() // returns y value
    {
        return this.y;
    }
    
    public Image getImage()
    {
        return image;
    }
    
    public void setX(int xVal)
    {
        x = xVal;
    }
}