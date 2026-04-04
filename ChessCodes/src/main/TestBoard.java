package main;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TestBoard {
    private BufferedImage wp, bp, wn, bn, wk, bk, wq, bq, wr, br, wb, bb; // Piece images
    private ChessPiece[][] TestBoardGrid = new ChessPiece[8][8]; // Each element represents a ChessPiece object

    final int MAX_COL = 8;
    final int MAX_ROW = 8;

    public static final int SQUARE_SIZE = 100;

    public TestBoard() {
        loadImages();
        initializeTestBoard(); // Set up the TestBoard with ChessPiece objects
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

    private void initializeTestBoard() {
        // Pawns
        for (int i = 0; i < 8; i++) {
            TestBoardGrid[1][i] = new Pawn("White", 1, i);
            TestBoardGrid[6][i] = new Pawn("Black", 6, i);
        }

        // Rooks
        TestBoardGrid[0][0] = new Rook("White", 0, 0);
        TestBoardGrid[0][7] = new Rook("White", 0, 7);
        TestBoardGrid[7][0] = new Rook("Black", 7, 0);
        TestBoardGrid[7][7] = new Rook("Black", 7, 7);

        // Knights
        TestBoardGrid[0][1] = new Knight("White", 0, 1);
        TestBoardGrid[0][6] = new Knight("White", 0, 6);
        TestBoardGrid[7][1] = new Knight("Black", 7, 1);
        TestBoardGrid[7][6] = new Knight("Black", 7, 6);

        // Bishops
        TestBoardGrid[0][2] = new Bishop("White", 0, 2);
        TestBoardGrid[0][5] = new Bishop("White", 0, 5);
        TestBoardGrid[7][2] = new Bishop("Black", 7, 2);
        TestBoardGrid[7][5] = new Bishop("Black", 7, 5);

        // Queens
        TestBoardGrid[0][3] = new Queen("White", 0, 3);
        TestBoardGrid[7][3] = new Queen("Black", 7, 3);

        // Kings
        TestBoardGrid[0][4] = new King("White", 0, 4);
        TestBoardGrid[7][4] = new King("Black", 7, 4);
    }

    public ChessPiece getPieceAt(int row, int col) {
        return TestBoardGrid[row][col];
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

    public void draw(Graphics2D g2) {
        int c = 0;

        // Draw chessTestBoard squares
        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++) {
                g2.setColor((c == 0) ? new Color(210, 165, 125) : new Color(175, 115, 70));
                g2.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                c = (c == 0) ? 1 : 0;
            }
            c = (c == 0) ? 1 : 0; // Flip color for the next row
        }

        // Draw the pieces
        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++) {
                ChessPiece piece = getPieceAt(row, col);
                if (piece != null) {
                    BufferedImage pieceImage = getPieceImage(piece);
                    if (pieceImage != null) {
                        g2.drawImage(pieceImage, col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, null);
                    }
                }
            }
        }
    }
}
