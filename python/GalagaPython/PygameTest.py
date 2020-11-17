import sys, pygame
import time
pygame.init()

size = width, height = 320, 240
speed = [2, 2]
black = (0, 0, 0)

screen = pygame.display.set_mode(size)

obj = pygame.image.load("../../Java Galaga/Images/pShip.gif")
objrect = obj.get_rect()

while True:
  for event in pygame.event.get():
    if event.type == pygame.QUIT: sys.exit()
  
  objrect = objrect.move(speed)

  if objrect.left < 0 or objrect.right > width:
    speed[0] = -speed[0]
  if objrect.top < 0 or objrect.bottom > height:
    speed[1] = -speed[1]
  
  screen.fill(black)
  screen.blit(obj, (10, 50, 20, 20))
  pygame.display.flip()

  time.sleep(1/30)