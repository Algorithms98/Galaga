class Rectangle:
  def __init__(self, x, y, w, h):
    self.x = x
    self.y = y
    self.width = w
    self.height = h
  
  def intersects(self, rect2):
    leftRect = self if self.x < rect2.x else rect2
    rightRect = self if self.x >= rect2.x else rect2

    xIntersect = leftRect.x + leftRect.width > rightRect.x

    topRect = self if self.x < rect2.y else rect2
    bottomRect = self if self.x >= rect2.y else rect2

    yIntersect = topRect.y + topRect.height > bottomRect.y

    return xIntersect and yIntersect