package main;
import java.util.*;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

class DummyBoard{
    public ChessPiece[][] board = new ChessPiece[8][8];
    ArrayList<int[]> previousMoveSquares = new ArrayList<int[]>();

    public DummyBoard(ChessPiece[][] board, ArrayList<int[]> squares) {
        this.board = new ChessPiece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.board[i][j] = board[i][j] != null ? board[i][j].clone() : null;
            }
        }
        this.previousMoveSquares = new ArrayList<>(squares); // Copy previous move squares
    }

    public ChessPiece getPieceAt(int row, int col) {
        return board[row][col];
    }

    public ChessPiece[][] getBoardCopy() {
        ChessPiece[][] copy = new ChessPiece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                copy[i][j] = board[i][j] != null ? board[i][j].clone() : null;
            }
        }
        return copy;
    }

}

public class Board {
    private BufferedImage wp, bp, wn, bn, wk, bk, wq, bq, wr, br, wb, bb; // Piece images
    //private ChessPiece[][] boardGrid = new ChessPiece[8][8]; // Each element represents a ChessPiece object
    public ChessPiece[][] boardGrid = new ChessPiece[8][8]; // Each element represents a ChessPiece object
    public ArrayList<int[]> previousMoveSquares = new ArrayList<int[]>();
    public List<int[]> possibleSquares = new ArrayList<int[]>() ;
    ArrayList <DummyBoard> history  = new ArrayList<>();
    public boolean isInViewMode;
    public int viewCounter;
    private boolean whitePerspective = true;
    final int MAX_COL = 8;
    final int MAX_ROW = 8;


    public static final int SQUARE_SIZE = 100;

    public Board() {
        loadImages();
        initializeBoard(); // Set up the board with ChessPiece objects
        history.add(new DummyBoard(boardGrid,new ArrayList<>()));
        viewCounter = 0;
        isInViewMode = false;
    }

    private void loadImages() {
        try {
            wp = ResourceLoader.loadImage("/res/piece/wp.png");
            bp = ResourceLoader.loadImage("/res/piece/bp.png");
            wn = ResourceLoader.loadImage("/res/piece/wn.png");
            bn = ResourceLoader.loadImage("/res/piece/bn.png");
            wk = ResourceLoader.loadImage("/res/piece/wk.png");
            bk = ResourceLoader.loadImage("/res/piece/bk.png");
            wq = ResourceLoader.loadImage("/res/piece/wq.png");
            bq = ResourceLoader.loadImage("/res/piece/bq.png");
            wr = ResourceLoader.loadImage("/res/piece/wr.png");
            br = ResourceLoader.loadImage("/res/piece/br.png");
            wb = ResourceLoader.loadImage("/res/piece/wb.png");
            bb = ResourceLoader.loadImage("/res/piece/bb.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeBoard() {
        // Pawns
        for (int i = 0; i < 8; i++) {
            boardGrid[1][i] = new Pawn("White", 1, i);
            boardGrid[6][i] = new Pawn("Black", 6, i);
        }

        // Rooks
        boardGrid[0][0] = new Rook("White", 0, 0);
        boardGrid[0][7] = new Rook("White", 0, 7);
        boardGrid[7][0] = new Rook("Black", 7, 0);
        boardGrid[7][7] = new Rook("Black", 7, 7);

        // Knights
        boardGrid[0][1] = new Knight("White", 0, 1);
        boardGrid[0][6] = new Knight("White", 0, 6);
        boardGrid[7][1] = new Knight("Black", 7, 1);
        boardGrid[7][6] = new Knight("Black", 7, 6);

        // Bishops
        boardGrid[0][2] = new Bishop("White", 0, 2);
        boardGrid[0][5] = new Bishop("White", 0, 5);
        boardGrid[7][2] = new Bishop("Black", 7, 2);
        boardGrid[7][5] = new Bishop("Black", 7, 5);

        boardGrid[0][3] = new Queen("White", 0, 3);
        boardGrid[7][3] = new Queen("Black", 7, 3);

        boardGrid[0][4] = new King("White", 0, 4);
        boardGrid[7][4] = new King("Black", 7, 4);
    }

    public ChessPiece getPieceAt(int row, int col) {
        return boardGrid[row][col];
    }

    public BufferedImage getPieceImage(ChessPiece piece) {
        if (piece == null) return null;

        switch (piece.getType()) {
            case "wp": return wp;
            case "bp": return bp;
            case "wn": return wn;
            case "bn": return bn;
            case "wk": return wk;
            case "bk": return bk;
            case "wq": return wq;
            case "bq": return bq;
            case "wr": return wr;
            case "br": return br;
            case "wb": return wb;
            case "bb": return bb;
            default: return null;
        }
    }

    public void setPreviousMoveSquares(ArrayList<int[]> squares){
        this.previousMoveSquares = squares;
    }

    public void clearPossibleSquares(){this.possibleSquares = new ArrayList<int[]>();}

    public void setPossibleSquares(List<int[]> squares){
        this.possibleSquares = squares;
    }

    public void setWhitePerspective(boolean whitePerspective) {
        this.whitePerspective = whitePerspective;
    }

    private int toScreenCol(int boardCol) {
        return whitePerspective ? 7 - boardCol : boardCol;
    }

    private int toScreenRow(int boardRow) {
        return whitePerspective ? 7 - boardRow : boardRow;
    }

    public void draw(Graphics2D g2,DummyBoard board) {
        int c = 1;

        // Draw chessboard squares
        /*for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++) {
                g2.setColor((c == 0) ? new Color(135, 206, 235) : new Color(255, 255, 240));
                g2.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                c = (c == 0) ? 1 : 0;
            }
            c = (c == 0) ? 1 : 0; // Flip color for the next row
        }

        for (int[] sq : board.previousMoveSquares){
            g2.setColor(new Color(255,222,173));
            g2.fillRect(sq[0] * SQUARE_SIZE , sq[1]*SQUARE_SIZE,SQUARE_SIZE,SQUARE_SIZE);
        }
        // Draw the pieces
        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++) {
                ChessPiece piece = history.get(viewCounter).getPieceAt(row, col);
                if (piece != null) {
                    BufferedImage pieceImage = getPieceImage(piece);
                    if (pieceImage != null) {
                        g2.drawImage(pieceImage, col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, null);
                    }
                }
            }
        }

        for (int[] sq : possibleSquares){
            g2.setColor(new Color(0,128,0,75));
            g2.fillOval((int)((sq[1]+0.375) * SQUARE_SIZE), (int)((sq[0]+0.375) * SQUARE_SIZE)  ,SQUARE_SIZE/4,SQUARE_SIZE/4);
        }*/
        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++) {
                g2.setColor((c == 0) ? new Color(135, 206, 235) : new Color(255, 255, 240));
                g2.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                c = (c == 0) ? 1 : 0;
            }
            c = (c == 0) ? 1 : 0; // Flip color for the next row
        }

        for (int[] sq : board.previousMoveSquares){
            g2.setColor(new Color(255,222,173));
            g2.fillRect(toScreenCol(sq[0]) * SQUARE_SIZE , toScreenRow(sq[1]) * SQUARE_SIZE,SQUARE_SIZE,SQUARE_SIZE);
        }
        // Draw the pieces
        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++) {
                ChessPiece piece = history.get(viewCounter).getPieceAt(row, col);
                if (piece != null) {
                    BufferedImage pieceImage = getPieceImage(piece);
                    if (pieceImage != null) {
                        g2.drawImage(pieceImage, toScreenCol(col) * SQUARE_SIZE, toScreenRow(row) * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, null);
                    }
                }
            }
        }

        for (int[] sq : possibleSquares){
            g2.setColor(new Color(0,128,0,75));
            g2.fillOval((int)((toScreenCol(sq[1])+0.375) * SQUARE_SIZE), (int)((toScreenRow(sq[0])+0.375) * SQUARE_SIZE)  ,SQUARE_SIZE/4,SQUARE_SIZE/4);
        }
    }
}
