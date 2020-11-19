import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;

public class ChaserEnemy extends FlyingEnemy
{
	private final int case9YTarget = 495;
	private Random random = new Random();
	
	private int gridDestinationX = 100 + random.nextInt(800);
	private int gridDestinationY = 100 + random.nextInt(300);	
	private int currentTurnStepsTaken;
	private int linearStepsTaken = 0;
	private int flyingDownXTarget;
	private int spawnLocation = 0; // which side of the screen was this enemy spawned at, 1 = bottom left, 2 - bottom right, 3 = top left, 4 = top right 
	private int actionState = 0;
	private int currentCenterY;
	private int currentCenterX;
	private int initialXGrid;
	private int gridCol;
	private int gridRow;
	private int futureX;
	private int futureY;

	private double angle;
	private double radius;
	private double previousX;
	private double previousY;
	private double angleTrack;
	private double angleDelta;
	private double circleHelpX;
	private double circleHelpY;
	private double linearSteps;
	private double linearDeltaX;
	private double linearDeltaY;
	private double initialXLinear;
	private double initialYLinear;
	private double currentTurnSteps;
	private double currentTargetAngle;
	private double currentTurnDeltaAngle;
	private double flyingDownTargetAngle;

	private float drawAngle;	

	private boolean onGrid = false;
	private boolean isBoss = false;
	private boolean bossHit = false;
	
	//in case you need to use player location for any enemy behavior
	private Player player;
	
	public ChaserEnemy(String path, int xLoc, int yLoc, Player player) {
		super(path, xLoc, yLoc, player);
		this.player = player;
	}
	
	@Override
	public void move(enemyGrid grid)
	{

		switch(actionState) {
		
		// moving onto the screen from off screen spawn point
		case 0:
			y+=10;
		    drawAngle = 0;
			actionState ++;
			
			if(y>=-10) {
				setLinearTarget(100,350,90);
				actionState++;
			}
			break;
			
		// first coming on screen
		case 1:                
				moveTowardLinearTarget();
					// Enemies spawned at the top 
					if(moveTowardLinearTarget())
					{
						;
					}
					else 
					{
						setCircle(x+150,y+10,false,6);
						actionState++;
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
			
		// getting off of the grid in a circle path, counterclockwise or clockwise depending on what half of grid this enemy is on
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
					setLinearTarget(player.getHeight(), player.getY(),45);
					setLinearTarget(player.getHeight(), player.getY(),45);
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
			
		// near the player Y, bottom of screen 
		case 11:
				moveAroundSetCircle();
				if(y>763) 
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
			
			
		// transition case to mark when a previously flying enemy has settled back onto the grid
		// keeps the game's count of enemiesInFlight accurate
		case 30: 					
				actionState=3;
			break;
					
		// used if the enemy does a circle at the bottom of the screen
		case 40:
				moveAroundSetCircle();
				
				if(angleDelta>0)
				{
					if((int) angle >= (int) angleTrack-8 &&( int) angle <= (int) angleTrack)
					{
						actionState=12;
					}
				}
				else
				{
					if((int) angle <= (int) angleTrack+8 &&( int) angle >= (int) angleTrack+4)
					{
						actionState=12;
					}
				}
			break;
		}
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
			
			//normalize angle
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
}
