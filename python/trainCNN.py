import torch
import torch.nn as nn
import torch.optim as optim

from PIL import Image
from random import shuffle
import numpy as np

from CNN import CNN

BATCH_SIZE = 16
LEARNING_RATE = 1e-4

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

inputs = []
true_outputs = open("../trainingData/dataLogged.txt").readlines()
print(f"{len(true_outputs)} inputs found!")

print("Loading images...")
failures = []
for i in pbar(range(len(true_outputs))):
  true_outputs[i] = true_outputs[i].split(",")
  succeeded = True
  try:
    for j in range(len(true_outputs[i])):
      true_outputs[i][j] = int(true_outputs[i][j])
    true_outputs[i] = torch.from_numpy(np.array([np.array(true_outputs[i], dtype=np.float32)]))
  except ValueError as ex:
    succeeded = False
    failures.append(i)
    true_outputs = true_outputs[:i] + true_outputs[i+1:]

  if succeeded:
    imageArr = np.array(Image.open(f"../trainingData/images/IMG_{i}.jpg"))
    imageArr = imageArr.reshape(imageArr.shape[2], imageArr.shape[0], imageArr.shape[1])
    data = np.array([np.array(imageArr)])
    data = data.astype(np.float32)
    inputs.append(torch.from_numpy(data))

print(f"{len(failures)} failures")
print(failures)

print("Assigning training and testing IDs")
ids = list(range(len(inputs)))
shuffle(ids)

trainingIDs = ids[:int(len(inputs)*.9)]
testingIDs = ids[int(len(inputs)*.9):]

print("Creating model...")
model = CNN().to(device)
optimizer = optim.Adam(model.parameters(), lr=LEARNING_RATE, betas=(0.5, 0.999))
print(model)

print("Training!")
bestLoss = 0.0
for epoch in range(200):
  shuffle(trainingIDs)
  shuffle(testingIDs)

  averageLoss = 0
  for imageNum in range(len(trainingIDs)):
    model.zero_grad()

    predictions = model(inputs[trainingIDs[imageNum]])
    criterion = nn.MSELoss()
    loss = criterion(predictions, true_outputs[trainingIDs[imageNum]])

    loss.backward()
    optimizer.step()

    if imageNum == 0: averageLoss = loss.item()
    else: averageLoss = (averageLoss * imageNum + loss.item()) / (imageNum + 1)
  
    if (imageNum + 1) % 50 == 0 or (imageNum + 1) == len(trainingIDs):
      print(f"[{imageNum+1}/{len(trainingIDs)}]: Loss: {round(loss.item(),5)} (AVG: {round(averageLoss,5)})")
  
  testingLoss = 0
  for imageNum in range(len(testingIDs)):
    predictions = model(inputs[testingIDs[imageNum]])
    criterion = nn.MSELoss()
    loss = criterion(predictions, true_outputs[testingIDs[imageNum]])

    if imageNum == 0: testingLoss = loss.item()
    else: testingLoss = (testingLoss * imageNum + loss.item()) / (imageNum + 1)

  if testingLoss < bestLoss: bestLoss = testingLoss
  print(f"Testing... Loss: {round(testingLoss, 5)} (Best: {round(testingLoss, 5)})")

torch.save(model.state_dict(), "cnn.pt")