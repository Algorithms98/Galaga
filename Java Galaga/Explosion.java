import java.awt.*;//must be imported to use Graphics and Color
import javax.swing.*;

public class Explosion
{
	private int x;                  // x position
	private int y;                  // y position
	private Image image;
	private int lifetime;

	public Explosion(Image img, int xLoc, int yLoc )
	{
		x = xLoc;
		y = yLoc;
		image = img;
		lifetime = 30;
	}

	public Explosion(String path, int xLoc, int yLoc)
	{
		this(new ImageIcon(path).getImage(), xLoc, yLoc);
	}
	
	public void draw(Graphics page)
	{
		page.drawImage(image, x - 15, y - 10, null);
	}

	public boolean update()
	{
		lifetime--;
		return lifetime < 0;
	}
}