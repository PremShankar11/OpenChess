# OpenChess

OpenChess is a Java Swing chess application for playing games, practicing against a bot, and analyzing PGN positions. It includes a playable board, move validation, bot play with multiple difficulties, move-history browsing, and a PGN analysis mode that lets you branch into a practice game from any analyzed position.

# Demo Video

https://drive.google.com/file/d/1p23zUSXfLQ4Nq0XFLt5EkVSnOroB-phc/view?usp=sharing

## Features

- Play a local chess game
- Play against the built-in bot
- Choose to play as White or Black against the bot
- Browse move history with back and forward controls
- Load PGN files for analysis
- Start a bot practice branch from any PGN position
- Return from a practice branch back to the original PGN analysis line
- Flip the board orientation during play or analysis

## Project Structure

- `ChessCodes/src/main`
  Main Java source files
- `ChessCodes/src/res`
  Piece images, icons, and backgrounds
- `out/production/OpenChess-main`
  Compiled output used by the current IntelliJ run setup

## Requirements

- Java JDK 17 or newer
- IntelliJ IDEA recommended, but command-line compilation also works

## Run In IntelliJ

1. Open the project folder `OpenChess-main` in IntelliJ IDEA.
2. Make sure a JDK is configured for the project.
3. Use `main.Main` as the run class if IntelliJ does not create it automatically.
4. Run the application.

The main menu lets you:

- play with a friend
- play with the bot
- load a PGN for analysis

## Run From Command Line

From the project root:

```powershell
javac -d out\production\OpenChess-main ChessCodes\src\main\*.java ChessCodes\src\App.java
java -cp out\production\OpenChess-main main.Main
```

You can also build to the alternate output folder used in the repository:

```powershell
javac -d ChessCodes\out\production\ChessCodes ChessCodes\src\main\*.java ChessCodes\src\App.java
java -cp ChessCodes\out\production\ChessCodes main.Main
```

## How To Use

### Play With Bot

1. Click `Play with Bot`.
2. Choose difficulty.
3. Choose whether you want to play as White or Black.
4. The board will orient to your selected side.
5. The bot moves automatically when it is the bot's turn.

### Analyze A PGN

1. Click `Load Game`.
2. Choose a `.pgn` file.
3. Use `<` and `>` to move backward and forward through the analysis line.
4. Use `Flip` to rotate the board.
5. Click `Play` to start a bot practice branch from the currently viewed position.
6. Choose difficulty and the side you want to play.
7. Use `Back` to return to the original PGN analysis position.
8. Use `Now` to jump to the current live position in a branch.

## Notes

- Some PGN files contain only metadata and a result, without move text. Those files are still valid PGN files, but there may be no moves to analyze.
- Keep the `ChessCodes/src/res` folder intact because it contains the image assets used by the UI.

## Troubleshooting

- If the UI opens but images do not appear, make sure you are running from the project root or through IntelliJ with project resources available.
- If IntelliJ launches an older build, rebuild the project before running again.
- If a PGN does not show move navigation, check whether the file actually contains SAN move text and not only headers.
