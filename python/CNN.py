import torch
import torch.nn as nn
import torch.nn.functional as F

class CNN(nn.Module):
  def __init__(self):
    super(CNN, self).__init__()
    self.conv1 = nn.Conv2d(3, 12, 5, 3)
    self.conv2 = nn.Conv2d(12, 48, 5, 3)
    self.conv3 = nn.Conv2d(48, 392, 5, 3)
  
    self.batch1 = nn.BatchNorm2d(12)
    self.batch2 = nn.BatchNorm2d(48)
    self.batch3 = nn.BatchNorm2d(392)

    self.pool = nn.MaxPool2d(2, 2)

    self.drop = nn.Dropout(0.4)

    self.fc1 = nn.Linear(3528, 128)
    self.fc2 = nn.Linear(128, 6)
  
  def forward(self, x):
    x = self.pool(F.relu(self.batch1(self.conv1(x))))
    x = self.pool(F.relu(self.batch2(self.conv2(x))))
    x = self.pool(F.relu(self.batch3(self.conv3(x))))

    x = x.view(-1, x.shape[1]*x.shape[2]*x.shape[3])
    x = self.drop(F.relu(self.fc1(x)))
    x = torch.sigmoid(self.fc2(x))

    return x
