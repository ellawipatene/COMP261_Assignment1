# COMP261 Tutorial 1

This code is for the discussion of data structures and starting to play with moving things around.

## Running in the lab
For those of you who are in the lab you should be able to get this code to compile and run by:
1. Go to or create a folder you want to work in perhaps something like `comp261/tutorials/`
1. type `git clone https://gitlab.ecs.vuw.ac.nz/lms/comp261/2022/tutorial-1.git`  This will copy the repository into a folder tutorial-1
1. type `cd tutorial-1`
1. type `code .` This will open visual studio code in the tutorial 1 directory.  VSCode needs to open the folder at the level where you can see the .vscode folder
1. In VSCode either press `F5` or click on the play button with the bug on the left menu, and then the green play button above the inspection view that opens up.

## Outside the lab

1. If you are on Windows and do not have Git then [Install Git for windows](https://git-scm.com/download/win) 
1. [Install Java 17.02](https://jdk.java.net/17/)  
1. [Install JavaFX](https://openjfx.io/) this is built into Intellij, [Bluej](https://www.bluej.org/faq.html#faq_How_do_I_use_JavaFX_in_BlueJ_), and Eclipse via plugin e(fx)clipse.  (If you install into `"c:/Program Files/Java/javafx-sdk-17.0.2\` you will have to make fewer adjustments below) 
1. In VSCode you need to install the extension for JavaFX Support via the box icon on the left bar with the small box coming out.
1. Get the code via git
   * In VSCode you can do this in a new Window, selecting "Clone Git Repository" then entering the text from the clone menu under https.  ie for this repo `https://gitlab.ecs.vuw.ac.nz/lms/comp261/2022/tutorial-1.git`. Select the location to copy files to.
   * You can also use git in the Command Prompt tool. Open a Command Propmt (terminal) and change directory to where you want the code to be (ie something like `cd comp261\tutorial`) then 
      1. type `git clone -b windows https://gitlab.ecs.vuw.ac.nz/lms/comp261/2022/tutorial-1.git` (the -b downloads the widows branch of the repository which has the windows settings). 
      1. type `cd tutortial-1` 
      1. then `code .` which will start vscode in the current folder.

1. Run the code, from the Run menu.  The button in the top left with the arrow will sometime try and run the **file** you are in rather than the project.  So run from the Run menu, Debug menu, or using `F5`

## Tutorial activities

As part of the tutorial we would like you to
1. Start editing the code to change the colours of the nodes and the edges.
1. Use the weight of the edges in the file to change the thickness of the edges.
1. Add some text on the graph using gc.strokeText() for example `gc.strokeText("Origin", model2ScreenX(0), model2ScreenY(0));`
1. Change the highlighting so that when a node is clicked it changes the colour of the edges going OUT from that node.
1. Extention: Design and implement a way to show the direction of the edges. ( you could add a circle or text to the end of the edge )
1. Extention: Edit the data structures and play with the code to try something interesting.


This code has both an Edge Adjacency List and an [Adjacency Matrix line 11 Graph.java](/src/comp261/tut1/Graph.java#L11) `[Adjacency Matrix line 11 Graph.java](/src/comp261/tut1/Graph.java#L11)`


## Nodes on updates
* version 0.2.1  
    * Changed all file paths to relative and added null checks on intial load
    * Edited the default language to English
    * Changed to linux paths for main and create a windows branch for outside the lab
