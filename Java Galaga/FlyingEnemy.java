import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

public class FlyingEnemy extends Enemy{

	private int actionState = 0;
	private int friction = 0;
	private int radius = 0;
	private double angleDelta;
	private double angle = 0;
	private float rotation;	
	private Player player;
	private int currentCenterY;
	private int currentCenterX;
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
		switch(actionState) {
		case 0: x++;
				if(x>=350 && x <=650)
				{
					actionState ++;
					setCircle(x,y+100,true);
				}
				break;
		case 1:
			
			moveAroundSetCircle();
			break;
		case 2: final int frictionMax = 0;
				friction ++;
	
				if(friction >= frictionMax)
				{
					
					moveAroundSetCircle();
					friction = 0;
					
				}
				break;
		
		}
	}

	public void draw( Graphics page )
	{
		//used to rotate the rendering of the image, does not effect coordinates as it rotates in place from center
		
		Graphics2D g2d=(Graphics2D)page;       // Create a Java2D version of g.
		AffineTransform a = AffineTransform.getRotateInstance(rotation, x+image.getHeight(null)/2, y + image.getWidth(null)/2);
		//rotation= (float) angle;
		
	    //Set our Graphics2D object to the transform
	    g2d.setTransform(a);
	    //Draw our image like normal
	    g2d.drawImage(image, x, y, null);
	    g2d.drawRect(currentCenterX, currentCenterY, 20, 20);
		//page.drawImage(image, x - 5, y, null);
	}
	
	private void moveAroundSetCircle()
	{
		
		
		x = (int) (currentCenterX + Math.cos(Math.toRadians(angle))*radius);
		y = (int) (currentCenterY + Math.sin(Math.toRadians(angle))*radius);
		
			if(angle >360)
			{
				angle = 0+angle%360;
			}
		
			
			if(angle <0)
			{
				angle = 360 - Math.abs(angle);
			}
			angle+= angleDelta;
			
		
	}
	// supports only a center that has a difference of only one component (centerX must = X || centerY must == y)
	private void setCircle(int centerX, int centerY, boolean clockwise)
	{
		
		if(clockwise)
		{
			angleDelta = 2;
		} else angleDelta = -2;

		this.currentCenterX = centerX;
		this.currentCenterY = centerY;
		if(centerX==x)
		{
			radius = Math.abs(centerY-y);
			angle = Math.toDegrees(Math.asin((double)((y - centerY)/radius))); // calculate the degree corresponding to our location on the circular path
		}
		else 
			{
			radius = Math.abs(centerX-x);
			angle = Math.toDegrees(Math.acos((double)((x - centerX)/radius)));// calculate the degree corresponding to our location on the circular path
			}
		
		
		
	}

}
