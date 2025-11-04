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

```
com.comp2042
|-- controller/
|
|-- logic.bricks/
|    |-- Brick
|    |-- BrickGenerator
|    |-- IBrick
|    |-- JBrick
|    |-- LBrick
|    |-- OBrick
|    |-- RandomBrickGenerator
|    |-- SBrick
|    |-- TBrick
|    |-- RandomBrickGenerator
|    |-- SBrick
|    |-- TBrick
|    |-- ZBrick
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
|
|-- view/
|    |-- GameOverPanel
|    |-- GuiController
|    |-- InputEventListener
|    |-- Main
|    |-- NotificationPanel
|
|-- EventSource
|-- EventType
|-- GameController
|-- MoveEvent

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
- [ ] Directory Refactoring (Controller)
- [ ] Code Refactoring
- [ ] Code Modification (Modification)
- [ ] Code Extension (Should Implement)





---



## Implemented and Working Properly

## Implemented but Not Working Properly

## Features Not Implemented

## New Java Classes

## Modified Java Classes

## Unexpected Problems


