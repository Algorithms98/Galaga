from Enemy import Enemy

import random
from math import sin, cos, atan2, sqrt
import sys

class FlyingEnemy(Enemy):
  def __init__(self, img, xLoc, yLoc, spawnLocation, player, gridRowNum, gridColNum, isBoss):
    super().__init__(img, xLoc, yLoc)
    self.case9YTarget = 495
    self.gridDestinationX = 100 + random.randint(0, 800)
    self.gridDestinationY = 100 + random.randint(0, 300)
    self.currentTurnStepsTaken = 0
    self.linearStepsTaken = 0
    self.flyingDownXTarget = None
    self.spawnLocation = spawnLocation
    self.actionState = 0
    self.currentCenterY = 0
    self.currentCenterX = 0
    self.initialXGrid = 0
    self.gridCol = gridColNum
    self.gridRow = gridRowNum
    self.futureX = 0
    self.futureY = 0

    self.angle = 0
    self.radius = 0
    self.previousX = 0
    self.previousY = 0
    self.angleTrack = 0
    self.angleDelta = 0
    self.circleHelpX = 0
    self.circleHelpY = 0
    self.linearSteps = 0
    self.linearDeltaX = 0
    self.linearDeltaY = 0
    self.initialXLinear = 0
    self.initialYLinear = 0
    self.currentTurnSteps = 0
    self.currentTargetAngle = 0
    self.currentTurnDeltaAngle = 0
    self.flyingDownTargetAngle = 0

    self.drawAngle = 0

    self.onGrid = False
    self.isBoss = isBoss
    self.bossHit = False

    self.player = player
  
  def update(self, turnToShoot, enCanShoot, xGrid, yGrid, playerBullets, game, grid):
    self.normalizeDrawAngle()

    for bullet in playerBullets:
      if bullet.isInside(self):
        return bullet
    
    if self.actionState == 2 or self.actionState == 13:
      if self.actionState == 2:
        self.futureX = grid.calcXCordInFrames(self.gridRow, self.gridCol, 35)
        self.futureY = grid.calcYCordInFrames(self.gridRow, self.gridCol, 35)
      else:
        self.futureX = grid.calcXCordInFrames(self.gridRow, self.gridCol, 60)
        self.futureY = grid.calcYCordInFrames(self.gridRow, self.gridCol, 60)
    
    self.move(grid)

    self.gridDestinationX = xGrid
    self.gridDestinationY = yGrid

    if game.gridIsBreathing() and self.actionState == 30:
      game.minusOneFlying()
    
    self.allowedToShoot = self.y < 500

    if turnToShoot and enCanShoot and self.allowedToShoot:
      game.enemyShoot(self.x + 10, self.y)
    
    return None
  
  def move(self, grid):
    if self.actionState == 0:
      if self.spawnLocation == 1:
        self.x += 10
        self.drawAngle = 270
      elif self.spawnLocation == 2:
        self.x -= 10
        self.drawAngle = 90
      elif self.spawnLocation == 3:
        self.y += 10
        self.drawAngle = 0
      elif self.spawnLocation == 4:
        self.y += 10
        self.drawAngle = 0
      
      if self.spawnLocation == 1 or self.spawnLocation == 2:
        if self.x >= 0 and self.x <= 876:
          if self.x < 400:
            self.setCircle(self.x, self.y-350, True,  2)
          else:
            self.setCircle(self.x, self.y-350, False, 2)

          self.actionState += 1
      
      elif self.spawnLocation == 3 or self.spawnLocation == 4:
        if self.y >= -10:
          if self.spawnLocation == 3:
            self.setLinearTarget(100, 350, 90)
          else:
            self.setLinearTarget(750, 350, 90)
          
          self.actionState += 1
    
    elif self.actionState == 1:
      if self.spawnLocation == 1 or self.spawnLocation == 2: self.moveAroundSetCircle()
      else:                                                  self.moveTowardLinearTarget()

      if self.spawnLocation == 1 or self.spawnLocation == 2:
        if self.angle == 0 or self.angle == 180:
          self.actionState += 1

          if self.spawnLocation == 1: self.setCircle(self.x-85, self.y, False, 8)
          else:                       self.setCircle(self.x+85, self.y, True,  8)
    
      else:
        if self.moveTowardLinearTarget(): pass
        else:
          if self.spawnLocation == 3: self.setCircle(self.x+150, self.y+10, False, 6)
          else:                       self.setCircle(self.x-150, self.y+10, True,  6)
        
          self.actionState += 1
      
    
    elif self.actionState == 2:
      self.moveAroundSetCircle()

      if self.drawAngle > 177 and self.drawAngle < 184:
        self.actionState += 1

        self.setLinearTarget(self.futureX, self.futureY, 35)
      
    
    elif self.actionState == 3:
      if self.moveTowardLinearTarget(): pass
      else:
        self.actionState += 1
        self.setTurnTargetAngle(180, 18)
    
    elif self.actionState == 4:
      self.x = self.gridDestinationX
      self.y = self.gridDestinationY
      self.turnToTargetAngle()

      if self.drawAngle == 180: self.actionState += 1
    
    elif self.actionState == 5:
      self.onGrid = True
      self.x = self.gridDestinationX
      self.y = self.gridDestinationY
  
    elif self.actionState == 6:
      self.onGrid = False
      if self.gridCol < 5: self.setCircle(self.x-90, self.y, False, 6)
      else:                self.setCircle(self.x+90, self.y, True,  6)

      self.actionState += 1
    
    elif self.actionState == 7:
      self.moveAroundSetCircle()

      if int(self.drawAngle) == 0 or int(self.drawAngle) == 360:
        self.actionState += 1
        if self.gridCol < 5: self.setCircle(self.x+80, self.y, False, 6)
        else:                self.setCircle(self.x-80, self.y, True,  6)
      
    elif self.actionState == 8:
      self.flyingDownXTarget = 0
      if self.gridCol < 5: self.flyingDownXTarget = 438 + random.randint(0, 200)
      else:                self.flyingDownXTarget = 100 + random.randint(0, 337)
      
      self.actionState += 1
    
    elif self.actionState == 9:
      self.moveAroundSetCircle()

      self.flyingDownTargetAngle = int(270 + (180 / 3.14159265358979) * atan2(self.case9YTarget-self.y, self.flyingDownXTarget-self.x))
      self.flyingDownTargetAngle = self.flyingDownTargetAngle % 360

      if abs(self.drawAngle-self.flyingDownTargetAngle) < 4 or abs(self.drawAngle-self.flyingDownTargetAngle) < 4:
        self.actionState += 1
        self.setLinearTarget(self.flyingDownXTarget, self.case9YTarget, 45)
        self.setLinearTarget(self.flyingDownXTarget, self.case9YTarget, 45)
    
    elif self.actionState == 10:
      if self.moveTowardLinearTarget():
        pass
      else:
        self.actionState += 1
        if self.flyingDownXTarget <= 437: self.setCircle(self.x+100, self.y+250, True, 2)
        else:                             self.setCircle(self.x-100, self.y+250, True, 2)
    
    elif self.actionState == 11:
      self.moveAroundSetCircle()

      if self.y > 763:
        if random.randint(0, 5) == 2:
          self.actionState = 40
          self.angleTrack = self.angle
          if self.flyingDownXTarget <= 437: self.setCircle(self.x+75, self.y-50, True, 4)
          else:                             self.setCircle(self.x-75, self.y-50, True, 4)

        else:
          self.actionState += 1
    
    elif self.actionState == 12:
      self.moveAroundSetCircle()
      self.circleHelpY += 3.5

      if self.y > 879: self.actionState += 1
    
    elif self.actionState == 13:
      self.y += 5

      if self.y > 1030: self.y = -30
      elif 0 < self.y and self.y < 100:
        self.actionState = 30
        self.setLinearTarget(self.futureX, self.futureY, 60)
    
    elif self.actionState == 30: self.actionState = 3

    elif self.actionState == 40:
      self.moveAroundSetCircle()

      if self.angleDelta > 0:
        if int(self.angle) >= int(self.angleTrack - 8) and int(self.angle) <= int(self.angleTrack):
          self.actionState = 12
      else:
        if int(self.angle) <= int(self.angleTrack + 8) and int(self.angle) >= int(self.angleTrack + 4):
          self.actionState = 12

    if self.x == 0 and self.y == 0: sys.exit()

  def moveAroundSetCircle(self):
    self.previousX = self.currentCenterX + cos((3.14159265358979 / 180)*self.angle)*self.radius
    self.previousY = self.currentCenterY + sin((3.14159265358979 / 180)*self.angle)*self.radius

    self.angle += self.angleDelta
    self.drawAngle += self.angleDelta

    self.circleHelpX += self.currentCenterX + cos((3.14159265358979 / 180)*self.angle)*self.radius - self.previousX
    self.circleHelpY += self.currentCenterY + sin((3.14159265358979 / 180)*self.angle)*self.radius - self.previousY

    if self.angle > 360: self.angle = 0 + self.angle%360
    if self.angle < 0: self.angle = 360 - abs(self.angle)

    self.x = int(self.circleHelpX)
    self.y = int(self.circleHelpY)
  
  def setCircle(self, centerX, centerY, clockwise, speed):
    self.previousX, self.previousY = 0, 0
    self.circleHelpX, self.circleHelpY = self.x, self.y

    self.normalizeDrawAngle()

    if clockwise: self.angleDelta = speed
    else:         self.angleDelta = -speed

    self.currentCenterX = centerX
    self.currentCenterY = centerY

    self.radius = sqrt((centerY - self.y)**2 + (centerX - self.x)**2)

    if centerX == self.x:
      if centerY < self.y:
        if self.drawAngle == 270:
          clockwise = False
          self.angle = 90
        elif self.drawAngle == 90:
          clockwise = True
          self.angle = 90
        else:
          print("setCircle used incorrectly - draw angle is not left or right at bottom of circle")
      elif centerY > self.y:
        if self.drawAngle == 270:
          clockwise = True
          self.angle = 270
        elif self.drawAngle == 90:
          clockwise = False
          self.angle = 270
        else:
          print("setCircle used incorrectly - draw angle is not left or right at top of circle")
      else:
        print("setCircle used incorrectly - cannot set center to current position")
    
    elif centerY == self.y:
      if centerX < self.x:
        if self.drawAngle == 180:
          clockwise = False
          self.angle = 0
        elif self.drawAngle == 0 or self.drawAngle == 360:
          clockwise = True
          self.angle = 0
        else:
          print("setCircle used incorrectly - draw angle is not up or down at the right of circle")
      elif centerX > self.x:
        if self.drawAngle == 180:
          clockwise = True
          self.angle = 180
        elif self.drawAngle == 0 or self.drawAngle == 360:
          clockwise = False
          self.angle = 180
        else:
          print("setCircle used incorrectly - draw angle is not up or down at the left of the circle")
      else:
        print("setCircle used incorrectly - cannot set center to current position")
    
    elif centerX < self.x:
      if centerY < self.y:
        if self.drawAngle >= 0 and self.drawAngle <= 90:
          self.angle = self.drawAngle
          clockwise = True
        elif self.drawAngle <= 270 and self.drawAngle >= 180:
          self.angle = self.drawAngle - 180
          clockwise = False
        else:
          print(f"incompatible starting draw angle for starting location on ircle - bottom right, DrawAngle: {self.drawAngle} ActionState: {self.actionState}")
      else:
        if self.drawAngle >= 270 and self.drawAngle <= 360:
          self.angle = self.drawAngle
          clockwise = True
        elif self.drawAngle <= 180 and self.drawAngle >= 90:
          self.angle = self.drawAngle + 180
          clockwise = False
        else:
          print(f"incompatible starting draw angle for starting location on circle - top right, DrawAngle:  {self.drawAngle}")
    
    elif centerY < self.y:
      if self.drawAngle >= 90 and self.drawAngle <= 180:
        self.angle = self.drawAngle
        clockwise = True
      elif self.drawAngle <= 360 and self.drawAngle >= 270:
        self.angle = self.drawAngle - 180
        clockwise = False
      else:
        print(f"incompatible starting draw angle for starting location on circle - bottom left, DrawAngle: {self.drawAngle}")
    
    else:
      if self.drawAngle >= 180 and self.drawAngle <= 270:
        self.angle = self.drawAngle
        clockwise = True
      elif self.drawAngle <= 90 and self.drawAngle >= 0:
        self.angle = self.drawAngle + 180
        clockwise = False
      else:
        print(f"incompatible starting draw angle for starting location on circle - top left, DrawAngle: {self.drawAngle}")
    
    if clockwise: self.angleDelta = speed
    else:         self.angleDelta = -speed

    self.previousX = self.currentCenterX + cos((3.14159265358979 / 180))*self.radius
    self.previousY = self.currentCenterY + sin((3.14159265358979 / 180))*self.radius
  
  def setLinearTarget(self, xTarget, yTarget, steps):
    self.linearStepsTaken = 0
    self.linearSteps = steps

    xDistanceToTravel = xTarget - self.x
    yDistanceToTravel = yTarget - self.y

    self.linearDeltaX = xDistanceToTravel / steps
    self.linearDeltaY = yDistanceToTravel / steps

    self.initialXLinear = self.x
    self.initialYLinear = self.y

    self.drawAngle = 270 + (180/3.14159265358979)*atan2(yDistanceToTravel,xDistanceToTravel)
  
  def moveTowardLinearTarget(self):
    if self.linearStepsTaken == self.linearSteps+1:
      return False
    else:
      self.x = int(self.initialXLinear + self.linearDeltaX*self.linearStepsTaken)
      self.y = int(self.initialYLinear + self.linearDeltaY*self.linearStepsTaken)
      self.linearStepsTaken += 1
      return True
  
  def setTurnTargetAngle(self, targetAngle, steps):
    self.currentTurnStepsTaken = 0
    self.currentTurnSteps = steps
    self.currentTargetAngle = targetAngle%360
    self.currentTurnDeltaAngle = (self.currentTargetAngle-self.drawAngle)/self.currentTurnSteps
  
  def turnToTargetAngle(self):
    if self.currentTurnSteps != self.currentTurnStepsTaken:
      self.drawAngle += self.currentTurnDeltaAngle
      self.currentTurnStepsTaken += 1
    else:
      self.drawAngle = abs(self.drawAngle)
      self.drawAngle = round(self.drawAngle)
      self.drawAngle = self.drawAngle%360
  
  def isOnGrid(self): return self.onGrid  
  def setOnGrid(self, onGrid): self.onGrid = onGrid
  def getInitialXGrid(self): return self.initialXGrid

  def getGridRow(self): return self.gridRow
  def getGridCol(self): return self.gridCol

  def advanceAction(self): self.actionState = 6

  def normalizeDrawAngle(self):
    self.drawAngle = self.drawAngle % 360
    if self.drawAngle < 0: self.drawAngle = 360 + self.drawAngle
  
  def hitBoss(self): self.bossHit = True

  def isBossHit(self): return self.bossHit