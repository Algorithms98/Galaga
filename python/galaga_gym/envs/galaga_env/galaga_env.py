from DeepLearning.Game import Game

import gym
from gym import spaces
import numpy as np
import sys

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

    return self.game.getObservation(), reward, done, ""