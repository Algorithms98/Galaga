
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

private boolean movingRight = true;
private boolean shouldStartBreathing = false;
private boolean breatheForRestOfRound = false;
private boolean breathingDown = true;

private double breatheDelta;

public enemyGrid(int distanceBetweenPoints, int leftBound, int rightBound)
{
	
	this.leftBound = leftBound;
	this.rightBound = rightBound;
	this.distanceBetweenPoints = distanceBetweenPoints;
	
	enemiesX = new double[4][10];
	enemiesY = new double[4][10];
	
	 for (int row = 0; row < enemiesX.length; row++) {
		    for (int col = 0; col < enemiesX[row].length; col++) {
		    	enemiesX[row][col] = 200+(distanceBetweenPoints*col);
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
	if(shouldStartBreathing && enemiesX[0][4]==(leftBound+rightBound)/2)
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
				    	breatheDelta =(breatheSpread/1.28*(row +1))/breatheMaxSteps;
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
							breatheDelta =(breatheSpread/1.28*(row +1))/breatheMaxSteps;
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
public int calcXCordInFrames(int row, int col, int numOfFrames)
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

public void reset()
{
	shouldStartBreathing = false;
	breatheForRestOfRound = false;
	breathingDown = true;
	breatheCurrentStep = 1;
	 for (int row = 0; row < enemiesX.length; row++) {
		    for (int col = 0; col < enemiesX[row].length; col++) {
		    	enemiesX[row][col] = 200+(distanceBetweenPoints*col);
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

}

