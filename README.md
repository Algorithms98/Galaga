# CS4345-GroupProject
## Developers
Andrew Havard, Baohua Yu, Chris Alvear, Prince Ndhlovu, Zach Newton

## Requirements
This project requires that the user has Java, Python, and Pip installed. Pip usually comes installed with Python.  
Java requires the JUnit library to be installed for unit testing.  
To see the unit testing, uncomment all of the files in the `Java Galaga` directory that end with "Test.java".
The following Python packages are also required:
+ `torch`
+ `pillow`
+ `numpy`
+ `pyautogui`
+ `keyboard`

For training your own neural network using the Deep Q Learning algorithm, the package `gym` is also required.

## Download/Installation
Download this repository. Additionally, the neural network weights are too large to fit on GitHub, so they are available in the file [CNN.pt](https://drive.google.com/file/d/1e_fMKJWhlBeZ7dbglviZuxRXJJFHFUWb/view?usp=sharing).
Put this `CNN.pt` file into the `python` directory, in the same directory as the `playGalaga.py` file.
If you have not already, download and install the previously noted Python packages.

## Setup
In order for the neural network to be able to recgonize the game, `playGalaga.py` needs a couple of tweaks.  
Locate line 11: `IMAGE_BOUNDS = (0, 46, 876, 900-46)`  
This line specifies where the neural network should look for the game.  
Find the location of the pixel of the top-left corner on your screen.  
This can be done by taking a screenshot of your computer and locating the pixel in an image editor such as MS Paint or GIMP.  
Put those pixel coordinates into the first two numbers of `IMAGE_BOUNDS`, with the x-coordinate of the pixel corresponding to the virst value and the y-coordinate corresponding to the second.
If the bottom of the game is cut off by the bottom of your screen, edit the last value of `IMAGE_BOUNDS`, so that its value is your screen height minus the y-coordinate of the top-left pixel of the game.  
Otherwise, set this number to 876.

## Playing the Game
To run the Java game of Galaga, locate the `run.bash` and `run.sh` files.  
If you are on a Windows computer, run the `run.bash` script.
Otherwise, run the `run.sh` script.
Navigate the main menu with the arrow keys, and select the mode with the enter key.  
You move the player's ship around with the left and right arrow keys, and fire with the space bar.  

To run the neural network to play the game of Galaga, locate the aforementioned `playGalaga.py` file.  
In the same directory as the `playGalaga.py`, run the command `python playGalaga.py`, with administrative privileges if you want to be be able to stop the neural network by holding down the 'q' key.  
The neural network will automatically start the game when it is ready, so simply select the "AI Game" or "Player vs. AI" options without hitting the "enter" key.
To stop the neural network, either hold down the 'q' key if you ran `playGalaga.py` with administrative privileges, or continually move your mouse between various corners of your screen until the script stops.  
Make sure the Java game screen is positioned where you specified in the `IMAGE_BOUNDS` variable!

## How it Works
This neural network was trained to recognize what sort of moves its trainer made for different situations.  
Given a screenshot of the game, it tries to compare it to moves it has seen in the past to try to make the best move.  

## How it Learns
There are many different ways to train neural networks, but this one was trained as a classifier.  
It was given different scenarios with a specific move that was expected for the neural network to make.  
The neural network would guess which move is supposed to go with the given scenario, and begins to notice patterns in the images.  
For instance, when there are a lot of enemies on the left side of the screen, it recognizes it should go left and start shooting.  
After spending hours looking at and comparing these images, it strengthens the patterns it recognizes until it makes the right decision "enough", though the definition of enough depends on who is training the neural network.  
This same algorithm is being used by self-driving cars to recognize stop signs, traffic lights, and other cars from images!

