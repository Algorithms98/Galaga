from galaga_gym.envs.Player import Player
from galaga_gym.envs.EnemyGrid import EnemyGrid
from galaga_gym.envs.EnemyProjectile import EnemyProjectile
from galaga_gym.envs.FlyingEnemy import FlyingEnemy
from galaga_gym.envs.Projectile import Projectile
from galaga_gym.envs.Rectangle import Rectangle

import numpy as np

import random
from random import shuffle
import time

class Game:
  def __init__(self):
    self.enemies = []
    self.enemyBullets = []
    self.playerBullets = []
    self.enemyExplosions = []

    self.score = 0
    self.lives = 5
    self.roundNum = 1
    self.maxWidth = 876
    self.maxHeight = 876
    self.gridLeftBound = 100
    self.gridRightBound = 876 - 100
    self.enemiesInFlight = 0
    self.enemiesInFlightMax = 3

    self.MAX_ENEMY_BULLETS = 3
    self.MAX_PLAYER_BULLETS = 2
    
    self.initialized = False
    self.gameWillEnd = False
    self.respaning = False
    self.roundOver = False

    self.roundOverTime = 0
    self.deathTime = 0
    self.gameOverTime = 0
    self.elapsedRoundTime = 0
    self.elapsedDeathTime = 0
    self.elapsedGameOverTime = 0

    self.player = None
    self.grid = EnemyGrid(60, self.gridLeftBound, self.gridRightBound)

    self.initialize()

  def initialize(self):
    self.player = Player("galaga-gym/galaga_gym/envs/Images/pShip.gif", 150, 760)

    if not self.initialized:
      self.initalized = True
    
    self.over = False
    self.score = 0
    self.roundNum = 1
    self.lives = 5
    self.elapsedGameOverTime = 0
    self.elapsedDeathTime = 0
    self.elapsedRoundTime = 0
    self.gameWillEnd = False
    self.respawning = False

    self.reset()
  
  def playGame(self, playerInput=0):
    if not self.over:
      self.grid.update()
      
      if not self.respawning:
        if playerInput >= 3 and len(self.playerBullets) < self.MAX_PLAYER_BULLETS and not self.over:
          self.playerBullets.append(Projectile("galaga-gym/galaga_gym/envs/Images/rocket.gif", self.player.getX() + self.player.getWidth()/2, self.player.getY()))

        if playerInput % 3 == 1:
          self.player.moveLeft()
        
        if playerInput % 3 == 2:
          self.player.moveRight()

      turnToShoot = 0
      if len(self.enemies) > 0:
        turnToShoot = random.randint(0, len(self.enemies) - 1)
      
      enemiesToRemove = []

      if not self.grid.isBreathing():
        tempCheck = True
        for enemy in self.enemies:
          if not enemy.isOnGrid(): tempCheck = False
        
        if tempCheck: self.grid.setToBreathe()

        allEnemiesOnGrid = True
        for enemy in self.enemies:
          if not enemy.isOnGrid():
            allEnemiesOnGrid = False

        if allEnemiesOnGrid: self.grid.setToBreathe()

      if self.grid.isBreathing() or self.grid.isSetToBreathe():
        if self.enemiesInFlight < self.enemiesInFlightMax and not self.respawning:
          enemiesEligible = []
          for enemy in self.enemies:
            if enemy.isOnGrid(): enemiesEligible.append(self.enemies.index(enemy))
          
          shuffle(enemiesEligible)

          if len(enemiesEligible) < self.enemiesInFlightMax - self.enemiesInFlight:
            for enemyFly in enemiesEligible:
              self.enemiesInFlight += 1

              self.enemies[enemyFly].setOnGrid(False)
              self.enemies[enemyFly].advanceAction()
              self.enemies[enemyFly].setCircle(self.enemies[enemyFly].getX()+150, self.enemies[enemyFly].getY(), False, 6)
          
          else:
            for i in range(self.enemiesInFlightMax - self.enemiesInFlight):
              self.enemiesInFlight += 1
              enemy = self.enemies[enemiesEligible[i]]
              enemy.setOnGrid(False)
              enemy.advanceAction()
              enemy.setCircle(enemy.getX()+150, enemy.getY(), False, 6)

      if self.grid.isSetToBreathe():
        if self.enemiesInFlight < self.enemiesInFlightMax and not self.respawning:
          enemiesEligible = []
          for enemy in self.enemies:
            if enemy.isOnGrid(): enemiesEligible.append(self.enemies.index(enemy))
          
          shuffle(enemiesEligible)

          if len(enemiesEligible) < self.enemiesInFlightMax - self.enemiesInFlight:
            for enemyFly in enemiesEligible:
              self.enemies[enemyFly].advanceAction()
          
          else:
            for i in range(self.enemiesInFlightMax - self.enemiesInLight):
              self.enemies[enemiesEligible[i]].advanceAction()
          
      for enemy in self.enemies:
        collidingBullet = enemy.update(turnToShoot == 0, len(self.enemyBullets) < self.MAX_ENEMY_BULLETS, self.grid.getXCord(enemy.getGridRow(), enemy.getGridCol()),
            self.grid.getYCord(enemy.getGridRow(), enemy.getGridCol()), self.playerBullets, self, self.grid)
        
        if collidingBullet != None:
          if not enemy.isBoss:
            enemiesToRemove.append(enemy)
            self.playerBullets.remove(collidingBullet)
            self.score += 10
          else:
            if not enemy.isBossHit():
              enemy.hitBoss()
              self.playerBullets.remove(collidingBullet)
              self.score += 10
            else:
              enemiesToRemove.append(enemy)
              self.playerBullets.remove(collidingBullet)
              self.score += 10
        
        turnToShoot -= 1

        r1 = Rectangle(enemy.getX()+2, enemy.getY()+2, enemy.getWidth()-2, enemy.getHeight()-2)
        r2 = Rectangle(self.player.getX()+4, self.player.getY()+4, 56, 30)

        if r1.intersects(r2):
          self.hitPlayer()
          enemiesToRemove.append(enemy)

      for enemy in enemiesToRemove:
        if self.grid.isBreathing() and not enemy.isOnGrid():
          self.enemiesInFlight -= 1
        self.enemies.remove(enemy)
      
      enemyBulletsToRemove = []
      for enemyBullet in self.enemyBullets:
        if enemyBullet.update(self.player, self):
          enemyBulletsToRemove.append(enemyBullet)
      
      for enemyBullet in enemyBulletsToRemove:
        self.enemyBullets.remove(enemyBullet)
      
      playerBulletsToRemove = []
      for playerBullet in self.playerBullets:
        if playerBullet.update():
          playerBulletsToRemove.append(playerBullet)
      
      for playerBullet in playerBulletsToRemove:
        self.playerBullets.remove(playerBullet)
      
      if len(self.enemies) == 0 and not self.roundOver and not self.respawning:
        self.roundOver = True

        if self.roundNum == 3:
          # TODO Win
          pass
        if self.roundNum < 3:
          self.grid.reset()

          self.lives += 1
          self.roundNum += 1
        else: self.over = True
      
      if self.respawning and self.elapsedDathTime > 8*1000 and not self.gameWillEnd:
        self.respawning = False
        self.player.setX(437)
      
      if self.roundOver:
        self.reset()
        self.roundOver = False
      
      if self.gameWillEnd:
        self.over = True
      
      if self.respawning:
        self.elapsedDeathTime = time.time() - self.deathTime

      # TODO Paint
  
  def hitPlayer(self):
    self.lives -= 1
  
  def enemyShoot(self, x, y):
    self.enemyBullets.append(EnemyProjectile("galaga-gym/galaga_gym/envs/Images/alienRocket.gif", x, y, self.player.getX()))
  
  def gameOver(self):
    self.gameOverTime = time.time() * 1000
    self.elapsedGameOverTime = 0
    self.gameWillEnd = True
  
  def reset(self):
    self.grid.reset()
    self.enemiesInFlight = 0
    self.enemies.clear()
    self.playerBullets.clear()
    self.enemyBullets.clear()
    self.flyInType1()
    self.over = False

  def minusOneFlying(self):
    self.enemiesInFlight -= 1
  
  def gridIsBreathing(self):
    return self.grid.isSetToBreathe()
  
  def flyInType1(self):
    self.grid.reset()
    self.enemiesInFlight = 0

    for i in range(4):
      self.enemies.append(FlyingEnemy("galaga-gym/galaga_gym/envs/Images/eShip2.gif", -1000-(180*i), 700, 1, self.player,
          0, 3+i, True))
    

    for i in range(2):
      self.enemies.append(FlyingEnemy("galaga-gym/galaga_gym/envs/Images/eShip.gif", -1090-(180*i), 700, 1, self.player,
          1+i, 3, False))

    for i in range(2):
      self.enemies.append(FlyingEnemy("galaga-gym/galaga_gym/envs/Images/eShip.gif", -1270-(180*i), 700, 1, self.player,
          1+i, 6, False))
    

    for i in range(2):
      self.enemies.append(FlyingEnemy("galaga-gym/galaga_gym/envs/Images/eShip.gif", 487, -200-(60*i), 3, self.player,
          1, i+4, False))
    
    for i in range(2):
      self.enemies.append(FlyingEnemy("galaga-gym/galaga_gym/envs/Images/eShip.gif", 487, -320-(60*i), 3, self.player,
          2, i+4, False))
    

    for i in range(2):
      self.enemies.append(FlyingEnemy("galaga-gym/galaga_gym/envs/Images/eShip3.gif", 387, -200-(60*i), 4, self.player,
          3, i+4, False))

    for i in range(2):
      self.enemies.append(FlyingEnemy("galaga-gym/galaga_gym/envs/Images/eShip3.gif", 387, -320-(60*i), 4, self.player,
          4, i+4, False))
    

    for i in range(2):
      self.enemies.append(FlyingEnemy("galaga-gym/galaga_gym/envs/Images/eShip.gif", 1000+2300+(60*i), 700, 2, self.player,
          1, i+7, False))
    
    for i in range(2):
      self.enemies.append(FlyingEnemy("galaga-gym/galaga_gym/envs/Images/eShip.gif", 1000+2420+(60*i), 700, 2, self.player,
          2, i+7, False))
    
    for i in range(2):
      self.enemies.append(FlyingEnemy("galaga-gym/galaga_gym/envs/Images/eShip.gif", 1000+2540+(60*i), 700, 2, self.player,
          1, i+1, False))
    
    for i in range(2):
      self.enemies.append(FlyingEnemy("galaga-gym/galaga_gym/envs/Images/eShip.gif", 1000+2660+(60*i), 700, 2, self.player,
          2, i+1, False))
    

    for i in range(2):
      self.enemies.append(FlyingEnemy("galaga-gym/galaga_gym/envs/Images/eShip3.gif", 387, -3500-200-(60*i), 4, self.player,
          3, i+2, False))
    
    for i in range(2):
      self.enemies.append(FlyingEnemy("galaga-gym/galaga_gym/envs/Images/eShip3.gif", 387, -3500-320-(60*i), 4, self.player,
          4, i+6, False))
    
    for i in range(2):
      self.enemies.append(FlyingEnemy("galaga-gym/galaga_gym/envs/Images/eShip3.gif", 387, -3500-420-(60*i), 4, self.player,
          3, i+6, False))

    for i in range(2):
      self.enemies.append(FlyingEnemy("galaga-gym/galaga_gym/envs/Images/eShip3.gif", 387, -3500-540-(60*i), 4, self.player,
          4, i+2, False))


    for i in range(2):
      self.enemies.append(FlyingEnemy("galaga-gym/galaga_gym/envs/Images/eShip3.gif", 487, -5000-200-(60*i), 3, self.player,
          3, i, False))

    for i in range(2):
      self.enemies.append(FlyingEnemy("galaga-gym/galaga_gym/envs/Images/eShip3.gif", 487, -5000-320-(60*i), 3, self.player,
          4, i+8, False))

    for i in range(2):
      self.enemies.append(FlyingEnemy("galaga-gym/galaga_gym/envs/Images/eShip3.gif", 487, -5000-420-(60*i), 3, self.player,
          3, i+8, False))

    for i in range(2):
      self.enemies.append(FlyingEnemy("galaga-gym/galaga_gym/envs/Images/eShip3.gif", 487, -5000-540-(60*i), 3, self.player,
          4, i, False))
  
  def draw(self, screen):
    screen.fill((0, 0, 0))

    for bullet in self.playerBullets:
      bullet.draw(screen)
    
    for enemyBullet in self.enemyBullets:
      enemyBullet.draw(screen)
    
    self.player.draw(screen)

    for enemy in self.enemies:
      enemy.draw(screen)
  
  def getObservation(self):
    enemyLocations = np.zeros((18, 18), dtype=np.float32)
    enemyBulletLocations = np.zeros((18, 18), dtype=np.float32)
    playerBulletLocations = np.zeros((18, 18), dtype=np.float32)
    playerLocations = np.zeros((18), dtype=np.float32)

    for row in range(18):
      for col in range(18):
        thisRect = Rectangle(row*48, col*48, 48, 48)

        for enemy in self.enemies:
          newRect = Rectangle(enemy.x, enemy.y, enemy.width, enemy.height)
          if newRect.intersects(thisRect):
            enemyLocations[row][col] = 1
            break

        for enemyBullet in self.enemyBullets:
          newRect = Rectangle(enemyBullet.x, enemyBullet.y, enemyBullet.width, enemyBullet.height)
          if newRect.intersects(thisRect):
            enemyBulletLocations[row][col] = 1
            break

        for playerBullet in self.playerBullets:
          newRect = Rectangle(playerBullet.x, playerBullet.y, playerBullet.width, playerBullet.height)
          if newRect.intersects(thisRect):
            playerBulletLocations[row][col] = 1
            break

        if row == 0:
          playerRect = Rectangle(self.player.x, self.player.y, self.player.width, self.player.height)
          if playerRect.intersects(thisRect):
            playerLocations[row] = 1
    
    retVal = np.concatenate((enemyLocations.flatten(), enemyBulletLocations.flatten(), playerBulletLocations.flatten(), playerLocations.flatten()))
    return retVal
