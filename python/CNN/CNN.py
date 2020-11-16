import torch
import torch.nn as nn
import torch.nn.functional as F

class CNN(nn.Module):
  def __init__(self):
    super(CNN, self).__init__()
    self.conv1 = nn.Conv2d(3, 12, 5, 2)
    self.conv2 = nn.Conv2d(12, 48, 5, 2)
    self.conv3 = nn.Conv2d(48, 392, 5, 2)
    self.conv4 = nn.Conv2d(392, 1698, 5, 2)

    self.pool = nn.MaxPool2d(2, 2)

    self.fc1 = nn.Linear(6792, 5200)
    self.fc2 = nn.Linear(5200, 3924)
  
  def forward(self, x):
    x = self.pool(F.relu(self.conv1(x)))
    x = self.pool(F.relu(self.conv2(x)))
    x = self.pool(F.relu(self.conv3(x)))
    x = self.pool(F.relu(self.conv4(x)))

    x = x.view(-1, x.shape[1]*x.shape[2]*x.shape[3])
    x = F.relu(self.fc1(x))
    x = torch.sigmoid(self.fc2(x))

    return x