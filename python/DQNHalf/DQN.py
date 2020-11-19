import torch
import torch.nn as nn
import torch.nn.functional as F

class DQN(nn.Module):
  def __init__(self):
    super().__init__()

    self.fc1 = nn.Linear(18*18*3+18, 64)
    self.fc2 = nn.Linear(64, 6)

    self.softmax = nn.Softmax(dim=0)
  
  def forward(self, x):
    x = F.relu(self.fc1(x))
    x = self.softmax(self.fc2(x))

    return x
