from Object import Object

class Enemy(Object):
  def __init__(self, img, x=0, y=0):
    super().__init__(img, x, y)
    self.vx = 1
    self.vy = 20
    numMoves = 0
    maxMoves = 150
    self.allowedToShoot = False
  
  def update(self, turnToShoot, enCanShoot, gridMovingRight, playerBullets, game):
    for bullet in playerBullets:
      if bullet.inside(self):
        return bullet
    
    self.move()

    if turnToShoot and enCanShoot and allowedToShoot:
      game.enemyShoot(self.x + 10, self.y)
    
    return None
  
  def move(self):
    self.x += self.vx

    if self.numMoves > self.maxMoves:
      self.vx = -self.vx
      self.y += self.vy
      self.numMoves = 0
      self.maxMoves = 300
    
    self.numMoves += 1
  
  def draw(self, screen):
    screen.blit(self.img, (self.x - 5, self.y, self.width, self.height))