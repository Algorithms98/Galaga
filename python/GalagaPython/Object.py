import pygame

class Object:
  def __init__(self, imagePath, x=0, y=0):
    self.img = pygame.image.load(imagePath)
    self.x = x
    self.y = y
    self.width = self.img.get_rect()[2]
    self.height = self.img.get_rect()[3]

  def draw(self, screen):
    screen.blit(self.img, (self.x, self.y, self.width, self.height))
  
  def getX(self):
    return self.x
  
  def getY(self):
    return self.y

  def getWidth(self):
    return self.width
  
  def getHeight(self):
    return self.height