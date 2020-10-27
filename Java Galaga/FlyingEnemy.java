import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Random;
public class FlyingEnemy extends Enemy{
	
	private Random random = new Random();
	private int spawnLocation = 0; // which side of the screen was this enemy spawned at, 1 = bottom left, 2 - bottom right, 3 = top left, 4 = top right 
	private int actionState = 0;
	private int radius = 0;
	private int linearStepsTaken=0;
	private int gridDestinationX = 100 + random.nextInt(800);
	private int gridDestinationY = 100 + random.nextInt(300);
	private int currentCenterY;
	private int currentCenterX;
	private int initialXGrid;
	private double angle = 270;
	private double angleDelta;
	private double initialXLinear;
	
	private double initialYLinear;
	private double linearSteps;
	private double linearDeltaX;
	private double linearDeltaY;
	private double currentTurnSteps;
	private double currentTargetAngle;
	private float drawAngle = 270;	
	private Player player;
	private int currentTurnStepsTaken;
	private double currentTurnDeltaAngle;
	private boolean onGrid = false;
	private boolean gridMovingRight = true;
	
	private int gridCol;
	private int gridRow;
	private int futureX;
	private int futureY;
	
	
	
	
	
	public FlyingEnemy(Image img, int xLoc, int yLoc, Player player) {
		super(img, xLoc, yLoc);
		this.player = player;
		
	}
	public FlyingEnemy(String path, int xLoc, int yLoc, Player player) {
		super(path, xLoc, yLoc);
		this.player = player;
	}
	
	public FlyingEnemy(String path, int xLoc, int yLoc,int spawnLocation, Player player, int gridRowNum, int gridColumnNum ) {
		
		super(path, xLoc, yLoc);
		
		this.player = player;
		

		this.spawnLocation = spawnLocation;
		this.gridCol = gridColumnNum;
		this.gridRow = gridRowNum;
		
		
		
	}
	
	public Projectile update(boolean turnToShoot, boolean enCanShoot, boolean breathing,int xGrid,int yGrid, ArrayList<Projectile> playerBullets, Game game, enemyGrid grid)
	{
		for (Projectile bullet: playerBullets)
		{
			if (bullet.isInside(this)) {
				return bullet;
			}
		}
		
		if(actionState==2)
		{
			futureX =grid.calcXCordInFrames(gridRow, gridCol, 35);
			futureY = grid.getYCord(gridRow, gridCol);
		}
		
		move();
		
		gridDestinationX = xGrid;
		gridDestinationY = yGrid;
		
		if (turnToShoot && enCanShoot) {
			game.enemyShoot(x + 10, y);
		}
		return null;
	}
	
	public void move()
	{

		switch(actionState) {
		
		
		// moving onto the screen from off screen spawn point
		case 0: 
			   if(spawnLocation==1)
				x+=10;	
			   else 
				   if(spawnLocation == 2)
			   {
				   x-=10;
				   drawAngle = 90;
			   }
				   else 
					   if(spawnLocation == 3)
					   {
						   y+=10;
						   drawAngle = 0;
					   }
					   else
						   if(spawnLocation == 4)
						   {
							   y+=10;
							   drawAngle = 0;
						   }
			   if(spawnLocation == 1|| spawnLocation ==2)
				if(x>=0 && x <=1000)
				{
					if(x<100)
					setCircle(x,y-150,false,2);
					else 
						setCircle(x,y-250,true,2);
					
					
					actionState ++;
					
				}
			   
			   if(spawnLocation == 3|| spawnLocation ==4)
				   if(y>=-10)
				   {
					   if (spawnLocation == 3)
					   {
						   setLinearTarget(100,500,90);
					   }
					   else
						   if (spawnLocation == 4)
						   {
							   setLinearTarget(900,500,90);
						   }
					   actionState ++;
				   }
			break;
			
		// first coming on screen
		case 1:                
				if(spawnLocation == 1 || spawnLocation == 2)
				moveAroundSetCircle();
				else
					moveTowardLinearTarget();
				
				if(spawnLocation == 1 || spawnLocation == 2)
				{
					if(angle==0||angle ==180)
					{
						actionState++;
						x++;
						y-=2;
						if(spawnLocation ==1)
							setCircle(x-120, y, false,8);
						if(spawnLocation ==2)
							setCircle(x+120, y, true,8);
							
					}
				}
				else
				{
					// Enemies spawned at the top 
					if(moveTowardLinearTarget())
					{
						;
					}
					else 
					{
						   if (spawnLocation == 3)
						   {
							   setCircle(x+150,y,false,6);
						   }
						   else
							   if (spawnLocation == 4)
							   {
								   setCircle(x-150,y,true,6);
							   }
						actionState++;
					}
				}
				
			break;
			
		// fly in a circle at some y before heading toward position on grid
		case 2: 			
				
				moveAroundSetCircle();
				if(angle==0||angle==180)
				{
					actionState++;
					//predict grid location, the grid is moving so we must anticipate where the grid position will be in 
					//x amount of frames it takes to complete the linear movement(grid moves +-1 each frame)
					
					// Create a "local" right and left boundary based on row and column of enemy on grid
					int framesToComplete = 35;
					
					
					setLinearTarget(futureX,futureY,framesToComplete);
				}
				
			break;
			
		// flying toward position on grid
		case 3: 					
				//setLinearTarget(gridDestinationX, gridDestinationY,1);
				if(moveTowardLinearTarget()) 
				{
					;
				}
				
				else 
					{
						
						actionState++;
						setTurnTargetAngle(180,10);
						onGrid = true;
						
						
						
					}
			break;
			
		// situate to correct draw angle
		case 4:			
			
						
			// keeps enemies on the grid while they are idle
		 	x=gridDestinationX;
			y=gridDestinationY;
		
			
				turnToTargetAngle();
				
				if(drawAngle==180)
					
				{
					actionState++;
					
				}
			break;
			
		// waiting for fly down, moves with grid if not all waves spawn,  breathing if all enemies have spawned
		case 5: 		
			
			// keeps enemies on the grid while they are idle
			
				x=gridDestinationX;
				y=gridDestinationY;
			
		
				
				
				
				
			
			
			break;
			
		// general fly down screen
		case 6:				
			moveAroundSetCircle();
			if(angle ==90)
				setCircle(x,y+35,false,2);
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
				setCircle(x,y+50,true,2);
			}
			break;
		}
	}

	public void draw( Graphics page )
	{
		//used to rotate the rendering of the image, does not effect coordinates as it rotates in place from center
		
		Graphics2D g2d =(Graphics2D)page.create();       // Create a Java2D version of g.
		AffineTransform a = AffineTransform.getRotateInstance(Math.toRadians(drawAngle), x+image.getHeight(null)/2, y + image.getWidth(null)/2);
		
		
	    //Set our Graphics2D object to the transform
	    g2d.setTransform(a);
	    //Draw our image like normal
	    g2d.drawImage(image, x, y, null);
	    
	    a = AffineTransform.getRotateInstance(0, 0, 0);
	    g2d.setTransform(a);
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
	private void setCircle(int centerX, int centerY, boolean clockwise,int speed)
	{
		
		if(clockwise)
		{
			angleDelta = speed;
		} else angleDelta = - speed;

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
				
				if(centerX > x)
				{
					if(clockwise)
					drawAngle = 180;
					else
						drawAngle = 0;
				}
				else 
				{
					if(clockwise)
						drawAngle = 0;
						else
							drawAngle = 180;
				}
			}
		
		
		
	}
	
	// sets the target to move in a straight line toward, steps are the number of frames it will take to complete, less steps = faster movement;
	// must be used only once per movement
	private void setLinearTarget(int xTarget, int yTarget, double steps)
	{
		linearStepsTaken =0;
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
	//moves toward current target location in a straight line, return true if it is currently go toward a target and false if otherwise
	private boolean moveTowardLinearTarget()
	{
		
			
			
			
			if(linearStepsTaken == linearSteps+1)
			{
				
				
				return false;
			}
			else
			{
				x= (int)(initialXLinear + linearDeltaX*linearStepsTaken);
				y= (int)(initialYLinear + linearDeltaY*linearStepsTaken);
				linearStepsTaken++;
				return true;
			}
	}
	
	// set the target angle for which turnToTargetAngle will turn to, steps are the number of frames it will take to complete, less steps = faster movement;
	// must be used only once per movement
	private void setTurnTargetAngle(int targetAngle, double steps)
	{
		currentTurnStepsTaken =0;
		currentTurnSteps = steps;
		currentTargetAngle = targetAngle%360;
		currentTurnDeltaAngle = (currentTargetAngle-drawAngle)/currentTurnSteps;
	}
	private void turnToTargetAngle()
	{
		
		if(currentTurnSteps != currentTurnStepsTaken)
		{
			drawAngle += currentTurnDeltaAngle;
			currentTurnStepsTaken++;
		}
		else
		{
			
			drawAngle = Math.abs(drawAngle);
			drawAngle = (float) Math.round(drawAngle);
			drawAngle = drawAngle%360;
			
		}
		
	
	}
	
	public boolean getOnGrid()
	{
		return onGrid;
	}
	
	public int getInitialXGrid()
	{
		return initialXGrid;
	}
	
	public int getGridRow()
	{
		return gridRow;
	}
	public int getGridCol()
	{
		return gridCol;
	}
}
