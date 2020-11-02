import java.awt.*;
import java.util.Random;

import javax.swing.*;

public class enProject
{
	private int x; // x and y location
	private int y;
	private int xDelta;
	
	private Image image;
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
		if(y <= p.getY()-20 && y >= p.getY() - 60)
		{
			for(int i=0; i<9;i++)
			if(x+i <= p.getX() + 58 && x+i >= p.getX()+2)
			{
				return true;
			}
		}
		return false;
	}
}