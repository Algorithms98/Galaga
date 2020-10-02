import java.awt.Color;
import java.util.Random; 
public class BackgroundDot {
	
	private int x;
	private int y;
	private Color color;

	private boolean alphaUp = true;
	public BackgroundDot(int x, int y)
	{
		Random rand = new Random(); 
		this.x = x;
		this.y = y;
		
		int min = 0;
		int max = 255;
		
		int R = rand.nextInt(256);
		int G = rand.nextInt(256);
		int B = rand.nextInt(256);
		int alpha = rand.nextInt(256);
		
		color = new Color((int)R,(int)G,(int)B,(int)alpha);
	
		
		
	}
	public void setAlphaDir(boolean alphaUp)
	{
		this.alphaUp = alphaUp;
		
	}
	public void setX(int x)
	{
		this.x = x;
	}
	public void setY(int y)
	{
		this.y = y;
	}
	public void setColor(Color color)
	{
		this.color = color;
	}
	public int getX()
	{
		return x;
		
	}
	public int getY()
	{
		return y;
	}
	public Color getColor()
	{
		return color;
	}
	
	public boolean getAlphaDir()
	{
		return alphaUp;
	}
	public void setColor(int red, int green, int blue, int alpha) {
		color = new Color(red,green,blue,alpha);
	}
}
