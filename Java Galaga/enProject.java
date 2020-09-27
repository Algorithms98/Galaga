import java.awt.*;
import javax.swing.*;

public class enProject
{
	private int x; // x and y location
	private int y;
	private Image image;

	public enProject(Image img, int xLoc, int yLoc )
	{
		x = xLoc;
		y = yLoc;
		image = img;
	}

	public enProject(String path, int xLoc, int yLoc)
	{
		this(new ImageIcon(path).getImage(), xLoc, yLoc);
	}

	public boolean update(Player player, Game game)
	{
		move();
		if (isInsideP(player))
		{
			game.hitPlayer();
			return true;
		}
		if (y > 780)
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
		for(int i = 1000; i > 0; i--)
		{
			if(y < 800 && i % 100 == 0)
			{
				this.y += 1;
			}
		}
	}

	public boolean isInsideP(Player p)
	{
		if(y <= p.getY() && y >= p.getY() - 75)
		{
			if(x <= p.getX() + 100 && x >= p.getX())
			{
				return true;
			}
		}
		return false;
  }
}