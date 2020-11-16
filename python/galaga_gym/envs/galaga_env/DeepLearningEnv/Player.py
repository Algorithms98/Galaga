from Object import Object

class Player(Object):
  def __init__(self, img, xLoc, yLoc):
    super().__init__(img, xLoc, yLoc)
  
  def moveRight(self):
    self.x += 8
    if self.x > 876 -90:
      self.x = 876 - 90
  
  def moveLeft(self):
    self.x -= 8
    if self.x < 0:
      self.x = 0