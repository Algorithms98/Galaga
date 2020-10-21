import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.Random;
public class FlyingEnemy extends Enemy{
	
	private Random random = new Random();
	private int actionState = 0;
	private int radius = 0;
	private int linearStepsTaken=0;
	private int gridDestinationX = 100 + random.nextInt(800);
	private int gridDestinationY = 100 + random.nextInt(300);
	private int currentCenterY;
	private int currentCenterX;
	private double angle = 270;
	private double angleDelta;
	private double initialXLinear;
	private double initialYLinear;
	private double linearSteps;
	private double linearDeltaX;
	private double linearDeltaY;
	private float drawAngle = 270;	
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

		
		
		switch(actionState) {
		
		// moving onto the screen from off screen spawn point
		case 0: 
				x+=10;					
				if(x>=0 && x <=1000)
				{
					actionState ++;
					setCircle(x,y-500,false);
					angleDelta=-2;
				}
			break;
			
		// initial flying up screen along the path of a circle for 90 degrees
		case 1:                
				moveAroundSetCircle();
				if(angle==0)
				{
					actionState++;
					x++;
					y-=2;
	
					setCircle(x-120, y, false);
				}
			break;
			
		// fly in a circle at some y before heading toward position on grid
		case 2: 			
				angleDelta=-8;
				moveAroundSetCircle();
				if(angle==0)
				{
					actionState++;
					setLinearTarget(gridDestinationX, gridDestinationY,60);
				}
			break;
			
		// flying toward position on grid
		case 3: 					
				if(moveTowardLinearTarget())
					;
				else 
					{
						actionState++;
						drawAngle=(int) (drawAngle%360); // covert from float angle of linear movement to ordinary discrete integer from 0-360
					}
			break;
			
		// situate to correct draw angle
		case 4:					
			
				//TODO make the speed of angle change adjustable through steps
				// improve fluidity
				if(drawAngle>=180)
					drawAngle++;
				else 
					drawAngle--;
				
				if(drawAngle==0 || drawAngle==360)
					actionState++;
			break;
			
		// waiting for flydown
		case 5: 			
			break;
			
		// general fly down screen
		case 6:				
			moveAroundSetCircle();
			if(angle ==90)
				setCircle(x,y+35,false);
			if(y>900) 
				{
					actionState++;
				}
			break;
			
		// near the player Y
		case 7: 			
			
			y++;
			if(y>1030)
			{
				y= (-30);
				
			}
			if(y<100 && 0 <y)
			{
				actionState =1;
				setCircle(x,y+50,true);
			}
			break;
		}
	}

	public void draw( Graphics page )
	{
		//used to rotate the rendering of the image, does not effect coordinates as it rotates in place from center
		
		Graphics2D g2d=(Graphics2D)page;       // Create a Java2D version of g.
		AffineTransform a = AffineTransform.getRotateInstance(Math.toRadians(drawAngle), x+image.getHeight(null)/2, y + image.getWidth(null)/2);
		
		
	    //Set our Graphics2D object to the transform
	    g2d.setTransform(a);
	    //Draw our image like normal
	    g2d.drawImage(image, x, y, null);
	    
	    a = AffineTransform.getRotateInstance(0, 0, 0);
	    g2d.setTransform(a);
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
			drawAngle += angleDelta;
	}
	// supports only a center that has a difference of only one component (centerX must = X || centerY must == y)
	private void setCircle(int centerX, int centerY, boolean clockwise)
	{
		
		if(clockwise)
		{
			angleDelta = 5;
		} else angleDelta = -5;

		this.currentCenterX = centerX;
		this.currentCenterY = centerY;
		if(centerX==x)
		{
			radius = Math.abs(centerY-y);
			angle = Math.toDegrees(Math.asin((double)((y - centerY)/radius))); // calculate the degree corresponding to our location on the circular path
			if(centerY > y)
			{
				if(clockwise)
				drawAngle = 270;
				else
					drawAngle = 90;
			}
			else 
			{
				if(clockwise)
					drawAngle = 90;
					else
						drawAngle = 270;
			}
		}
		else 
			{
				radius = Math.abs(centerX-x);
				angle = Math.toDegrees(Math.acos((double)((x - centerX)/radius)));// calculate the degree corresponding to our location on the circular path
			}
		
		
		
	}
	
	// sets the target to move in a straight line toward, steps are the number of frames it will take to complete, less steps = faster movement;
	private void setLinearTarget(int xTarget, int yTarget, double steps)
	{
		 linearSteps = steps;
		 double xDistanceToTravel = xTarget-x;
		 double yDistanceToTravel = yTarget-y;
		 linearDeltaX = xDistanceToTravel / steps;
		 linearDeltaY = yDistanceToTravel / steps;
		 initialXLinear = x;
		 initialYLinear = y; 
		 
		 //angles the sprite in accordance to the line of the slope, 270 is added because that is the angle the pixel faces directly east(where atan2 begins its calculation)
		 drawAngle = 270 + (float) Math.toDegrees(Math.atan2(yDistanceToTravel,xDistanceToTravel));
		 
		
	}
	//moves toward current target location in a straight line
	private boolean moveTowardLinearTarget()
	{
		
			
			x= (int)(initialXLinear + linearDeltaX*linearStepsTaken);
			y= (int)(initialYLinear + linearDeltaY*linearStepsTaken);
			linearStepsTaken++;
			
			if(linearStepsTaken == linearSteps+1)
			{
				
				linearStepsTaken =0;
				return false;
			}
			else
			return true;
	}

}
