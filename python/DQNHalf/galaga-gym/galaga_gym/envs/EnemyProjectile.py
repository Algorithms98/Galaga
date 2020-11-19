from galaga_gym.envs.Object import Object
from galaga_gym.envs.Rectangle import Rectangle
import random

class EnemyProjectile(Object):
  def __init__(self, img, xLoc, yLoc, playerX):
    super().__init__(img, xLoc, yLoc)
    self.xDelta = (playerX - self.x)/50
    if self.xDelta > 0:
      self.xDelta += random.randint(0, 3)
    elif self.xDelta < 0:
      self.xDelta -= random.randint(0, 3)

  def update(self, player, game):
    self.move()

    if (self.isInsideP(player)):
      game.hitPlayer()
      return True
    if self.y > 950:
      return True

    return False
  
  def move(self):
    self.y += 10
    self.x += self.xDelta

  def isInsideP(self, p):
    r1 = Rectangle(self.x, self.y+8, 8, 16)
    r2 = Rectangle(p.getX()+4, p.getY()+4, 56, 30)

    return r1.intersects(r2)