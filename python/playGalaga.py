import torch
import pyautogui
import numpy as np
from PIL import Image
from CNN import CNN

import keyboard

import time

IMAGE_BOUNDS = (0, 46, 876, 900-46)


def getScreenshot():
  im = pyautogui.screenshot(region=IMAGE_BOUNDS)
  im_arr = np.array(im)
  im_arr = im_arr[:,:,:3]
  blackBottom = np.zeros((22, 876, 3), dtype=np.uint8)
  im_arr = np.concatenate((im_arr, blackBottom), axis=0).reshape(3, 876, 876)
  return im_arr


if __name__ == "__main__":
  print("5 seconds until neural network starts!")
  print("Press Q to stop it!")
  print("Or move mouse to a corner of the screen!")
  time.sleep(5)
  print("Running!")
  device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
  net = CNN()
  net.load_state_dict(torch.load("CNN.pt", map_location=device))
  pyautogui.keyDown("enter")
  time.sleep(0.01)
  pyautogui.keyUp("enter")
  while True:
    startTime = time.time()
    if keyboard.is_pressed('q'):
      print("Quitting...")
      break

    screen = getScreenshot()
    output = torch.from_numpy(np.array([screen], dtype=np.float32))
    move = net(output).argmax().item()

    if move % 3 == 0:
      pyautogui.keyUp("left")
      pyautogui.keyUp("right")
    if move % 3 == 1:
      pyautogui.keyDown("left")
      pyautogui.keyUp("right")
    if move % 3 == 2:
      pyautogui.keyUp("left")
      pyautogui.keyDown("right")

    if move >= 3:
      pyautogui.keyDown("space")
      time.sleep(0.01)
      pyautogui.keyUp("space")

    pyautogui.keyDown("enter")
    time.sleep(0.25)
    pyautogui.keyUp("enter")

    print(f"Time (seconds): {time.time() - startTime}")