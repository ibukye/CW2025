# COMP2042 Coursework - Tetris Maintenance and Extension
 
---

## GitHub Repository
[https://github.com/ibukye/CW2025](https://github.com/ibukye/CW2025)

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
  - Difficulty: Enum for game difficulty (EASY, NORMAL, HARD)
  - GameSettings: Manages loading/saving of user settings (e.g., keybindings)
  - HighScoreManager: Manages loading/saving of high scores (per difficulty)
  - MatrixOperations : Computation (intersect, copy, merge, checkRemoving)
  - NextShapeInfo : State
  - Score : Manages score -> State
  - ViewData : State of a brick (brickData, xPosition, yPosition, nextBrickData)
- **View** : GUI
  - GameOverPanel : UI component for game over
  - GuiController : Initializes the GameScreen (refreshGameBackGround, refreshBrick, setOnKeyPressed)
  - InputEventListener : Interface to process user input events from View
  - Main : Entry point of the application and Scene Manager
  - MainMenuController: Controller for the main menu screen (menu.fxml)
  - NotificationPanel : UI component to show score bonus
  - SettingController: Controller for the settings screen (settingScreen.fxml)
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
|    |-- Difficulty
|    |-- DownData
|    |-- GameSettings
|    |-- HighScoreManager
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
|    |-- MainMenuController
|    |-- NotificationPanel
|    |-- SettingController
|
|-- GameConfig


```


---

## TODO (Modification)
- [x] Game Over Logic (not high enough) : Solved by changing `GameConfig.BRICK_SPAWN_Y` from 10 to 0
- [x] Display Score : Solved by adding label to the gameLayout.fxml and bind it to `GuiController.bindScore`
- [x] Display Next Brick : Extended to 4 upcoming bricks

---

## TODO (Should Implement)
- [x] **Setting Screen (adjust volume, change key-binds)**: Implemented `settingScreen.fxml` and `SettingController` to manage volume and change keybindings
- [x] **Game Mode: Multi-Level (speed, difficulty)**: Implemented `Difficulty`, `GameController` now increases speed based on `totalLinesCleared` for Normal and Hard, and `SimpleBoard` adds obstacles for Hard
- [x] **High Score**: Implemented `HighScoreManager` to read/write high scores for individual mode
- [x] **Pause/Resume function**: Implemented pauseButton in `GuiController` to handle `stopGame()` and `resumeGame()` in `GameController`
- [x] **Sound Effect**: Implemented `MediaPlayer` to handle sounds for line cleared and level up
- [x] **Hard Drop**: Implemented via custom keybindings and default keybindings
- [x] **Drop Position Forecast (Ghost Piece)**: Implemented by adding `ghostBrickPanel` in `gameLayout.fxml` and show the current piece after dropped
- [x] **Multiple Next Bricks**: Implemented 4 next brick panels 
- [x] **Hold Brick Feature**: Implemented swap logic and hold a brick 
- [x] **Custom Keybinding**: Implemented `InputHandler` managed by `GameSettings`

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
- [x] Code Modification (Modification)
- [x] Code Extension (Should Implement)

---



## Implemented and Working Properly
- Custom Keybindings: Implemented `GameSettings` to save/load key preferences. `InputHandler` now supports customized keybindings
- Difficulty Modes: `GameController` loads speed settings based on `Difficulty`. `SimpleBoard` calls `initializeWithObstacles()` for Hard mode
- Double-Tap Hard Drop: Implemented a timestamp-based double-space detection in the InputHandler to distinguish between Soft Drop (single space) and Hard Drop (double space).
- Ghost Piece (Drop Forecast): A semi-transparent forecast of the landing position is now rendered in the correct color
- Multiple Next Bricks: There are 4 upcoming bricks now
- Hold Feature: Player can press V to hold the current brick and swap it later (once per turn)
- Sound & Volume: Sounds are loaded globally in `Main.java` and shared to different classes. Volume can be adjusted in the Settings screen.
- High Score: `HighScoreManager` now saves/loads high scores for each mode(level)

## Implemented but Not Working Properly


## Features Not Implemented
- BGM (Background Music)
- Custom Skin/Theme

## New Java Classes
- com.comp2042.controller.InputHandler
  - Purpose : To adhere to the Single Responsibility Principle (SRP). This class extracts all keyboard input handling logic from `GuiController`.
  - Reason : `GuiController`'s responsibility is now only View (rendering, displaying). `InputHandler` own the Controller which detect and interpreting key input and translating them into game commands

- com.comp2042.GameConfig
  - Purpose : To organize and make the codes easy to read by extracting Magic Numbers.
  - Reason : Hard coded values makes the code complicated to read since there's no explanation. To improve readability, maintainability, and makes it easy to adjust game difficulty later.

- com.comp2042.model.HighScoreManager
  - Purpose : To manage high score data.
  - Reason : To adhere to SRP by separating the file I/O logic (reading/writing `highscore.txt`) from the `GameController`.
  
- com.comp2042.model.GameSettings
  - Purpose: To manage persistent user settings (e.g., keybindings).
  - Reason: To adhere to SRP by separating settings I/O (settings.txt) from controllers.

- com.comp2042.model.Difficulty
  - Purpose: Enum (EASY, NORMAL, HARD) to represent game modes. 
  - Reason: Provides a type-safe way to pass difficulty settings from MainMenuController to GameController.

- com.comp2042.view.MainMenuController
  - Purpose: Controller for menu.fxml.
  - Reason: Handles navigation from the main menu (Start, Settings, Exit).

- com.comp2042.view.SettingController
  - Purpose: Controller for settingScreen.fxml.
  - Reason: Manages UI for changing volume and keybinding settings.

---
## Modified Java Classes

### Controller

- com.comp2042.controller.GameController
  - Changes
    1. This manages TimeLine (GameLoop)
    2. Added new methods `stopGame()` and `resumeGame()`
    3. Implemented `onHardDropEvent()` to handle hard drop (call `board.hardDrop()`, clear rows, and add score)
    4. Implemented `onRotateRightEvent()`, `onLeftMostEvent()`, and `onRightMostEvent()`
    5. Added `currentGameSpeed` to manage level progression
    6. Implemented `checkSpeedUp()` to manage the speed increase logic and restart the `Timeline` at a faster speed
    7. Modified `onDownEvent()` and `onHardDropEvent()` to call `checkSpeedUp()`
    8. Added `HighScoreManager` field
    9. Modified constructor to initialize `HighScoreManager` and pass the high score to `viewGuiController.updateHighScore()`
    10. Implemented `saveGameScore()` method to save the score on game over
    11. Implemented `initializeSounds()` to load `MediaPlayer` objects (for line clear and speed(level) up) and pass them to the `GuiController`
    12. Implemented `onHoldEvent()` to call `board.swapHoldBrick()` and check for game over
  - Reason :
    - To expand contact between View and Controller. This allows the View class to request stop/resume game. This class is now solely responsible for managing the game's progression, timing, and execute game logic
    - To provide new action requested by `InputHandler`
    - To implement the "Game Mode: Multi-Level" logic by managing game speed
    - To manage game state persistence (High Score) and sound resource loading

- com.comp2042.controller.InputHandler
  - Changes
    1. Re-mapped all keyboard inputs to new keybinding (S,F,J,L,etc.)
    2. Added a timestamp(`lastSpacePressTime`) to detect double-tap space for hard drop
    3. Added Double tap detection for detecting either moveDown or hardDrop
    4. Mapped `KeyCode.V` to call `gameController.onHoldEvent()`
  - Reason : To implement the innovative feature design of custom controls, separating it from the default key layout

### Model

- com.comp2042.model.bricks.RandomBrickGenerator
  - Changes
    1. Re-implemented to use a `upcomingBricks` (size=4)
    2. `getBrick()` polls from the queue and adds new brick
    3. Added `getNextBrickShapes()` for the UI
  - Reason: To support the Multiple Next Bricks feature by managing a queue fo upcoming pieces

- com.comp2042.model.Board (Interface)
  - Changes
    1. Added `hardDrop()` method
    2. Added `rotateRightBrick()`, `moveBrickLeftMost()`, and `moveBrickRightMost()`
    3. Added `swapHoldBrick()` and `getHoldBrickShape()`
  - Reason : To implement hard drop and new movements, and Hold feature

- com.comp2042.model.BrickRotator
  - Change
    1. Added `getPrevShape()` using decrement the index and handle error of out of bounds
    2. Added `getBrick()` method
  - Reason : To provide rotation right logic for `rotateRightBrick()` and allow `SimpleBoard` to retrieve the current holding brick

- com.comp2042.model.Score
  - Changes
    1. Added `totalLinesCleared`
    2. Added `addLines()` and `getTotalLinesCleared()`
    3. Added `getScore()` method to retrieve the final score for saving
  - Reason : To track the cumulative number of lines cleared, which is required for the "Game Mode: Multi-Level" speed up logic

- com.comp2042.model.SimpleBoard
  - Changes
    1. Replaced magic numbers for brick spawn point with `GameConfig.BRICK_SPAWN_X` and `GameConfig.BRICK_SPAWN_Y`
    2. Implemented `hardDrop()` method by repeatedly calling `moveBrickDown()` until collision occur.
    3. Implemented `rotateRightBrick()` using `brickRotator.getPrevShape()`
    4. Implemented `moveBrickLeftMost()` using while loop to call `moveBrickLeft()`
    5. Implemented `moveBrickRightMost()` using while loop to call `moveBrickRight()`
    6. Implemented `calculateGhostY()` to calculate Y coordinate for ghost piece
    7. Modified `getViewData()` to call `calculateGhostY()` and `brickGenerator.getNextBrickShapes()`, passing them to the `ViewData` constructor
    8. Added `holdingBrick` field and `canSwap` flag
    9. Implemented `swapHoldBrick()` and `getHoldBrickShape()`
    10. Updated `createNewBrick()` and `newGame()` to reset `canSwap` and `holdingBrick`
    11. Updated `getViewData()` constructor call to include `holdingShape()`
    - To improve maintainability and easier understanding and to implement logic for hard drop
    - To define new brick movements in the Model
    - To provide model-side logic for Ghost Piece and Hold feature, and Multiple Next Bricks features

- com.comp2042.model.ViewData
  - Changes
    1. Added `ghostYPosition`
    2. Changed `nextBrickData` from `int[][]` to `List<int[][]>` to store multiple bricks
    3. Added `holdBrickData` field
  - Reason : To pass the necessary data for the Ghost Piece and Multiple Next Bricks from Model to View, and to pass the holding brick shape

### View

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
    10. Added `MediaPlayer` and `setupSoundPlayer()` method to receive them from the `GameController`
    11. Implemented `playSound()` method and called it in `moveDown()` and `handleHardDrop()` for line clears
    12. Added `highScoreLabel` and `updateHighScore()` method to display high score
    13. Modified `gameOver()` to call `eventListener.saveGameScore()`
    14. Added @FXML fields for `nextBrickPanel2, 3, 4` and `ghostBrickPanel`
    15. Added Rectangle fields for upcoming panel and ghost panel
    16. Added `holdBrickPanel` and `holdBrickRectangle` for the hold feature
    17. Implemented `displayHoldBrick()` method
    18. Modified `refreshBrick()` to call `displayHoldBrick(brick.getHoldBrickData())` to render the hold piece
  - Reason : To ensure SRP and Separation of Concern, and to implement new UI features (Ghost Piece, Multiple Next Bricks, Sounds, Level Up Notification, High Score display, Hold Piece Display)

- com.comp2042.view.InputEventListener (Interface)
  - Changes
    1. Added `stopGame()` and `resumeGame()`
    2. Added `onHardDropEvent()`
    3. Added `onRotateRightEvent()`, `onLeftMostEvent()`, and `onRightMostEvent()`
    4. Added `saveGameScore()` method
    5. Added `onHoldEvent()` method
  - Reason : Same as above (GameController)

- com.comp2042.view.Main
  - Changes
    1. Refactored from loading `gameLayout.fxml` to load `menu.fxml` on start
    2. This class now acts as a Scene Manager with `showMainMenuScreen()`, `showGameScreen()`, and `showSettingScreen()` methods
    3. Globally loads `MediaPalyer` objects and `GameSettings` to be shared across controllers
  - Reason: To support multi screen and centralize resource management

---

## Unexpected Problems
- Sometimes the bonus score and row cleared sound doesn't come up
  - Predicted Reason: previous notification panel is still remains

