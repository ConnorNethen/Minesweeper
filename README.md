# Minesweeper Game

This repository contains the Java source code for a Minesweeper game. The game features a classic 16x16 grid where players uncover cells to locate mines using clues about the number of adjacent mines.

## Features

- GUI built using Java Swing.
- Interactive cells that respond to mouse clicks, including flags for potential mines.
- Dynamic updating of mine counts and game status.
- Comprehensive handling of win/lose scenarios with visual feedback.

## Files

- **MineSweapPart.java**: Main class that includes JFrame setup, game mechanics, and event listeners.
- **MyJButton.java**: Custom JButton class tailored to the Minesweeper grid, storing row and column data.

## How to Play

1. Download or clone the repository.
2. Compile the Java files. Make sure Java is installed on your machine.
3. Run `MineSweapPart.java` to start the game.
4. Left-click on cells to uncover them, ctrl + left-click to flag a cell as a mine, and alt + left-click to remove a flag.
5. Uncover all non-mine cells or correctly flag all mines to win the game.

## Game Mechanics

- The grid contains 25 mines randomly placed.
- Each cell shows a number indicating how many of the eight adjacent cells contain mines.
- Flagging all mines correctly or uncovering all non-mine cells will result in a win.
- Clicking on a mine will trigger a game over.
