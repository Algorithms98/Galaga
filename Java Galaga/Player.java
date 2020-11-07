import java.awt.*;//must be imported to use Graphics
import java.awt.geom.AffineTransform;

import javax.swing.*;

public class Player extends DrawableObject
{
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
		x += 8;
		if (x>Main.WIDTH - 90) {
			x = Main.WIDTH - 90;
		}
	}

	public void moveLeft() // moves player left
	{
		x -= 8;
		if (x<0) {
			x = 0;
		}
	}

  @Override
	public void draw( Graphics page ) // draws player paddle
	{
		page.drawImage(image, x, y, null);
	}
}