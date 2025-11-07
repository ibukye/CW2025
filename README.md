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
- [ ] Game Over Logic (not high enough)
- [ ] Display Score
- [ ] Display Next Brick

---

## TODO (Should Implement)
- [ ] **Setting Screen (adjust volume, change key-binds)**
- [ ] **Game Mode: Multi-Level (speed, difficulty)**
- [ ] **High Score**
- [ ] **Pause/Resume function**
- [ ] **Sound Effect/BGM**
- [ ] **Custom Skin/Theme**
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
  - Reason : To ensure SRP and Separation of Concern. 
- com.comp2042.controller.GameController
  - Changes : Added new methods `stopGame()` and `resumeGame()`
  - Reason : To expand contact between View and Controller. This allows the View class to request stop/resume game.
- com.comp2042.model.SimpleBoard
  - Changes : Replaced magic numbers for brick spawn point with `GameConfig.BRICK_SPAWN_X` and `GameConfig.BRICK_SPAWN_Y`
  - Reason : To improve maintainability and easier understanding

---
## Unexpected Problems


