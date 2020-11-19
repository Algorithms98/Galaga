from galaga_gym.envs.Game import Game

import torch
import gym
from gym import spaces
import pygame
import numpy as np
import sys

import time

class GalagaEnv(gym.Env):
  """
  Trains a neural net to play Galaga
  """
  def __init__(self):
    # CONSTANTS

    # GYM SPECIFICS
    self.action_space = spaces.Discrete(6)
    self.observation_space = spaces.Box(np.full((3924), 0), np.full((3924), 1), dtype=np.float32)

    self.game = Game()

    # GALAGA SPECIFICS
    self.screen = None
    self.__framesAlive = 0

    self.reset()
  
  def step(self, action):
    # Check for valid action
    err_msg = f"{action} ({type(action)}) is an invalid action!"
    assert self.action_space.contains(action), err_msg

    # Update
    scoreBefore = self.game.score
    livesBefore = self.game.lives
    self.game.playGame(action)
    deltaScore = self.game.score - scoreBefore
    deltaLives = self.game.lives - livesBefore

    # Calculate reward
    reward = deltaScore*10 + deltaLives*5000 + 1

    # Check if done
    done = self.game.roundNum == 3 or self.game.lives == 0

    return self.getState(), reward, done, ""
  
  def reset(self):
    self.game.initialize()

  def render(self):
    if self.screen == None:
      size = width, height = 876, 876
      self.screen = pygame.display.set_mode(size)

    for event in pygame.event.get():
      pass

    self.game.draw(self.screen)
    pygame.display.flip()

  def getState(self):
    return torch.from_numpy(self.game.getObservation())