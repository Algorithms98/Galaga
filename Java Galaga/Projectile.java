import java.awt.*;
import javax.swing.*;

public class Projectile extends DrawableObject
{
	// x and y location
	private int x; 
	private int y;
	
	private Image image;

	public Projectile(Image img, int xLoc, int yLoc )
	{
		x = xLoc;
		y = yLoc;
		image = img;
	}

	public Projectile(String path, int xLoc, int yLoc)
	{
		this(new ImageIcon(path).getImage(), xLoc, yLoc);
	}

	public void draw(Graphics page)
	{
		page.drawImage(image, x , y, null);
		
	}

	public boolean update()
	{
		y -= 20;
		return y < 0;
	}

	public boolean isInside(Enemy en)
	{
		Rectangle r1 = new Rectangle(en.getX()+1,en.getY()+1,en.getWidth()-1,en.getHeight()-1); // enemy ship hitbox definition
		Rectangle r2 = new Rectangle(x+1,y,9,30); // player ship hitbox definition
		if(r1.intersects(r2))
		{
			return true;
		}
		return false;
	}
}