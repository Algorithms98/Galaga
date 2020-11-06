import java.awt.*;
import java.util.Random;

import javax.swing.*;

public class enProject extends DrawableObject
{
	private int xDelta;
	
	private Random rand;

	public enProject(Image img, int xLoc, int yLoc, int playerX )
	{
		rand = new Random();
		x = xLoc;
		y = yLoc;
		
		xDelta = (playerX-x)/50;
		if(xDelta>0)
			xDelta+=rand.nextInt(3);
			if(xDelta < 0)
				xDelta-=rand.nextInt(3);

		image = img;
		
		
	}

	public enProject(String path, int xLoc, int yLoc,int playerX)
	{
		this(new ImageIcon(path).getImage(), xLoc, yLoc,playerX);
		
	}

	public boolean update(Player player, Game game)
	{
		move();
		
		if (isInsideP(player))
		{
			game.hitPlayer();
			return true;
		}
		if (y > 950)
		{
			return true;
		}
		return false;
	}

  @Override
	public void draw(Graphics page)
	{
		
		page.drawImage(image, x, y + 10, null);
	}

	public void move()
	{
		y+=10;
		
		x+= xDelta;
	}

	public boolean isInsideP(Player p)
	{
		Rectangle r1 = new Rectangle(x,y+2,8,25);
		Rectangle r2 = new Rectangle(p.getX()+2,p.getY()+2,58,30);
		if(r1.intersects(r2))
		{
		    return true;
		}
		return false;
	}
}