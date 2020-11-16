from Object import Object
from Rectangle import Rectangle

class Projectile(Object):
  def __init__(self, img, xLoc, yLoc):
    super().__init__(img, xLoc, yLoc)
  
  def update(self):
    self.y -= 20
    return self.y < 0
  
  def isInside(self, en):
    r1 = Rectangle(en.getX()+1, en.getY()+1, en.getWidth()-1, en.getHeight()-1)
    r2 = Rectangle(self.x+1, self.y, 9, 30)
    return r1.intersects(r2)