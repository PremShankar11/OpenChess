package main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

abstract class ChessPiece implements Cloneable {
    protected String color;
    protected String name;
    protected int row;
    protected int col;


    public abstract String getType(); // Make it abstract

    public ChessPiece(String color, String name, int row, int col) {
        this.color = color;
        this.name = name;
        this.row = row;
        this.col = col;
    }

    protected ChessPiece clone() {
        try {
            return (ChessPiece) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // should never happen
        }
    }

    public abstract List<int[]> getLegalMoves(ChessBoard board);

    public abstract Character getNotationCode();

    public String getColor() {
        return color;
    }

    public String toString() {
        return name + "-" + color.charAt(0);
    }

    public void move(int newRow, int newCol) {
        this.row = newRow;
        this.col = newCol;
    }

    public abstract int getValue();
}

class Pawn extends ChessPiece {
    private boolean hasMoved = false;

    public Pawn(String color, int row, int col) {
        super(color, "P", row, col);
    }

    protected Pawn clone() {
        Pawn cloned = (Pawn) super.clone();
        cloned.hasMoved = this.hasMoved;
        return cloned;
    }

    /*@Override
    public List<int[]> getLegalMoves(ChessBoard board) {
        List<int[]> moves = new ArrayList<>();
        int direction = color.equals("White") ? 1 : -1;

        // Move forward
        if (board.isValidPosition(row + direction, col) && board.getPiece(row + direction, col) == null) {
            moves.add(new int[]{row + direction, col});

            // Double move on first turn
            if (!hasMoved && board.isValidPosition(row + 2 * direction, col) && board.getPiece(row + 2 * direction, col) == null) {
                moves.add(new int[]{row + 2 * direction, col});
            }
        }

        // Capture diagonally
        for (int colOffset : new int[]{-1, 1}) {
            if (board.isValidPosition(row + direction, col + colOffset)) {
                ChessPiece piece = board.getPiece(row + direction, col + colOffset);
                if (piece != null && !piece.getColor().equals(color)) {
                    moves.add(new int[]{row + direction, col + colOffset});
                }
            }
        }

        return moves;
    }*/
    //new pawn move method
    public List<int[]> getLegalMoves(ChessBoard board) {
        List<int[]> moves = new ArrayList<>();
        int direction = color.equals("White") ? 1 : -1;
        int startRow = color.equals("White") ? 1 : 6;

        // Move forward
        if (board.isValidPosition(row + direction, col) && board.getPiece(row + direction, col) == null) {
            moves.add(new int[]{row + direction, col});

            // Double move on first turn
            if (row == startRow && board.isValidPosition(row + 2 * direction, col) && board.getPiece(row + 2 * direction, col) == null) {
                moves.add(new int[]{row + 2 * direction, col});
            }
        }

        // Capture diagonally
        for (int colOffset : new int[]{-1, 1}) {
            if (board.isValidPosition(row + direction, col + colOffset)) {
                ChessPiece piece = board.getPiece(row + direction, col + colOffset);
                if (piece != null && !piece.getColor().equals(color)) {
                    moves.add(new int[]{row + direction, col + colOffset});
                }
            }
        }

        return moves;
    }

    @Override
    public void move(int newRow, int newCol) {
        super.move(newRow, newCol);
        hasMoved = true;
    }

    @Override
    public String getType() {
        return (color.equals("White")) ? "wp" : "bp"; // wp for white pawn, bp for black pawn
    }

    public Character getNotationCode(){
        return ' ';
    }

    public int getValue(){return 1;}
}

class Rook extends ChessPiece {

    boolean hasMoved = false;

    public boolean hasMoved() {
        return hasMoved;
    }

    public Rook(String color, int row, int col) {
        super(color, "R", row, col);
    }

    protected Rook clone() {
        Rook cloned = (Rook) super.clone();
        cloned.hasMoved = this.hasMoved;
        return cloned;
    }

    @Override
    public void move(int newRow, int newCol) {
        super.move(newRow, newCol);
        hasMoved = true;
    }
    @Override
    public List<int[]> getLegalMoves(ChessBoard board) {
        List<int[]> moves = new ArrayList<>();
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        for (int[] dir : directions) {
            for (int i = 1; i < 8; i++) {
                int newRow = row + i * dir[0];
                int newCol = col + i * dir[1];
                if (!board.isValidPosition(newRow, newCol)) break;

                ChessPiece piece = board.getPiece(newRow, newCol);
                if (piece == null) {
                    moves.add(new int[]{newRow, newCol});
                } else {
                    if (!piece.getColor().equals(color)) {
                        moves.add(new int[]{newRow, newCol});
                    }
                    break;
                }
            }
        }

        return moves;
    }
    public String getType() {
        return (color.equals("White")) ? "wr" : "br";
    }

    public Character getNotationCode(){
        return 'R';
    }

    public int getValue(){return 5;}
}

class Knight extends ChessPiece {
    public Knight(String color, int row, int col) {
        super(color, "N", row, col);
    }

    protected Knight clone() {
        return (Knight) super.clone();
    }

    @Override
    public List<int[]> getLegalMoves(ChessBoard board) {
        List<int[]> moves = new ArrayList<>();
        int[][] knightMoves = {{2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}, {1, -2}, {2, -1}};

        for (int[] move : knightMoves) {
            int newRow = row + move[0];
            int newCol = col + move[1];
            if (board.isValidPosition(newRow, newCol)) {
                ChessPiece piece = board.getPiece(newRow, newCol);
                if (piece == null || !piece.getColor().equals(color)) {
                    moves.add(new int[]{newRow, newCol});
                }
            }
        }

        return moves;
    }

    public String getType() {
        return (color.equals("White")) ? "wn" : "bn";
    }
    public Character getNotationCode(){
        return 'N';
    }
    public int getValue(){return 3;}
}

class Bishop extends ChessPiece {
    public Bishop(String color, int row, int col) {
        super(color, "B", row, col);
    }
    protected Bishop clone() {
        return (Bishop) super.clone();
    }

    @Override
    public List<int[]> getLegalMoves(ChessBoard board) {
        List<int[]> moves = new ArrayList<>();
        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        for (int[] dir : directions) {
            for (int i = 1; i < 8; i++) {
                int newRow = row + i * dir[0];
                int newCol = col + i * dir[1];
                if (!board.isValidPosition(newRow, newCol)) break;

                ChessPiece piece = board.getPiece(newRow, newCol);
                if (piece == null) {
                    moves.add(new int[]{newRow, newCol});
                } else {
                    if (!piece.getColor().equals(color)) {
                        moves.add(new int[]{newRow, newCol});
                    }
                    break;
                }
            }
        }

        return moves;
    }
    public String getType() {
        return (color.equals("White")) ? "wb" : "bb";
    }
    public Character getNotationCode(){
        return 'B';
    }
    public int getValue(){return 3;}
}

class Queen extends ChessPiece {
    public Queen(String color, int row, int col) {
        super(color, "Q", row, col);
    }
    protected Queen clone() {
        return (Queen) super.clone();
    }

    @Override
    public List<int[]> getLegalMoves(ChessBoard board) {
        List<int[]> moves = new ArrayList<>();
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        for (int[] dir : directions) {
            for (int i = 1; i < 8; i++) {
                int newRow = row + i * dir[0];
                int newCol = col + i * dir[1];
                if (!board.isValidPosition(newRow, newCol)) break;

                ChessPiece piece = board.getPiece(newRow, newCol);
                if (piece == null) {
                    moves.add(new int[]{newRow, newCol});
                } else {
                    if (!piece.getColor().equals(color)) {
                        moves.add(new int[]{newRow, newCol});
                    }
                    break;
                }
            }
        }

        return moves;
    }

    public String getType() {
        return (color.equals("White")) ? "wq" : "bq";
    }
    public Character getNotationCode(){
        return 'Q';
    }
    public int getValue(){return 9;}
}

class King extends ChessPiece {
    boolean hasMoved = false;
    boolean castle = true;

    public boolean hasMoved() {
        return hasMoved;
    }

    public King(String color, int row, int col) {
        super(color, "K", row, col);
    }
    protected King clone() {
        King cloned = (King) super.clone();
        cloned.hasMoved = this.hasMoved;
        cloned.castle = this.castle;
        return cloned;
    }

    @Override
    public void move(int newRow, int newCol) {
        super.move(newRow, newCol);
        hasMoved = true;
        castle = false;
    }

    @Override
    public List<int[]> getLegalMoves(ChessBoard board) {
        List<int[]> moves = new ArrayList<>();
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (board.isValidPosition(newRow, newCol)) {
                ChessPiece piece = board.getPiece(newRow, newCol);
                if (piece == null || !piece.getColor().equals(color)) {
                    moves.add(new int[]{newRow, newCol});
                }
            }
        }

        if (castle && !hasMoved()) {
            if (board.canCastleKingside(getColor())) {
                moves.add(new int[]{row, col + 2}); // Move to g-file
            }
            if (board.canCastleQueenside(getColor())) {
                moves.add(new int[]{row, col - 2}); // Move to c-file
            }
        }

        return moves;
    }
    public String getType() {
        return (color.equals("White")) ? "wk" : "bk";
    }
    public Character getNotationCode(){
        return 'K';
    }
    public int getValue(){return 1000;}

}

class ChessBoard {
    private ChessPiece[][] board;

    public ChessBoard() {
        board = new ChessPiece[8][8];
        setupBoard();
    }

    public ChessBoard(ChessPiece[][] initialBoard) {
        board = new ChessPiece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = initialBoard[i][j] != null ? initialBoard[i][j].clone() : null;
            }
        }
    }

    private void setupBoard() {
        // Set up pawns
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn("White", 1, i);
            board[6][i] = new Pawn("Black", 6, i);
        }

        // Set up other pieces
        board[0][0] = new Rook("White", 0, 0);
        board[0][7] = new Rook("White", 0, 7);
        board[7][0] = new Rook("Black", 7, 0);
        board[7][7] = new Rook("Black", 7, 7);

        board[0][1] = new Knight("White", 0, 1);
        board[0][6] = new Knight("White", 0, 6);
        board[7][1] = new Knight("Black", 7, 1);
        board[7][6] = new Knight("Black", 7, 6);

        board[0][2] = new Bishop("White", 0, 2);
        board[0][5] = new Bishop("White", 0, 5);
        board[7][2] = new Bishop("Black", 7, 2);
        board[7][5] = new Bishop("Black", 7, 5);

        board[0][3] = new Queen("White", 0, 3);
        board[7][3] = new Queen("Black", 7, 3);

        board[0][4] = new King("White", 0, 4);
        board[7][4] = new King("Black", 7, 4);
    }

    public ChessPiece[][] getBoard() {
        return board;
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    public ChessPiece getPiece(int row, int col) {
        return board[row][col];
    }

    public void movePiece(int startRow, int startCol, int endRow, int endCol) {
        ChessPiece piece = board[startRow][startCol];
        if (piece == null) {
            return;
        }
        board[endRow][endCol] = piece;
        board[startRow][startCol] = null;
        piece.move(endRow, endCol);
    }

    public void movePieceWithValidation(int startRow, int startCol, int endRow, int endCol) {
        ChessPiece piece = getPiece(startRow, startCol);

        // Check if the piece exists
        if (piece == null) {
            System.out.println("Invalid move: No piece at starting position.");
            return; // Invalid move
        }

        // Handle castling for the King
        if (piece instanceof King) {
            King king = (King) piece;

            // Kingside castling (e1 to g1)
            if (startCol == 4 && endCol == 6 && !king.hasMoved()) {
                Rook rook = (Rook) getPiece(startRow, 7); // Rook on h1
                if (rook != null && !rook.hasMoved() && canCastleKingside(king.getColor())) {
                    movePiece(startRow, startCol, endRow, endCol); // Move king to g1
                    movePiece(startRow, 7, startRow, 5); // Move rook to f1
                    return; // Castling successful
                }
            }

            // Queenside castling (e1 to c1)
            if (startCol == 4 && endCol == 2 && !king.hasMoved()) {
                Rook rook = (Rook) getPiece(startRow, 0); // Rook on a1
                if (rook != null && !rook.hasMoved() && canCastleQueenside(king.getColor())) {
                    movePiece(startRow, startCol, endRow, endCol); // Move king to c1
                    movePiece(startRow, 0, startRow, 3); // Move rook to d1
                    return; // Castling successful
                }
            }
        }

        List<int[]> legalMoves = piece.getLegalMoves(this);

        // Check if the desired move is legal
        boolean isLegalMove = legalMoves.stream()
                .anyMatch(move -> move[0] == endRow && move[1] == endCol);

        if (!isLegalMove) {
            System.out.println("Invalid move: Move is not legal.");
            return; // Invalid move
        }

        // Normal piece movement logic
        movePiece(startRow, startCol, endRow, endCol); // Move the piece
    }

    public boolean isInCheck(String color) {
        // Find the king
        int kingRow = -1, kingCol = -1;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] instanceof King && board[i][j].getColor().equals(color)) {
                    kingRow = i;
                    kingCol = j;
                    break;
                }
            }
            if (kingRow != -1) break;
        }

        // Check if any opponent piece can attack the king
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = board[i][j];
                if (piece != null && !piece.getColor().equals(color)) {
                    if (piece instanceof King) {
                        ((King) piece).castle = false;
                    }
                    List<int[]> moves = piece.getLegalMoves(this);
                    if (piece instanceof King) {
                        ((King) piece).castle = true;
                    }
                    for (int[] move : moves) {
                        if (move[0] == kingRow && move[1] == kingCol) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isCheckmate(String color) {
        if (!isInCheck(color)) return false;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = board[i][j];
                if (piece != null && piece.getColor().equals(color)) {
                    for (int[] move : piece.getLegalMoves(this)) {
                        if (isLegalMove(i, j, move[0], move[1], color)) {
                            return false; // Found a legal move that escapes check
                        }
                    }
                }
            }
        }
        return true; // No legal moves to escape check
    }

    public boolean isStalemate(String color) {
        if (isInCheck(color)) return false;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = board[i][j];
                if (piece != null && piece.getColor().equals(color)) {
                    for (int[] move : piece.getLegalMoves(this)) {
                        if (isLegalMove(i, j, move[0], move[1], color)) {
                            return false; // Found a legal move
                        }
                    }
                }
            }
        }
        return true; // No legal moves available
    }

    public boolean canCastleKingside(String color) {
        int kingRow = (color.equals("White")) ? 0 : 7;
        int kingCol = 4; // King's starting position
        int rookCol = 7; // Kingside rook

        ChessPiece king = getPiece(kingRow, kingCol);
        ChessPiece rook = getPiece(kingRow, rookCol);

        if (king == null || !(king instanceof King) || rook == null || !(rook instanceof Rook)) {
            return false;
        }

        if (((King) king).hasMoved() || ((Rook) rook).hasMoved()) {
            return false;
        }

        if (isInCheck(color)) {
            return false;
        }

        for (int i = 5; i < 7; i++) {
            if (getPiece(kingRow, i) != null) {
                return false; // Blocked by another piece
            }
        }

        if (isAttacked(kingRow, kingCol + 1, (color.equals("White")) ? "Black" : "White") ||
                isAttacked(kingRow, kingCol + 2, (color.equals("White")) ? "Black" : "White")) {
            return false; // Squares are attacked
        }

        return true; // Kingside castling is possible
    }

    public boolean canCastleQueenside(String color) {
        int kingRow = (color.equals("White")) ? 0 : 7;
        int kingCol = 4; // King's starting position
        int rookCol = 0; // Queenside rook

        ChessPiece king = getPiece(kingRow, kingCol);
        ChessPiece rook = getPiece(kingRow, rookCol);

        if (king == null || !(king instanceof King) || rook == null || !(rook instanceof Rook)) {
            return false;
        }

        // Only disable castling if either the King or the queenside rook has moved
        if (((King) king).hasMoved() || ((Rook) rook).hasMoved()) {
            return false;
        }

        if (isInCheck(color)) {
            return false;
        }

        // Check that squares between king and rook are clear
        for (int i = 1; i < 4; i++) {
            if (getPiece(kingRow, i) != null) {
                return false; // Path blocked by another piece
            }
        }

        // Ensure the path is not under attack
        if (isAttacked(kingRow, kingCol - 1, (color.equals("White")) ? "Black" : "White") ||
                isAttacked(kingRow, kingCol - 2, (color.equals("White")) ? "Black" : "White")) {
            return false; // Path squares are under attack
        }

        return true; // Queenside castling is possible
    }


    public boolean canCastle(String color) {
        return canCastleKingside(color) || canCastleQueenside(color);
    }

    // Helper method to check if a square is attacked
    private boolean isAttacked(int row, int col, String color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = getPiece(i, j);
                if (piece != null && piece.getColor().equals(color)) {
                    if(piece instanceof King)
                    {
                        ((King)piece).castle = false;
                    }
                    List<int[]> moves = piece.getLegalMoves(this);
                    if(piece instanceof King)
                    {
                        ((King)piece).castle = true;
                    }
                    for (int[] move : moves) {
                        if (move[0] == row && move[1] == col) {
                            return true; // This square is attacked
                        }
                    }
                }
            }
        }
        return false; // Not attacked
    }


    public void printBoard() {
        System.out.println("  a   b   c   d   e   f   g   h");
        for (int i = 7; i >= 0; i--) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < 8; j++) {
                System.out.print(board[i][j] == null ? ".   " : board[i][j] + " ");
            }
            System.out.println(i + 1);
        }
        System.out.println("  a   b   c   d   e   f   g   h");
    }

    public ChessBoard cloneBoard(){
        ChessBoard clone = new ChessBoard();
        ChessPiece [][] cloneBoard = new ChessPiece[8][8];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j< 8; j++){
                if(this.board[i][j]!=null) {
                    cloneBoard[i][j] = this.board[i][j].clone();
                }
            }
        }
        clone.board = cloneBoard;
        return clone;
    }

    public void setPiece(int row, int col, ChessPiece capturedPieceToCheck) {
        board[row][col] = capturedPieceToCheck;
    }

    public boolean isLegalMove(int startRow, int startCol, int endRow, int endCol, String playerColor) {
        ChessPiece piece = getPiece(startRow, startCol);
        if (piece == null || !piece.getColor().equals(playerColor)) {
            return false;
        }

        boolean isPseudoLegal = false;
        for (int[] move : piece.getLegalMoves(this)) {
            if (move[0] == endRow && move[1] == endCol) {
                isPseudoLegal = true;
                break;
            }
        }

        if (!isPseudoLegal) {
            return false;
        }

        ChessBoard clonedBoard = cloneBoard();
        clonedBoard.movePieceWithValidation(startRow, startCol, endRow, endCol);
        ChessPiece movedPiece = clonedBoard.getPiece(endRow, endCol);

        return movedPiece != null
                && movedPiece.getColor().equals(playerColor)
                && !clonedBoard.isInCheck(playerColor);
    }


}

public class ChessGame {
    private ChessBoard board;
    private String currentPlayer;
    private ArrayList<ChessBoard> history = new ArrayList<>();
    private ArrayList<String> moves = new ArrayList<>();
    private LinkedList<String> movesPlayed = new LinkedList<>();
    private ChessBot bot;
    private String botColor;
    private int botDifficulty;
    private boolean isBot;
    private List<String> moveHistory;
    private final ArrayList<DummyBoard> pendingBoardStates = new ArrayList<>();
    private final ArrayList<DummyBoard> analysisSnapshots = new ArrayList<>();
    private final ArrayList<String> analysisPlayersToMove = new ArrayList<>();
    private final ArrayList<String> analysisMoveLabels = new ArrayList<>();
    private boolean gameOver = false;
    private String gameOverMessage = "";

    public ChessGame() {
        board = new ChessBoard();
        currentPlayer = "White";
        moveHistory = new ArrayList<>();  // Initialize the list here
    }

    public ChessGame(ChessBoard initialBoard, String currentPlayer) {
        board = initialBoard.cloneBoard();
        this.currentPlayer = currentPlayer;
        moveHistory = new ArrayList<>();
    }

    /*public ChessGame() {
        board = new ChessBoard();
        currentPlayer = "White";
    }*/

    public ChessBoard getChessBoard() {
        return board;
    }

    public ChessPiece[][] getBoardPieces() {
        return board.getBoard();
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean hasAnalysisSnapshots() {
        return !analysisSnapshots.isEmpty();
    }

    public ArrayList<DummyBoard> getAnalysisSnapshots() {
        return new ArrayList<>(analysisSnapshots);
    }

    public String getAnalysisPlayerToMove(int index) {
        return analysisPlayersToMove.get(index);
    }

    public String getAnalysisMoveLabel(int index) {
        return analysisMoveLabels.get(index);
    }

    public int getAnalysisMoveCount() {
        return analysisSnapshots.size();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getGameOverMessage() {
        return gameOverMessage;
    }

    public ArrayList<DummyBoard> consumePendingBoardStates() {
        ArrayList<DummyBoard> snapshots = new ArrayList<>(pendingBoardStates);
        pendingBoardStates.clear();
        return snapshots;
    }

    private void recordBoardState(int startRow, int startCol, int endRow, int endCol) {
        ArrayList<int[]> moveSquares = new ArrayList<>();
        moveSquares.add(new int[]{endCol, endRow});
        moveSquares.add(new int[]{startCol, startRow});
        pendingBoardStates.add(new DummyBoard(board.getBoard(), moveSquares));
    }

    private void applyMove(int startRow, int startCol, int endRow, int endCol, String moveStr) {
        board.movePieceWithValidation(startRow, startCol, endRow, endCol);
        recordBoardState(startRow, startCol, endRow, endCol);
        movesPlayed.add(moveStr);
        currentPlayer = currentPlayer.equals("White") ? "Black" : "White";
    }

    /*public void play() {vcb
        Scanner scanner = new Scanner(System.in);

        while (true) {
            board.printBoard();
            System.out.println(currentPlayer + "'s turn:");
            System.out.print("Enter move (e.g., e2 e4) or 'quit' to end the game: ");
            String move = scanner.nextLine();

            if (move.equalsIgnoreCase("quit")) {
                break;
            }

            String[] parts = move.split(" ");
            if (parts.length != 2) {
                System.out.println("Invalid input. Please use the format 'e2 e4'.");
                continue;
            }

            int startCol = parts[0].charAt(0) - 'a';
            int startRow = Character.getNumericValue(parts[0].charAt(1)) - 1;
            int endCol = parts[1].charAt(0) - 'a';
            int endRow = Character.getNumericValue(parts[1].charAt(1)) - 1;

            ChessPiece piece = board.getPiece(startRow, startCol);

            if (piece == null || !piece.getColor().equals(currentPlayer)) {
                System.out.println("Invalid move. Please select your own piece.");
                continue;
            }

            List<int[]> legalMoves = piece.getLegalMoves(board);
            boolean isLegalMove = false;
            for (int[] legalMove : legalMoves) {
                if (legalMove[0] == endRow && legalMove[1] == endCol) {
                    isLegalMove = true;
                    break;
                }
            }

            if (!isLegalMove) {
                System.out.println("Invalid move. Please try again.");
                continue;
            }

            // Make the move
            ChessPiece capturedPiece = board.getPiece(endRow, endCol);
            ChessPiece movingPiece = board.getPiece(startRow, startCol);
            board.movePiece(startRow, startCol, endRow, endCol);

            // Check if the move puts the current player in check
            if (board.isInCheck(currentPlayer)) {
                System.out.println("Invalid move. You cannot put yourself in check.");
                // Undo the move
                board.movePiece(endRow, endCol, startRow, startCol);
                movingPiece.move(startRow, startCol);
                if (capturedPiece != null) {
                    ChessPiece pieceAtEnd = board.getPiece(endRow, endCol);
                    if (pieceAtEnd != null) {
                        pieceAtEnd = capturedPiece;
                    } // Restore the captured piece
                }
                continue;
            }

            // Switch players
            currentPlayer = currentPlayer.equals("White") ? "Black" : "White";

            // Check for checkmate or stalemate
            if (board.isInCheck(currentPlayer)) {
                boolean isCheckmate = true;
                // Check if any move can get the player out of check
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        ChessPiece pieceToCheck = board.getPiece(i, j);
                        if (pieceToCheck != null && pieceToCheck.getColor().equals(currentPlayer)) {
                            List<int[]> moves = pieceToCheck.getLegalMoves(board);
                            for (int[] move1 : moves) {
                                // Try the move
                                ChessPiece capturedPieceToCheck = board.getPiece(move1[0], move1[1]);
                                board.movePiece(i, j, move1[0], move1[1]);

                                // Check if the move gets the player out of check
                                if (!board.isInCheck(currentPlayer)) {
                                    isCheckmate = false;
                                }

                                // Undo the move
                                board.movePiece(move1[0], move1[1], i, j);
                                if (capturedPieceToCheck != null) {
                                    board.setPiece(move1[0], move1[1], capturedPieceToCheck);
                                }
                            }
                        }
                    }
                }

                if (isCheckmate) {
                    board.printBoard();
                    System.out.println("Checkmate! " + (currentPlayer.equals("White") ? "Black" : "White") + " wins!");
                    break;
                }
            } else if (board.isStalemate(currentPlayer)) {
                board.printBoard();
                System.out.println("Stalemate! The game is a draw.");
                break;
            }
        }

        scanner.close();
        System.out.println("Game over. Thank you for playing!");
    }*/

    //Start of parsing by prawin
    public void loadPGNFile(File pgnFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(pgnFile))) {
            StringBuilder pgnContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip metadata lines (those starting with '[')
                if (!line.trim().startsWith("[")) {
                    pgnContent.append(line).append(" ");
                }
            }
            parsePGN(pgnContent.toString());
        }
    }

    private void parsePGN(String pgnContent) {
        System.out.println(pgnContent);
        // Reset the board to initial position
        board = new ChessBoard();
        currentPlayer = "White";
        moveHistory.clear();
        analysisSnapshots.clear();
        analysisPlayersToMove.clear();
        analysisMoveLabels.clear();
        analysisSnapshots.add(new DummyBoard(board.getBoard(), new ArrayList<>()));
        analysisPlayersToMove.add(currentPlayer);
        analysisMoveLabels.add("Start");

        // Remove comments and variations
        pgnContent = pgnContent.replaceAll("\\{[^}]*\\}", "");
        pgnContent = pgnContent.replaceAll("\\([^)]*\\)", "");

        // Split into tokens
        String[] tokens = pgnContent.split("\\s+");

        for (String token : tokens) {
            // Skip move numbers and game result
            if (token.matches("\\d+\\..*") || token.matches("1-0|0-1|1/2-1/2|\\*") || token.trim().isEmpty()) {
                continue;
            }

            // Process the move
            if (processMove(token)) {
                moveHistory.add(token);
                currentPlayer = currentPlayer.equals("White") ? "Black" : "White";
                analysisSnapshots.add(new DummyBoard(board.getBoard(), new ArrayList<>()));
                analysisPlayersToMove.add(currentPlayer);
                analysisMoveLabels.add(token);
            }
        }
    }

    private boolean processMove(String moveNotation) {
        // Remove check/checkmate symbols
        moveNotation = moveNotation.replaceAll("[+#?!]", "");

        // Handle castling
        if (moveNotation.equals("O-O")) {
            return processCastling(true); // Kingside
        }
        if (moveNotation.equals("O-O-O")) {
            return processCastling(false); // Queenside
        }

        // Parse regular moves
        try {
            int toCol, toRow;
            int fromCol = -1, fromRow = -1;
            char pieceType = 'P';
            moveNotation = moveNotation.replaceAll("=.[+#?!]?", "");
            boolean isCapture = moveNotation.contains("x");

            if (Character.isUpperCase(moveNotation.charAt(0)) && "RNBQK".indexOf(moveNotation.charAt(0)) >= 0) {
                pieceType = moveNotation.charAt(0);
                moveNotation = moveNotation.substring(1);
            }

            String normalizedMove = moveNotation.replace("x", "");

            // Get destination square
            String destination = normalizedMove.substring(normalizedMove.length() - 2);
            toCol = destination.charAt(0) - 'a';
            toRow = destination.charAt(1) - '1';
            String disambiguation = normalizedMove.substring(0, normalizedMove.length() - 2);

            // Find the piece that can make this move
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    ChessPiece piece = board.getPiece(row, col);
                    if (piece != null &&
                            piece.getColor().equals(currentPlayer) &&
                            matchesPieceType(piece, pieceType)) {

                        if (matchesDisambiguation(row, col, disambiguation) &&
                                isValidMove(row, col, toRow, toCol) &&
                                board.isLegalMove(row, col, toRow, toCol, currentPlayer)) {
                            fromRow = row;
                            fromCol = col;
                            break;
                        }
                    }
                }
                if (fromRow != -1) break;
            }

            // Make the move if valid piece was found
            if (fromRow != -1 && fromCol != -1) {
                board.movePieceWithValidation(fromRow, fromCol, toRow, toCol);
                return true;
            }

        } catch (Exception e) {
            System.err.println("Error processing move: " + moveNotation);
            e.printStackTrace();
        }
        return false;
    }

    private boolean matchesPieceType(ChessPiece piece, char pieceType) {
        switch (pieceType) {
            case 'P': return piece instanceof Pawn;
            case 'R': return piece instanceof Rook;
            case 'N': return piece instanceof Knight;
            case 'B': return piece instanceof Bishop;
            case 'Q': return piece instanceof Queen;
            case 'K': return piece instanceof King;
            default: return false;
        }
    }

    private boolean processCastling(boolean kingSide) {
        int row = currentPlayer.equals("White") ? 0 : 7;

        // Check if king is in correct position
        ChessPiece king = board.getPiece(row, 4);
        if (!(king instanceof King) || !king.getColor().equals(currentPlayer)) {
            return false;
        }

        // Check if rook is in correct position
        int rookCol = kingSide ? 7 : 0;
        ChessPiece rook = board.getPiece(row, rookCol);
        if (!(rook instanceof Rook) || !rook.getColor().equals(currentPlayer)) {
            return false;
        }

        // Perform castling
        if (kingSide) {
            board.movePieceWithValidation(row, 4, row, 6); // King move also repositions rook
        } else {
            board.movePieceWithValidation(row, 4, row, 2); // King move also repositions rook
        }

        return true;
    }

    private boolean matchesDisambiguation(int row, int col, String disambiguation) {
        if (disambiguation == null || disambiguation.isEmpty()) {
            return true;
        }

        if (disambiguation.length() == 1) {
            char marker = disambiguation.charAt(0);
            if (marker >= 'a' && marker <= 'h') {
                return col == marker - 'a';
            }
            if (marker >= '1' && marker <= '8') {
                return row == marker - '1';
            }
        }

        if (disambiguation.length() == 2) {
            return col == disambiguation.charAt(0) - 'a' &&
                    row == disambiguation.charAt(1) - '1';
        }

        return true;
    }

    private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        // Get the piece at the starting position
        ChessPiece piece = board.getPiece(fromRow, fromCol);
        if (piece == null) {
            return false;
        }

        // Check if the move is valid according to piece rules
        List<int[]> legalMoves = piece.getLegalMoves(board);
        for (int[] move : legalMoves) {
            if (move[0] == toRow && move[1] == toCol) {
                return true;
            }
        }
        return false;
    }
    //End of Parsing By prawin
    public boolean processMouseMove(int[] start, int[] end) {
        int startCol = start[0];
        int startRow = start[1];
        int endCol = end[0];
        int endRow = end[1];

        ChessPiece piece = board.getPiece(startRow, startCol);

        if (piece == null || !piece.getColor().equals(currentPlayer)) {
            System.out.println("Invalid move. Please select your own piece.");
            return false;
        }

        if (!board.isLegalMove(startRow, startCol, endRow, endCol, currentPlayer)) {
            System.out.println("Invalid move. Please try again.");
            return false;
        }

        String moveStr = convertMoveToPGN(new int[]{startRow, startCol, endRow, endCol}, this.board.cloneBoard());

        applyMove(startRow, startCol, endRow, endCol, moveStr);

        if (board.isInCheck(currentPlayer)) {
            if (board.isCheckmate(currentPlayer)) {
                board.printBoard();
                gameOver = true;
                gameOverMessage = "Checkmate! " + (currentPlayer.equals("White") ? "Black" : "White") + " wins!";
                System.out.println(gameOverMessage);
                movesPlayed.removeLast();
                movesPlayed.add(moveStr+"#");
                displayMovesPlayed();
                return true;
            }
        } else if (board.isStalemate(currentPlayer)) {
            board.printBoard();
            gameOver = true;
            gameOverMessage = "Stalemate! The game is a draw.";
            System.out.println(gameOverMessage);
            displayMovesPlayed();
            return true;
        }
        if (isBot && currentPlayer.equals(botColor)) {
            processBotMove(botColor, botDifficulty);
        }
        return true;
    }

    public void setBotParameters(boolean isBot, String color, String difficulty){
        this.isBot = isBot;
        this.botColor = color;
        if(difficulty.equals("Easy")){
            botDifficulty = 1;
        }
        else if(difficulty.equals("Medium")){
            botDifficulty = 2;
        }
        else if(difficulty.equals("Hard")){
            botDifficulty = 3;
        }

        if (this.isBot && currentPlayer.equals(this.botColor)) {
            processBotMove(this.botColor, botDifficulty);
        }
    }

    public void processBotMove(String turn,int depth) {
        if (!currentPlayer.equals(turn)) {
            return;
        }
        bot = new ChessBot(this.board,turn);
        int[] move = bot.findBestMove(depth);
        if (move == null || move.length < 4 || !board.isLegalMove(move[0], move[1], move[2], move[3], turn)) {
            return;
        }
        String moveStr = convertMoveToPGN(move, this.board.cloneBoard());
        applyMove(move[0], move[1], move[2], move[3], moveStr);
    }

    public String convertMoveToPGN(int[] move,ChessBoard board) {
        int startRow = move[0];
        int startCol = move[1];
        int endRow = move[2];
        int endCol = move[3];

        ChessPiece movingPiece = board.getPiece(startRow, startCol);
        ChessPiece targetPiece = board.getPiece(endRow, endCol);

        if (movingPiece == null || !board.isLegalMove(startRow, startCol, endRow, endCol, movingPiece.getColor())) {
            return ""; // Invalid move; no piece to move.
        }

        if (movingPiece instanceof King && Math.abs(endCol - startCol) == 2) {
            return endCol > startCol ? "O-O" : "O-O-O";
        }

        StringBuilder pgn = new StringBuilder();

        // Add piece notation
        Character pieceCode = movingPiece.getNotationCode();
        if (pieceCode != ' ') {
            pgn.append(pieceCode);
        }

        // Check if it's a capture
        if (targetPiece != null) {
            if (pieceCode == ' ') { // Pawn capture notation requires the starting file
                pgn.append((char) ('a' + (7-startCol)));
            }
            pgn.append('x');
        }

        // Append destination square
        pgn.append((char) ('a' + (7-endCol))); // Column as a letter
        pgn.append(endRow+1); // Row as a number (in chess notation, rows are 8 to 1)

        // Check for special conditions
        board.movePieceWithValidation(startRow, startCol, endRow, endCol);
        String opponentColor = movingPiece.getColor().equals("White") ? "Black" : "White";
        if (board.isInCheck(opponentColor)) {
            pgn.append('+'); // Check notation
            if (board.isCheckmate(opponentColor)) {
                pgn.append('#'); // Checkmate notation
            }
        }
        System.out.println(pgn.toString());
        return pgn.toString();
    }

    /*public int[] convertStrMoveToIntMove(String moveStr, ChessPiece[][] board,String color){
        int startRow,startCol,endRow,endCol;
        if (Character.isLowerCase(moveStr.charAt(0))){

            endRow = (int)moveStr.charAt(1) - 49;
            endCol = (int)moveStr.charAt(0) - 97;
            System.out.println(""+endRow+" "+endCol);
            ChessPiece movedPiece = new Pawn(color,endRow,endCol);

            if (color.equals("White")){
                if(board[endRow][endCol]!=null){
                    if(board[endRow-1][endCol-1]==null){
                        return new int[]{endRow-1,endCol+1,endRow,endCol};
                    }
                    else{
                        return new int[]{endRow-1,endCol-1,endRow,endCol};
                    }
                }
                if(board[endRow-1][endCol]==null && board[endRow-1][endCol-1]==null && board[endRow-1][endCol+1]==null){
                    return new int[]{endRow-2,endCol,endRow,endCol};
                }
                return new int[]{endRow-1,endCol,endRow,endCol};
            }
            else if (color.equals("Black")) {
                // Logic for black pawns
                if (board[endRow][endCol] != null) {
                    // Capture logic for black pawn
                    if (board[endRow + 1][endCol + 1] == null) {
                        return new int[]{endRow + 1, endCol - 1, endRow, endCol};
                    } else {
                        return new int[]{endRow + 1, endCol + 1, endRow, endCol};
                    }
                }
                if (board[endRow + 1][endCol] == null && board[endRow + 1][endCol - 1] == null && board[endRow + 1][endCol + 1] == null) {
                    // Two-step move for black pawn
                    return new int[]{endRow + 2, endCol, endRow, endCol};
                }
                // Single-step move for black pawn
                return new int[]{endRow + 1, endCol, endRow, endCol};
            }
        }

        return new int[]{9,9,9,9};
    }*/


    public int[] convertStrMoveToIntMove(String moveStr, ChessBoard board, String color) {
        int startRow, startCol, endRow, endCol;
        if(Character.isLowerCase(moveStr.charAt(0))){
            endRow = (int) moveStr.charAt(1) - 49;
            endCol = (int) moveStr.charAt(0) - 97;
        }
        else{
            endRow = (int) moveStr.charAt(2) - 49; // Convert char '1'-'8' to int 0-7
            endCol = (int) moveStr.charAt(1) - 97; // Convert char 'a'-'h' to int 0-7
        }
        // Convert the end position from string notation to numeric indices
        System.out.println("" + endRow + " " + endCol);
        // Determine the piece type (assuming lowercase input for columns)
        char pieceType = Character.isLowerCase(moveStr.charAt(0)) ? 'P' : moveStr.charAt(0);

        // Iterate over the board to find a matching piece that can legally move to (endRow, endCol)
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece currentPiece = board.getBoard()[row][col];
                if (currentPiece != null && currentPiece.getColor().equals(color)) {
                    // Check if the piece matches the type
                    boolean matchesType = switch (pieceType) {
                        case 'P' -> currentPiece instanceof Pawn;
                        case 'R' -> currentPiece instanceof Rook;
                        case 'N' -> currentPiece instanceof Knight;
                        case 'B' -> currentPiece instanceof Bishop;
                        case 'Q' -> currentPiece instanceof Queen;
                        case 'K' -> currentPiece instanceof King;
                        default -> false;
                    };

                    if (matchesType) {
                        // Check if this piece can legally move to the target square
                        /*System.out.println(currentPiece.toString());
                        List<int[]> moves = currentPiece.getLegalMoves(board.cloneBoard());
                        Iterator<int[]> moveIterator = moves.iterator();

                        while (moveIterator.hasNext()) {
                            int[] currentMove = moveIterator.next();
                            System.out.println(currentMove[0] + " " + currentMove[1]);
                        }*/
                        for (int[] move : currentPiece.getLegalMoves(board.cloneBoard())) {
                            if (Arrays.equals(move, new int[]{endRow, endCol})) {
                                return new int[]{row, col, endRow, endCol};
                            }
                        }
                    }
                }
            }
        }

        // Return an invalid move indicator if no piece was found
        return new int[]{9, 9, 9, 9};
    }

    public void displayMovesPlayed(){
        Iterator<String> iterator = movesPlayed.iterator();
        int i = 0;
        for (; i < movesPlayed.size()/2;i++){
            System.out.println((i+1)+ ": "+iterator.next()+" "+iterator.next());
        }
        if(iterator.hasNext()){
            System.out.println((i+1)+ ": "+iterator.next());
        }
        if(movesPlayed.size()%2==0){
            System.out.println("    0-1");
        }
        else{
            System.out.println("    1-0");
        }
    }
    public static void main(String[] args) {
        ChessGame game = new ChessGame();
        /*String move = "e4";
        int[] intMove = game.convertStrMoveToIntMove(move, game.board.cloneBoard(),"White");
        System.out.println("Move: "+intMove[0]+" "+intMove[1]+" "+intMove[2]+" "+intMove[3]);
        game.board.movePiece(intMove[0],intMove[1],intMove[2],intMove[3]);
        move = "e5";
        intMove = game.convertStrMoveToIntMove(move, game.board.cloneBoard(),"Black");
        System.out.println("Move: "+intMove[0]+" "+intMove[1]+" "+intMove[2]+" "+intMove[3]);
        game.board.movePiece(intMove[0],intMove[1],intMove[2],intMove[3]);
        game.board.printBoard();
        move = "Nf3";
        intMove = game.convertStrMoveToIntMove(move, game.board.cloneBoard(),"White");
        System.out.println("Move: "+intMove[0]+" "+intMove[1]+" "+intMove[2]+" "+intMove[3]);
        game.board.movePiece(intMove[0],intMove[1],intMove[2],intMove[3]);
        move = "Nf6";
        intMove = game.convertStrMoveToIntMove(move, game.board.cloneBoard(),"Black");
        System.out.println("Move: "+intMove[0]+" "+intMove[1]+" "+intMove[2]+" "+intMove[3]);
        game.board.movePiece(intMove[0],intMove[1],intMove[2],intMove[3]);*/
        String pgnMoves = "1.e4 e6 2.Nf3 Nc6 3.Bb5 a6 4.Ba4 Nf6";

        String strMoves = "e4 e6 Nf3 Nc6 Bb5 a6 Ba4 Nf6";
        String player = "White";
        String[] strArrayMoves = strMoves.split(" ");
        for(int i =0;i<strArrayMoves.length;i++){
            int[] newIntMove = game.convertStrMoveToIntMove(strArrayMoves[i], game.board.cloneBoard(),player);
            game.board.movePiece(newIntMove[0],newIntMove[1],newIntMove[2],newIntMove[3]);
            player = player.equals("White") ? "Black" : "White";
        }
        game.board.printBoard();
        //game.play();
    }
}
