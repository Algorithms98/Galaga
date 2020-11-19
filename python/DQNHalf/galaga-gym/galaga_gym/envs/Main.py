import pygame
import time
import sys

from Game import Game

if __name__ == "__main__":
  size = width, height = 876, 876

  game = Game()
  screen = pygame.display.set_mode(size)

  spacePressed = False

  while True:
    for event in pygame.event.get():
      if event.type == pygame.QUIT: sys.exit()

    keyboardInput = 0
    pressed = pygame.key.get_pressed()
    if pressed[pygame.K_LEFT]:
      keyboardInput = 1
    elif pressed[pygame.K_RIGHT]:
      keyboardInput = 2
    if pressed[pygame.K_SPACE] and not spacePressed:
      keyboardInput += 3
    
    spacePressed = pressed[pygame.K_SPACE]

    game.playGame(keyboardInput)
    game.draw(screen)
    pygame.display.flip()

    time.sleep(.02)