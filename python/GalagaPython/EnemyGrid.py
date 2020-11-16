import numpy as np
import random

class EnemyGrid:
  def __init__(self, distanceBetweenPoints, leftBound, rightBound):
    self.enemiesX = np.empty((5, 10))
    self.enemiesY = np.empty((5, 10))

    self.leftBound = leftBound
    self.rightBound = rightBound

    self.distanceBetweenPoints = distanceBetweenPoints
    self.breatheCurrentStep = 1

    self.breatheSpread = 35

    self.breatheDelta = 0.0
    
    self.breatheMaxSteps = 180

    self.ySpreadRatio = 1.6

    self.movingRight = True
    self.shouldStartBreathing = False
    self.breatheForRestOfRound = False
    self.breathingDown = True

    self.breatheYDelta = self.breatheSpread / self.ySpreadRatio / self.breatheMaxSteps
  
  def update(self):
    if self.shouldStartBreathing and self.enemiesX[0][5] == int((self.leftBound + self.rightBound)/2) + 25:
      self.breatheForRestOfRound = True
    
    if self.breatheForRestOfRound:
      if self.breathingDown:
        for row in range(self.enemiesX.shape[0]):
          for col in range(self.enemiesX.shape[1]):
            self.breatheDelta = (self.breatheSpread * abs(col - 5)) / self.breatheMaxSteps
            if col > 4:
              self.enemiesX[row][col] += self.breatheDelta
            else:
              self.enemiesX[row][col] -= self.breatheDelta
        
        for row in range(self.enemiesY.shape[0]):
          for col in range(self.enemiesY.shape[1]):
            self.breatheDelta = (self.breatheSpread / self.ySpreadRatio * (row+1))/self.breatheMaxSteps
            self.enemiesY[row][col] += self.breatheDelta
        
      else:
        for row in range(self.enemiesX.shape[0]):
          for col in range(self.enemiesX.shape[1]):
            self.breatheDelta = (self.breatheSpread * abs(col-5)) / self.breatheMaxSteps
            if col > 4: self.enemiesX[row][col] -= self.breatheDelta
            else:       self.enemiesX[row][col] += self.breatheDelta
        
        for row in range(self.enemiesY.shape[0]):
          for col in range(self.enemiesY.shape[1]):
            self.breatheDelta = (self.breatheSpread / self.ySpreadRatio * (row+1))/self.breatheMaxSteps
            self.enemiesY[row][col] -= self.breatheDelta

      self.breatheCurrentStep += 1 if self.breathingDown else -1

      if self.breatheCurrentStep == 0:
        self.breathingDown = True
      elif self.breatheCurrentStep == self.breatheMaxSteps:
        self.breathingDown = False
    
    else:
      for row in range(self.enemiesX.shape[0]):
        for col in range(self.enemiesX.shape[1]):
          if self.movingRight: self.enemiesX[row][col] += 1
          else:                self.enemiesX[row][col] -= 1
      
      if self.enemiesX[0][0] <= self.leftBound:  self.movingRight = True
      if self.enemiesX[0][9] >= self.rightBound: self.movingRight = False
  
  def getXCord(self, row, col):
    return int(self.enemiesX[row][col])
  
  def getYCord(self, row, col):
    return int(self.enemiesY[row][col])
  
  def setToBreathe(self):
    self.shouldStartBreathing = True
  
  def calcXCordInFrames(self, row, col, numOfFrames):
    if not self.breatheForRestOfRound:
      if self.movingRight:
        if self.enemiesX[0][9] + numOfFrames >= self.rightBound:
          return int(self.enemiesX[row][col] - (numOfFrames - (self.rightBound - self.enemiesX[0][9])) + self.rightBound - self.enemiesX[0][9])
        else:
          return int(self.enemiesX[row][col] + numOfFrames)
      
      else:
        if self.enemiesX[1][0] - numOfFrames <= self.leftBound:
          return int(self.enemiesX[row][col] + (numOfFrames - (self.enemiesX[1][0]-self.leftBound)) - (self.enemiesX[1][0] - self.leftBound))
        else:
          return int(self.enemiesX[row][col] - numOfFrames)
    
    else:
      if self.breathingDown:
        if self.breatheMaxSteps - self.breatheCurrentStep > numOfFrames:
          if col < 5: return int(self.enemiesX[row][col] - numOfFrames * (self.breatheSpread * abs(col - 5) / self.breatheMaxSteps))
          else:       return int(self.enemiesX[row][col] + numOfFrames * (self.breatheSpread * abs(col - 5) / self.breatheMaxSteps))
        
        else:
          if col < 5:
            return int(self.enemiesX[row][col] + (-(self.breatheMaxSteps - self.breatheCurrentStep)*(self.breatheSpread * abs(col - 5) / self.breatheMaxSteps) +
                (numOfFrames - (self.breatheMaxSteps - self.breatheCurrentStep)) * (self.breatheSpread * abs(col - 5) / self.breatheMaxSteps)))
          else:
            return int(self.enemiesX[row][col] + ((self.breatheMaxSteps-self.breatheCurrentStep)*(self.breatheSpread * abs(col - 5)/self.breatheMaxSteps) -
                (numOfFrames - (self.breatheMaxSteps - self.breatheCurrentStep)) * (self.breatheSpread * abs(col - 5) / self.breatheMaxSteps)))
      else:
        if self.breatheCurrentStep >= numOfFrames:
          if col < 5:
            return int(self.enemiesX[row][col] + numOfFrames*(self.breatheSpread * abs(col - 5)/self.breatheMaxSteps))
          else:
            return int(self.enemiesX[row][col] - numOfFrames*(self.breatheSpread * abs(col - 5)/self.breatheMaxSteps))
        
        else:
          if col < 4:
            return int(self.enemiesX[row][col] + ((self.breatheCurrentStep) * (self.breatheSpread * abs(col - 5) / self.breatheMaxSteps)
                -(numOfFrames - self.breatheCurrentStep) * (self.breatheSpread * abs(col - 5)/self.breatheMaxSteps)))
          else:
            return int(self.enemiesX[row][col] + (-(self.breatheCurrentStep)*(self.breatheSpread * abs(col - 5) / self.breatheMaxSteps)
                +(numOfFrames - self.breatheCurrentStep) * (self.breatheSpread * abs(col - 5)/self.breatheMaxSteps)))
  
  def calcYCordInFrames(self, row, col, numOfFrames):
    if self.isBreathing():
      if self.breathingDown:
        if self.breatheMaxSteps - self.breatheCurrentStep >= numOfFrames:
          return int(self.enemiesY[row][col] + (numOfFrames * (self.breatheYDelta*(row + 1))))
        else:
          return int(self.enemiesY[row][col] + (((self.breatheMaxSteps - self.breatheCurrentStep) * (self.breatheYDelta*(row+1)) -
              (numOfFrames - (self.breatheMaxSteps - self.breatheCurrentStep)) * (self.breatheYDelta*(row + 1)))))
      else:
        if self.breatheCurrentStep >= numOfFrames:
          return int(self.enemiesY[row][col] - (numOfFrames * (self.breatheYDelta*(row+1))))
        else:
          return int(self.enemiesY[row][col] + (-(self.breatheYDelta*(row+1) * self.breatheCurrentStep) +
              (numOfFrames - self.breatheCurrentStep) * (self.breatheYDelta*(row+1))))
    
    else:
      return self.getYCord(row, col)
  
  def reset(self):
    self.movingRight = bool(random.getrandbits(1))
    self.shouldStartBreathing = False
    self.breatheForRestOfRound = False
    self.breathingDown = True
    self.breatheCurrentStep = 1
    seed = random.randint(0, 400)

    for row in range(self.enemiesX.shape[0]):
      for col in range(self.enemiesX.shape[1]):
        self.enemiesX[row][col] = seed + 100 + (self.distanceBetweenPoints * col)
    
    for row in range(self.enemiesY.shape[0]):
      for col in range(self.enemiesY.shape[1]):
        self.enemiesY[row][col] = 50 + (50 * row)
  
  def isBreathing(self):
    return self.breatheForRestOfRound
  
  def isSetToBreathe(self):
    return self.shouldStartBreathing
  
  def isBreathingDown(self):
    return self.breathingDown