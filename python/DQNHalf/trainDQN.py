from DQN import DQN
from ReplayMemory import Transition, ReplayMemory

import gym
import galaga_gym

import torch
import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim

import math
import random
from itertools import count

env = gym.make("GalagaEnv-v0")
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

BATCH_SIZE = 1
GAMMA = 0.999
EPS_START = 0.9
EPS_END = 0.05
EPS_DECAY = 200
TARGET_UPDATE = 10
NUM_EPOCHS = 800

n_actions = env.action_space.n

policy_net = DQN()
target_net = DQN()
target_net.load_state_dict(policy_net.state_dict())
target_net.eval()

optimizer = optim.RMSprop(policy_net.parameters())
memory = ReplayMemory(10000)

steps_done = 0

def select_action(state):
  global steps_done
  sample = random.random()

  eps_threshold = EPS_END + (EPS_START - EPS_END) / math.exp(-1. * steps_done / EPS_DECAY)

  steps_done += 1

  if sample > eps_threshold:
    with torch.no_grad():
      return policy_net(state).max(0)[1].view(1, 1)
  
  else:
    return torch.tensor([[random.randrange(n_actions)]], device=device, dtype=torch.long)

def optimize_model():
  if len(memory) < BATCH_SIZE:
    return
  
  transitions = memory.sample(BATCH_SIZE)

  batch = Transition(*zip(*transitions))

  non_final_mask = torch.tensor(tuple(map(lambda s: s is not None, batch.next_state)), device=device, dtype=torch.bool)
  non_final_next_states = torch.cat([s for s in batch.next_state if s is not None]).reshape(len(batch.next_state), batch.next_state[0].shape[0])

  state_batch = torch.cat(batch.state).reshape(len(batch.state), batch.state[0].shape[0])
  action_batch = torch.cat(batch.action)
  reward_batch = torch.cat(batch.reward)

  state_action_values = policy_net(state_batch).gather(1, action_batch)

  next_state_values = torch.zeros(BATCH_SIZE, device=device)
  next_state_values[non_final_mask] = target_net(non_final_next_states).max(1)[0].detach()

  expected_state_action_values = (next_state_values * GAMMA) + reward_batch

  loss = F.smooth_l1_loss(state_action_values, expected_state_action_values.unsqueeze(1))

  optimizer.zero_grad()
  loss.backward()

  for param in policy_net.parameters():
    param.grad.data.clamp_(-1, 1)
  
  optimizer.step()

try:
  for epoch in range(NUM_EPOCHS):
    env.reset()
    state = env.getState()

    for t in count():
      action = select_action(state)
      _, reward, done, _ = env.step(action.item())
      reward = torch.tensor([reward], device=device)

      last_state = state
      state = env.getState()

      memory.push(last_state, action, state, reward)

      optimize_model()

      if done:
        print(f"Epoch {epoch} finished with score of {env.game.score} on round {env.game.roundNum} with {env.game.lives} lives")
        break
    
    if epoch % TARGET_UPDATE == 0:
      target_net.load_state_dict(policy_net.state_dict())
except KeyboardInterrupt as ex:
  print("Keyboard Interrupt!")

print("Complete!")
torch.save(policy_net.state_dict(), "policy_net.pt")
torch.save(target_net.state_dict(), "target_net.pt")
