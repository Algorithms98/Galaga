import java.awt.*;
import java.util.Random; 
import javax.swing.*;

public class Background extends JLabel {
	private static final long serialVersionUID = 1376221642883404767L;
	private BackgroundDot[] dots;
	final private int flickerRate = 4;
	final private int numOfDots = 150;
	final private int width;
	final private int height;
	private int fallRate = 3;

	public Background(Game game) {	
		width = game.getPreferredSize().width;
		height=	game.getPreferredSize().height;
		this.setBounds(0, 0,  width, height);
		this.setVisible(true);
		this.setLocation(0, 0);
		
		dots = new BackgroundDot[numOfDots]; //array of our background dots
		Random rand = new Random(); 
		System.out.println(game.getPreferredSize().width);
		for(int i = 0; i <numOfDots; i++)
		{
			dots[i] = new BackgroundDot(rand.nextInt(width),rand.nextInt(height)); // put the dots in random locations based on the width and height
	
			
		}
	
	}

	public void draw(Graphics page)
	{
		
		// position updating loop
		for(int i = 0; i <numOfDots; i++)
		{
			dots[i].setY((dots[i].getY()+ fallRate));  // checks if dot is offscreen and puts it at the top or bottom
			if(dots[i].getY() > height)
			{
				dots[i].setY((-1));
			}
			if(dots[i].getY() < -1)
			{
				dots[i].setY(height);
			}
		}
		
		// drawing loop
		for(int i = 0; i <numOfDots; i++)
		{
			//sets the jFrame color to the current dots color
			page.setColor(dots[i].getColor());
			//draws the *star*
			page.drawRect(dots[i].getX(), dots[i].getY(), 2, 2);
			
			// code used to flicker the stars,essentially causes each dot to gain or lose alpha by flickerRate amount
			if(dots[i].getAlphaDir())
			{
				//changes alpha of current dot's color
				dots[i].setColor(dots[i].getColor().getRed(),dots[i].getColor().getGreen(),dots[i].getColor().getBlue(),Math.min(dots[i].getColor().getAlpha()+flickerRate,255));
				
				// when alpha is at max value, start subtracting
				if(dots[i].getColor().getAlpha() == 255)
				{
					// causes the alpha to be subtracted from
					dots[i].setAlphaDir(false);
		
				}
			}
				else
				{
					//changes alpha of current dot's color
					dots[i].setColor(dots[i].getColor().getRed(),dots[i].getColor().getGreen(),dots[i].getColor().getBlue(),Math.max(dots[i].getColor().getAlpha()-flickerRate,0));
					
					// when alpha is at min value, start adding
					if(dots[i].getColor().getAlpha() == 0)
					{
						// causes the alpha to be added to
						dots[i].setAlphaDir(true);
					}
				}
		}
	}
}