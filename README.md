# COMP2042 Coursework - Tetris Maintenance and Extension
 
---

## GitHub Repository
[https://github.com/Null-pointar/CW2025](https://github.com/Null-pointar/CW2025)

---



## Compilation Instructions
1. Install JavaFX SDK
2. Set Run/Debug Configurations
3. Click Modify options
4. Java - Add VM options
5. Enter
```
--module-path
"PATH_TO_JAVAFX_SDK_LIB"
--add-modules
javafx.controls,javafx.fxml,javafx.media
```
	to VM options
6. select Main class
---

## Directory Structure

**MVC Design Pattern (Model-View-Controller)**
- **Model** : State, Logic of Application (Data Structure, Rule, Computation) 
  - bricks : Model of the bricks and manages brick generation (Data Structure, Computation)
  - Board, SimpleBoard : State of game board & bricks (Moving of bricks, rotation, generate new brick, clear rows, score management)
  - BrickRotator : Rotational logic of brick
  - ClearRow : Computation (linesRemoved, newMatrix, scoreBonus)
  - DownData: State change (ClearRow, ViewData)
  - MatrixOperations : Computation (intersect, copy, merge, checkRemoving)
  - NextShapeInfo : State
  - Score : Manages score -> State
  - ViewData : State of a brick (brickData, xPosition, yPosition, nextBrickData)
- **View** : GUI
  - GameOverPanel : UI component for game over
  - GuiController : Initializes the GameScreen (refreshGameBackGround, refreshBrick, setOnKeyPressed)
  - InputEventListener : Interface to process user input events from View
  - Main : Entry point of the application
  - NotificationPanel : UI component to show score bonus
- **Controller** : Update Model & View (in between)
  - EventSource : To identify where the command came from (USER, THREAD) 
  - EventType : Command type from user (DOWN, LEFT, RIGHT, ROTATE)
  - GameController : Implements InputEventListener, receives events from GuiController, and call methods of Board (onDownEvent, onLeftEvent, onRightEvent, onRotateEvent, createNewGame)
  - InputHandler : Controller for all keyboard input event
  - MoveEvent : Controller-layer event object in MVC architecture that encapsulates What happened (EventType), Who caused it (EventSource)

```
com.comp2042
|-- controller/
|    |-- EventSource
|    |-- EventType
|    |-- GameController
|    |-- InputHandler
|    |-- MoveEvent
|
|-- model/
|    |-- Board
|    |-- BrickRotator
|    |-- ClearRow
|    |-- DownData
|    |-- MatrixOperations
|    |-- NextShapeInfo
|    |-- Score
|    |-- SimpleBoard
|    |-- ViewData
|    |-- bricks/
|       |-- Brick
|       |-- BrickGenerator
|       |-- IBrick
|       |-- JBrick
|       |-- LBrick
|       |-- OBrick
|       |-- RandomBrickGenerator
|       |-- SBrick
|       |-- TBrick
|       |-- ZBrick
|
|-- view/
|    |-- GameOverPanel
|    |-- GuiController
|    |-- InputEventListener
|    |-- Main
|    |-- NotificationPanel
|
|-- GameConfig


```


---

## TODO (Modification)
- [x] Game Over Logic (not high enough) : Solved by changing `GameConfig.BRICK_SPAWN_Y` from 10 to 0
- [x] Display Score : Solved by adding label to the gameLayout.fxml and bind it to `GuiController.bindScore`
- [x] Display Next Brick

---

## TODO (Should Implement)
- [ ] **Setting Screen (adjust volume, change key-binds)**
- [x] **Game Mode: Multi-Level (speed, difficulty)**
- [ ] **High Score**
- [x] **Pause/Resume function**
- [x] **Sound Effect(/BGM)**
- [ ] **Custom Skin/Theme**
- [x] **Hard Drop**
- [ ] **Drop Position Forecast (Ghost Piece)**

** Difficulties **
- Easy : No modification
- Normal : Speed will be increased as the player clears rows
- Hard : Normal + Some bricks are placed before it starts(obstacle)


---

## TimeLine
- [x] Create issues
- [x] Directory Refactoring (Model)
- [x] Directory Refactoring (View)
- [x] Directory Refactoring (Controller)
- [x] Code Refactoring
- [ ] Code Modification (Modification)
- [ ] Code Extension (Should Implement)





---



## Implemented and Working Properly
- Custom Keybindings: Implemented a new, advanced keybinding scheme (CAPS, S, F, J, L, ENTER) for enhanced playability.
- Double-Tap Hard Drop: Implemented a timestamp-based double-space detection in the InputHandler to distinguish between Soft Drop (single space) and Hard Drop (double space).

## Implemented but Not Working Properly


## Features Not Implemented


## New Java Classes
- com.comp2042.controller.InputHandler
  - Purpose : To adhere to the Single Responsibility Principle (SRP). This class extracts all keyboard input handling logic from `GuiController`.
  - Reason : `GuiController`'s responsibility is now only View (rendering, displaying). `InputHandler` own the Controller which detect and interpreting key input and translating them into game commands
- com.comp2042.GameConfig
  - Purpose : To organize and make the codes easy to read by extracting Magic Numbers.
  - Reason : Hard coded values makes the code complicated to read since there's no explanation. To improve readability, maintainability, and makes it easy to adjust game difficulty later.


---
## Modified Java Classes
- com.comp2042.view.GuiController
  - Changes
    1. Removed internal TimeLine (GameLoop)
    2. Removed all keyboard handling logic from `initialize()`
    3. Simplified `newGame()`, `gameOver()`, and `pauseGame()` to pass to `InputEventListener`
    4. Removed the BRICK_SIZE magic number
    5. Added handling of Pause/Resume and Restart button and its assets(icons)
    6. Added `handleHardDrop()` method to update view after hard drop
    7. Added `GridPane nextBrickPanel` and `Rectangle[][] nextBrickRectangle` to display the next piece
    8. Implemented: `displayNextBrick()`
    9. Modified `refreshBrick()` to call `displayNextBrick(brick.getNextBrickData())`
  - Reason : To ensure SRP and Separation of Concern, and to implement new UI features (Next Brick, Level Up Notification)

- com.comp2042.controller.GameController
  - Changes 
    1. This manages TimeLine (GameLoop)
    2. Added new methods `stopGame()` and `resumeGame()`
    3. Implemented `onHardDropEvent()` to handle hard drop (call `board.hardDrop()`, clear rows, and add score)
    4. Implemented `onRotateRightEvent()`, `onLeftMostEvent()`, and `onRightMostEvent()`
    5. Added `currentGameSpeed` to manage level progression
    6. Implemented `checkSpeedUp()` to manage the speed increase logic and restart the `Timeline` at a faster speed
    7. Modified `onDownEvent()` and `onHardDropEvent()` to call `checkSpeedUp()`
  - Reason : 
    - To expand contact between View and Controller. This allows the View class to request stop/resume game. This class is now solely responsible for managing the game's progression, timing, and execute game logic
    - To provide new action requested by `InputHandler` 
    - To implement the "Game Mode: Multi-Level" logic by managing game speed

- com.comp2042.view.InputEventListener (Interface)
  - Changes
    1. Added `stopGame()` and `resumeGame()`
    2. Added `onHardDropEvent()`
    3. Added `onRotateRightEvent()`, `onLeftMostEvent()`, and `onRightMostEvent()`
  - Reason : Same as above (GameController)

- com.comp2042.model.SimpleBoard
  - Changes 
    1. Replaced magic numbers for brick spawn point with `GameConfig.BRICK_SPAWN_X` and `GameConfig.BRICK_SPAWN_Y`
    2. Implemented `hardDrop()` method by repeatedly calling `moveBrickDown()` until collision occur. 
    3. Implemented `rotateRightBrick()` using `brickRotator.getPrevShape()`
    4. Implemented `moveBrickLeftMost()` using while loop to call `moveBrickLeft()`
    5. Implemented `moveBrickRightMost()` using while loop to call `moveBrickRight()`
    - To improve maintainability and easier understanding and to implement logic for hard drop
    - To define new brick movements in the Model

- com.comp2042.model.Board (Interface)
  - Changes 
    1. Added `hardDrop()` method
    2. Added `rotateRightBrick()`, `moveBrickLeftMost()`, and `moveBrickRightMost()`
  - Reason : To implement hard drop and new movements

- com.comp2042.controller.InputHandler
  - Changes
    1. Re-mapped all keyboard inputs to new keybinding (S,F,J,L,etc.)
    2. Added a timestamp(`lastSpacePressTime`) to detect double-tap space for hard drop
    3. Added Double tap detection for detecting either moveDown or hardDrop
  - Reason : To implement the innovative feature design of custom controls, separating it from the default key layout

- com.comp2042.model.BrickRotator
  - Change : Added `getPrevShape()` using decrement the index and handle error of out of bounds
  - Reason : To provide rotation right logic for `rotateRightBrick()`

- com.comp2042.model.Score
  - Changes
    1. Added `totalLinesCleared`
    2. Added: `addLines()` and `getTotalLinesCleared()`
  - Reason : To track the cumulative number of lines cleared, which is required for the "Game Mode: Multi-Level" speed up logic

---
## Unexpected Problems
- Sometimes the bonus score and row cleared sound doesn't come up

