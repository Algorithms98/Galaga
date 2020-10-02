import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

public class FlyingEnemy extends Enemy{

	private int friction = 0;
	private float rotation;
	private float dx = 0;
	private float dy = 0;
	private Player player;
	public FlyingEnemy(Image img, int xLoc, int yLoc, Player player) {
		super(img, xLoc, yLoc);
		this.player = player;
		
	}
	public FlyingEnemy(String path, int xLoc, int yLoc, Player player) {
		super(path, xLoc, yLoc);
		this.player = player;
		
	}
	public void move()
	{
		final int frictionMax = 5;
		friction ++;
		if(y<400)
		{
			
			if(friction >= frictionMax)
			{
				double p = (double)player.getX()/1000.0;
				
				if(player.getX() >450)
				dx +=.1;
				else dx-=.1;
				
				dy = (int) (dx*dx/(p*2));
				x += dx; 
				y += dy;
				friction = 0;
				
			}
		}else 
			{
				y=0;
				dy=0;
				dx =0;
			}
		
	}

	public void draw( Graphics page )
	{
		//used to rotate the rendering of the image, does not effect coordinates as it rotates in place from center
		
		Graphics2D g2d=(Graphics2D)page;       // Create a Java2D version of g.
		AffineTransform a = AffineTransform.getRotateInstance(rotation, x+image.getHeight(null)/2, y + image.getWidth(null)/2);
		rotation+=0;
		
	    //Set our Graphics2D object to the transform
	    g2d.setTransform(a);
	    //Draw our image like normal
	    g2d.drawImage(image, x, y, null);
		//page.drawImage(image, x - 5, y, null);
	}

}
