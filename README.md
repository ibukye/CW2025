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

```
com.comp2042
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
|-- Board
|-- BrickRotator
|-- ClearRow
|-- DownData
|-- EventSource
|-- EventType
|-- GameController
|-- GameOverPanel
|--GuiController
|--InputEventListener
|-- Main
|-- MatrixOperations
|-- MoveEvent
|-- NextShapeInfo
|-- NotificationPanel
|-- Score
|-- SimpleBoard
|-- ViewData
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
- [ ] Create issues
- [ ] Directory Refactoring
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


