import pygame
import time
import sys

from Game import Game

if __name__ == "__main__":
  size = width, height = 876, 876

  game = Game()
  screen = pygame.display.set_mode(size)

  while True:
    for event in pygame.event.get():
      if event.type == pygame.QUIT: sys.exit()

    keyboardInput = 0
    pressed = pygame.key.get_pressed()
    if pressed[pygame.K_LEFT]:
      keyboardInput = 1
    elif pressed[pygame.K_RIGHT]:
      keyboardInput = 2
    if pressed[pygame.K_SPACE]:
      keyboardInput += 3

    game.playGame(keyboardInput)
    game.draw(screen)
    pygame.display.flip()

    time.sleep(.02)