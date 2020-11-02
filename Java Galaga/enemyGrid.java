import java.util.Random;

public class enemyGrid {
	
private double[][] enemiesX;
private double[][] enemiesY;
private int leftBound;
private int rightBound;
private int distanceBetweenPoints;

// frames it will take to complete one down breathing motion or one up breathing motion
private double breatheMaxSteps = 180;
private int breatheCurrentStep = 1;

// how far the grid will "breathe"
private int breatheSpread = 22;

//how far the grid will breathe in they direction compared to the x values
private double ySpreadRatio = 1.28;

private boolean movingRight = true;
private boolean shouldStartBreathing = false;
private boolean breatheForRestOfRound = false;
private boolean breathingDown = true;
private Random random;

private double breatheDelta;

public enemyGrid(int distanceBetweenPoints, int leftBound, int rightBound)
{
	random = new Random();
	this.leftBound = leftBound;
	this.rightBound = rightBound;
	this.distanceBetweenPoints = distanceBetweenPoints;
	
	enemiesX = new double[4][10];
	enemiesY = new double[4][10];
	
	int seed = random.nextInt(500);
	 for (int row = 0; row < enemiesX.length; row++) {
		    for (int col = 0; col < enemiesX[row].length; col++) {
		    	enemiesX[row][col] = seed+100+(distanceBetweenPoints*col);
		    }
		 }
	 
	 for (int row = 0; row < enemiesY.length; row++) {
		    for (int col = 0; col < enemiesY[row].length; col++) {
		    	enemiesY[row][col] = 50+(100*row);
		    }
		 }
}


public void update()
{
	
	// when we actually start breathing, which is when all enemies are on the grid and the grid's middle is in the middle of the screen
	if(shouldStartBreathing && enemiesX[0][5]==((leftBound+rightBound)/2)+25)
		breatheForRestOfRound = true;
	
	// breathing
	if(breatheForRestOfRound)
	{
			if(breathingDown)
			{
				
				 for (int row = 0; row < enemiesX.length; row++) {
					    for (int col = 0; col < enemiesX[row].length; col++) {
					    	if(col>4)
					    	{
					    		breatheDelta = (breatheSpread * Math.abs(col-5)) / breatheMaxSteps;
					    		enemiesX[row][col] += breatheDelta ;
					    	}
					    	else
					    	{
					    		breatheDelta = (breatheSpread * Math.abs(col-5))/ breatheMaxSteps;
					    		enemiesX[row][col] -= breatheDelta ;
					    	}
					    }
					 }
			 for (int row = 0; row < enemiesY.length; row++) {
				    for (int col = 0; col < enemiesY[row].length; col++) {
				    	breatheDelta =(breatheSpread/ySpreadRatio*(row +1))/breatheMaxSteps;
				    	enemiesY[row][col] += breatheDelta;
				    }
				 }
			}
			else
			{
				for (int row = 0; row < enemiesX.length; row++) {
				    for (int col = 0; col < enemiesX[row].length; col++) {
					    	if(col>4)
					    	{
					    		breatheDelta = (breatheSpread * Math.abs(col-5)) / breatheMaxSteps;
					    		enemiesX[row][col] -= breatheDelta;
					    	}
					    	else
					    	{
					    		breatheDelta = (breatheSpread * Math.abs(col-5))/ breatheMaxSteps;
					    		enemiesX[row][col] += breatheDelta;
					    	}
				    	}
				    }
				
				for (int row = 0; row < enemiesY.length; row++) {
					for (int col = 0; col < enemiesY[row].length; col++) {
							breatheDelta =(breatheSpread/ySpreadRatio*(row +1))/breatheMaxSteps;
							enemiesY[row][col]-= breatheDelta; 
						} 
					}
				
			}
			
			if(breathingDown)
			breatheCurrentStep++;
			else breatheCurrentStep--;
			
			if(breatheCurrentStep ==0)
			{
				breathingDown = true;
			}
			if(breatheCurrentStep == breatheMaxSteps)
				breathingDown = false;

		
		
	}
	else 
	{
		for (int row = 0; row < enemiesX.length; row++) {
		    for (int col = 0; col < enemiesX[row].length; col++) {
		    	
		    	if(movingRight)
		    	{
		    		enemiesX[row][col] +=1;
		    	}
		    	else
		    		enemiesX[row][col] -=1;
		    	
		    	
		    }
		 }
		if(enemiesX[0][0]<=leftBound)
		    movingRight = true;
		
		if(enemiesX[0][9]>= rightBound)
			movingRight = false;
	}
}

public int getXCord(int row, int col)
{
	return (int) enemiesX[row][col];
}
public int getYCord(int row, int col)
{
	return (int) enemiesY[row][col];
}
public void setToBreathe()
{
	shouldStartBreathing = true;
}

// gives the x cord of a grid location in a number of frames in the future
public int calcXCordInFrames(int row, int col, int numOfFrames)
{
	// handles x position prediction when the grid is just moving between the bounds(not breathing)
	if(!breatheForRestOfRound)
	{
		if(movingRight)
		{
			if(enemiesX[0][9]+numOfFrames >= rightBound)
				return (int) (enemiesX[row][col] - (numOfFrames - (rightBound-enemiesX[0][9]))+rightBound-enemiesX[0][9]);
				else
					return (int) (enemiesX[row][col]+numOfFrames);
		}
		else
		{
			if(enemiesX[1][0]-numOfFrames <= leftBound)
			{
				
				return (int) (enemiesX[row][col] + (numOfFrames - (enemiesX[1][0]-leftBound)) -(enemiesX[1][0]-leftBound));
			}
				else
					return (int) (enemiesX[row][col]-numOfFrames);
		}
	}
	
	// handles x position prediction when the grid is breathing
	else
	{
		
		// grid currently breathing down
		if(breathingDown)
		{
			// checks if the grid doesn't switch breathing directions in the amount of frames specified
			if(breatheMaxSteps-breatheCurrentStep>numOfFrames)
			{
				// since were not expecting a shift, use the original formula of breathing to predict where we will be in numOfFrames
				if(col<4)
				return (int) (enemiesX[row][col] - (numOfFrames)*(breatheSpread * Math.abs(col-5)/breatheMaxSteps));
				else 
					return (int) (enemiesX[row][col] + (numOfFrames)*(breatheSpread * Math.abs(col-5)/breatheMaxSteps));
			}
			// grid will shift directions during the amount of frames specified
			// handled by going in the direction expected for(breatheMaxSteps-breatheCurrentStep)(how many steps left we have till the grid shifts breathing directions)
			// then going in the opposite direction by (numOfFrames-(breatheMaxSteps-breatheCurrentStep)) which is how many frames we have left after doing the first breathing direction
			else
			{
				if(col<4)
				return (int) (enemiesX[row][col] +(-(breatheMaxSteps-breatheCurrentStep)*(breatheSpread * Math.abs(col-5)/breatheMaxSteps) 
						+(numOfFrames-(breatheMaxSteps-breatheCurrentStep))*(breatheSpread * Math.abs(col-5)/breatheMaxSteps)));
				else 
					return
							(int) (enemiesX[row][col] +((breatheMaxSteps-breatheCurrentStep)*(breatheSpread * Math.abs(col-5)/breatheMaxSteps) 
									-(numOfFrames-(breatheMaxSteps-breatheCurrentStep))*(breatheSpread * Math.abs(col-5)/breatheMaxSteps)));
			}
		}
		// grid currently breathing up
		else
		{
			// checks if the grid doesn't switch breathing directions in the amount of frames specified
			if(breatheCurrentStep>=numOfFrames)
			{
				// since were not expecting a shift, use the original formula of breathign to predict where we will be in numOfFrames
				if(col<4)
				return (int) (enemiesX[row][col] + (numOfFrames)*(breatheSpread * Math.abs(col-5)/breatheMaxSteps));
				else 
					return (int) (enemiesX[row][col] - (numOfFrames)*(breatheSpread * Math.abs(col-5)/breatheMaxSteps));
			}
			
			// grid will shift directions during the amount of frames specified 
			// handled by going in the direction expected for breatheCurrentStep amount then going in (numOfFrames-breatheCurrentStep) since we expect the numOfFrames to be larger
			else
			{
				if(col<4)
				return (int) (enemiesX[row][col] + ((breatheCurrentStep)*(breatheSpread * Math.abs(col-5)/breatheMaxSteps) 
						-(numOfFrames-breatheCurrentStep)*(breatheSpread * Math.abs(col-5)/breatheMaxSteps)));
				else 	
					return (int) (enemiesX[row][col] + (-(breatheCurrentStep)*(breatheSpread * Math.abs(col-5)/breatheMaxSteps) 
									+(numOfFrames-breatheCurrentStep)* (breatheSpread * Math.abs(col-5)/breatheMaxSteps)));
			}
		}
	}
}
public int calcYCordInFrames(int row, int col, int numOfFrames)
{
	return 0;
}
public void reset()
{
	shouldStartBreathing = false;
	breatheForRestOfRound = false;
	breathingDown = true;
	breatheCurrentStep = 1;
	int seed = random.nextInt(600);
	 for (int row = 0; row < enemiesX.length; row++) {
		    for (int col = 0; col < enemiesX[row].length; col++) {
		    	enemiesX[row][col] = seed+100+(distanceBetweenPoints*col);
		    }
		 }
	 
	 for (int row = 0; row < enemiesY.length; row++) {
		    for (int col = 0; col < enemiesY[row].length; col++) {
		    	enemiesY[row][col] = 50+(100*row);
		    }
		 }
}

public boolean isBreathing()
{
	return breatheForRestOfRound;
}

public boolean isSetToBreathe()
{
	return shouldStartBreathing;
}

}

