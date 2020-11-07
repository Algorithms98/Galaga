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
	private double radius = 0;
	private int linearStepsTaken=0;
	private int gridDestinationX = 100 + random.nextInt(800);
	private int gridDestinationY = 100 + random.nextInt(300);
	private int currentCenterY;
	private int currentCenterX;
	private int initialXGrid;
	private int gridCol;
	private int gridRow;
	private int futureX;
	private int futureY;
	private int currentTurnStepsTaken;
	private int case9YTarget = 495;
	private int flyingDownXTarget;
	
	private double flyingDownTargetAngle;
	private double angle = 270;
	private double angleTrack;
	private double angleDelta;
	private double initialXLinear;
	private double initialYLinear;
	private double linearSteps;
	private double linearDeltaX;
	private double linearDeltaY;
	private double currentTurnSteps;
	private double currentTargetAngle;
	private double currentTurnDeltaAngle;
	private float drawAngle = 270;	

	
	private double circleHelpX;
	private double circleHelpY;
	double previousX;
	double previousY;
	private boolean onGrid = false;
	private Player player;
	
	
	
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
	
	public Projectile update(boolean turnToShoot, boolean enCanShoot, int xGrid,int yGrid, ArrayList<Projectile> playerBullets, Game game, enemyGrid grid)
	{
		drawAngle=drawAngle%360;
		if(drawAngle <0)
			drawAngle = 360-Math.abs(drawAngle);
		for (Projectile bullet: playerBullets)
		{
			if (bullet.isInside(this)) {
				return bullet;
			}
		}
		
		// used when trying to get on grid
		if(actionState==2||actionState==13)
		{
			if(actionState==2)
			{
				futureX =grid.calcXCordInFrames(gridRow, gridCol, 35);
				futureY = grid.calcYCordInFrames(gridRow, gridCol,35);
			}
			else
			{
				futureX = grid.calcXCordInFrames(gridRow, gridCol, 60);
				futureY = grid.calcYCordInFrames(gridRow, gridCol,60);
			}
		}
		
		move(grid);
		
		gridDestinationX = xGrid;
		gridDestinationY = yGrid;
		
		if(game.gridIsBreathing() && actionState == 30)
			game.minusOneFlying();
		
		if (turnToShoot && enCanShoot) {
			game.enemyShoot(x + 10, y);
		}
		return null;
	}
	
	public void move(enemyGrid grid)
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
				if(x>=0 && x <=876)
				{
					if(x<400)
					setCircle(x,y-350,true,2);
					else 
						setCircle(x,y-350,false,2);
					
					
					actionState ++;
					
				}
			   
			   if(spawnLocation == 3|| spawnLocation ==4)
				   if(y>=-10)
				   {
					   if (spawnLocation == 3)
					   {
						   setLinearTarget(100,350,90);
					   }
					   else
						   if (spawnLocation == 4)
						   {
							   setLinearTarget(750,350,90);
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
	
						if(spawnLocation ==1)
							setCircle(x-85, y, false,8);
						if(spawnLocation == 2)
							setCircle(x+85, y, true,8);
							
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
							   setCircle(x+150,y+10,false,6);
						   }
						   else
							   if (spawnLocation == 4)
							   {
								   setCircle(x-150,y+10,true,6);
							   }
						actionState++;
					}
				}
				
			break;
			
		// fly in a circle at some y before heading toward position on grid
		case 2: 			
				moveAroundSetCircle();
				
				if((drawAngle>177 && drawAngle<184))
				{
					actionState++;
					
					//predict grid location, the grid is moving so we must anticipate where the grid position will be in 
					//x amount of frames it takes to complete the linear movement(grid moves +-1 each frame)
					
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
						setTurnTargetAngle(180,18);
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
			onGrid = true;
				x=gridDestinationX;
				y=gridDestinationY;
				
			break;
			
		// transition from onGrid to flying down, used by game 	
		case 6:
			
			onGrid = false;
			if(gridCol < 5)
				setCircle(x-90,y,false,6);
				else
					setCircle(x+90,y,true,6);
			
			actionState++;
			break;
			
		// getting off of the grid in a circle path, counterclockwise or clockwise depending on what side of screen
		case 7:				
			
			moveAroundSetCircle();
			if((int)drawAngle == 0||(int)drawAngle == 360)
			{
				actionState++;
				if(gridCol<5)
					setCircle(x+80,y,false,6);
					else
						setCircle(x-80,y,true,6);

			}
			break;
			
			
			// setup for next case angle smoothing, should be used before wanting to use case 9
		case 8:
			flyingDownXTarget =0;
			if(gridCol<5)
				flyingDownXTarget = 438 + random.nextInt(200);
			else
				flyingDownXTarget = 100+ random.nextInt(337);
			actionState++;
			break;
			
			//moving along the bottom left quadrant of a circle until its the enemies draw degree is near what the draw degree would be for the up coming linear motion
			// done so angle is handled smoothly
		case 9:
			moveAroundSetCircle();
			
			
			// angle we hope to get off the circle at so the transition from circular movement to linear motion is smooth
			flyingDownTargetAngle = (int)(270 + (float) Math.toDegrees(Math.atan2(case9YTarget-y,flyingDownXTarget-x)));
			flyingDownTargetAngle = flyingDownTargetAngle%360;
			
			if(Math.abs(drawAngle-flyingDownTargetAngle)<4||Math.abs(drawAngle-flyingDownTargetAngle)<4)
			{
				actionState++;
				
				setLinearTarget(flyingDownXTarget,case9YTarget,45);
				
					setLinearTarget(flyingDownXTarget,case9YTarget,45);
			}
			break;
			
			//flying toward x and y fly target in linear motion, 
		case 10:
			if(moveTowardLinearTarget())
				;
			else
			{
				actionState++;
				if(flyingDownXTarget <=437)		
				setCircle(x+100,y+250,true,2);
				else
					setCircle(x-100,y+250,true,2);
			}
			
			break;
		// near the player Y, TODO add a chance for the enemy to do a circular motion near the bottom
		case 11:
			moveAroundSetCircle();
			if(y>760) 
				{
					if(random.nextInt(5)==2) 
					{
						actionState=40;
						angleTrack = angle;
						if(flyingDownXTarget <=437)	
						setCircle(x+75,y-50,true,4);
						else
							setCircle(x-75,y-50,true,4);
							
					}
					else
					actionState++;
				}
			break;
			
		//last movements at bottom of screen
		case 12:
			moveAroundSetCircle();
			circleHelpY+=3.5;
			
		if(y>879) 
		{
			actionState++;
		}
			break;
			
			// reset to back of screen, TODO should choose to either go back to grid or continuing flying down
		case 13: 			
			
			y+=5;
			if(y>1030)
			{
				y= (-30);
				
			}
			if(y<100 && 0 <y)
			{
				actionState =30;
				setLinearTarget(futureX,futureY,60);
				
			}
			break;
			
			
			// transition case to mark when a previously flying enemy has settled back onto the grid
			// keeps the game's count of enemiesInFlight accurate
		case 30: 					
			
					
					actionState=3;
					break;
					
		case 40:
			moveAroundSetCircle();
			
			if(angleDelta>0)
			{
					if( (int) angle >= (int) angleTrack-8 &&( int) angle <= (int) angleTrack)
				{
					actionState=12;
					
				}
			}
			else
			{
				if( (int) angle <= (int) angleTrack+8 &&( int) angle >= (int) angleTrack+4)
				{
					actionState=12;
					
				}
			}
			
			break;
					
				
	
		}
	}

  @Override
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
			
			
			previousX = currentCenterX + Math.cos(Math.toRadians(angle))*radius;
			previousY = currentCenterY + Math.sin(Math.toRadians(angle))*radius;
			 
			angle+= angleDelta;
			drawAngle += angleDelta;
			
			//keeps track of the total movements done while going in a circular movement, needed as x and y are integers, this keeps alot of info intact
			circleHelpX+=(currentCenterX + Math.cos(Math.toRadians(angle))*radius)-previousX;
			circleHelpY+=(currentCenterY + Math.sin(Math.toRadians(angle))*radius)-previousY;
			
			if(angle >360)
			{
				angle = 0+angle%360;
			}

			if(angle <0)
			{
				angle = 360 - Math.abs(angle);
			}
			x=(int)circleHelpX;
			y=(int)circleHelpY;
			
			
			
			
	}
	// supports only a center that has a difference of only one component (centerX must = X || centerY must == y)
	public void setCircle(int centerX, int centerY, boolean clockwise,int speed)
	{
		previousX=0;
		previousY=0;
		circleHelpX=x;
		circleHelpY=y;
		normalizeDrawAngle();
		if(clockwise)
		{
			angleDelta = speed;
		} else angleDelta = - speed;

		this.currentCenterX = centerX;
		this.currentCenterY = centerY;
		radius = Math.sqrt(Math.pow((centerY-y),2)+Math.pow((centerX-x),2));
		
		if(centerX == x)
		{
			if(centerY<y)
			{
				if(drawAngle==270)
				{
					clockwise=false;
					angle = 90;
				}
				else
					if(drawAngle==90)
					{
						clockwise=true;
						angle =90;
					}
					else
					{
						System.out.println("setCircle used incorrently - draw angle is not left or right at bottom of circle");
					}
			}
			else
				if(centerY>y)
				{
					if(drawAngle==270)
					{
						clockwise=true;
						angle = 270;
					}
					else
						if(drawAngle==90)
						{
							clockwise=false;
							angle =270;
						}
						else
						{
							System.out.println("setCircle used incorrently - draw angle is not left or right at top of circle");
						}
				}
				else
				{
					System.out.println("setCircle used incorrently - cannot set center to current position");
				}
		}
		else 
			if(centerY==y)
			{
				if(centerX<x)
				{
					if(drawAngle==180)
					{
						clockwise=false;
						angle = 0;
					}
					else
						if(drawAngle==0||drawAngle==360)
						{
							clockwise=true;
							angle =0;
						}
						else
						{
							System.out.println("setCircle used incorrently - draw angle is not up or down at the right of circle");
							
						}
				}
				else
					if(centerX>x)
					{
						if(drawAngle==180)
						{
							clockwise=true;
							angle = 180;
						}
						else
							if(drawAngle==0||drawAngle==360)
							{
								clockwise=false;
								angle =180;
							}
							else
							{
								System.out.println("setCircle used incorrently - draw angle is not up or down at the left of circle");
							}
					}
					else
					{
						System.out.println("setCircle used incorrently - cannot set center to current position");
					}
			}
		
				else
					// cases in which neither X or Y are equal to their centers
					// bottom right and top right
					if(centerX < x)
					{
						// initial position of object is bottom right from center of circle
						if(centerY < y)
						{
							if(drawAngle >=0 && drawAngle<=90)
							{
								angle = drawAngle; // calculate the degree corresponding to our location on the circular path
								clockwise = true;
							}
							else
								if(drawAngle<=270 && drawAngle>=180)
							{
								angle = drawAngle-180;
								clockwise = false;
							}
								else
								{
									System.out.println("incompatible starting draw angle for starting location on circle - bottom right, DrawAngle: " +drawAngle);
								}
						}
						// initial position of object is top right from center of circle
						else
						{
							if(drawAngle >=270 && drawAngle<=360)
							{
								angle = drawAngle; // calculate the degree corresponding to our location on the circular path
								clockwise = true;
							}
							else
								if(drawAngle<=180 && drawAngle>=90)
							{
								angle = drawAngle+180;
								clockwise = false;
							}
								else
								{
									System.out.println("incompatible starting draw angle for starting location on circle - top right, DrawAngle: " +drawAngle);
								}
						}
						
						
					
					}
		
					// bottom left and top left
					else 
						if(centerY < y)
						{
						
								if(drawAngle >=90 && drawAngle<=180)
								{
									angle = drawAngle; // calculate the degree corresponding to our location on the circular path
									clockwise = true;
								}
								else
									if(drawAngle<=360 && drawAngle>=270)
								{
									angle = drawAngle-180;
									clockwise = false;
								}
									else
									{
										System.out.println("incompatible starting draw angle for starting location on circle - bottom left, DrawAngle: " +drawAngle);
									}
						}
							// initial position of object is top left from center of circle
							else
							{
								if(drawAngle >=180 && drawAngle<=270)
								{
									angle = drawAngle; // calculate the degree corresponding to our location on the circular path
									clockwise = true;
								}
								else
									if(drawAngle<=90 && drawAngle>=0)
								{
									angle = drawAngle+180;
									clockwise = false;
								}
									else
									{
										System.out.println("incompatible starting draw angle for starting location on circle - top left, DrawAngle: " +drawAngle);
									}
							}
		if(clockwise)
		{
			angleDelta = speed;
		} else angleDelta = - speed;
		
		previousX = currentCenterX + Math.cos(Math.toRadians(angle))*radius;
		previousY = currentCenterY + Math.sin(Math.toRadians(angle))*radius;
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
	
	public boolean isOnGrid()
	{
		return onGrid;
	}
	public void setOnGrid(boolean onGrid)
	{
		this.onGrid = onGrid;
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
	public void advanceAction()
	{
		actionState=6;
	}
	public void normalizeDrawAngle()
	{
		drawAngle = drawAngle %360;
		if(drawAngle < 0)
			drawAngle = 360+drawAngle;
	}
}
