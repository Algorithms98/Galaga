import torch
import torch.nn as nn
import torch.optim as optim

from PIL import Image
from random import shuffle
import numpy as np

from sys import argv

from CNN import CNN

outputFile = open(f"{argv[2]}/{argv[3]}", "w+")
outputFile.write("Opened output file\n")
outputFile.flush()

BATCH_SIZE = 16
LEARNING_RATE = 1e-4
NUM_EPOCHS = 400

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
cpuDevice = torch.device("cpu")

inputs = []
true_outputs = open(f"{argv[2]}/../trainingData/dataLogged.txt").readlines()
outputFile.write(f"{len(true_outputs)} inputs found!\n")
outputFile.write("Loading images...\n")
outputFile.flush()

failures = []
for i in range(len(true_outputs)):
  true_outputs[i] = true_outputs[i].split(",")
  succeeded = True
  oneHot = np.zeros((6), dtype=np.float32)
  oneHot[int(true_outputs[i][0])] = 1
  true_outputs[i] = torch.from_numpy(oneHot)

  if succeeded:
    imageArr = np.array(Image.open(f"{argv[2]}/../trainingData/images/IMG_{i}.jpg"))
    imageArr = imageArr.reshape(imageArr.shape[2], imageArr.shape[0], imageArr.shape[1])
    data = np.array([np.array(imageArr)])
    data = data.astype(np.float32)
    inputs.append(torch.from_numpy(data).to(cpuDevice))

outputFile.write(f"{len(failures)} failures\n")
outputFile.flush()

outputFile.write("Assigning training and testing IDs\n")
outputFile.flush()
ids = list(range(len(inputs)))
shuffle(ids)

trainingIDs = ids[:int(len(inputs)*.9)]
testingIDs = ids[int(len(inputs)*.9):]

outputFile.write("Creating model...\n")
outputFile.flush()
model = CNN().to(device)
optimizer = optim.Adam(model.parameters(), lr=LEARNING_RATE, betas=(0.5, 0.999))
outputFile.write("Done creating model!\n")
outputFile.flush()

outputFile.write("Training!\n")
outputFile.flush()
bestLoss = 100.0
roundsSinceImprove = 0
MAX_ROUNDS_WITHOUT_IMPROVE = 20

for epoch in range(NUM_EPOCHS):
  outputFile.write(f"Epoch {epoch}:\n")
  outputFile.flush()
  shuffle(trainingIDs)
  shuffle(testingIDs)

  averageLoss = 0
  for imageNum in range(len(trainingIDs)):
    model.zero_grad()

    predictions = model(inputs[trainingIDs[imageNum]].to(device))
    criterion = nn.MSELoss()
    loss = criterion(predictions, true_outputs[trainingIDs[imageNum]].to(device))

    loss.backward()
    optimizer.step()

    if imageNum == 0: averageLoss = loss.item()
    else: averageLoss = (averageLoss * imageNum + loss.item()) / (imageNum + 1)
  
    if (imageNum + 1) % 500 == 0 or (imageNum + 1) == len(trainingIDs):
      outputFile.write(f"  [{imageNum+1}/{len(trainingIDs)}]: Loss: {round(loss.item(),7)} (AVG: {round(averageLoss,7)})\n")
      outputFile.flush()
  
  testingLoss = 0.0
  for imageNum in range(len(testingIDs)):
    predictions = model(inputs[testingIDs[imageNum]].to(device))
    criterion = nn.MSELoss()
    loss = criterion(predictions, true_outputs[testingIDs[imageNum]].to(device))

    if imageNum == 0: testingLoss = loss.item()
    else: testingLoss = (testingLoss * imageNum + loss.item()) / (imageNum + 1)

  if testingLoss < bestLoss:
    bestLoss = testingLoss
    roundsSinceImprove = 0
  else: roundsSinceImprove += 1

  outputFile.write(f"  Testing... Loss: {round(testingLoss, 7)} (Best: {round(bestLoss, 7)})\n")
  outputFile.write(f"             {roundsSinceImprove} rounds since improvement...\n\n")
  outputFile.flush()

  if roundsSinceImprove >= MAX_ROUNDS_WITHOUT_IMPROVE:
    break

torch.save(model.state_dict(), f"{argv[2]}/{argv[1]}")
outputFile.write(f"Saved results to {argv[2]}/{argv[1]}!")
outputFile.flush()
