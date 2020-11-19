class Rectangle:
  def __init__(self, x, y, w, h):
    self.x = x
    self.y = y
    self.width = w
    self.height = h
  
  def intersects(self, rect2):
    if self.x >= rect2.x + rect2.width or rect2.x >= self.x + self.width:
      return False
    
    if self.y >= rect2.y + rect2.width or rect2.y >= self.y + self.height:
      return False
    
    return True