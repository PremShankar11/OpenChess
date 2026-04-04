package main;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;

//import main.ChessGame;

import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
//import java.util.*;
import java.util.ArrayList;
import java.util.List;

//package main;
import javax.swing.*;
//import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.util.ArrayList;
//import java.util.List;

public class GamePanel extends JPanel implements MouseListener , Runnable  {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    final int FPS = 60;
    Thread gameThread;
    Board drawBoard = new Board();
    ChessPiece selectedPiece = null;
    List<int[]> possibleMovesOfSelectedPiece = new ArrayList<>();
    ChessGame game = null;
    ChessGame analysisGame = null;
    private boolean whitePerspective;
    private int[] mouseStart = null;  // Stores the mouseStart position
    private int[] mouseEnd = null;
    private boolean endDialogShown = false;
    private boolean analysisMode = false;
    private boolean branchMode = false;
    private int analysisReturnIndex = 0;
    private final ArrayList<DummyBoard> analysisHistory = new ArrayList<>();
    private final ArrayList<String> analysisPlayersToMove = new ArrayList<>();
    private final ArrayList<String> analysisMoveLabels = new ArrayList<>();
    private JLabel modeLabel;
    private JButton playHereButton;
    private JButton returnToAnalysisButton;

    private void appendPendingSnapshots() {
        ArrayList<DummyBoard> snapshots = game.consumePendingBoardStates();
        if (!snapshots.isEmpty()) {
            drawBoard.history.addAll(snapshots);
            drawBoard.viewCounter = drawBoard.history.size() - 1;
        }
    }

    private void refreshModeLabel() {
        if (modeLabel == null) {
            return;
        }

        if (analysisMode && !branchMode && !analysisHistory.isEmpty()) {
            int index = Math.max(0, Math.min(drawBoard.viewCounter, analysisHistory.size() - 1));
            modeLabel.setText("Analysis " + index + "/" + (analysisHistory.size() - 1) + ": " + analysisMoveLabels.get(index));
        } else if (branchMode) {
            modeLabel.setText("Practice Branch");
        } else {
            modeLabel.setText("Live Game");
        }
    }

    private void syncAnalysisMetadata() {
        analysisHistory.clear();
        analysisPlayersToMove.clear();
        analysisMoveLabels.clear();
        analysisHistory.addAll(analysisGame.getAnalysisSnapshots());
        for (int i = 0; i < analysisGame.getAnalysisMoveCount(); i++) {
            analysisPlayersToMove.add(analysisGame.getAnalysisPlayerToMove(i));
            analysisMoveLabels.add(analysisGame.getAnalysisMoveLabel(i));
        }
    }

    private void enterAnalysisMode(int index) {
        branchMode = false;
        analysisMode = true;
        game = analysisGame;
        drawBoard.history.clear();
        drawBoard.history.addAll(analysisHistory);
        drawBoard.viewCounter = Math.max(0, Math.min(index, drawBoard.history.size() - 1));
        drawBoard.isInViewMode = true;
        drawBoard.clearPossibleSquares();
        mouseStart = null;
        mouseEnd = null;
        selectedPiece = null;
        playHereButton.setVisible(true);
        returnToAnalysisButton.setVisible(false);
        refreshModeLabel();
    }

    private void startBranchFromCurrentPosition() {
        if (!analysisMode || analysisHistory.isEmpty()) {
            return;
        }

        String[] levels = {"Easy", "Medium", "Hard"};
        String[] colors = {"White", "Black"};
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        JComboBox<String> levelBox = new JComboBox<>(levels);
        JComboBox<String> colorBox = new JComboBox<>(colors);
        panel.add(new JLabel("Select difficulty:"));
        panel.add(levelBox);
        panel.add(new JLabel("Play as:"));
        panel.add(colorBox);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Play From Here", JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        analysisReturnIndex = drawBoard.viewCounter;
        DummyBoard snapshot = analysisHistory.get(analysisReturnIndex);
        String currentTurn = analysisPlayersToMove.get(analysisReturnIndex);
        game = new ChessGame(new ChessBoard(snapshot.getBoardCopy()), currentTurn);
        String chosenColor = (String) colorBox.getSelectedItem();
        String botColor = "White".equals(chosenColor) ? "Black" : "White";
        game.setBotParameters(true, botColor, (String) levelBox.getSelectedItem());
        whitePerspective = "White".equals(chosenColor);
        drawBoard.setWhitePerspective(whitePerspective);

        drawBoard.history.clear();
        drawBoard.history.add(new DummyBoard(snapshot.getBoardCopy(), new ArrayList<>(snapshot.previousMoveSquares)));
        drawBoard.viewCounter = 0;
        drawBoard.isInViewMode = false;
        drawBoard.clearPossibleSquares();
        branchMode = true;
        endDialogShown = false;
        playHereButton.setVisible(false);
        returnToAnalysisButton.setVisible(true);
        appendPendingSnapshots();
        refreshModeLabel();
    }


    /*public GamePanel(ChessGame game) {
        this.game = game;
        addMouseListener(this);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));  // This is valid in JPanel
        setBackground(Color.BLACK);
    }*/
    public GamePanel(ChessGame game, boolean whitePerspective) {
        this.game = game;
        this.analysisGame = game;
        this.whitePerspective = whitePerspective;
        setLayout(null); // Use null layout to allow absolute positioning
        setPreferredSize(new Dimension(WIDTH + 100, HEIGHT));  // Set panel width larger to fit button
        setBackground(Color.BLACK);
        drawBoard.setWhitePerspective(whitePerspective);
        drawBoard.history.clear();
        drawBoard.history.add(new DummyBoard(game.getBoardPieces(), new ArrayList<>()));
        drawBoard.viewCounter = 0;
        analysisMode = game.hasAnalysisSnapshots();
        if (analysisMode) {
            syncAnalysisMetadata();
            drawBoard.history.clear();
            drawBoard.history.addAll(analysisHistory);
            drawBoard.viewCounter = drawBoard.history.size() - 1;
            drawBoard.isInViewMode = true;
        }

        addMouseListener(this);

        // Create and position the "undo" button
        JButton undoButton = new JButton("<");
        undoButton.setBounds(820, 350, 50, 50); // Position at (820, 350) with a size of 50x50

        // Add action listener to print "undo" when clicked
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(drawBoard.viewCounter>0){
                    drawBoard.viewCounter--;
                }
                drawBoard.clearPossibleSquares();
                //drawBoard.previousMoveSquares = drawBoard.history.get(drawBoard.viewCounter).previousMoveSquares;
                //System.out.println("Undo");
                //System.out.println(drawBoard.viewCounter);
                drawBoard.isInViewMode = true;
            }
        });

        JButton nextButton = new JButton(">");
        nextButton.setBounds(820, 410, 50, 40);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (drawBoard.viewCounter < drawBoard.history.size() - 1) {
                    drawBoard.viewCounter++;
                }
                drawBoard.isInViewMode = drawBoard.viewCounter < drawBoard.history.size() - 1;
                drawBoard.clearPossibleSquares();
            }
        });

        JButton currentButton = new JButton("Now");
        currentButton.setBounds(815, 460, 60, 40);
        currentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawBoard.viewCounter = drawBoard.history.size() - 1;
                drawBoard.isInViewMode = false;
                drawBoard.clearPossibleSquares();
                selectedPiece = null;
                mouseStart = null;
                mouseEnd = null;
            }
        });

        playHereButton = new JButton("Play");
        playHereButton.setBounds(810, 510, 70, 40);
        playHereButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startBranchFromCurrentPosition();
            }
        });
        playHereButton.setVisible(analysisMode);

        returnToAnalysisButton = new JButton("Back");
        returnToAnalysisButton.setBounds(810, 560, 70, 40);
        returnToAnalysisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enterAnalysisMode(analysisReturnIndex);
            }
        });
        returnToAnalysisButton.setVisible(false);

        JButton rotateButton = new JButton("Flip");
        rotateButton.setBounds(810, 610, 70, 40);
        rotateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GamePanel.this.whitePerspective = !GamePanel.this.whitePerspective;
                drawBoard.setWhitePerspective(GamePanel.this.whitePerspective);
                repaint();
            }
        });

        modeLabel = new JLabel("", SwingConstants.CENTER);
        modeLabel.setBounds(800, 655, 90, 90);
        modeLabel.setForeground(Color.WHITE);

        // Add the button to the GamePanel
        add(undoButton);
        add(nextButton);
        add(currentButton);
        add(playHereButton);
        add(returnToAnalysisButton);
        add(rotateButton);
        add(modeLabel);
        refreshModeLabel();
    }

    public void launchGame()
    {
        gameThread = new Thread(this);
        gameThread.start();

    }

    public void run() {

        // GAME LOOP
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
    
        while (gameThread != null) {
            currentTime = System.nanoTime();
    
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
    
            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update()
    {
        if (!analysisMode || branchMode) {
            drawBoard.boardGrid = game.getBoardPieces();
            appendPendingSnapshots();
        }
        refreshModeLabel();
        if (game.isGameOver() && !endDialogShown) {
            endDialogShown = true;
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, game.getGameOverMessage(), "Game Over", JOptionPane.INFORMATION_MESSAGE);
                if (branchMode && analysisMode) {
                    enterAnalysisMode(analysisReturnIndex);
                } else {
                    Window window = SwingUtilities.getWindowAncestor(this);
                    if (window != null) {
                        window.dispose();
                    }
                    Main.returnToMainMenu();
                }
            });
        }
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        drawBoard.draw(g2,drawBoard.history.get(drawBoard.viewCounter));
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if(drawBoard.isInViewMode || (analysisMode && !branchMode)){
                return;
            }
            int x = e.getX() / 100;
            int y = e.getY() / 100;
            if (x < 0 || x >= 8 || y < 0 || y >= 8) {
                return;
            }
            if (whitePerspective) {
                x = 7 - x;
                y = 7 - y;
            }
            ChessPiece clickedPiece = drawBoard.getPieceAt(y, x);
            if (mouseStart == null && clickedPiece == null){
                return;
            }

            if (mouseStart == null) {
                if (clickedPiece == null || !clickedPiece.getColor().equals(game.getCurrentPlayer())) {
                    return;
                }
                mouseStart = new int[]{x, y};
                selectedPiece = clickedPiece;
                possibleMovesOfSelectedPiece = clickedPiece.getLegalMoves(game.getChessBoard());
                drawBoard.setPossibleSquares(possibleMovesOfSelectedPiece);
                System.out.println("Start: " + mouseStart[0] + " " + mouseStart[1]);
            } else {
                if (clickedPiece != null && clickedPiece.getColor().equals(game.getCurrentPlayer())) {
                    mouseStart = new int[]{x, y};
                    selectedPiece = clickedPiece;
                    possibleMovesOfSelectedPiece = clickedPiece.getLegalMoves(game.getChessBoard());
                    drawBoard.setPossibleSquares(possibleMovesOfSelectedPiece);
                    System.out.println("Start: " + mouseStart[0] + " " + mouseStart[1]);
                    return;
                }
                mouseEnd = new int[]{x, y};
                System.out.println("End: " + mouseEnd[0] + " " + mouseEnd[1]);
                if (game.processMouseMove(mouseStart, mouseEnd)) { //legal game move is played
                    drawBoard.isInViewMode = false;
                    drawBoard.setPossibleSquares(new ArrayList<>());
                }

                // Reset for next move
                mouseStart = null;
                mouseEnd = null;
            }
        }
        if  (e.getButton() == MouseEvent.BUTTON2){
            drawBoard.viewCounter = drawBoard.history.size()-1;
            drawBoard.isInViewMode = false;
            drawBoard.clearPossibleSquares();
            selectedPiece = null;
            mouseStart = null;
            mouseEnd = null;

        }
    }

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
}
